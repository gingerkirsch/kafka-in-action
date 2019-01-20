package tv.mycujoo.test

import java.io.{File, PrintWriter}

import com.typesafe.config.ConfigFactory
import play.api.libs.json._
import tv.mycujoo.test.json.{FieldDTO, Schema}

import scala.io.Source

object KafkaSqlConnectorSample extends App {
  val url =  ConfigFactory.load.getString("list.url")
  val template = ConfigFactory.load.getString("schema.url.template")
  val subjectsJson = Source.fromURL(url).mkString
  val subjects = Json.parse(subjectsJson).as[Seq[String]]

  val schemas = subjects.map { subject =>
    val url = template.replace("[NAME_OF_THE_SUBJECT]", subject)
    val json = Source.fromURL(url).mkString
    println(json)
    val schema = Json.parse(json).as[Schema].record

    val fieldsDTO: List[FieldDTO] = schema.fields.map{ field =>
      val attribute: List[String] = field.`type` match {
        case JsString(str) => if (str == "string") List("varchar") else List(str)
        case arr: JsArray =>
          val str = arr(0).as[String]
          List(if (str == "string") "varchar" else str, arr(1).asOpt[String].getOrElse(""))
        case obj: JsObject => {
          (obj \ "type").as[String] match {
            case "record" | "string" | "array" => List("varchar")
            case "enum" => List("varchar", s"ENUM(${(obj \\ "symbols").map(_.asOpt[Seq[String]]
              .map(str => str.map(str => s"'$str'").mkString(",")).mkString(",")).mkString("")})")
            case _ => List.empty[String]
          }
        }
        case _ => List.empty[String]
      }

      FieldDTO(field.name, attribute.mkString(" "))
    }.toList

    val query = s"DROP IF EXISTS ${schema.name};\n" +
      s"CREATE TABLE ${schema.name}(\n" +
      fieldsDTO.map(field => s" ${field.name} ${field.attribute}").mkString(",\n") +
      s"\n);"

    val pw = new PrintWriter(new File(s"${schema.name}.sql"))
    pw.write(query)
    pw.close()

  }
}

package tv.mycujoo.test.json

import play.api.libs.functional.syntax._
import play.api.libs.json.{JsPath, Reads}

case class ComplexType(`type`: String, name: String, symbols: Option[Seq[String]], fields: Option[Seq[Field]])
object ComplexType{
  implicit val reads: Reads[ComplexType] = (
    (JsPath \ "type").read[String] and
      (JsPath \ "name").read[String] and
      (JsPath \ "symbols").readNullable[Seq[String]] and
      (JsPath \ "fields").readNullable[Seq[Field]]
  )(ComplexType.apply _)
}
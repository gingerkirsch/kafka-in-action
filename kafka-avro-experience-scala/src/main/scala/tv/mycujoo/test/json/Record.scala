package tv.mycujoo.test.json

import play.api.libs.functional.syntax._
import play.api.libs.json.{JsPath, Reads}

case class Record(`type`: String, name: String, fields: Seq[Field])
object Record {
  implicit val reads: Reads[Record] = (
    (JsPath \ "type").read[String] and
      (JsPath \ "name").read[String] and
      (JsPath \ "fields").read[Seq[Field]]
    )(Record.apply _)
}

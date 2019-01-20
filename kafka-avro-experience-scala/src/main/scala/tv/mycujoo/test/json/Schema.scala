package tv.mycujoo.test.json

import play.api.libs.functional.syntax._
import play.api.libs.json.{JsObject, JsPath, Json, Reads}

case class Schema(subject: String, version: Int, id: Int, record: Record)
object Schema {
  implicit val readsSchema: Reads[Schema] = (
    (JsPath \ "subject").read[String] and
    (JsPath \ "version").read[Int] and
    (JsPath \ "id").read[Int] and
    (JsPath \ "schema").read[String].map(Json.parse(_).as[Record])
  )(Schema.apply _)
}

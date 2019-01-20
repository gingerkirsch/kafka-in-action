package tv.mycujoo.test.json

import play.api.libs.functional.syntax._
import play.api.libs.json._

case class Field(name: String, `type`: JsValue/* Either[String, ComplexType]*/)
object Field{
  implicit val reads: Reads[Field] = (
    (JsPath \ "name").read[String] and
      //(JsPath \ "type").read[Either[String, ComplexType]]
      (JsPath \ "type").read[JsValue]
    )(Field.apply _)

  implicit def eitherReads[A, B](implicit A: Reads[A], B: Reads[B]): Reads[Either[A, B]] =
    Reads[Either[A, B]] { json =>
      A.reads(json) match {
        case JsSuccess(value, path) => JsSuccess(Left(value), path)
        case JsError(e1) => B.reads(json) match {
          case JsSuccess(value, path) => JsSuccess(Right(value), path)
          case JsError(e2) => JsError(JsError.merge(e1, e2))
        }
      }
    }
}

package controllers

import org.json4s.NoTypeHints
import org.json4s.native.Serialization
import org.json4s.native.Serialization.write
import responses.Response
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

class BaseController(cc: ControllerComponents)(implicit ec: ExecutionContext) extends AbstractController(cc) {

  implicit val formats = Serialization.formats(NoTypeHints)

  def process(f: Future[AnyRef]): Future[Result] = {
    f.map { message =>
      Ok(json(Response.success(message)))
    } recover {
      case e: Exception => RequestTimeout(json(Response.failure(e.getMessage)))
    }
  }

  def json(content: AnyRef): String = {
    write(content)
  }

}

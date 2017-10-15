import javax.inject._

import org.json4s.NoTypeHints
import org.json4s.native.Serialization
import org.json4s.native.Serialization.write
import play.api._
import play.api.http.DefaultHttpErrorHandler
import play.api.mvc.Results._
import play.api.mvc._
import play.api.routing.Router
import responses.Response

import scala.concurrent._

@Singleton
class ErrorHandler @Inject() (
                               env: Environment,
                               config: Configuration,
                               sourceMapper: OptionalSourceMapper,
                               router: Provider[Router]
                             ) extends DefaultHttpErrorHandler(env, config, sourceMapper, router) {

  implicit val formats = Serialization.formats(NoTypeHints)

  override def onProdServerError(request: RequestHeader, exception: UsefulException): Future[Result] = {
    Future.successful(InternalServerError(write(Response.failure("An error occurred"))))
  }

}
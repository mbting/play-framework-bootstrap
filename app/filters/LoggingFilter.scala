package filters

import javax.inject.Inject

import akka.stream.Materializer
import play.api.Logger
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

class LoggingFilter @Inject() (implicit val mat: Materializer, ec: ExecutionContext) extends Filter {

  def apply(nextFilter: RequestHeader => Future[Result])
           (requestHeader: RequestHeader): Future[Result] = {

    val startTime = System.currentTimeMillis
    val wri = requestHeader.headers.get("WebRequestId")

    nextFilter(requestHeader).map { result =>
      val endTime = System.currentTimeMillis
      val requestTime = endTime - startTime
      wri match {
        case Some(r) =>
          Logger.info("[%37s] %-3s %-4s %6s %s %s".format(wri.getOrElse(""), result.header.status, requestHeader.method, requestTime + "ms", requestHeader.uri, requestHeader.remoteAddress))
        case None =>
          Logger.info("%-3s %-4s %6s %s %s".format(result.header.status, requestHeader.method, requestTime + "ms", requestHeader.uri, requestHeader.remoteAddress))
      }
      result.withHeaders("Processing-Time" -> (requestTime.toString + "ms"))
    }
  }
}
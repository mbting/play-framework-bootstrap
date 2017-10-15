package controllers

import javax.inject.{Inject, _}

import actors.ConnectionActor.GoAway
import actors.work.WorkingActor.{WebWork, Work, WorkDone}
import akka.actor._
import akka.pattern.ask
import akka.util.Timeout
import play.api.mvc._

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

@Singleton
class Application @Inject()
  (@Named("connection-actor") connectionActor: ActorRef, cc: ControllerComponents)
  (implicit ec: ExecutionContext) extends BaseController(cc) {

  implicit val timeout: Timeout = 1.seconds

  def index: Action[AnyContent] = Action.async {
    process((connectionActor ? Work("Controller")).mapTo[WorkDone])
  }

  def web: Action[AnyContent] = Action.async {
    process((connectionActor ? WebWork("http://mibdev2:9000/")).mapTo[WorkDone])
  }

  def error: Action[AnyContent] = Action.async {
    process((connectionActor ? GoAway("Controller")).mapTo[String])
  }

}
package actors.work

import actors.work.WorkingActor.{WebWork, Work, WorkDone}
import akka.actor.{Actor, ActorLogging, Props}
import play.api.libs.ws._

class WorkingActor (config: String, ws: WSClient) extends Actor with ActorLogging {

  import scala.concurrent.ExecutionContext.Implicits._

  def receive: PartialFunction[Any, Unit] = {
    case Work(work: String) =>
      sender() ! WorkDone(complete = true, config + " completed " + work)
    case WebWork(url: String) =>
      val snd = sender()
      val wsResponse = ws.url(url).get()
      wsResponse.map { r => snd ! WorkDone(complete = true, config + " completed " + r.body) }
      wsResponse.recover { case e: Exception =>
        snd ! WorkDone(complete = false, config + " failed " + e.getMessage)
      }
    case _ =>
      sender() ! WorkDone(complete = false,"What do I do with this?")
      throw new Exception("Unknown message")
  }
}

object WorkingActor {
  def props(config: String = "default", ws: WSClient) = Props(classOf[WorkingActor], config, ws)
  trait Workable
  case class Work(work: String) extends Workable
  case class WebWork(url: String) extends Workable
  case class WorkDone(complete: Boolean = false, work: String)
}

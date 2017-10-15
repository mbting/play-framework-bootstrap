package actors

import javax.inject._

import actors.ConnectionActor.{GoAway, Hello}
import actors.work.WorkingActor
import actors.work.WorkingActor.Workable
import akka.actor.SupervisorStrategy.Restart
import akka.actor._
import play.api.Configuration
import play.api.libs.ws.WSClient

import scala.concurrent.duration._

object ConnectionActor {
  def props: Props = Props[ConnectionActor]
  case class GoAway(name: String)
  case class Hello(name: String)
}

class ConnectionActor @Inject()(config: Configuration, ws: WSClient) extends Actor with ActorLogging {

  override val supervisorStrategy: OneForOneStrategy = OneForOneStrategy(maxNrOfRetries = 10, withinTimeRange = 1.minute) {
    case e @ _ =>
      log.error("Restarting " + sender().toString + " Exception : " + e.getMessage)
      Restart
  }

  override def preStart() { context.system.eventStream.subscribe(self, classOf[DeadLetter]) }
  override def postStop() { context.system.eventStream.unsubscribe(self, classOf[DeadLetter]) }

  val workingActor: ActorRef = context.actorOf(WorkingActor.props(config.get[String]("working.name"), ws), "working-actor")

  def receive: PartialFunction[Any, Unit] = {
    case GoAway(name: String) => Thread.sleep(2000); sender() ! "Goodbye, " + name
    case m: Hello => workingActor forward m
    case m: Workable => workingActor forward m
    case d: DeadLetter => log.error(d.toString)
  }
}
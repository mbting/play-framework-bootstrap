import com.google.inject.AbstractModule
import play.api.libs.concurrent.AkkaGuiceSupport

import actors.ConnectionActor

class Module extends AbstractModule with AkkaGuiceSupport {
  def configure(): Unit = {
    bindActor[ConnectionActor]("connection-actor")
  }
}
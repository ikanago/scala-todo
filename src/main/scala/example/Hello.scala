package example

import cats.effect.{ExitCode, IO, IOApp}
import com.comcast.ip4s._
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s.{HttpApp, HttpRoutes}
import org.http4s.circe._
import org.http4s.dsl.io._
import org.http4s.ember.server.EmberServerBuilder

case class Song(title: String, artist: String, releaseYear: Int)

object Main extends IOApp {
  def run(args: List[String]): IO[ExitCode] = {
    val routes = HttpRoutes
      .of[IO] {
        case GET -> Root / "echo" / arg =>
          Ok(arg)
        case GET -> Root / "song" =>
          Ok(Song("Bohemian Rhapsody", "Queen", 1975).asJson)
      }
      .orNotFound
    EmberServerBuilder
      .default[IO]
      .withHost(ipv4"0.0.0.0")
      .withPort(port"8080")
      .withHttpApp(routes)
      .build
      .useForever
      .as(ExitCode.Success)
  }
}

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class Flask_Blog_1000_Users extends Simulation {

	object Read {
		val read = exec(http("Index Page")
			.get("/"))
		.pause(7)
		.exec(http("Create page")
	    .get("/articles/create"))
		.pause(2)
	}

	val users = scenario("Users").exec(Read.read)

	val httpConf = http 
		.baseURL("http://127.0.0.1:5000")
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
		.doNotTrackHeader("1")
		.acceptLanguageHeader("en-US,en;q=0.5")
		.acceptEncodingHeader("gzip, deflate")
		.userAgentHeader("Mozilla/5.0 (Windows NT 5.1; rv:31.0) Gecko/20100101 Firefox/31.0")

	val scn = scenario("FlaskBlogSimulation").exec(Read.read)

	setUp(
		users.inject(rampUsers(1000) over (10 minutes))
		).protocols(httpConf)
}

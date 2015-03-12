import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class Ruby_On_Rails_Sakila_100_Users extends Simulation {

	object Read {
		val read = exec(http("Index Page")
			.get("/"))
		.pause(5)
		.exec(http("Actor Index page")
	    .get("/actors"))
		.pause(2)
		.exec(http("Addresse Index page")
			.get("/addresses"))
		.pause(4)
		.exec(http("Payments Index page")
			.get("/payments"))
		.pause(7)
		.exec(http("Inventories Index page")
			.get("/inventories"))
		.pause(3)
	}

	val users = scenario("Users").exec(Read.read)

	val httpConf = http 
		.baseURL("http://127.0.0.1")
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
		.doNotTrackHeader("1")
		.acceptLanguageHeader("en-US,en;q=0.5")
		.acceptEncodingHeader("gzip, deflate")
		.userAgentHeader("Mozilla/5.0 (Windows NT 5.1; rv:31.0) Gecko/20100101 Firefox/31.0")

	val scn = scenario("RubyOnRailsSakilaSimulation").exec(Read.read)

	setUp(
		users.inject(rampUsers(100) over (10 minutes))
		).protocols(httpConf)
}

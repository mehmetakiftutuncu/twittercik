import controllers._
import helpers.TestHelpers._
import org.specs2.mutable._
import play.api.mvc.Cookie
import play.api.test._

/**
 * Unit tests and specifications for authorization by Application.getSessionForRequest()
 */
class AuthorizationSpec extends Specification
{
  "Authorization mechanism" should {

    "get session for valid cookie" in new WithApplication with RandomSessionInsertingAndDeleting {
      Application.getSessionForRequest(FakeRequest().withCookies(Cookie(
        name = "logged_user",
        value = testSession.cookieid,
        maxAge = Option(60 * 60 * 24 * 15)))) must beSome.which(_.username == testSession.username)
    }

    "not be able to get session for invalid cookieid" in new WithApplication with RandomSessionInsertingAndDeleting {
      Application.getSessionForRequest(FakeRequest().withCookies(Cookie(
        name = "logged_user",
        value = generateRandomText,
        maxAge = Option(60 * 60 * 24 * 15)))) must beNone
    }

    "get session for valid session cookie" in new WithApplication with RandomSessionInsertingAndDeleting {
      Application.getSessionForRequest(FakeRequest()
        .withSession("logged_user" -> testSession.cookieid)) must beSome.which(_.username == testSession.username)
    }

    "not be able to get session for invalid session cookieid" in new WithApplication with RandomSessionInsertingAndDeleting {
      Application.getSessionForRequest(FakeRequest()
        .withSession("logged_user" -> generateRandomText)) must beNone
    }

    "not be able to get session for request without credentials" in new WithApplication with RandomSessionInsertingAndDeleting {
      Application.getSessionForRequest(FakeRequest()) must beNone
    }
  }
}
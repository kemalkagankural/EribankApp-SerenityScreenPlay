package steps;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.actors.OnlineCast;
import net.thucydides.core.annotations.Managed;
import org.junit.Before;
import org.openqa.selenium.WebDriver;
import tasks.LoginToEriBank;
import ui.HomePageElements;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;

public class AuthSteps {
    @Managed(driver = "Appium")
    public WebDriver herMobileDevice;

    String actorName="kemal";
    Actor actor = Actor.named(actorName);

    @Before
    public void set_the_stage() {
        OnStage.setTheStage(new OnlineCast());
    }

    @When("I login with username company and password company")
    public void i_login_with_username_company_and_password_company() {
        actor.can(BrowseTheWeb.with(herMobileDevice));
        actor.attemptsTo(LoginToEriBank.login("company","company"));
    }
    @Then("you should see the home page")
    public void you_should_see_the_home_page() {
        actor.should(seeThat((Question<Boolean>) HomePageElements.PAYMENT_BTN));
    }

}

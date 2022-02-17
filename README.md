# EribankApp SerenityScreenPlay

## Description
In this project I will be creating a screenplay for the EribankApp.Also I use Appium to automate the testing.
In this project I have two scenarios:

## The first scenario

```Gherkin
Feature: I as a user want to authenticate in the application to check my credentials

 
  Scenario: check login with correct username and password
    When I login with username company and password company
    Then you should see the home page

```
## The second scenario
I make a payment to three different clients.Client countries are CN, USA, JPN.Client amounts are 10, 20, 30.After that for each payment user's balance should be checked.

```Gherkin
Feature: make a payment to three different client
  
  Scenario Outline: make a payment
    When  user login
    And   user make deposit via "<phone>" "<name>" "<amount>" "<client>"
    Then user should check
    And user will logout
    Examples:

      | phone | name | amount | client|
      | 00000 | name | 10 | China     |
      | 00001 | name | 20 | USA     |
      | 00002 | name | 30 | India     |

```

### The Screenplay implementation
The sample code in the master branch uses the Screenplay pattern. The Screenplay pattern describes tests in terms of actors and the tasks they perform. Tasks are represented as objects performed by an actor, rather than methods. This makes them more flexible and composable, at the cost of being a bit more wordy. Here is an example:

```java
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
```

For second scenario:
```java
public class PaymentSteps {
    @Managed(driver = "Appium")
    public WebDriver herMobileDevice;

    String actorName="kemal";
    Actor actor = Actor.named(actorName);

    @Before
    public void set_the_stage() {
        OnStage.setTheStage(new OnlineCast());
    }

    @When("user login")
    public void user_login() {
        actor.can(BrowseTheWeb.with(herMobileDevice));
        actor.attemptsTo(LoginToEriBank.login("company","company"));
    }

    @And("user make deposit via {string} {string} {string} {string}")
    public void userMakeDepositVia(String phone, String name, String amount,String client) {
        actor.attemptsTo(Payment.type(phone, name, amount,client));
    }

    @Then("user should check")
    public void user_should_check() {
        actor.attemptsTo(
                Ensure.that(HomePageElements.BALANCE_CHECK).attribute("content-desc").startsWith("Your balance is")
        );
    }
    @And("user will logout")
    public void userWillLogout() {
        actor.attemptsTo(Logout.logout());
    }
    
}
```

## The Screenplay Tasks
First one is the LoginToEriBank.java class.

```java
public class LoginToEriBank implements Task {
    private String username="";
    private String password="";

    public LoginToEriBank(String username, String password) {

        this.username = username;
        this.password = password;
    }


    @Override
    @Step("{0} logins to the eribank")
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Click.on(LoginPageElements.USERNAME_FIELD),
                SendKeys.of(this.username).into(LoginPageElements.USERNAME_FIELD),
                Click.on(LoginPageElements.PASSWORD_FIELD),
                SendKeys.of(this.password).into(LoginPageElements.PASSWORD_FIELD),
                Click.on(LoginPageElements.LOGIN_BTN)
        );
    }

    public static LoginToEriBank login(String username, String password){
        return instrumented(LoginToEriBank.class, username,password);
    }
}
```
Second one is the Payment.java class.
```java
public class Payment implements Task {

    private String phone="";
    private String name ="";
    private String amount ="";
    private String client = "";


    public Payment(String phone,String name,String amount,String client) {
        this.phone = phone;
        this.name = name;
        this.amount = amount;
        this.client = client;

    }

    @Override
    @Step("{0} deposite to the eribank")
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Click.on(HomePageElements.PAYMENT_BTN),
                Click.on(PaymentPageElements.PHONE_FIELD),
                SendKeys.of(this.phone).into(PaymentPageElements.PHONE_FIELD),
                Click.on(PaymentPageElements.NAME_FIELD),
                SendKeys.of(this.name).into(PaymentPageElements.NAME_FIELD),
                SendKeys.of(this.amount).into(PaymentPageElements.AMOUNT_SCROLL),
                Click.on(PaymentPageElements.COUNTRY_BUTTON),
                Click.on(By.xpath("//android.widget.TextView[@text='"+this.client+"']")),
                Click.on(PaymentPageElements.PAYMENT_BUTTON),
                Click.on(PaymentPageElements.CONFIRM_BUTTON)
        );
    }
    public static  Payment type(String phone, String name, String amount,String client){
        return  instrumented(Payment.class,phone,name,amount,client);
    }
}
```

Third one is the Logout.java class.
```java
public class Logout implements Task {
    @Override
    @Step("{0} deposite to the eribank")
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Click.on(HomePageElements.LOGOUT_BTN)
        );
    }
    public static  Logout logout(){
        return  instrumented(Logout.class);
    }
}

```

Also You can see all elements in ui file.I made three file for Page Elements
These are HomePageElements.java,LoginPageElements.java and PaymentPageElements.java and 
When I created Page Elements I used to Serenetiy Bdd Screenplay Target method.

## How can I import Appium Driver?
Firstly, I created serenity.properties file.In this file I added the following lines.

```java
        webdriver.driver= appium
        appium.hub = http://localhost:4723/wd/hub
        
        ####### Android CAPS ######
        appium.automationName=uiautomator2
        appium.platformName = ANDROID
        appium.platformVersion = 7.0
        appium.avd  =Pixel_3a_API_24
        appium.udid = emulator-5554
        appium.app =  C:\\Users\\kemal\\OneDrive\\Masaüstü\\n11\\apk\\EriBank.apk
```

Also I want to explain the  Appium.
Appium is an open source ecosystem of tools and libraries that help you drive your mobile apps (native, hybrid, and mobile web on platforms such as Android, iOS), desktop apps (Windows, Mac), and now even on platforms like Smart TV and much more


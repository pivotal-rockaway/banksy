package io.pivotal.payup.acceptancetests;

import io.pivotal.payup.Application;
import org.fluentlenium.adapter.FluentTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.fluentlenium.core.filter.FilterConstructor.withText;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.core.IsEqual.equalTo;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest({"server.port=0"})
public class AccountsTest extends FluentTest {

    @Value("${local.server.port}")
    private int port;

    private String baseUrl;

    @Before
    public void setUp() throws Exception {
        baseUrl = "http://localhost:" + port;
    }

    @Test
    public void shouldShowNewAccountsForm() {
        goTo(baseUrl + "/accounts/new");
        assertThat(find("h2").getText(), equalTo("New account"));
    }

    @Test
    public void shouldCreateANewAccount() throws Exception {
        goTo(baseUrl + "/accounts/new");
        fill("#newAccountName").with("Nick");
        click("button");

        assertThat(find("dd", withText("Nick")), not(empty()));
    }

    @Test
    public void shouldDepositAmountToAccount(){
        goTo(baseUrl + "/accounts/new");
        fill("#newAccountName").with("Nick");
        click("button");
        fill("#depositAmount").with("200");
        click("button");
        assertThat(find("dd", withText("200")), not(empty()));

        fill("#depositAmount").with("150");
        click("button");
        assertThat(find("dd", withText("350")), not(empty()));
    }

    @Test
    public void shouldWithdrawAmountFromAccount(){
        goTo(baseUrl + "/accounts/new");
        fill("#newAccountName").with("Nick");
        click("button");
        fill("#depositAmount").with("200");
        find("button", withText("Deposit")).click();
        fill("#withdrawAmount").with("20");
        find("button", withText("Withdraw")).click();
        assertThat(find("dd",withText("180")),not(empty()));
    }

    @Override
    public WebDriver getDefaultDriver(){
        System.setProperty("webdriver.chrome.driver", "/Applications/chromedriver");
        return new ChromeDriver();
    }
}

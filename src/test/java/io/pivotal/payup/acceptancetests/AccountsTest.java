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
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasItem;
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
        createNewAccountWithName("Nick");

        assertThat(find("dd", withText("Nick")), not(empty()));
    }

    @Test
    public void shouldDepositAmountToAccount() {
        createNewAccountWithName("Nick");

        fill("#depositAmount").with("200");
        find("button", withText("Deposit")).click();
        assertThat(find("dd", withText("200")), not(empty()));

        fill("#depositAmount").with("150");
        find("button", withText("Deposit")).click();
        assertThat(find("dd", withText("350")), not(empty()));
    }

    @Test
    public void shouldWithdrawAmountFromAccount() {
        createNewAccountWithName("Nick");

        fill("#depositAmount").with("200");
        find("button", withText("Deposit")).click();
        fill("#withdrawAmount").with("20");
        find("button", withText("Withdraw")).click();

        assertThat(find("dd", withText("180")), not(empty()));
        find("button", withText("Deposit")).click();
    }

    @Test
    public void shouldListAllAccounts() {
        createNewAccountWithName("Checking 1");
        createNewAccountWithName("Checking 2");
        createNewAccountWithName("Checking 3");

        goTo(baseUrl + "/accounts");

        assertThat(find(".account", withText("Checking 1 0")), not(empty()));
        assertThat(find(".account", withText("Checking 2 0")), not(empty()));
        assertThat(find(".account", withText("Checking 3 0")), not(empty()));
        assertThat(find("a", withText("Transfer Funds")), not(empty()));

    }

    @Test
    public void shouldTransferBetweenAccounts() {
        createNewAccountWithName("Checking 1");
        fill("#depositAmount").with("2000");
        find("button", withText("Deposit")).click();
        createNewAccountWithName("Checking 2");
        fill("#depositAmount").with("4000");
        find("button", withText("Deposit")).click();

        goTo(baseUrl + "/accounts");
        find("a", withText("Transfer Funds")).click();

        fill("#fromAccountName").with("Checking 1");
        fill("#toAccountName").with("Checking 2");
        fill("#amount").with("500");
        fill("#description").with("Credit Card Payment");
        find("button", withText("Initiate Transfer")).click();

        assertThat(find(".account").getTexts(), hasItem(containsString("Checking 1 1500")));
        assertThat(find(".account").getTexts(), hasItem(containsString("Checking 2 4500")));
    }

    @Test
    public void shouldValidateNegativeBalance() {
        createNewAccountWithName("Checking 1");
        fill("#depositAmount").with("2000");
        find("button", withText("Deposit")).click();
        fill("#withdrawAmount").with("2500");
        find("button", withText("Withdraw")).click();
        assertThat(find("#error", withText("You Can't Exceed Your Current Balance")), not(empty()));
    }

    @Test
    public void shouldValidateNegativeBalanceWhenTransfer() {
        createNewAccountWithName("Checking 1");
        fill("#depositAmount").with("2000");
        find("button", withText("Deposit")).click();
        createNewAccountWithName("Checking 2");
        fill("#depositAmount").with("4000");

        find("button", withText("Deposit")).click();

        goTo(baseUrl + "/accounts");
        find("a", withText("Transfer Funds")).click();
        fill("#fromAccountName").with("Checking 1");
        fill("#toAccountName").with("Checking 2");
        fill("#amount").with("2500");
        fill("#description").with("Credit Card Payment");

        find("button", withText("Initiate Transfer")).click();
        assertThat(find("#error", withText("You Can't Exceed Your Current Balance")), not(empty()));
    }


    @Test
    public void shouldShowAllTransactionsForAnAccount() {
        createNewAccountWithName("Checking");
        fill("#depositAmount").with("2000");
        find("button", withText("Deposit")).click();

        fill("#withdrawAmount").with("1000");
        find("button", withText("Withdraw")).click();

        createNewAccountWithName("Savings");
        fill("#depositAmount").with("1000");
        find("button", withText("Deposit")).click();

        goTo(baseUrl + "/accounts");
        find("a", withText("Transfer Funds")).click();

        fill("#fromAccountName").with("Checking");
        fill("#toAccountName").with("Savings");
        fill("#amount").with("500");
        fill("#description").with("Credit Card Payment");
        find("button", withText("Initiate Transfer")).click();

        goTo(baseUrl + "/Checking/transactions");

        assertThat(find("tr", withText("Deposit 2000")), not(empty()));
        assertThat(find("tr", withText("Withdraw 1000")), not(empty()));
        assertThat(find("tr", withText("Withdraw 500")), not(empty()));
    }

    private void createNewAccountWithName(String name) {
        goTo(baseUrl + "/accounts/new");
        fill("#newAccountName").with(name);
        click("button");
    }

    @Override
    public WebDriver getDefaultDriver() {
        System.setProperty("webdriver.chrome.driver", "/Applications/chromedriver");
        return new ChromeDriver();
    }
}

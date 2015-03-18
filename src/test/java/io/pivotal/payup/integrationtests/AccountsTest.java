package io.pivotal.payup.integrationtests;

import io.pivotal.payup.Application;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class AccountsTest {

    @Autowired
    protected WebApplicationContext wac;

    private MockMvc mvc;

    @Before
    public void setUp() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void shouldShowNewAccountsForm() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/accounts/new").accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("New account")));
    }

    @Test
    public void shouldCreateANewAccount() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/accounts")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "Nick");

        mvc.perform(request)
                .andDo(print())
                .andExpect(redirectedUrl("/accounts/Nick"));

        mvc.perform(MockMvcRequestBuilders.get("/accounts/Nick").accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("<dt>Name</dt>")))
                .andExpect(content().string(containsString("<dd>Nick</dd>")));
    }
}
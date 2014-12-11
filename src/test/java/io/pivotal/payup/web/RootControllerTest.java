package io.pivotal.payup.web;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class RootControllerTest {

    private RootController controller;

    @Before
    public void before() {
        controller = new RootController();
    }

    @Test
    public void shouldRedirect(){
        assertThat(controller.root(), equalTo("redirect:/accounts/new"));
    }
}

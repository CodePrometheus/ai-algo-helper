package com.codeprometheus.aialgohelper.plugin.actions.toolbar;

import org.junit.Test;

import static org.junit.Assert.assertFalse;

public class LoginActionTest {

    @Test
    public void doesNotRequireSavedConfiguration() {
        assertFalse(new LoginAction().requiresConfiguration());
    }
}

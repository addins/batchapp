package org.addin.batchapp.cucumber.stepdefs;

import org.addin.batchapp.BatchappApp;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;

import org.springframework.boot.test.context.SpringBootTest;

@WebAppConfiguration
@SpringBootTest
@ContextConfiguration(classes = BatchappApp.class)
public abstract class StepDefs {

    protected ResultActions actions;

}

package com.automation.jira.zephyr.api;

import com.automation.configuration.JiraConfiguration;
import com.automation.configuration.PropertiesValue;
import com.automation.jira.ApiEndPoints;
import com.automation.jira.RequestBuilder;
import com.automation.jira.beanclass.Execution;
import com.automation.jira.beanclass.JiraResult;
import com.automation.jira.beanclass.TestResult;
import com.automation.jira.beanclass.TestStatus;
import com.automation.utility.ExtentReport;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.model.Test;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.reflect.TypeToken;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public class AutomationResultapi {
  private static Logger logger = LogManager.getLogger(AutomationResultapi.class);
  @Autowired
  private RequestBuilder requestBuilder;
  @Autowired
  private JiraConfiguration jc;
  @Autowired
  private ExtentReport extentreport;
  @Autowired
  private PropertiesValue properties;

  /**
   * method used to post automation result to jira.
   * @param file contain the zip file need to send
   */
  public void postTestResult(File file) {

    RequestSpecification rs = requestBuilder.postDataResult(ApiEndPoints.TEST_AUTOMATION_RESULT,
                    file, jc.getJiraProjectkey());
    Response res = RestAssured.given(rs).post();
    logger.debug(res.asString());
    Type type = new TypeToken<JiraResult>() {}.getType();
    JiraResult jiraresult = res.as(type);
  }

  /**
   * method is used to result file of autoamtion to update in jira.
   * @param filename is location name filename in which file has to created.
   * @return location of result file
   */
  public Path jiraresult(String filename) {

    List<Test> tests =  extentreport.getTestDetail();

    List<Execution> execution = new ArrayList<>();

    for (Test test : tests) {
      Execution ex = new Execution();
      if (test.getStatus().equals(Status.PASS)) {
        ex.setTestResult("Passed");
      } else {
        ex.setTestResult("Failed");
      }
      TestStatus ts = new TestStatus();
      ts.setTestcaseId(test.getName());

      ex.setTeststatus(ts);
      execution.add(ex);
    }

    TestResult result = new TestResult();
    result.setVersion(1);
    result.setExecutions(execution);

    File file = null;
    try {
      //create a json file 
      ObjectMapper resultjson = new ObjectMapper();
      file = new File(properties.getTemplocation() + File.separator + filename);
      resultjson.writeValue(file, result);
    } catch (JsonGenerationException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (JsonMappingException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return file.toPath();
  }

}

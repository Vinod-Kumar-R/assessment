package com.automation.facebook;

import com.automation.configuration.PropertiesValue;
import com.automation.utility.GenericMethod;
import com.automation.utility.WaitMethod;
import com.automation.webdriver.BrowserInitialize;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;

public class Facebook {
  
  private static Logger logger = LogManager.getLogger(Facebook.class.getName());
  private WebDriver driver;
  
  @Autowired
  private GenericMethod genericmethod;
  @Autowired
  private WaitMethod waitmethod;
  @Autowired
  private PropertiesValue properties;
  @Autowired
  private BrowserInitialize browserinitialize;

  
  /**
   * Method is used to login for facebook page.
   * @param dataParam contain username and password
   * @return "pass" after login to facebook page.
   */
  public String login(List<String> dataParam) {
    logger.debug("Opening the URL " + properties.getFacebookUrl());
    driver = browserinitialize.getWebDriverInstance();
    driver.get(properties.getFacebookUrl());
    
    logger.debug("Enter username");
    WebElement element = genericmethod.getElement("username");
    element.sendKeys(dataParam.get(0));
    
    logger.debug("Enter the password");
    element = genericmethod.getElement("password");
    element.sendKeys(dataParam.get(1));
    
    logger.debug("click on the login button");
    element = genericmethod.getElement("login");
    element.click();
    
    //verify page title 
    String title = driver.getTitle();
    
    if (!title.contains("Facebook")) {
      return "fail";
    }
    return "Pass";
  }
  
  /**
   * Method used to post the message in feed.
   * @param dataParam
   *      dataParam[0] contain the message which need to feed. 
   * @return "pass" if message has been post
   */
  public String postMessage(List<String> dataParam) {
    
    logger.debug("wait for Home button");
    waitmethod.waitForElementPresent("home");
    
    logger.debug("click on the home button");
    WebElement element = genericmethod.getElement("home");
    element.click();
    
    logger.debug("click on the post message text box");
    waitmethod.waitForElementPresent("post");
    element = genericmethod.getElement("post");
    element.click();
    
    logger.debug("Type the message for post");
    waitmethod.waitForElementPresent("postMessageType");
    element = genericmethod.getElement("postMessageType");
    element.sendKeys(dataParam.get(0));
    
    logger.debug("click on the post button");
    element = genericmethod.getElement("postmessage");
    element.submit();
    
    return "pass";
  }
  
}

package com.automation.wallethub.insurance;

import com.automation.configuration.PropertiesValue;
import com.automation.custom.webelement.ReveiwFeed;
import com.automation.utility.GenericMethod;
import com.automation.utility.WaitMethod;
import com.automation.webdriver.BrowserInitialize;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;

public class WalletHub {
  
  private static Logger logger = LogManager.getLogger(WalletHub.class.getName());
  private WebDriver driver;
  
  @Autowired
  private GenericMethod genericmethod;
  @Autowired
  private WaitMethod waitmethod;
  @Autowired
  private PropertiesValue properties;
  @Autowired
  private BrowserInitialize browserinitialize;
  @Autowired
  private ReveiwFeed reveiw;
  
  /**
   * Method is used to login to WalletHub.
   * @param dataParam
   *     dataParam[0] contain username
   *     dataParam[1] contain password.
   * @return "pass" after login to wallethub and verify the title of the page.
   */
  public String login(List<String> dataParam) {
    
    logger.debug("Opening the URL " + properties.getWallethubUrl());
    driver = browserinitialize.getWebDriverInstance();
    driver.get(properties.getWallethubUrl());
    
    logger.debug("click on the login button");
    waitmethod.waitForElementPresent("wallethub_login");
    WebElement element = genericmethod.getElement("wallethub_login");
    element.click();
    
    logger.debug("Enter the Email address");
    waitmethod.waitForElementPresent("wallethub_email");
    element = genericmethod.getElement("wallethub_email");
    element.sendKeys(dataParam.get(0));
    
    logger.debug("Enter the password");
    element = genericmethod.getElement("wallethub_password");
    element.sendKeys(dataParam.get(1));
    
    logger.debug("click on the login button");
    element = genericmethod.getElement("wallethub_submit");
    element.click();
    
    return "pass";
  }
  
  /**
   * Method used to mouse over the rating and verify the start are filled with color.
   * @param dataParam 
   *      dataParam[0] contain the Integer value to select number of star.
   *      dataParam[1] contain the color valued has to show on hover.
   * @return "pass" after mouse over the star and verify the color of star.
   */
  public String starrating(List<String> dataParam) {

    logger.debug("wait for Review Star element");
    waitmethod.waitForElementPresent("walletthub_ratingstart");
    List<WebElement> elements = genericmethod.getElements("walletthub_ratingstart");
    
    // initialize i to 1 to mouse over 
    int i = 1;
    for (WebElement element : elements) {
      
      if (i < Integer.valueOf(dataParam.get(0))) {
        
        logger.debug("Mouse over on the star in Review");
        genericmethod.mouseover(element);
        
        logger.debug("Get the Star color of after mouse over");
        WebElement color = genericmethod.getWebElement(element, "walletthub_ratingcolor");
        
        logger.debug("filled color of Star in RGB -->" + color.getCssValue("fill"));
        logger.debug("check the color of start has been changed");

        if (!dataParam.get(1).equalsIgnoreCase(color.getCssValue("fill"))) {
          return "fail";
        }
        // increment the i value for mouse over
        i++;
      } else {
        element.click();
        break;
      }
    }
    return "pass";

  }
  
  /**
   * Method is used write a data to review comment for particular drop down select.
   * @param dataParam
   *     dataParam[0] contain drop down data to be select
   *     dataParam[1] contain comment to update for it.
   * @return "pass" after updating the comment and submit it.
   */
  public String reviewcomment(List<String> dataParam) {
    
    logger.debug("wait for drop down");
    waitmethod.waitForElementPresent("wallethub_select");
    
    logger.debug("click on the review submit button");
    WebElement element = genericmethod.getElement("wallethub_submitreivew");
    element.click();
    
    logger.debug("click on the dorp down button");
    element = genericmethod.getElement("wallethub_select");
    element.click();
    
    logger.debug("Get the list of drop down list value");
    List<WebElement> elements = genericmethod.getElements("wallethub_options");
    
    //loop through the list and click on particular value
    for (WebElement list : elements) {
      
      logger.debug("wait for drop down list show");
      waitmethod.waitForElementVisible(list);
      
      logger.debug("drop down list value --->" + list.getText());
      if (list.getText().equals(dataParam.get(0))) {
        logger.debug("Text found and click on it");
        list.click();
        break;
      }
    }
    
    logger.debug("Writing review comment");
    element = genericmethod.getElement("wallethub_review");
    element.sendKeys(dataParam.get(1));
    
    logger.debug("click on the review submit button");
    element = genericmethod.getElement("wallethub_submitreivew");
    element.click();
    
    logger.debug("wait for confirmation message");
    waitmethod.waitForElementPresent("wallethub_successreview");
    
    logger.debug("verify the message display");
    element = genericmethod.getElement("wallethub_successreview");
    if (!dataParam.get(2).equals(element.getText())) {
      return "fail";
    }
    return "Pass";
  }
  
  /**
   * Method is used to go any one of the page i.e. Profile, Settings, Logout
   * @param dataParam
   *      dataParam[0] contain any of "Profile", "Settings","Logout"
   * @return "pass" after execution
   */
  public String profile(List<String> dataParam) {
    
    logger.debug("wait for the login user to show");
    waitmethod.waitForElementPresent("wallethub_loginuser");
    
    logger.debug("Mouse over on the user name");
    WebElement element = genericmethod.getElement("wallethub_loginuser");
    genericmethod.mouseover(element);
    
    logger.debug("get the list of options");
    waitmethod.waitForElementPresent("wallethub_profile");
    List<WebElement> userprofiles = genericmethod.getElements("wallethub_profile");
    for (WebElement profile : userprofiles) {
      waitmethod.waitForElementVisible(profile);
      if (profile.getText().equals(dataParam.get(0))) {
        profile.click();
        break;
      }
    }
    return "Pass";
  }
  
  /**
   * Method is used to verify the feed in the home page, 
   * loop through all the visible feed to verify it.
   * @param dataParam
   *      dataParam[0] contain verification name in feed.
   *      dataParam[1] contain verification for user received star
   *      dataParam[2] contain verification of comment
   * @return verification data are present then return "pass" else "fail"
   */
  public String verifyfeed(List<String> dataParam) {
    
    logger.debug("wait for feed message to display");
    waitmethod.waitForElementPresent("wallethub_feedreviewlist");
    
    List<WebElement> revewfeeds = genericmethod.getElements("wallethub_feedreviewlist");
    
    for (WebElement revewfeed : revewfeeds) {
      reveiw.setElement(revewfeed);
      logger.debug("username---->" + reveiw.getUsername());
      logger.debug("ratting---->" + reveiw.getRatting());
      logger.debug("descripton---->" + reveiw.getDescription());
      
      if (reveiw.getUsername().equals(dataParam.get(0))
                      && reveiw.getRatting() == Integer.valueOf(dataParam.get(1))
                      && reveiw.getDescription().equals(dataParam.get(2).trim())) {
        return "pass";
      }
    }
    return "fail";
  }
  
  /**
   * Method is used to redirect to home page.
   * @return "pass"
   */
  public String gotoHomepage() {
    
    waitmethod.waitForElementPresent("wallethub_profilepage");
    WebElement element = genericmethod.getElement("wallethub_profilepage");
    element.click();
    return "pass";
  }
}

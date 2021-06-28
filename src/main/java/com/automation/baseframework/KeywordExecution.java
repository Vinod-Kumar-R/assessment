package com.automation.baseframework;

import com.automation.facebook.Facebook;
import com.automation.utility.ExtentReport;
import com.automation.utility.GenericMethod;
import com.automation.wallethub.insurance.WalletHub;
import com.automation.webdriver.BrowserInitialize;
import com.aventstack.extentreports.Status;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * This class contain the keyword which need to executed after reading from the test script file.
 * @author Vinod Kumar R
 *
 */
public class KeywordExecution {
  private static Logger logger = LogManager.getLogger(KeywordExecution.class.getName());
  @Autowired
  private GenericMethod genericMethod;
  @Autowired
  private ExtentReport extentReport;
  private String status;
  private KeywordType keyword;
  @Autowired
  private BrowserInitialize browserinitialize;
  @Autowired
  private Facebook facebook;
  @Autowired
  private WalletHub wallethub;
  @Autowired
  private ExtentReport extenreport;



  /**
   * This method is used to set the value for keyword.
   * @param keyword which will be read from Test Data file and executed method
   */
  public void setvalue(KeywordType keyword) {
    this.keyword = keyword;

  }

  /**
   * This method is used to executed the keyword which is read from Test Script file.
   * 
   * <p>the 2nd column in the Test script file is called Keyword 
   * @param dataParam contain the array of string data which is required executed particular
   *     keyword (nothing but Method)
   * 
   */
  public void executed(List<String> dataParam)  {

    switch (keyword) {

      case quitbrowser :
        status = browserinitialize.quitBrowser();
        logger.debug("QuiteBrowser");
        testResult(status, keyword.toString());
        break;

      case browsertype:
        status = genericMethod.browsertype(dataParam.get(0));
        logger.debug("Opening the browser");
        testResult(status, keyword.toString());
        break;
        
      case closebrowser:
        logger.debug("closing the browser");
        genericMethod.browserClose();
        testResult("pass", keyword.toString());
        break;

      case facebooklogin:
        logger.debug("login to facebook");
        status = facebook.login(dataParam);
        testResult(status, keyword.toString());
        break;
        
      case postmessage:
        logger.debug("post the data");
        status = facebook.postMessage(dataParam);
        testResult(status, keyword.toString());
        extenreport.writeLog(Status.PASS, "", genericMethod.takeScreenshot());
        break;
        
      case wallethublogin:
        logger.debug("login to wallethub");
        status = wallethub.login(dataParam);
        testResult(status, keyword.toString());
        break;
        
      case rating:
        logger.debug("rating and verify");
        status = wallethub.starrating(dataParam);
        testResult(status, keyword.toString());
        break;
        
      case reviewcomment:
        logger.debug("rating and verify");
        status = wallethub.reviewcomment(dataParam);
        testResult(status, keyword.toString());
        break;
        
      case userinfo:
        logger.debug("profile verification");
        status = wallethub.profile(dataParam);
        testResult(status, keyword.toString());
        break;
        
      case verifyfeed:
        logger.debug("feed verification");
        status = wallethub.verifyfeed(dataParam);
        testResult(status, keyword.toString());
        break;
      
      case homepage:
        logger.debug("feed verification");
        status = wallethub.gotoHomepage();
        testResult(status, keyword.toString());
        break;
        
      default: logger.debug("Invalid Keyword");

    }
  }

  /**
   * This method capture the status of each keyword by saying pass or fail.
   * 
   * @param status it contain pass or fail 
   * @param message Message contain information of the keyword executed
   */
  public void testResult(String status, String message)   {
    if (status.equalsIgnoreCase("Pass")) {
      extentReport.writeLog(Status.PASS, message);
    } else {
      extentReport.attachScreenshot(genericMethod.takeScreenshot());
      extentReport.writeLog(Status.FAIL, message);
    }
  }

}

package com.automation.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PropertiesValue {
  
  @Value("${facebookurl}")
  private String facebookUrl;

  @Value("${wallethuburl}")
  private String wallethubUrl;
  
  @Value("${appiumServerurl}")
  private String appiumUrl;
  
  @Value("${browserstackurl}")
  private String browserStackUrl;
  
  @Value("${testcase}")
  private String testcase;
  
  @Value("${testData}")
  private String testdata;
  
  @Value("${testobject}")
  private String testobject;
  
  @Value("${test_execution}")
  private String testBrowser;
  
  @Value("${headlessbrowser}")
  private Boolean headlessBrowser;
  
  @Value("${explictwait}")
  private int explictwait;
  
  @Value("${polling}")
  private int polling;

  @Value("${sendemail}")
  private boolean sendemail;
  
  @Value("${automation}/properties/extentreportpropertes.xml")
  private String extentReportsPropeties;
  
  @Value("${automation}")
  private String configLocation;
  
  @Value("${automation}/properties/klov.properties")
  private String klovrproperties;
  
  @Value("${klov}")
  private boolean isKlov;
  
  @Value("${jiraintegration}")
  private boolean isJiraIntegration;
  
  private String extentreportlocation;
  
  private String templocation;
  
  public String getTemplocation() {
    return templocation;
  }

  public void setTemplocation(String templocation) {
    this.templocation = templocation;
  }

  public String getExtentreportlocation() {
    return extentreportlocation;
  }

  public void setExtentreportlocation(String extentreportlocation) {
    this.extentreportlocation = extentreportlocation;
  }

  public boolean isKlov() {
    return isKlov;
  }
  
  public boolean isJiraIntegration() {
    return isJiraIntegration;
  }
  
  public String getBrowserStackUrl() {
    return browserStackUrl;
  }

  public String getklovrproperties() {
    return klovrproperties;
  }

  public String getConfigLocation() {
    return configLocation;
  }

  public String getExtentReportsPropeties() {
    return extentReportsPropeties;
  }

  public String getAppiumUrl() {
    return appiumUrl;
  }

  public String getTestcase() {
    return testcase;
  }

  public String getTestdata() {
    return testdata;
  }

  public String getTestobject() {
    return testobject;
  }

  public String getTestBrowser() {
    return testBrowser;
  }

  public Boolean getHeadlessBrowser() {
    return headlessBrowser;
  }

  public int getExplictwait() {
    return explictwait;
  }

  public int getPolling() {
    return polling;
  }

  public boolean isSendemail() {
    return sendemail;
  }

  public String getFacebookUrl() {
    return facebookUrl;
  }

  public String getWallethubUrl() {
    return wallethubUrl;
  }

  public String getKlovrproperties() {
    return klovrproperties;
  }

}

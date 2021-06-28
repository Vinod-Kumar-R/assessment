package com.automation.custom.webelement;

import com.automation.utility.GenericMethod;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;

public class ReveiwFeed {
  
  private static Logger logger = LogManager.getLogger(ReveiwFeed.class);
  WebElement element;
  @Autowired
  private GenericMethod genericmethod;
  
  public void setElement(WebElement element) {
    this.element = element;
  }
  
  public String getUsername() {
    WebElement username = genericmethod.getWebElement(this.element, "wallethub_reviewname");
    return username.getText().trim();
  }
  
  public int getRatting() {
    List<WebElement> stars = genericmethod.getWebElements(this.element, "wallethub_reviewstar");
    return stars.size();
  }
  
  public String getDescription() {
    WebElement text = genericmethod.getWebElement(this.element, "wallethub_reviewtext");
    return text.getText();
  }

}

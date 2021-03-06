package com.automation.custom.wait;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.springframework.context.annotation.EnableAspectJAutoProxy;


public class NotificationDisAppear implements ExpectedCondition<Boolean> {
  private static Logger logger = LogManager.getLogger(NotificationDisAppear.class.getName());
  private WebElement element;
 
  
  public NotificationDisAppear(WebElement element) {
    super();
    this.element = element;
  }
  
  

  @Override
  public @Nullable Boolean apply(WebDriver driver) {
    // TODO Auto-generated method stub
    
    logger.debug("waiting for the notification");
    
    int notificationsize = this.element.findElements(By.xpath("//nz-notification")).size();
    
    if (notificationsize == 0) {
      logger.debug("notification size is equal to zero");
      return true;
    }
    logger.debug("notification size is greater then zero");

    return false;
  }

}

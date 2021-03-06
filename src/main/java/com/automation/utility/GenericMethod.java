package com.automation.utility;

import com.automation.configuration.ApplicationStoreValue;
import com.automation.configuration.ConstantVariable;
import com.automation.configuration.PropertiesValue;
import com.automation.webdriver.BrowserInitialize;
import com.paulhammant.ngwebdriver.ByAngular;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.ui.Select;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * This class contain the Generic method which is used during Automation execution Script. 
 * @author Vinod Kumar R
 *
 */
public class GenericMethod {
  static Logger logger = LogManager.getLogger(GenericMethod.class.getName());

  @Autowired
  private ApplicationStoreValue storevalue;
  @Autowired
  private BrowserInitialize browserinitialize; 
  @Autowired
  private PropertiesValue properties;
  @Autowired
  private WaitMethod waitmethod;
 

  /**
   * This is used to click on the WebElement on current html page.
   * @param dataParam
   *     dataParam is a array of String variable which hold data 
   *     dataParam[0] contain the Object i.e xpath 
   * @return it return the status "pass" if execution success else throw an exception 
   */
  public String click(String dataParam)  {
    WebElement element = getElement(dataParam);
    waitmethod.waitForElementClickable(element);
    element.click();
    return "pass";
  }

  /**
   * This method is used to verify the text present for an WebElement.
   * @param dataParam
   *     dataParam is a array of String variable which hold data 
   *     dataParam[0] contain the Object which need to compare the text  in html page
   *     dataParam[1] contain the expected text when need to compare with text on html page 
   * @return it return the status "pass" if execution success else throw an exception 
   */
  public String verifyText(List<String> dataParam)  {
    logger.debug("Verifying the text-------> " + dataParam.get(1));
    WebElement element = getElement(dataParam.get(0));
    if (element.getText().equalsIgnoreCase(dataParam.get(1))) {
      return "pass";
    }
    return "fail";
  }

  /**
   * This Method is used to take an WebBrowser Screenshot.
   * @return the file location in the String format
   */
  public  String takeScreenshot()   {
    WebDriver driver = browserinitialize.getWebDriverInstance();
    long filename = System.currentTimeMillis();
    if (driver instanceof TakesScreenshot) {
      File tempFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
      try {
        FileUtils.copyFile(tempFile, new File(ConstantVariable.ScreenShotlocation 
                        + File.separator + filename + ".png"));
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
        logger.error(e.getStackTrace());
      }
    }
    String absolutePath = ConstantVariable.ScreenShotlocation +  File.separator
                    + filename + ".png";
    logger.debug("absolutepath " + absolutePath);
    
    /*Block of code is comment becase in KLOV report required the absolute path rather 
     * then relative path 
     * relative path is used only for attach the extent repor to email 
     * as of now report are not attach to email so comment out the below code
    */ 
    /*
    String relativePath = new File(ConstantVariable.ResultLocation).toURI().relativize(
                    new File(absolutePath).toURI()).getPath();
    logger.debug(" relative path " + relativePath);
    return ".." + File.separator + relativePath;
    */
    return absolutePath;
  }
  
  /**
   * This method is used to override test execution script browser.
   * @param dataParam contain which browser need to switch
   * @return if success then return "pass" else "fail"
   */
  public String browsertype(String dataParam) {
    
    if (dataParam.equalsIgnoreCase("config")) {
      browserinitialize.setWebDriverInstance(properties.getTestBrowser());
    } else {
      browserinitialize.setWebDriverInstance(dataParam);
    }
    return "pass";
  }
  
  /**
   * This method is used to stored the current window information
   * and switch to newly created window of type browser.
   * @param dataParam contain the browser info
   * @return "pass" if execution success
   */
  public String browserSwtich(List<String> dataParam) {
    
    currentDriver(dataParam.get(0));
    browserinitialize.setSwitchDriverInstance(dataParam.get(1));
  
    return "pass";
  }
  
  /**
   * This method is used to store the current Driver instance.
   * @param key contain the name in which it has to store.
   */
  public void currentDriver(String key) {
    EventFiringWebDriver driver = (EventFiringWebDriver) browserinitialize.getWebDriverInstance();
    storevalue.driverinstance.put(key, driver);
 
  }
  
  /**
   * This method is used to switch between Driver instance.
   * @param key contain the name of instance key to which it has to set
   */
  public void switchDriver(String key) {
    EventFiringWebDriver driver = storevalue.driverinstance.get(key);
    logger.debug("dirver instance restore " + driver);
    browserinitialize.setDriverInstance(driver);
  }
  
  /**
   * This method is used to close the current browser.
   */
  public void browserClose() {
    browserinitialize.closeBrowser();
  }

  /**
   * This method is used to scroll until WebElement is view in web page.
   * @param element is a WebElement of web page
   * @return execution success then return pass
   */
  public String scrolltoelementTop(WebElement element) {
    WebDriver driver = browserinitialize.getWebDriverInstance();

    JavascriptExecutor je = (JavascriptExecutor) driver;
    je.executeScript("arguments[0].scrollIntoView(true);", element);

    return "pass";
  }
  
  /**
   * This method is used to scroll until WebElement is view in web page.
   * @param element is a WebElement of web page
   * @return execution success then return pass
   */
  public String scrolltoelementBottom(WebElement element) {
    WebDriver driver = browserinitialize.getWebDriverInstance();

    JavascriptExecutor je = (JavascriptExecutor) driver;
    je.executeScript("arguments[0].scrollIntoView(false);", element);

    return "pass";
  }
  
  /**
   * this method is used to scroll browser in y axis with specific.
   * negative value is used to scroll up and positive value is used scroll down
   * @param element is WebElement of element in which it has to scroll
   * @param offset is a int value in which it has to scroll.
   * @return pass if execution success
   */
  public String scrollToElementOffsetYaxix(WebElement element, int offset) {

    WebDriver driver = browserinitialize.getWebDriverInstance();

    JavascriptExecutor je = (JavascriptExecutor) driver;

    je.executeScript("window.scrollTo(" + element.getLocation().getX() + "," 
                    + (element.getLocation().getY() + offset) + ");");

    return "pass";
  }
  
  /**
   * This Element is used to get the WebElement for and element.
   * @param objectName this contain the object value of web page
   * @return the webelement
   */
  public WebElement getElement(String objectName) {
    WebDriver driver = browserinitialize.getWebDriverInstance();
    By byElement = byType(objectName);
    WebElement element = driver.findElement(byElement);
    return element;
  }

  /**
   * This method accept the By element type and return webElement.
   * @param byElement contain the DOM location 
   * @return WebElement
   */
  public WebElement getElement(By byElement) {
    WebDriver driver = browserinitialize.getWebDriverInstance();
    WebElement element = driver.findElement(byElement);
    return element;
  }

  /**
   * This Method is used to get the WebElements from an element.
   * @param object this contain the object value of web page
   * @return WebElement
   */
  public List<WebElement> getElements(String object) {
    WebDriver driver = browserinitialize.getWebDriverInstance();
    By byElement = byType(object);
    List<WebElement> element = driver.findElements(byElement);
    return element;
  }

  /**
   * This method is used to child WebElement.
   * @param element contain parent WebElement
   * @param object contain child WebElement location
   * @return WebElement
   */
  public WebElement getWebElement(WebElement element, String object) {
    By byElement = byType(object);
    return element.findElement(byElement);
  }

  /**
   * This method is used to get all the child WebElement list of parent Element.
   * @param element contain the parent WebElement
   * @param object contain the child WebElement location 
   * @return List&lt;WebElement&gt;
   */
  public List<WebElement> getWebElements(WebElement element, String object) {

    By byElement = byType(object);
    List<WebElement> elements = element.findElements(byElement);
    return elements;
  }
  
  /**
   * This method is used to get the object in the form of By.
   * @param objectName is String
   * @return ByElement
   */
  public By getBy(String objectName) {
    
    By byElement = byType(objectName);
    return byElement;
   
  }
  
  /**
   * This method is used to stored the current window with key name. 
   * @param key is used to uniquely identified 
   */
  public void currentWindow(String key) {
    WebDriver driver = browserinitialize.getWebDriverInstance();
    String uniquename = driver.getWindowHandle();
    storevalue.windowHandle.put(key, uniquename);
 
  }
  
  /**
   * This method is used to switch between the window by providing the key name.
   * @param key is used to get the unique Window
   */
  public void switchWindow(String key) {
    WebDriver driver = browserinitialize.getWebDriverInstance();
    String uniquename = storevalue.windowHandle.get(key);
    driver.switchTo().window(uniquename);
  }
  
  /**
   * This method is used to created new blank tab.
   */
  public void newTab() {
    WebDriver driver = browserinitialize.getWebDriverInstance();
    
    String a = "window.open('about:blank','_blank');";
    ((JavascriptExecutor ) driver). executeScript(a);
    
    Set<String> handles = driver.getWindowHandles();
    String currentWindowHandle = driver.getWindowHandle();
    for (String handle : handles) {
      if (!currentWindowHandle.equals(handle)) {
        driver.switchTo().window(handle);
      }
    }
    
  }
  
  /**
   * This method is used to switch to particular frame.
   * @param element contain the frame WebElement
   */
  public void switchframe(WebElement element) {
    WebDriver driver = browserinitialize.getWebDriverInstance();
    
    logger.debug("Switch to frame " + element);
    driver.switchTo().frame(element);
  }
  
  /**
   * This method is used to switch to parent frame.
   */
  public void switchframedefault() {
    WebDriver driver = browserinitialize.getWebDriverInstance();

    logger.debug("Switch to parent frame ");
    driver.switchTo().defaultContent();
  }
  
  /**
   * This method is used to mouse over  WebElement.
   * @param element is a WebElement
   */
  public void mouseover(WebElement element) {
    WebDriver driver = browserinitialize.getWebDriverInstance();
    
    Actions action = new Actions(driver);
    action.moveToElement(element).build().perform();
    
  }
  
  /**
  * This method used to refresh current page.
  */
  public void refreshPage() {

    WebDriver driver = browserinitialize.getWebDriverInstance();
    driver.navigate().refresh();
  }
 
  /**
   * This method is used to zip a folder.
   * @param sourcNoteseDirPath  is the source path folder to which it has to zip
   * @param zipFilePath  is the destination folder in which zip folder has to present
   * @return Path where zip file has been created.
   * @throws IOException  exception are throw when file not found exception
   */
  public Path zip(final String sourcNoteseDirPath, final String zipFilePath)
                  throws IOException {
    //delete the file if exist
    Files.deleteIfExists(Paths.get(zipFilePath));
    
    Path zipFile = Files.createFile(Paths.get(zipFilePath));

    Path sourceDirPath = Paths.get(sourcNoteseDirPath);
    try (ZipOutputStream zipOutputStream = new ZipOutputStream(Files.newOutputStream(zipFile));
                    Stream<Path> paths = Files.walk(sourceDirPath)) {
      paths
      .filter(path -> !Files.isDirectory(path))
      .forEach(path -> {
        
        ZipEntry zipEntry = new ZipEntry(sourceDirPath.relativize(path).toString());
        try {
          zipOutputStream.putNextEntry(zipEntry);
          Files.copy(path, zipOutputStream);
          zipOutputStream.closeEntry();
        } catch (IOException e) {
          System.err.println(e);
        }
      });
    }

    logger.debug("Zip is created at : " + zipFile);
    
    return zipFile;
  }
  
  /**
   * Method used to zip the single file
   * @param zippath source path
   * @param filetozip file which need to zip
   * @return file location of zip. 
   * @throws IOException if file not able to perform IO operation.
   */
  public File zipFile(String zippath, Path filetozip) throws IOException {
    Path zipFile = Files.createFile(Paths.get(zippath));
    
    ZipEntry zipEntry = new ZipEntry(filetozip.getFileName().toString());
    ZipOutputStream zipOutputStream = new ZipOutputStream(Files.newOutputStream(zipFile));
    zipOutputStream.putNextEntry(zipEntry);
    Files.copy(filetozip, zipOutputStream);
    zipOutputStream.close();

    return zipFile.toFile();
  }
  
  /**
   * This method is used for generic way of getting By class type
   * i.e. id, xpath,css,name etc
   * @param object contain the Object key
   * @return the By type
   */
  private By byType(String object) {

    List<String> data = ConstantVariable.GetObject.get(object);

    //get the locator value type
    ByMethod locator = ByMethod.valueOf(data.get(0));
    String expression = data.get(1);

    By byElement = null;
    switch (locator) {
      case xpath: {
        byElement = By.xpath(expression);
        break;
      }
      case id: {
        byElement = By.id(expression);
        break;
      }
      case name: {
        byElement = By.name(expression);
        break;
      }
      case classname: {
        byElement = By.className(expression);
        break;
      }
      case linktext: {
        byElement = By.linkText(expression);
        break;
      }
      case paritallinktext: {
        byElement = By.partialLinkText(expression);
        break;
      }
      case tagname: {
        byElement = By.tagName(expression);
        break;
      }
      case angularbuttontext: {
        byElement = ByAngular.buttonText(expression);
        break;
      }
      case repeater: {
        byElement = ByAngular.repeater(expression);
        break;
      }
      case exactrepeater : {
        byElement = ByAngular.exactRepeater(expression);
        break;
      }
      case binding: {
        byElement = ByAngular.binding(expression);
        break;
      }
      case exactbinding: {
        byElement = ByAngular.exactBinding(expression);
        break;
      }
      case model: {
        byElement = ByAngular.model(expression);
        break;
      }
      case options: {
        byElement = ByAngular.options(expression);
        break;
      }
      case partialbuttontext: {
        byElement = ByAngular.partialButtonText(expression);
        break;
      }
      case csscontainingtext: {
        byElement = ByAngular.cssContainingText(expression, "searchText");
        break;
      }
      default :
        logger.info("Invalid By Class");
    }
    return byElement;

  }

}

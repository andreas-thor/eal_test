package eal_test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Selenium2Example {
	public static void main(String[] args) throws InterruptedException {

		int waitInSeconds = 2;

		/* !!! Path to geckodriver.exe must be in PATH variable */

		// Create a new instance of the Firefox driver
		// Notice that the remainder of the code relies on the interface,
		// not the implementation.

		System.setProperty("webdriver.gecko.driver", "C:/webdrivers/geckodriver.exe");
		System.setProperty("webdriver.chrome.driver", "C:/webdrivers/chromedriver.exe");

		WebDriver driver = new ChromeDriver();

		// login
		driver.get("http://localhost/wordpress/wp-admin/");
		(new WebDriverWait(driver, waitInSeconds)).until(ExpectedConditions.presenceOfElementLocated(By.id("user_login"))).sendKeys("publisher");
		driver.findElement(By.id("user_pass")).sendKeys("publisher");
		driver.findElement(By.id("wp-submit")).click();

		for (int page = 1; page <= 64; page++) {

			// get the list of SC items
			driver.get("http://localhost/wordpress/wp-admin/edit.php?post_type=itemsc&mode=list&paged=" + page);
			
			// get the preview
			new WebDriverWait(driver, waitInSeconds).until(ExpectedConditions.presenceOfElementLocated(By.id("doaction2")));
			driver.findElement(By.id("cb-select-all-1")).click();
			new Select(driver.findElement(By.id("bulk-action-selector-top"))).selectByValue("view");
			driver.findElement(By.id("doaction")).click();

			// construct the URL for onyx download
			(new WebDriverWait(driver, waitInSeconds)).until(ExpectedConditions.presenceOfElementLocated(By.className("page-title-action")));
			String href = driver.findElement(By.className("page-title-action")).getAttribute("href");
			String itemids = href.substring(href.indexOf("itemids="));
			itemids = itemids.substring(0, itemids.indexOf("&"));

			// download
			driver.get("http://localhost/wordpress/wp-admin/admin.php?page=download&type=item&format=onyx&" + itemids);
		}


		// driver.findElement(By.id("wp-admin-bar-onyx"))


		/*
		 * // Alternatively the same thing can be done like this //
		 * driver.navigate().to("http://www.google.com");
		 * 
		 * // Find the text input element by its name // WebElement element =
		 * driver.findElement(By.name("q"));
		 * 
		 * // Enter something to search for element.sendKeys("Cheese!");
		 * 
		 * // Now submit the form. WebDriver will find the form for us from the element
		 * element.submit();
		 * 
		 * // Check the title of the page System.out.println("Page title is: " +
		 * driver.getTitle());
		 * 
		 * // Google's search is rendered dynamically with JavaScript. // Wait for the
		 * page to load, timeout after 10 seconds (new WebDriverWait(driver,
		 * 10)).until(new ExpectedCondition<Boolean>() { public Boolean apply(WebDriver
		 * d) { return d.getTitle().toLowerCase().startsWith("cheese!"); } });
		 * 
		 * // Should see: "cheese! - Google Search" System.out.println("Page title is: "
		 * + driver.getTitle());
		 */
		// Close the browser

		Thread.sleep(waitInSeconds * 1000);
		driver.quit();
	}
}
package eal_test;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Selenium2Example {
	
	private static WebDriver driver;
	private static int waitInSeconds = 2;
	
	private static void login (String username, String password, String domain) throws TimeoutException, InterruptedException {

		// login
		driver.navigate().to("http://localhost/wordpress/wp-admin/?domain=" + domain);
		(new WebDriverWait(driver, waitInSeconds)).until(ExpectedConditions.presenceOfElementLocated(By.id("user_login")));
		driver.findElement(By.id("user_login")).sendKeys(username);
		driver.findElement(By.id("user_pass")).sendKeys(password);
		driver.findElement(By.id("wp-submit")).click();


		// wait until fully loaded
		(new WebDriverWait(driver, waitInSeconds)).until(ExpectedConditions.presenceOfElementLocated(By.id("wp-admin-bar-my-account")));

	}
	
	
	private static boolean getListOfItems (String item_type, int page)  throws TimeoutException, InterruptedException {
		
		// get the list of items
		driver.navigate().to("http://localhost/wordpress/wp-admin/edit.php?post_type=" + item_type + "&mode=list&paged=" + page);
		new WebDriverWait(driver, waitInSeconds).until(ExpectedConditions.presenceOfElementLocated(By.id("doaction2")));
		
		return driver.findElements(By.className("next-page")).size() > 0;
	}
	
	private static void getPreview ()  throws TimeoutException, InterruptedException {

		// get preview: select all in list and click "view"
		driver.findElement(By.id("cb-select-all-1")).click();
		new Select(driver.findElement(By.id("bulk-action-selector-top"))).selectByValue("view");
		driver.findElement(By.id("doaction")).click();

		// wait until fully loaded
		(new WebDriverWait(driver, waitInSeconds)).until(ExpectedConditions.presenceOfElementLocated(By.className("page-title-action")));
	}

	
	private static void downloadItems (String format) throws InterruptedException {			
	
		// construct the URL for onyx download
		String href = driver.findElement(By.className("page-title-action")).getAttribute("href");
		String itemids = href.substring(href.indexOf("itemids="));
		itemids = itemids.substring(0, itemids.indexOf("&"));
	
		// download
		driver.navigate().to("http://localhost/wordpress/wp-admin/admin.php?page=download&type=item&format=" + format + "&" + itemids);

	}
	
	public static void main(String[] args) {

		

		/* !!! Path to geckodriver.exe must be in PATH variable */

		// Create a new instance of the Firefox driver
		// Notice that the remainder of the code relies on the interface,
		// not the implementation.

		System.setProperty("webdriver.gecko.driver", "C:/webdrivers/geckodriver.exe");
		System.setProperty("webdriver.chrome.driver", "C:/webdrivers/chromedriver.exe");

		driver = new ChromeDriver();
		try {
//			login ("publisher", "publisher");
			login ("e", "e", "paedagogik");
		} catch (InterruptedException e) {
			e.printStackTrace();
			driver.quit();
		} catch (TimeoutException e) {
			e.printStackTrace();
			driver.quit();
		}

		
		int page = 0;
		boolean hasNext = true;
		while (hasNext) {

			page++;
			
			try {
				
				System.out.println("Page=" + page);

				hasNext = getListOfItems("item", page);
				getPreview();
				downloadItems ("onyx");
				downloadItems ("ilias");
				downloadItems ("moodle");
				downloadItems ("json");
			} catch (InterruptedException e) {
				e.printStackTrace();
				driver.quit();
			} catch (TimeoutException e) {
				e.printStackTrace();
				driver.quit();
			}
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

	}
}
package cawstudios;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;

import Files.JsonFile;
import io.restassured.path.json.JsonPath;

public class Assignment {

	@Test
	public void Test()
	{
		//Starting driver
		WebDriver driver = new ChromeDriver();
		
		//Maximize Window
		driver.manage().window().maximize();
		
		//Go to URL
		driver.get("https://testpages.herokuapp.com/styled/tag/dynamic-table.html");
		
		//Automating application
		driver.findElement(By.xpath("//details//summary[text() = 'Table Data']")).click();
		driver.findElement(By.xpath("//textarea[@id = 'jsondata']")).sendKeys(Keys.CONTROL+"A");
		driver.findElement(By.xpath("//textarea[@id = 'jsondata']")).sendKeys(JsonFile.data());
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		driver.findElement(By.xpath("//button[@id='refreshtable']")).click();
		
		//Getting Data from JSON
		JsonPath js = new JsonPath(JsonFile.data());
		
		//Finding all related elements
		List<WebElement> tableRows = driver.findElements(By.xpath("//table[@id=\"dynamictable\"]//tr"));
		int Rowssize = tableRows.size();
		
		for(int i = 2; i <= Rowssize; i++)
		{
			String Resultxpath = "//table[@id= 'dynamictable']//tr["+i+"]//td";
			List<WebElement> tableColumns = driver.findElements(By.xpath(Resultxpath));
			int Columnsize = tableColumns.size();
			
			String JSONname = js.getString("name["+(i-2)+"]");
			String JSONage = js.getString("age["+(i-2)+"]");
			String JSONgender = js.getString("gender["+(i-2)+"]");
				
			for(int j = 1; j <= Columnsize; j++)
			{
				String finalXpath = Resultxpath+"["+j+"]";
				String UIText = driver.findElement(By.xpath(finalXpath)).getText();
				if(UIText.equalsIgnoreCase(JSONname) || UIText.equalsIgnoreCase(JSONage) || UIText.equalsIgnoreCase(JSONgender))
				{
					Assert.assertTrue(true);
				}
				else
				{
					System.out.println("Data : "+ (i-1) + " => not matched");
					Assert.assertTrue(false);
				}
			}
		}
		
		//Closing Browser
		driver.quit();
	}
}

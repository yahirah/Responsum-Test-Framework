package test.java.cases;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import main.java.pom.Helpers;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import test.java.helpers.Constants;
import test.java.views.Common;
import test.java.views.ShortcutsView;

public class InterruptionUseCases {
	WebDriver driver;
	String password;
	private static String LOGIN = "walach.anna.or";

	@BeforeTest
	public void prepare() throws IOException {
		System.setProperty("webdriver.chrome.driver",
				"src/main/resources/chromedriver.exe");
		for (String line : Files.readAllLines(
				Paths.get("src/main/resources/.secret"),
				Charset.defaultCharset())) {
			password = line;
		}
	}

	@Test
	public void should_call_and_stop() {
		driver = new FirefoxDriver();
		driver.get("http://client.openreception.org");
		System.out.println("Should call and stop before being pick up. ");

		Common.login(driver, LOGIN, password, false);
		ShortcutsView.getReady(driver);

		System.out.println("User calls company: " + Constants.DEFAULT_COMPANY);
		Common.callCompany(Constants.DEFAULT_COMPANY);

		Helpers.waiting(5000);
		Common.hangOutCustomer();
	}

	@Test
	public void should_call_pick_up_and_stop() {
		driver = new FirefoxDriver();
		driver.get("http://client.openreception.org");
		System.out.println("Should call, be picked up and stop call.");

		Common.login(driver, LOGIN, password, false);
		ShortcutsView.getReady(driver);

		System.out.println("User calls company: " + Constants.DEFAULT_COMPANY);
		Common.callCompany(Constants.DEFAULT_COMPANY);

		System.out.println("Receptionist pick up the cal");
		ShortcutsView.pickup(driver);
		Helpers.waiting(5000);
		Common.hangOutCustomer();
	}
}

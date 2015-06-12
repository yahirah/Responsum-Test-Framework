package test.java.cases;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import main.java.pom.Helpers;
import main.java.utils.ExternalCall;
import main.java.utils.Receptionist;
import main.java.utils.TestService;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import test.java.helpers.Constants;
import test.java.views.Common;
import test.java.views.HomeView;
import test.java.views.ShortcutsView;

import com.mashape.unirest.http.Unirest;

public class EmployeeHatesYouUseCases {
	WebDriver driver;
	String password;
	Receptionist rep;
	ExternalCall customer;
	ExternalCall employee;

	@BeforeTest
	public void prepare() throws IOException {
		for (String line : Files.readAllLines(
				Paths.get("src/main/resources/.secret"),
				Charset.defaultCharset())) {
			password = line;
		}
	}

	@AfterTest
	public void closing() {
		try {
			Unirest.shutdown();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@BeforeMethod
	public void setUp() {
		System.out.println("----N-E-W--T-E-S-T----");
		driver = new FirefoxDriver();
		driver.manage().window().maximize();
		rep = TestService.aquireReceptionist();
		driver.get("http://ci.bitstack.dk:8000?settoken=" + rep.auth_token);
		customer = TestService.aquireCustomer();
		employee = TestService.aquireCustomer();
		ShortcutsView.getReady(driver);
	}

	@AfterMethod
	public void tearDown() {

		TestService.releaseReceptionist(rep);
		TestService.releaseCustomer(customer);
		TestService.releaseCustomer(employee);
		Helpers.waiting(1000);
		driver.quit();
	}

	@Test
	public void should_leave_message() {
		System.out
				.println("***Should leave message, because employee is unavailable.***");

		System.out.println("Customer calls company: "
				+ Constants.DEFAULT_COMPANY);
		TestService.dial(customer);
		Helpers.waiting(3000);
		HomeView.checkCallQueue(1, driver);

		System.out.println("Receptionist pick up the call.");
		ShortcutsView.pickup(driver);
		Helpers.waiting(2000);

		System.out
				.println("Customer asks for: " + Constants.DEFAULT_EMPLOYEE_1);
		ShortcutsView.switchToSearch(driver);
		Common.sendToLastActiveElement(Constants.DEFAULT_EMPLOYEE_1, driver);

		System.out.println("Receptionist checks, if employee is available.");

		ShortcutsView.switchToContactCalendar(driver);
		HomeView.checkContactEvents(Constants.EVENTS_ENTRIES_KIM, driver);

		ShortcutsView.switchToCompanyCalendar(driver);
		HomeView.checkCalendar(Constants.CALENDAR_ENTRIES_BS, driver);

		System.out
				.println("Receptionist informs, that employee is unavailable.");
		System.out.println("Customer wants to send a message.");

		HomeView.sendMessage(driver);

		System.out.println("Message sent, all is done.");
		ShortcutsView.hangup(driver);
		TestService.hangUp(customer);
	}

	@Test
	public void should_not_answer() {
		System.out
				.println("***Should do nothing, because employee doesn't answer.***");

		System.out.println("Customer calls company: "
				+ Constants.DEFAULT_COMPANY);
		TestService.dial(customer);
		Helpers.waiting(3000);
		HomeView.checkCallQueue(1, driver);

		System.out.println("Receptionist pick up the call.");
		ShortcutsView.pickup(driver);
		Helpers.waiting(2000);

		System.out
				.println("Customer asks for: " + Constants.DEFAULT_EMPLOYEE_1);
		ShortcutsView.switchToSearch(driver);
		Common.sendToLastActiveElement(Constants.DEFAULT_EMPLOYEE_1, driver);

		System.out.println("Receptionist checks, if employee is available.");

		ShortcutsView.switchToContactCalendar(driver);
		HomeView.checkContactEvents(Constants.EVENTS_ENTRIES_KIM, driver);

		ShortcutsView.switchToCompanyCalendar(driver);
		HomeView.checkCalendar(Constants.CALENDAR_ENTRIES_BS, driver);

		System.out.println("Receptionist informs, that employee is available.");

		Helpers.waiting(500);
		System.out.println("Receptionist calls:  "
				+ Constants.DEFAULT_EMPLOYEE_1);

		// HomeView.callSelectedPerson(driver);
		TestService.dial_ext(employee, rep);
		// TODO should not answer

		Helpers.waiting(1000);
		HomeView.checkMyCalls(2, driver);

		System.out.println(Constants.DEFAULT_EMPLOYEE_1 + "doesnt't answer.");
		Helpers.waiting(500);
		TestService.hangUp(employee);

		System.out.println("Customer is done.");

		ShortcutsView.hangup(driver);
		TestService.hangUp(customer);

	}

	@Test
	public void should_refuse_contact() {
		System.out
				.println("***Should leave message, because employee refuse to talk.***");

		System.out.println("Customer calls company: "
				+ Constants.DEFAULT_COMPANY);
		TestService.dial(customer);
		Helpers.waiting(3000);
		HomeView.checkCallQueue(1, driver);

		System.out.println("Receptionist pick up the call.");
		ShortcutsView.pickup(driver);
		Helpers.waiting(2000);

		System.out
				.println("Customer asks for: " + Constants.DEFAULT_EMPLOYEE_1);
		ShortcutsView.switchToSearch(driver);
		Common.sendToLastActiveElement(Constants.DEFAULT_EMPLOYEE_1, driver);

		System.out.println("Receptionist checks, if employee is available.");

		ShortcutsView.switchToContactCalendar(driver);
		HomeView.checkContactEvents(Constants.EVENTS_ENTRIES_KIM, driver);

		ShortcutsView.switchToCompanyCalendar(driver);
		HomeView.checkCalendar(Constants.CALENDAR_ENTRIES_BS, driver);

		System.out.println("Receptionist informs, that employee is available.");

		Helpers.waiting(500);
		System.out.println("Receptionist calls:  "
				+ Constants.DEFAULT_EMPLOYEE_1);

		// HomeView.callSelectedPerson(driver);
		TestService.dial_ext(employee, rep);

		Helpers.waiting(3000);
		HomeView.checkMyCalls(2, driver);

		System.out.println(Constants.DEFAULT_EMPLOYEE_1 + " answers");
		Helpers.waiting(1000);
		TestService.pickup(employee);
		Helpers.waiting(1000);

		HomeView.checkMyCalls(2, driver);

		System.out.println(Constants.DEFAULT_EMPLOYEE_1
				+ " doesn't want to talk");
		TestService.hangUp(employee);

		System.out.println("Receptionist informs, that employee is busy.");
		System.out.println("Customer wants to leave a message.");
		HomeView.sendMessage(driver);

		ShortcutsView.hangup(driver);
		TestService.hangUp(customer);
	}

}

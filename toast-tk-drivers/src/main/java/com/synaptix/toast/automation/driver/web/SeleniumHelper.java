package com.synaptix.toast.automation.driver.web;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.synaptix.toast.automation.api.IMiniResult;
import com.synaptix.toast.core.runtime.IWebElement;

public class SeleniumHelper {

	public static void wait(
		int timeMs) {
		try {
			Thread.sleep(timeMs);
		}
		catch(InterruptedException e1) {
			e1.printStackTrace();
		}
	}

	public static void waitCondition(
		int retry,
		int timeMs,
		IMiniResult condition) {
		try {
			for(int i = 0; i <= retry; i++) {
				Thread.sleep(timeMs);
				if(condition.result()) {
					break;
				}
			}
		}
		catch(InterruptedException e1) {
			e1.printStackTrace();
		}
	}

	public static WebElement positionSelect(
		WebDriver driver,
		IWebElement item) {
		WebElement el = null;
		try {
			switch(item.getMethod()) {
				case CSS :
					return driver.findElements(By.cssSelector(item.getLocator())).get(item.getPosition());
				case XPATH :
					return driver.findElements(By.xpath(item.getLocator())).get(item.getPosition());
				case ID :
					return driver.findElement(By.id(item.getLocator()));
				default :
					return null;
			}
		}
		catch(IndexOutOfBoundsException e) {
			System.err.println("Locator: " + item.getLocator() + " - Position: " + item.getPosition());
			e.printStackTrace();
		}
		return el;
	}

	public static WebElement singleSelect(
		WebDriver driver,
		IWebElement item) {
		switch(item.getMethod()) {
			case CSS :
				return driver.findElement(By.cssSelector(item.getLocator()));
			case XPATH :
				return driver.findElement(By.xpath(item.getLocator()));
			case ID :
				return driver.findElement(By.id(item.getLocator()));
			default :
				return null;
		}
	}
}

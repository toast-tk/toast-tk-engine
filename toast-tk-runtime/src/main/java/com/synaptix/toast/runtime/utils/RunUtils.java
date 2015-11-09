package com.synaptix.toast.runtime.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.synaptix.toast.dao.domain.impl.test.block.ITestPage;

public class RunUtils {

	public static String readFileAsString(String path) throws IOException{
		return new String(Files.readAllBytes(Paths.get(path)));
	}
	
	public static void printResult(
		List<ITestPage> testPages) {
		int totalErrors = 0;
		int totalTechnical = 0;
		int totalSuccess = 0;
		List<String> filesWithErrorsList = new ArrayList<String>();
		for(ITestPage testPage : testPages) {
			totalErrors += testPage.getTestFailureNumber();
			totalTechnical += testPage.getTechnicalErrorNumber();
			if(testPage.getTechnicalErrorNumber() > 0) {
				filesWithErrorsList.add(testPage.getName());
			}
			totalSuccess += testPage.getTestSuccessNumber();
		}
		print(totalErrors, totalTechnical, totalSuccess, filesWithErrorsList);
	}

	private static void print(
		int totalErrors,
		int totalTechnical,
		int totalSuccess,
		List<String> filesWithErrorsList) {
		if(totalErrors > 0) {
			System.out.println("Files with failed tests : ");
			for(String string : filesWithErrorsList) {
				System.out.println("- " + string);
			}
		}
		System.out.println("Technical errors: " + totalTechnical);
		System.out.println("Failures: " + totalErrors);
		System.out.println("Success: " + totalSuccess);
	}
}

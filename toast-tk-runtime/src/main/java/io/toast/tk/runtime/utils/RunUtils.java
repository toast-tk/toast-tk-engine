package io.toast.tk.runtime.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.toast.tk.dao.domain.impl.test.block.ITestPage;

public class RunUtils {

	private static final Logger LOG = LogManager.getLogger(RunUtils.class);

	public static String readFileAsString(String path) throws IOException{
		return new String(Files.readAllBytes(Paths.get(path)));
	}
	
	public static void printResult(
		final List<ITestPage> testPages
	) {
		int totalErrors = 0;
		int totalTechnical = 0;
		int totalSuccess = 0;
		final List<String> filesWithErrorsList = new ArrayList<>();
		for(final ITestPage testPage : testPages) {
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
		final int totalErrors,
		final int totalTechnical,
		final int totalSuccess,
		final List<String> filesWithErrorsList
	) {
		if(totalErrors > 0) {
			LOG.info("Files with failed tests : ");
			for(final String string : filesWithErrorsList) {
				LOG.info("- {}", string);
			}
		}
		LOG.info("Technical errors: {}", totalTechnical);
		LOG.info("Failures: {}", totalErrors);
		LOG.info("Success: {}", totalSuccess);
	}
}
package com.synaptix.toast.runtime.parse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.synaptix.toast.dao.domain.DaoBeanFactory;
import com.synaptix.toast.dao.domain.impl.test.block.IBlock;
import com.synaptix.toast.dao.domain.impl.test.block.ITestPage;

public class TestParser extends AbstractParser {

	private static final Logger LOG = LogManager.getLogger(TestParser.class);

	public TestParser() {
		super();
		LOG.info("Parser intializing..");
	}

	public ITestPage parse(InputStream inputStream, String filename) throws IOException, IllegalArgumentException {
		List<String> list;
		try (BufferedReader buffer = new BufferedReader(new InputStreamReader(inputStream))) {
			list = buffer.lines().collect(Collectors.toList());
		}
		if (list.isEmpty()) {
			throw new IllegalArgumentException("File " + filename + " is empty");
		}
		removeBom(list);
		return buildTestPage(list, filename, inputStream);
	}

	private ITestPage buildTestPage(
			List<String> lines,
			final String pageName,
			final InputStream input
	) throws IllegalArgumentException, IOException {
		LOG.info("Starting test page parsing: {}", pageName);
		final ITestPage testPage = DaoBeanFactory.getBean(ITestPage.class);
		testPage.setName(pageName);
		while (CollectionUtils.isNotEmpty(lines)) {
			final IBlock block = readBlock(lines, input);
			testPage.addBlock(block);
			final int numberOfLines = TestParserHelper.getNumberOfBlockLines(block);
			final int numberOfLineIncludingHeaderSize = numberOfLines + block.getHeaderSize();
			lines = lines.subList(numberOfLineIncludingHeaderSize, lines.size()); //FIXME index offset needs to be revised, check test case 5
		}
		return testPage;
	}

	public ITestPage readString(String scenarioAsString, String scenarioName) throws IllegalArgumentException, IOException {
		String[] split = StringUtils.split(scenarioAsString, "\n");
		ArrayList<String> list = new ArrayList<>(Arrays.asList(split));
		return buildTestPage(list, scenarioName, null);
	}
}
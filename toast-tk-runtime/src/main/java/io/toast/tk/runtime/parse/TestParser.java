package io.toast.tk.runtime.parse;

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

import io.toast.tk.dao.domain.DaoBeanFactory;
import io.toast.tk.dao.domain.impl.test.block.IBlock;
import io.toast.tk.dao.domain.impl.test.block.ITestPage;

public class TestParser extends AbstractParser {

	private static final Logger LOG = LogManager.getLogger(TestParser.class);

	public TestParser() {
		super();
		LOG.info("Parser intializing..");
	}

	public ITestPage parse(List<String> lines, String filename) throws IOException, IllegalArgumentException {
		if (lines.isEmpty()) {
			throw new IllegalArgumentException("File " + filename + " is empty");
		}
		return buildTestPage(lines, filename);
	}

	private List<String> getStrings(InputStream inputStream) throws IOException {
		List<String> list;
		try (BufferedReader buffer = new BufferedReader(new InputStreamReader(inputStream))) {
			list = buffer.lines().collect(Collectors.toList());
		}
		FileHelper.removeBom(list);
		return list;
	}

	private ITestPage buildTestPage(
			List<String> lines,
			final String pageName
	) throws IllegalArgumentException, IOException {
		LOG.info("Starting test page parsing: {}", pageName);
		final ITestPage testPage = DaoBeanFactory.getBean(ITestPage.class);
		testPage.setName(pageName);
		while (CollectionUtils.isNotEmpty(lines)) {
			final IBlock block = readBlock(lines);
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
		return buildTestPage(list, scenarioName);
	}
}
package com.synaptix.toast.runtime.parse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

	public ITestPage parse(String path) throws IOException, IllegalArgumentException {
		path = cleanPath(path);
		final Path p = Paths.get(path);
		List<String> list;
		try(Stream<String> lines = Files.lines(p)) {
			list = lines.collect(Collectors.toList());
		}
		assertFileWasNotEmpty(path, list);
		removeBom(list);
		return buildTestPage(list, p.getFileName().toString(), path);
	}

	private static void assertFileWasNotEmpty(
		final String path, 
		final List<String> list
	) {
		if (list.isEmpty()) {
			throw new IllegalArgumentException("File empty at path: " + path);
		}
	}

	private ITestPage buildTestPage(
		List<String> lines, 
		final String pageName, 
		final String filePath
	) throws IllegalArgumentException, IOException {
		LOG.info("Starting test page parsing: {}", pageName);
		final ITestPage testPage = DaoBeanFactory.getBean(ITestPage.class);
		testPage.setName(pageName);
		while(CollectionUtils.isNotEmpty(lines)) {
			final IBlock block = readBlock(lines, filePath);
			testPage.addBlock(block);
			final int numberOfLines = TestParserHelper.getNumberOfBlockLines(block);
			final int numberOfLineIncludingHeaderSize = numberOfLines + block.getHeaderSize();
			lines = lines.subList(numberOfLineIncludingHeaderSize, lines.size()); //FIXME index offset needs to be revised, check test case 5
		}
		return testPage;
	}

	public ITestPage readString(String scenarioAsString, String scenarioName) throws IllegalArgumentException, IOException {
		final String[] split = StringUtils.split(scenarioAsString, "\n");
		final List<String> list = new ArrayList<>(Arrays.asList(split));
		return buildTestPage(list, scenarioName, null);
	}
}
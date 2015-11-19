package com.synaptix.toast.runtime.parse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.synaptix.toast.dao.domain.BlockType;
import com.synaptix.toast.dao.domain.DaoBeanFactory;
import com.synaptix.toast.dao.domain.impl.test.block.CommentBlock;
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
		Path p = Paths.get(path);
		Stream<String> lines = Files.lines(p);
		List<String> list = lines.collect(Collectors.toList());
		if (list.isEmpty()) {
			throw new IllegalArgumentException("File empty at path: " + path);
		}
		removeBom(list);
		return buildTestPage(list, p.getFileName().toString(), path);
	}

	private ITestPage buildTestPage(List<String> lines, String pageName, String filePath) throws IllegalArgumentException {
		LOG.info("Starting test page parsing: {}", pageName);
		ITestPage testPage = DaoBeanFactory.getInstance().getBean(ITestPage.class);
		testPage.setName(pageName);
		while (CollectionUtils.isNotEmpty(lines)) {
			IBlock block = readBlock(lines, filePath);
			testPage.addBlock(block);
			int numberOfLines = TestParserHelper.getNumberOfBlockLines(block);
			int numberOfLineIncludingHeaderSize = numberOfLines + block.getHeaderSize();
			lines = lines.subList(numberOfLineIncludingHeaderSize, lines.size()); //FIXME index offset needs to be revised, check test case 5
		}

		return testPage;
	}

	public ITestPage readString(String scenarioAsString, String scenarioName) {
		String[] split = StringUtils.split(scenarioAsString, "\n");
		ArrayList<String> list = new ArrayList<>(Arrays.asList(split));
		return buildTestPage(list, scenarioName, null);
	}

}

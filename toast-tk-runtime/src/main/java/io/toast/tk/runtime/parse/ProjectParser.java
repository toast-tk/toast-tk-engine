package io.toast.tk.runtime.parse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.toast.tk.dao.domain.impl.report.Campaign;
import io.toast.tk.dao.domain.impl.report.TestPlanImpl;
import io.toast.tk.dao.domain.impl.test.block.CampaignBlock;
import io.toast.tk.dao.domain.impl.test.block.IBlock;
import io.toast.tk.dao.domain.impl.test.block.ICampaign;
import io.toast.tk.dao.domain.impl.test.block.ITestPlan;
import io.toast.tk.dao.domain.impl.test.block.line.CampaignLine;

public class ProjectParser extends AbstractParser {

	private static final Logger LOG = LogManager.getLogger(ProjectParser.class);

	public ProjectParser() {
		LOG.info("Parser initializing..");
		this.blockParserProvider = new BlockParserProvider();
	}

	public ITestPlan parse(String path) throws IOException, IllegalArgumentException {
		path = cleanPath(path);
		final Path p = Paths.get(path);
		List<String> list;
		try (Stream<String> lines = Files.lines(p)) {
			list = lines.collect(Collectors.toList());
		}

		if (list.isEmpty()) {
			throw new IllegalArgumentException("File empty at path: " + path);
		}
		removeBom(list);
		return buildProject(list, p.getFileName().toString(), path);
	}

	private ITestPlan buildProject(
		List<String> lines, 
		final String pageName, 
		final String filePath
	) throws IllegalArgumentException, IOException {
		LOG.info("Starting project parsing: {}", pageName);
		final TestPlanImpl project = initProject(pageName);
		while(CollectionUtils.isNotEmpty(lines)) {
			final IBlock block = readBlock(lines, filePath);
			if(block instanceof CampaignBlock) {
				project.getCampaigns().add(readCampaignBlock((CampaignBlock) block));
			}
			final int numberOfLines = TestParserHelper.getNumberOfBlockLines(block);
			final int numberOfLineIncludingHeaderSize = numberOfLines + block.getHeaderSize();
			lines = lines.subList(numberOfLineIncludingHeaderSize, lines.size()); //FIXME index offset needs to be revised, check test case 5
		}
		return project;
	}

	private static TestPlanImpl initProject(final String pageName) {
		final TestPlanImpl project = new TestPlanImpl();
		project.setName(pageName);
		project.setCampaigns(new ArrayList<>());
		return project;
	}

	private static ICampaign readCampaignBlock(final CampaignBlock block) {
		final Campaign campaign = initCampaign(block);
		block.getTestCases().stream().forEach(testCase -> addTestCaseToCampaign(campaign, testCase));
		return campaign;
	}

	private static void addTestCaseToCampaign(
		final Campaign campaign,
		final CampaignLine testCase
	) {
		testCase.getFile().setName(testCase.getName());
		campaign.getTestCases().add(testCase.getFile());
	}

	private static Campaign initCampaign(final CampaignBlock block) {
		final Campaign campaign = new Campaign();
		campaign.setTestCases(new ArrayList<>());
		campaign.setName(block.getCampaignName());
		return campaign;
	}
}
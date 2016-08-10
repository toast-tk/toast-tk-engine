package io.toast.tk.runtime.parse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.toast.tk.dao.domain.impl.report.Campaign;
import io.toast.tk.dao.domain.impl.report.Project;
import io.toast.tk.dao.domain.impl.test.block.CampaignBlock;
import io.toast.tk.dao.domain.impl.test.block.IBlock;
import io.toast.tk.dao.domain.impl.test.block.ICampaign;
import io.toast.tk.dao.domain.impl.test.block.IProject;
import io.toast.tk.dao.domain.impl.test.block.line.CampaignLine;

public class ProjectParser extends AbstractParser {

	private static final Logger LOG = LogManager.getLogger(ProjectParser.class);

	public ProjectParser() {
		LOG.info("Parser initializing..");
		this.blockParserProvider = new BlockParserProvider();
	}

	public IProject parse(String filename) throws IOException, IllegalArgumentException {
		return buildProject(FileHelper.getScript(filename), filename);
	}

	private IProject buildProject(List<String> lines, final String pageName) throws IllegalArgumentException, IOException {
		LOG.info("Starting project parsing: {}", pageName);
		final Project project = initProject(pageName);
		while (CollectionUtils.isNotEmpty(lines)) {
			final IBlock block = readBlock(lines);
			if (block instanceof CampaignBlock) {
				project.getCampaigns().add(readCampaignBlock((CampaignBlock) block));
			}
			final int numberOfLines = TestParserHelper.getNumberOfBlockLines(block);
			final int numberOfLineIncludingHeaderSize = numberOfLines + block.getHeaderSize();
			lines = lines.subList(numberOfLineIncludingHeaderSize, lines.size()); //FIXME index offset needs to be revised, check test case 5
		}
		return project;
	}

	private static Project initProject(final String pageName) {
		final Project project = new Project();
		project.setName(pageName);
		project.setCampaigns(new ArrayList<>());
		return project;
	}

	private static ICampaign readCampaignBlock(final CampaignBlock block) {
		final Campaign campaign = initCampaign(block);
		block.getTestCases().forEach(testCase -> addTestCaseToCampaign(campaign, testCase));
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
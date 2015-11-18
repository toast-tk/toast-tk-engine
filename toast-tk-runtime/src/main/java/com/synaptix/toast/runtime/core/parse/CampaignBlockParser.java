package com.synaptix.toast.runtime.core.parse;

import com.synaptix.toast.dao.domain.BlockType;
import com.synaptix.toast.dao.domain.impl.test.block.CampaignBlock;
import com.synaptix.toast.dao.domain.impl.test.block.IBlock;
import com.synaptix.toast.dao.domain.impl.test.block.ITestPage;
import com.synaptix.toast.runtime.parse.IBlockParser;
import com.synaptix.toast.runtime.parse.TestParser;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Parse a test block.
 * <p>
 * A test block starts with a title, which defines the block type. Each test line starts with a pipe '|'
 * <p>
 * <p>
 * Example : <br>
 * <p>
 * || Campaign || CampaignName || <br>
 * | script1.txt | <br>
 * | script2.txt | <br>
 * <p>
 * Created by Nicolas Sauvage on 13/11/2015.
 */
public class CampaignBlockParser implements IBlockParser {
    private static final Logger LOG = LogManager.getLogger(CampaignBlockParser.class);

    @Override
    public BlockType getBlockType() {
        return BlockType.CAMPAIGN;
    }

    @Override
    public IBlock digest(List<String> strings, String path) {
        String firstLine = strings.get(0);
        if (!firstLine.startsWith("||")) {
            throw new IllegalArgumentException("Campaign block does not have a title: " + firstLine);
        }

        CampaignBlock campaignBlock = new CampaignBlock();

        // Find default action type
        String[] title = StringUtils.split(firstLine, "||");
        if (title.length >= 2) {
        	String campaignName = title[1] != null ? title[1].trim() : null;
            campaignBlock.setCampaignName(campaignName);
        }

        // Add test lines to block
        for (String string : strings.subList(1, strings.size())) {
            if (!string.startsWith("|")) {
                return campaignBlock;
            }
            String[] split = StringUtils.split(string, "|");
            String name = split[0] != null ? split[0].trim() : null;
            String testPagePath = split[0] != null ? split[0].trim() : null;
            String pathName = StringUtils.trim(testPagePath);
            Path newPath = Paths.get(path).resolveSibling(pathName);
            ITestPage testPage = null;
            try {
                testPage = new TestParser().parse(newPath.toString());
            } catch (IOException e) {
                LOG.error(e.getMessage(), e);
            }
            campaignBlock.addTestCase(name,testPage);
        }

        return campaignBlock;
    }

    @Override
    public boolean isFirstLineOfBlock(String line) {
        String trimmedLine = line.trim();
        if (trimmedLine.startsWith("||")) {
            String[] split = StringUtils.split(trimmedLine,"||");

            if (split.length >= 1 && split[0].contains("campaign")) {
                return true;
            }
        }
        return false;
    }
}

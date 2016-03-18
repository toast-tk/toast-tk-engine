package com.synaptix.toast.runtime.core.parse;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.synaptix.toast.dao.domain.BlockType;
import com.synaptix.toast.dao.domain.impl.test.block.CampaignBlock;
import com.synaptix.toast.dao.domain.impl.test.block.IBlock;
import com.synaptix.toast.dao.domain.impl.test.block.ITestPage;
import com.synaptix.toast.runtime.parse.IBlockParser;
import com.synaptix.toast.runtime.parse.TestParser;

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
    public IBlock digest(
    	final List<String> strings, 
    	final String path
    ) {
    	final String firstLine = strings.get(0);
        if(!firstLine.startsWith("||")) {
            throw new IllegalArgumentException("Campaign block does not have a title: " + firstLine);
        }

        final CampaignBlock campaignBlock = new CampaignBlock();

        // Find default action type
        final String[] title = StringUtils.split(firstLine, "||");
        if(title.length >= 2) {
        	final String campaignName = title[1] != null ? title[1].trim() : null;
            campaignBlock.setCampaignName(campaignName);
        }

        // Add test lines to block
        for(final String string : strings.subList(1, strings.size())) {
            if (!string.startsWith("|")) {
                return campaignBlock;
            }
            final String[] split = StringUtils.split(string, "|");
            final String name = StringUtils.trim(split[0]);
            final String testPagePath = StringUtils.trim(split[1]);
            final String pathName = StringUtils.trim(testPagePath);
            final Path newPath = Paths.get(path).resolveSibling(pathName);
            ITestPage testPage = null;
            try {
                testPage = new TestParser().parse(newPath.toString());
            } 
            catch(final IOException e) {
                LOG.error(e.getMessage(), e);
            }
            campaignBlock.addTestCase(name,testPage);
        }

        return campaignBlock;
    }

    @Override
    public boolean isFirstLineOfBlock(String line) {
    	final String trimmedLine = line.trim();
        if(trimmedLine.startsWith("||")) {
        	final String[] split = StringUtils.split(trimmedLine,"||");
            return split.length >= 1 && split[0].contains("campaign");
        }
        return false;
    }
}
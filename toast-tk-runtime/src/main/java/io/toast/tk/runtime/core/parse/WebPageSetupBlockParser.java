package io.toast.tk.runtime.core.parse;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import io.toast.tk.dao.domain.BlockType;
import io.toast.tk.dao.domain.impl.test.block.IBlock;
import io.toast.tk.dao.domain.impl.test.block.WebPageBlock;
import io.toast.tk.dao.domain.impl.test.block.line.WebPageConfigLine;
import io.toast.tk.runtime.parse.IBlockParser;

/**
 * Created by Nicolas Sauvage on 10/09/2015.
 */
public class WebPageSetupBlockParser implements IBlockParser {

    @Override
    public BlockType getBlockType() {
        return BlockType.WEB_PAGE_SETUP;
    }

    @Override
    public IBlock digest(
		    final List<String> strings
    ) {
        final String firstLine = strings.get(0);
        assertIsTitle(firstLine);
        final WebPageBlock webPageBlock = new WebPageBlock();
        // Find web page name
        final String[] title = StringUtils.split(firstLine, "||");
        setFixtureName(webPageBlock, title);
        // Add test lines to block
        for(final String string : strings.subList(2, strings.size())) {
            if(!string.startsWith("|")) {
                return webPageBlock;
            }
            final String[] split = StringUtils.split(string, "|");
            assertLineHasEnoughColumn(firstLine, split);
            webPageBlock.addLine(
            	new WebPageConfigLine(
            		split[0].trim(), 
            		split[1].trim(),
            		split[2].trim(),
            		split[3].trim(),
            		new Integer(split[4].trim())
            	)
            );
        }
        return webPageBlock;
    }

	private static void setFixtureName(
		final WebPageBlock webPageBlock,
		final String[] title
	) {
		if(title.length >= 2) {
            webPageBlock.setFixtureName(title[2].trim());
        }
	}

	private static void assertIsTitle(final String firstLine) {
		if(!firstLine.startsWith("||")) {
            throw new IllegalArgumentException("Setup block does not have a title: " + firstLine);
        }
	}

	private static void assertLineHasEnoughColumn(String firstLine, String[] split) {
		if(split.length != 5) {
		    throw new IllegalArgumentException("Web page setup line does not have enough columns: " + firstLine);
		}
	}

    @Override
    public boolean isFirstLineOfBlock(final String line) {
        final String trimmedLine = line.trim();
        if(trimmedLine.startsWith("||")) {
            return isSetupOrWeb(StringUtils.split(trimmedLine, "||"));
        }
        return false;
    }

	private static boolean isSetupOrWeb(final String[] split) {
		return split.length >= 2 && split[0].contains("setup")  && split[1].contains("web");
	}
}
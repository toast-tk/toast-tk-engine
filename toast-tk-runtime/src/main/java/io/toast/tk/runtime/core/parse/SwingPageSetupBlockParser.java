package io.toast.tk.runtime.core.parse;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import io.toast.tk.dao.domain.BlockType;
import io.toast.tk.dao.domain.impl.test.block.IBlock;
import io.toast.tk.dao.domain.impl.test.block.SwingPageBlock;
import io.toast.tk.dao.domain.impl.test.block.line.SwingPageConfigLine;
import io.toast.tk.runtime.parse.IBlockParser;

public class SwingPageSetupBlockParser implements IBlockParser {

    @Override
    public BlockType getBlockType() {
        return BlockType.SWING_PAGE_SETUP;
    }

    @Override
    public IBlock digest(
		    final List<String> strings
    ) {
    	final String firstLine = strings.get(0);
        if (!firstLine.startsWith("||")) {
            throw new IllegalArgumentException("Setup block does not have a title: " + firstLine);
        }

        final SwingPageBlock swingPageBlock = new SwingPageBlock();

        // Find web page name
        final String[] title = StringUtils.split(firstLine, "||");
        if(title.length >= 2) {
        	final String fixtureName = title[2] != null ? title[2].trim() : null;
        	swingPageBlock.setFixtureName(fixtureName);
        }

        // Add test lines to block
        for(final String string : strings.subList(2, strings.size())) {
            if (isFirstLineOfBlock(string) || !string.startsWith("|")) {
                return swingPageBlock;
            }
            final String[] split = StringUtils.split(string, "|");
            assertHasElementNameTypeAndLocator(firstLine, split);
            swingPageBlock.addLine(new SwingPageConfigLine(split[0].trim(), split[1].trim(), split[2].trim()));
        }
        return swingPageBlock;
    }

	private static void assertHasElementNameTypeAndLocator(
		final String firstLine,
		final String[] split
	) {
		if(split.length != 3) {
		    throw new IllegalArgumentException("Swing page setup line does not have enough columns: " + firstLine);
		}
	}

    @Override
    public boolean isFirstLineOfBlock(String line) {
        final String trimmedLine = line.trim();
        if(trimmedLine.startsWith("||")) {
            final String[] split = StringUtils.split(trimmedLine,"||");
            if(isSetupOrSwing(split)) {
                return true;
            }
        }
        return false;
    }

	private static boolean isSetupOrSwing(final String[] split) {
		return split.length >= 2 && split[0].contains("setup")  && split[1].contains("swing");
	}
}
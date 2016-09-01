package io.toast.tk.runtime.core.parse;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import io.toast.tk.dao.domain.BlockType;
import io.toast.tk.dao.domain.impl.test.block.IBlock;
import io.toast.tk.dao.domain.impl.test.block.TestBlock;
import io.toast.tk.dao.domain.impl.test.block.line.TestLine;
import io.toast.tk.runtime.parse.IBlockParser;

/**
 * Parse a test block.
 * <p>
 * A test block starts with a title, which defines the block type. Each test line starts with a pipe '|'
 * <p>
 * <p>
 * Example : <br>
 * <p>
 * || Scenario || Swing || <br>
 * | do something | <br>
 * | check something | <br>
 * | check something 2 | <br>
 * | do something 2 | <br>
 * <p>
 */
public class TestBlockParser implements IBlockParser {

	@Override
    public BlockType getBlockType() {
        return BlockType.TEST;
    }

    @Override
    public IBlock digest(
		    final List<String> strings
    ) {
        final String firstLine = strings.get(0);
        assertTestBlockHasATitle(firstLine);

        final TestBlock testBlock = new TestBlock();

        // Find default action type
        final String[] title = StringUtils.split(firstLine, "||");
        if (title.length >= 2) {
        	final String fixtureName = title[1] != null ? title[1].trim() : null;
            testBlock.setFixtureName(fixtureName);
        }

        // Add test lines to block
        for(final String string : strings.subList(1, strings.size())) {
            if(!string.startsWith("|")) {
                return testBlock;
            }
            final String[] split = StringUtils.split(string, "|");
            final String test = StringUtils.trim(split[0]);
            final String expected = split.length > 1 ? (split[1]  != null ? split[1].trim() : null ) : null;
            final String comment= split.length > 2 ? (split[2]  != null ? split[2].trim() : null ) : null;
            testBlock.addLine(new TestLine(test, expected, comment));
        }
        return testBlock;
    }

	private static void assertTestBlockHasATitle(final String firstLine) {
		if(!firstLine.startsWith("||")) {
            throw new IllegalArgumentException("Test block does not have a title: " + firstLine);
        }
	}

    @Override
    public boolean isFirstLineOfBlock(final String line) {
    	final String trimmedLine = line.trim();
        if(trimmedLine.startsWith("||")) {
        	final String[] split = StringUtils.split(trimmedLine,"||");
            if(hasScenario(split)) {
                return true;
            }
        }
        return false;
    }

	private static boolean hasScenario(final String[] split) {
		return split.length >= 1 && split[0].contains("scenario");
	}
}
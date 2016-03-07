package com.synaptix.toast.runtime.core.parse;

import com.synaptix.toast.dao.domain.BlockType;
import com.synaptix.toast.dao.domain.impl.test.block.IBlock;
import com.synaptix.toast.dao.domain.impl.test.block.TestBlock;
import com.synaptix.toast.runtime.parse.IBlockParser;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

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
    public IBlock digest(List<String> strings, String path) {
        String firstLine = strings.get(0);
        if (!firstLine.startsWith("||")) {
            throw new IllegalArgumentException("Test block does not have a title: " + firstLine);
        }

        TestBlock testBlock = new TestBlock();

        // Find default action type
        String[] title = StringUtils.split(firstLine, "||");
        if (title.length >= 2) {
        	String fixtureName = title[1] != null ? title[1].trim() : null;
            testBlock.setFixtureName(fixtureName);
        }

        // Add test lines to block
        for (String string : strings.subList(1, strings.size())) {
            if (!string.startsWith("|")) {
                return testBlock;
            }
            String[] split = StringUtils.split(string, "|");
            String test = split[0] != null ? split[0].trim() : null;
            String expected = split.length > 1 ? (split[1]  != null ? split[1].trim() : null ) : null;
            String comment= split.length > 2 ? (split[2]  != null ? split[2].trim() : null ) : null;
            testBlock.addLine(test, expected, comment);
        }

        return testBlock;
    }

    @Override
    public boolean isFirstLineOfBlock(String line) {
        String trimmedLine = line.trim();
        if (trimmedLine.startsWith("||")) {
            String[] split = StringUtils.split(trimmedLine,"||");

            if (split.length >= 1 && split[0].contains("scenario")) {
                return true;
            }
        }
        return false;
    }
}

package com.synaptix.toast.runtime.core.parse;

import com.synaptix.toast.dao.domain.BlockType;
import com.synaptix.toast.dao.domain.impl.test.block.IBlock;
import com.synaptix.toast.dao.domain.impl.test.block.WebPageBlock;
import com.synaptix.toast.dao.domain.impl.test.block.line.WebPageConfigLine;
import com.synaptix.toast.runtime.parse.IBlockParser;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Created by Nicolas Sauvage on 10/09/2015.
 */
public class WebPageSetupBlockParser implements IBlockParser {

    @Override
    public BlockType getBlockType() {
        return BlockType.WEB_PAGE_SETUP;
    }

    @Override
    public IBlock digest(List<String> strings, String path) {
        String firstLine = strings.get(0);
        if (!firstLine.startsWith("||")) {
            throw new IllegalArgumentException("Setup block does not have a title: " + firstLine);
        }

        WebPageBlock webPageBlock = new WebPageBlock();

        // Find web page name
        String[] title = StringUtils.split(firstLine, "||");
        if (title.length >= 2) {
            webPageBlock.setFixtureName(title[2].trim());
        }

        // Add test lines to block
        for (String string : strings.subList(2, strings.size())) {
            if (!string.startsWith("|")) {
                return webPageBlock;
            }
            String[] split = StringUtils.split(string, "|");
            if (split.length != 5) {
                throw new IllegalArgumentException("Web page setup line does not have enough columns: " + firstLine);
            }
            WebPageConfigLine webPageConfigLine = new WebPageConfigLine();
            webPageConfigLine.setElementName(split[0].trim());
            webPageConfigLine.setType(split[1].trim());
            webPageConfigLine.setLocator(split[2].trim());
            webPageConfigLine.setMethod(split[3].trim());
            webPageConfigLine.setPosition(new Integer(split[4].trim()));
            webPageBlock.addLine(webPageConfigLine);
        }

        return webPageBlock;
    }

    @Override
    public boolean isFirstLineOfBlock(String line) {
        String trimmedLine = line.trim();
        if (trimmedLine.startsWith("||")) {
            String[] split = StringUtils.split(trimmedLine,"||");

            if (split.length >= 2 && split[0].contains("setup")  && split[1].contains("web")) {
                return true;
            }
        }
        return false;
    }
}

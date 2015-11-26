package com.synaptix.toast.runtime.parse;

import com.synaptix.toast.dao.domain.BlockType;
import com.synaptix.toast.dao.domain.impl.test.block.CommentBlock;
import com.synaptix.toast.dao.domain.impl.test.block.IBlock;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Abstract parser: contains methods to help parsing a file, and read blocks.
 * Created by Nicolas Sauvage on 19/11/2015.
 */
public class AbstractParser {
    protected BlockParserProvider blockParserProvider;

    public AbstractParser() {
        blockParserProvider = new BlockParserProvider();
    }

    protected String cleanPath(String path) {
        if (path.startsWith("\\") || path.startsWith("/")) {
            path = path.substring(1);
        }
        return path;
    }

    protected void removeBom(List<String> list) {
        if (list.get(0).startsWith("\uFEFF")) {
            list.set(0, list.get(0).substring(1));
        }
    }

    protected IBlock readBlock(List<String> list, String path) throws IllegalArgumentException, IOException {
        String firstLine = list.get(0);
        BlockType blockType = getBlockType(firstLine);
        if (blockType == BlockType.COMMENT) {
            return digestCommentBlock(list);
        } else {
            IBlockParser blockParser = blockParserProvider.getBlockParser(blockType);
            if (blockParser == null) {
                throw new IllegalArgumentException("Could not parse line: " + firstLine);
            }
            return blockParser.digest(list, path);
        }
    }

    private IBlock digestCommentBlock(List<String> lines) {
        CommentBlock commentBlock = new CommentBlock();
        for (String line : lines) {
            if (getBlockType(line) != BlockType.COMMENT) {
                return commentBlock;
            }
            commentBlock.addLine(line);
        }
        return commentBlock;
    }

    public BlockType getBlockType(String line) throws IllegalArgumentException {
        Collection<IBlockParser> allBlockParsers = blockParserProvider.getAllBlockParsers();

        List<BlockType> blockTypes = allBlockParsers.stream()
                .filter(iBlockParser -> iBlockParser.isFirstLineOfBlock(line))
                .map(IBlockParser::getBlockType)
                .collect(Collectors.toList());

        if (blockTypes.size() == 1) {
            return blockTypes.get(0);
        } else if (blockTypes.size() > 1) {
            throw new IllegalArgumentException("Too many parsers for line [" + line + "]: " + StringUtils.join(blockTypes, "; "));
        }
        return BlockType.COMMENT;
    }
}

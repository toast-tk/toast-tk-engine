package com.synaptix.toast.runtime.parse;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.synaptix.toast.dao.domain.BlockType;
import com.synaptix.toast.dao.domain.impl.test.block.CommentBlock;
import com.synaptix.toast.dao.domain.impl.test.block.IBlock;

public class AbstractParser {

    protected BlockParserProvider blockParserProvider;

    public AbstractParser() {
        this.blockParserProvider = new BlockParserProvider();
    }

    protected static void removeBom(final List<String> list) {
    	final String firstLine = list.get(0);
        if(firstLine.startsWith("\uFEFF")) {
            list.set(0, firstLine.substring(1));
        }
    }

    protected IBlock readBlock(
            final List<String> list
    ) throws IllegalArgumentException, IOException {
    	final String firstLine = list.get(0);
    	final BlockType blockType = getBlockType(firstLine);
        if (blockType == BlockType.COMMENT) {
            return digestCommentBlock(list);
        } 
        final IBlockParser blockParser = blockParserProvider.getBlockParser(blockType);
        if (blockParser == null) {
        	throw new IllegalArgumentException("Could not parse line: " + firstLine);
        }
        return blockParser.digest(list);
    }

    private IBlock digestCommentBlock(final List<String> lines) {
        CommentBlock commentBlock = new CommentBlock();
        for (String line : lines) {
            if (getBlockType(line) != BlockType.COMMENT) {
                return commentBlock;
            }
            commentBlock.addLine(line);
        }
        return commentBlock;
    }
    
    /**
     * returns the block type
     * 
     * @param line
     * @return
     * @throws IllegalArgumentException if more than one block is found
     */
    public BlockType getBlockType(final String line) throws IllegalArgumentException {
        final List<BlockType> blockTypes = blockParserProvider.getAllBlockParsers().stream()
                .filter(iBlockParser -> iBlockParser.isFirstLineOfBlock(line))
                .map(IBlockParser::getBlockType)
                .collect(Collectors.toList());
        final int nbBlockType = blockTypes.size();
        if(nbBlockType == 1) {
            return blockTypes.get(0);
        } 
        else if(nbBlockType > 1) {
            throw new IllegalArgumentException("Too many parsers for line [" + line + "]: " + StringUtils.join(blockTypes, "; "));
        }
        return BlockType.COMMENT;
    }
}
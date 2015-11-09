package com.synaptix.toast.runtime.parse;

import java.util.List;

import com.synaptix.toast.dao.domain.BlockType;
import com.synaptix.toast.dao.domain.impl.test.block.IBlock;

/**
 * Block parsers must implement this interface. 
 * A block parser can read a list of strings, and create an IBlock. See digest method.
 */
public interface IBlockParser {

    BlockType getBlockType();

    /**
     * Create a block from the list of strings.
     *
     * @param path Path of the test text file.
     */
    IBlock digest(List<String> strings, String path);

    /**
     * Return true if this string should be parsed with this parser.
     */
    boolean isFirstLineOfBlock(String line);
}

package io.toast.tk.runtime.parse;

import java.io.IOException;
import java.util.List;

import io.toast.tk.dao.domain.BlockType;
import io.toast.tk.dao.domain.impl.test.block.IBlock;

/**
 * Block parsers must implement this interface. 
 * A block parser can read a list of strings, and create an IBlock. See digest method.
 */
public interface IBlockParser {

    BlockType getBlockType();

    /**
     * Create a block from the list of strings.
     *
     * @throws IOException
     */
    IBlock digest(final List<String> strings) throws IOException;

    /**
     * Return true if this string should be parsed with this parser.
     */
    boolean isFirstLineOfBlock(final String line);
}
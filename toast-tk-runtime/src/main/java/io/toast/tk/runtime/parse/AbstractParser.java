package io.toast.tk.runtime.parse;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import io.toast.tk.dao.domain.BlockType;
import io.toast.tk.dao.domain.impl.test.block.CommentBlock;
import io.toast.tk.dao.domain.impl.test.block.IBlock;

public class AbstractParser {

	protected BlockParserProvider blockParserProvider;

	public AbstractParser() {
		this.blockParserProvider = new BlockParserProvider();
	}

	protected IBlock readBlock(final List<String> list) throws IllegalArgumentException, IOException {
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
	 * Returns the block type
	 *
	 * @throws IllegalArgumentException if more than one block is found
	 */
	public BlockType getBlockType(final String line) throws IllegalArgumentException {
		final List<BlockType> blockTypes = blockParserProvider.getAllBlockParsers().stream()
				.filter(iBlockParser -> iBlockParser.isFirstLineOfBlock(line))
				.map(IBlockParser::getBlockType)
				.collect(Collectors.toList());
		final int nbBlockType = blockTypes.size();
		if (nbBlockType == 1) {
			return blockTypes.get(0);
		} else if (nbBlockType > 1) {
			throw new IllegalArgumentException("Too many parsers for line [" + line + "]: " + StringUtils.join(blockTypes, "; "));
		}
		return BlockType.COMMENT;
	}
}
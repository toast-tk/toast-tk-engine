package com.synaptix.toast.dao.domain.impl.test.block;

import java.util.ArrayList;
import java.util.List;

import com.github.jmkgreen.morphia.annotations.Embedded;
import com.synaptix.toast.dao.domain.impl.test.block.IBlock;
import com.synaptix.toast.dao.domain.impl.test.block.line.BlockLine;

@Embedded
public class VariableBlock implements IBlock {

	private List<BlockLine> blockLines;
	
	private List<String> textLines;
	
	private BlockLine columns;

	public VariableBlock() {
		blockLines = new ArrayList<>();
		textLines = new ArrayList<>();
	}

	public void setBlockLines(
			List<BlockLine> blockLines) {
		this.blockLines = blockLines;
	}


	public List<BlockLine> getBlockLines() {
		return blockLines;
	}

	public BlockLine getColumns() {
		return columns;
	}

	public void setColumns(
			BlockLine columns) {
		this.columns = columns;
	}

	public void addline(
			BlockLine line) {
		blockLines.add(line);
	}

	@Override
	public String getBlockType() {
		return "variable";
	}

	@Override
	public int getHeaderSize() {
		return 0;
	}

	public void addTextLine(String line) {
		textLines.add(line);
	}
	
	public List<String> getTextLines() {
		return textLines;
	}
}

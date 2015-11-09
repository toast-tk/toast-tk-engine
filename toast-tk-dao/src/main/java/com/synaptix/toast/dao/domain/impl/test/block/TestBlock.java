package com.synaptix.toast.dao.domain.impl.test.block;

import java.util.ArrayList;
import java.util.List;

import com.github.jmkgreen.morphia.annotations.Embedded;
import com.synaptix.toast.dao.domain.impl.test.block.IBlock;
import com.synaptix.toast.dao.domain.impl.test.block.line.TestLine;

@Embedded
public class TestBlock implements IBlock {

	@Embedded
	private List<TestLine> blockLines;

	private String fixtureName;

	public TestBlock() {
		blockLines = new ArrayList<TestLine>();
	}

	public List<TestLine> getBlockLines() {
		return blockLines;
	}

	public void setBlockLines(
			List<TestLine> blockLines) {
		this.blockLines = blockLines;
	}

	/**
	 * Add a test line
	 */
	public void addLine(
			String test,
			String expected,
			String comment) {
		TestLine blockLine = new TestLine(test, expected, comment);
		blockLine.setComment(comment);
		this.blockLines.add(blockLine);
	}

	public String getFixtureName() {
		return fixtureName;
	}

	public void setFixtureName(
			String fixtureName) {
		this.fixtureName = fixtureName;
	}

	@Override
	public String getBlockType() {
		return "test";
	}

	@Override
	public int getHeaderSize() {
		return 1;
	}
}

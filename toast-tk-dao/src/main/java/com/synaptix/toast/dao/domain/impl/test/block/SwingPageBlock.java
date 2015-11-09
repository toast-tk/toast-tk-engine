package com.synaptix.toast.dao.domain.impl.test.block;

import java.util.ArrayList;
import java.util.List;

import com.github.jmkgreen.morphia.annotations.Embedded;
import com.synaptix.toast.dao.domain.api.test.ITestResult;
import com.synaptix.toast.dao.domain.impl.test.block.line.BlockLine;
import com.synaptix.toast.dao.domain.impl.test.block.line.SwingPageConfigLine;

@Embedded
public class SwingPageBlock implements IBlock {

	private List<SwingPageConfigLine> blockLines;

	private BlockLine columns;

	private String fixtureName;

	private ITestResult testResult;

	public SwingPageBlock() {
		blockLines = new ArrayList<SwingPageConfigLine>();
	}

	public List<SwingPageConfigLine> getBlockLines() {
		return blockLines;
	}

	public void setBlockLines(
		List<SwingPageConfigLine> blockLines) {
		this.blockLines = blockLines;
	}

	public BlockLine getColumns() {
		return columns;
	}

	public void setColumns(
		BlockLine columns) {
		this.columns = columns;
	}

	public String getFixtureName() {
		return fixtureName;
	}

	public void setFixtureName(
		String fixtureName) {
		this.fixtureName = fixtureName;
	}

	public void addLine(
		SwingPageConfigLine line) {
		blockLines.add(line);
	}

	public ITestResult getTestResult() {
		return testResult;
	}

	public void setTestResult(
			ITestResult testResult) {
		this.testResult = testResult;
	}

	@Override
	public String getBlockType() {
		return "swingPageBlock";
	}

	@Override
	public int getHeaderSize() {
		return 2;
	}
}

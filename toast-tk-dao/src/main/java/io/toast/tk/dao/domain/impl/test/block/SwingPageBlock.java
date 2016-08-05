package io.toast.tk.dao.domain.impl.test.block;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestResult;

import com.github.jmkgreen.morphia.annotations.Embedded;

import io.toast.tk.dao.domain.impl.test.block.IBlock;
import io.toast.tk.dao.domain.impl.test.block.line.BlockLine;
import io.toast.tk.dao.domain.impl.test.block.line.SwingPageConfigLine;

@Embedded
public class SwingPageBlock implements IBlock {

	private List<SwingPageConfigLine> blockLines;

	private BlockLine columns;

	private String fixtureName;

	private TestResult testResult;

	public SwingPageBlock() {
		this.blockLines = new ArrayList<>();
	}

	public List<SwingPageConfigLine> getBlockLines() {
		return blockLines;
	}

	public void setBlockLines(final List<SwingPageConfigLine> blockLines) {
		this.blockLines = blockLines;
	}

	public BlockLine getColumns() {
		return columns;
	}

	public void setColumns(final BlockLine columns) {
		this.columns = columns;
	}

	public String getFixtureName() {
		return fixtureName;
	}

	public void setFixtureName(final String fixtureName) {
		this.fixtureName = fixtureName;
	}

	public void addLine(final SwingPageConfigLine line) {
		blockLines.add(line);
	}

	public TestResult getTestResult() {
		return testResult;
	}

	public void setTestResult(final TestResult testResult) {
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
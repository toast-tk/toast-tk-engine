package io.toast.tk.dao.domain.impl.test.block;

import java.util.ArrayList;
import java.util.List;

import com.github.jmkgreen.morphia.annotations.Embedded;

import io.toast.tk.dao.core.report.TestResult;
import io.toast.tk.dao.domain.impl.test.block.IBlock;
import io.toast.tk.dao.domain.impl.test.block.line.BlockLine;

@Embedded
public class SetupBlock implements IBlock {

	private List<BlockLine> blockLines;

	private BlockLine columns;

	private String fixtureName;

	private TestResult testResult;

	public SetupBlock() {
		this.blockLines = new ArrayList<>();
	}

	public List<BlockLine> getBlockLines() {
		return blockLines;
	}

	public void setBlockLines(final List<BlockLine> blockLines) {
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

	public void addLine(final List<String> cells) {
		blockLines.add(new BlockLine(cells));
	}

	public void addLine(final BlockLine line) {
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
		return "setup";
	}

	@Override
	public int getHeaderSize() {
		return 0;
	}
}
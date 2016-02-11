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
	
    private int technicalErrorNumber;

    private int testSuccessNumber;

    private int testFailureNumber;

	public TestBlock() {
		this.blockLines = new ArrayList<>();
	}

	public List<TestLine> getBlockLines() {
		return blockLines;
	}

	public void setBlockLines(final List<TestLine> blockLines) {
		this.blockLines = blockLines;
	}

	/**
	 * Add a test line
	 */
	public void addLine(
		final String test,
		final String expected,
		final String comment
	) {
		final TestLine blockLine = new TestLine(test, expected, comment);
		blockLine.setComment(comment);
		this.blockLines.add(blockLine);
	}

	public String getFixtureName() {
		return fixtureName;
	}

	public void setFixtureName(final String fixtureName) {
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

	public int getTechnicalErrorNumber() {
        return technicalErrorNumber;
    }

    public void setTechnicalErrorNumber(final int technicalErrorNumber) {
        this.technicalErrorNumber = technicalErrorNumber;
    }

    public int getTestSuccessNumber() {
        return testSuccessNumber;
    }

    public void setTestSuccessNumber(final int testSuccessNumber) {
        this.testSuccessNumber = testSuccessNumber;
    }

    public int getTestFailureNumber() {
        return testFailureNumber;
    }

    public void setTestFailureNumber(final int testFailureNumber) {
        this.testFailureNumber = testFailureNumber;
    }
}
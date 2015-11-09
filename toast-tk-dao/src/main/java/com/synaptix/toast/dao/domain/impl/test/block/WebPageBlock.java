package com.synaptix.toast.dao.domain.impl.test.block;

import java.util.ArrayList;
import java.util.List;

import com.github.jmkgreen.morphia.annotations.Embedded;
import com.synaptix.toast.dao.domain.api.test.ITestResult;
import com.synaptix.toast.dao.domain.impl.test.block.line.BlockLine;
import com.synaptix.toast.dao.domain.impl.test.block.line.WebPageConfigLine;

@Embedded
public class WebPageBlock implements IBlock {

    private List<WebPageConfigLine> blockLines;

    private BlockLine columns;

    private String fixtureName;

    private ITestResult testResult;

    /**
     *
     */
    public WebPageBlock() {
        blockLines = new ArrayList<WebPageConfigLine>();
    }

    public List<WebPageConfigLine> getBlockLines() {
        return blockLines;
    }

    public void setBlockLines(
            List<WebPageConfigLine> blockLines) {
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
            WebPageConfigLine line) {
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
        return "webPageBlock";
    }

	@Override
	public int getHeaderSize() {
		return 2;
	}
}

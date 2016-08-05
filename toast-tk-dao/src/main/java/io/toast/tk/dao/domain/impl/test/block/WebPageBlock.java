package io.toast.tk.dao.domain.impl.test.block;

import java.util.ArrayList;
import java.util.List;

import com.github.jmkgreen.morphia.annotations.Embedded;

import io.toast.tk.dao.core.report.TestResult;
import io.toast.tk.dao.domain.impl.test.block.IBlock;
import io.toast.tk.dao.domain.impl.test.block.line.BlockLine;
import io.toast.tk.dao.domain.impl.test.block.line.WebPageConfigLine;

@Embedded
public class WebPageBlock implements IBlock {

    private List<WebPageConfigLine> blockLines;

    private BlockLine columns;

    private String fixtureName;

    private TestResult testResult;

    public WebPageBlock() {
        this.blockLines = new ArrayList<>();
    }

    public List<WebPageConfigLine> getBlockLines() {
        return blockLines;
    }

    public void setBlockLines(final List<WebPageConfigLine> blockLines) {
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

    public void addLine(final WebPageConfigLine line) {
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
        return "webPageBlock";
    }

	@Override
	public int getHeaderSize() {
		return 2;
	}
}
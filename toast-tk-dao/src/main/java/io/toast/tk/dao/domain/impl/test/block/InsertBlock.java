package io.toast.tk.dao.domain.impl.test.block;

import java.util.ArrayList;
import java.util.List;

import com.github.jmkgreen.morphia.annotations.Embedded;

import io.toast.tk.dao.domain.impl.test.block.IBlock;
import io.toast.tk.dao.domain.impl.test.block.line.BlockLine;

@Embedded
@Deprecated
public class InsertBlock implements IBlock {

	private List<BlockLine> blockLines;

	private BlockLine columns;

	private String componentString;


	public InsertBlock() {
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

	public String getComponentName() {
		return componentString;
	}

	public void setComponentName(final String componentName) {
		this.componentString = componentName;
	}

	public void addline(final BlockLine line) {
		blockLines.add(line);
	}

	@Override
	public String getBlockType() {
		return "insert";
	}

	@Override
	public int getHeaderSize() {
		return 0;
	}
}
package com.synaptix.toast.runtime.core.parse;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.synaptix.toast.dao.domain.BlockType;
import com.synaptix.toast.dao.domain.impl.test.block.IBlock;
import com.synaptix.toast.dao.domain.impl.test.block.ITestPage;
import com.synaptix.toast.runtime.AbstractScenarioRunner;
import com.synaptix.toast.runtime.parse.IBlockParser;
import com.synaptix.toast.runtime.parse.TestParser;

public class IncludeBlockParser implements IBlockParser {
	
	private static final Logger LOG = LogManager.getLogger(IncludeBlockParser.class);
	
	@Override
	public IBlock digest(
		final List<String> strings, 
		final String path
	) {
		final String string = strings.remove(0);
		final String pathName = StringUtils.removeStart(string, "#include").trim();
		final Path newPath = Paths.get(path).resolveSibling(pathName);
		ITestPage testPage = null;
		try {
			testPage = new TestParser().parse(newPath.toString());
		}
		catch(final IOException e) {
			LOG.error(e.getMessage(), e);
		}
		return testPage;
	}

	@Override
	public BlockType getBlockType() {
		return BlockType.INCLUDE;
	}
	
	@Override
	public boolean isFirstLineOfBlock(String line) {
		return line != null && line.startsWith("#include");
	}
}
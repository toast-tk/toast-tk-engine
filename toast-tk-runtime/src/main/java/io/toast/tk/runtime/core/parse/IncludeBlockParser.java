package io.toast.tk.runtime.core.parse;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.toast.tk.runtime.parse.FileHelper;

import io.toast.tk.dao.domain.BlockType;
import io.toast.tk.dao.domain.impl.test.block.IBlock;
import io.toast.tk.dao.domain.impl.test.block.ITestPage;
import io.toast.tk.runtime.parse.IBlockParser;
import io.toast.tk.runtime.parse.TestParser;

public class IncludeBlockParser implements IBlockParser {

	private static final Logger LOG = LogManager.getLogger(IncludeBlockParser.class);

	@Override
	public IBlock digest(final List<String> strings) throws IOException {
		final String string = strings.remove(0);
		final String filename = StringUtils.removeStart(string, "#include").trim();
		List<String> script = FileHelper.getScript(filename);

		ITestPage testPage = null;
		try {
			testPage = new TestParser().parse(script, filename);
		} catch (final IOException e) {
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
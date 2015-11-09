package com.synaptix.toast.runtime.parse;

import com.google.inject.Inject;
import com.synaptix.toast.dao.domain.BlockType;
import com.synaptix.toast.runtime.core.parse.IncludeBlockParser;
import com.synaptix.toast.runtime.core.parse.SwingPageSetupBlockParser;
import com.synaptix.toast.runtime.core.parse.TestBlockParser;
import com.synaptix.toast.runtime.core.parse.VariableBlockParser;
import com.synaptix.toast.runtime.core.parse.WebPageSetupBlockParser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class BlockParserProvider {

	private static final Logger LOG = LogManager.getLogger(BlockParserProvider.class);
	private Map<BlockType, IBlockParser> map;

	@Inject
	public BlockParserProvider(){
		map = new HashMap<>();
		map.put(BlockType.INCLUDE, new IncludeBlockParser());
		map.put(BlockType.TEST, new TestBlockParser());
		map.put(BlockType.VARIABLE, new VariableBlockParser());
		map.put(BlockType.WEB_PAGE_SETUP, new WebPageSetupBlockParser());
		map.put(BlockType.SWING_PAGE_SETUP, new SwingPageSetupBlockParser());
	}
		
	public IBlockParser getBlockParser(BlockType blockType) {
		IBlockParser parser = map.get(blockType);
		if(parser == null){
			LOG.info("No parser found for : " + blockType.name());
		}
		return parser;
	}

	public Collection<IBlockParser> getAllBlockParsers() {
		return map.values();
	}
	
}

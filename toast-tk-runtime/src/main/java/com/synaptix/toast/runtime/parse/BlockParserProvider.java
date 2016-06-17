package com.synaptix.toast.runtime.parse;

import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.inject.Inject;
import com.synaptix.toast.dao.domain.BlockType;
import com.synaptix.toast.runtime.core.parse.CampaignBlockParser;
import com.synaptix.toast.runtime.core.parse.IncludeBlockParser;
import com.synaptix.toast.runtime.core.parse.SwingPageSetupBlockParser;
import com.synaptix.toast.runtime.core.parse.TestBlockParser;
import com.synaptix.toast.runtime.core.parse.VariableBlockParser;
import com.synaptix.toast.runtime.core.parse.WebPageSetupBlockParser;

public class BlockParserProvider {

    private static final Logger LOG = LogManager.getLogger(BlockParserProvider.class);

    private Map<BlockType,IBlockParser> map;

    @Inject
    public BlockParserProvider() {
        this.map = new EnumMap<>(BlockType.class);
        fillBlockTypeMap();
    }

	private void fillBlockTypeMap() {
		map.put(BlockType.INCLUDE, new IncludeBlockParser());
        map.put(BlockType.TEST, new TestBlockParser());
        map.put(BlockType.VARIABLE, new VariableBlockParser());
        map.put(BlockType.WEB_PAGE_SETUP, new WebPageSetupBlockParser());
        map.put(BlockType.SWING_PAGE_SETUP, new SwingPageSetupBlockParser());
        map.put(BlockType.CAMPAIGN, new CampaignBlockParser());
	}

    public IBlockParser getBlockParser(final BlockType blockType) {
    	final IBlockParser parser = map.get(blockType);
        if (parser == null) {
            LOG.info("No parser found for : {}", blockType.name());
        }
        return parser;
    }

    public Collection<IBlockParser> getAllBlockParsers() {
        return map.values();
    }
}
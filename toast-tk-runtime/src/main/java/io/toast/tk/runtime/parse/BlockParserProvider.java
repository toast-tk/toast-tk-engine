package io.toast.tk.runtime.parse;

import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.inject.Inject;

import io.toast.tk.dao.domain.BlockType;
import io.toast.tk.runtime.core.parse.CampaignBlockParser;
import io.toast.tk.runtime.core.parse.IncludeBlockParser;
import io.toast.tk.runtime.core.parse.SwingPageSetupBlockParser;
import io.toast.tk.runtime.core.parse.TestBlockParser;
import io.toast.tk.runtime.core.parse.VariableBlockParser;
import io.toast.tk.runtime.core.parse.WebPageSetupBlockParser;
import io.toast.tk.runtime.parse.IBlockParser;

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
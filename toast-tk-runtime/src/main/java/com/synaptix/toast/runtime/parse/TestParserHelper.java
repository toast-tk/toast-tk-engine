package com.synaptix.toast.runtime.parse;

import com.synaptix.toast.dao.domain.impl.test.block.CampaignBlock;
import com.synaptix.toast.dao.domain.impl.test.block.CommentBlock;
import com.synaptix.toast.dao.domain.impl.test.block.IBlock;
import com.synaptix.toast.dao.domain.impl.test.block.SetupBlock;
import com.synaptix.toast.dao.domain.impl.test.block.SwingPageBlock;
import com.synaptix.toast.dao.domain.impl.test.block.TestBlock;
import com.synaptix.toast.dao.domain.impl.test.block.VariableBlock;
import com.synaptix.toast.dao.domain.impl.test.block.WebPageBlock;

public class TestParserHelper {

	public static int getNumberOfBlockLines(final IBlock block) {
		if(block instanceof CommentBlock){
			return ((CommentBlock) block).getLines().size();
		}
		else if(block instanceof WebPageBlock){
			return ((WebPageBlock) block).getBlockLines().size();
		}
		else if(block instanceof SetupBlock){
			return ((SetupBlock) block).getBlockLines().size() - 1;
		}
		else if(block instanceof SwingPageBlock){
			return ((SwingPageBlock) block).getBlockLines().size();
		}
		else if(block instanceof TestBlock){
			return ((TestBlock) block).getBlockLines().size();
		}
		else if(block instanceof VariableBlock){
			return ((VariableBlock) block).getTextLines().size();
		}
		else if(block instanceof CampaignBlock){
			return ((CampaignBlock) block).getTestCases().size();
		}
		return 0;
	}
}
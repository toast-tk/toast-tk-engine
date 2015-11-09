package com.synaptix.toast.runtime.parse;

import com.synaptix.toast.dao.domain.impl.test.block.CommentBlock;
import com.synaptix.toast.dao.domain.impl.test.block.IBlock;
import com.synaptix.toast.dao.domain.impl.test.block.SetupBlock;
import com.synaptix.toast.dao.domain.impl.test.block.SwingPageBlock;
import com.synaptix.toast.dao.domain.impl.test.block.TestBlock;
import com.synaptix.toast.dao.domain.impl.test.block.VariableBlock;
import com.synaptix.toast.dao.domain.impl.test.block.WebPageBlock;

public class TestParserHelper {

	public static int getNumberOfBlockLines(IBlock block) {
		if(block instanceof CommentBlock){
			return ((CommentBlock) block).getLines().size();
		}
		if(block instanceof WebPageBlock){
			return ((WebPageBlock) block).getBlockLines().size();
		}
		if(block instanceof SetupBlock){
			return ((SetupBlock) block).getBlockLines().size() -1;
		}
		if(block instanceof SwingPageBlock){
			return ((SwingPageBlock) block).getBlockLines().size();
		}
		if(block instanceof TestBlock){
			return ((TestBlock) block).getBlockLines().size();
		}
		if(block instanceof VariableBlock){
			return ((VariableBlock) block).getTextLines().size();
		}
		return 0;
	}

}

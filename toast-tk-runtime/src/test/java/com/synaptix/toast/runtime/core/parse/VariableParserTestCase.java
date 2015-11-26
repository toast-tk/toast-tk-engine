package com.synaptix.toast.runtime.core.parse;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.synaptix.toast.dao.domain.impl.test.block.VariableBlock;

public class VariableParserTestCase {

	VariableBlockParser parser = new VariableBlockParser();
	
	@Test
	public void fileReferenceTest(){
		String varFileDeclaration="$varName:=file('/data/flux.xml')";
		VariableBlock block = (VariableBlock) parser.digest(Arrays.asList(varFileDeclaration), null);
		List<String> cells = block.getBlockLines().get(0).getCells();
		boolean varNameOk = cells.get(0).equals("$varName");
		boolean varValueOK = cells.get(1).equals("file('/data/flux.xml')");
		Assert.assertEquals(true, varNameOk);
		Assert.assertEquals(false, varValueOK);
	}
	
}

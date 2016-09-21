package io.toast.tk.runtime.core.parse;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import io.toast.tk.dao.domain.impl.test.block.VariableBlock;
import io.toast.tk.runtime.core.parse.VariableBlockParser;

public class VariableParserTestCase {

	private VariableBlockParser parser = new VariableBlockParser();
	
	@Test
	public void fileReferenceTest() throws IOException{
		String fluxExpectedValue = IOUtils.toString(VariableParserTestCase.class.getResourceAsStream("/data/flux.xml"));
		String varFileDeclaration="$varName:=file('data/flux.xml')";
		VariableBlock block = (VariableBlock) parser.digest(Collections.singletonList(varFileDeclaration));
		List<String> cells = block.getBlockLines().get(0).getCells();
		boolean varNameOk = cells.get(0).equals("$varName");
		boolean varValueKo = cells.get(1).equals("file('data/flux.xml')");
		boolean varValueOk = cells.get(1).equals(fluxExpectedValue);
		Assert.assertEquals(true, varNameOk);
		Assert.assertEquals(false, varValueKo);
		Assert.assertEquals(true, varValueOk);
	}
	
}

package io.toast.tk.runtime.core.parse;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;

import io.toast.tk.dao.domain.BlockType;
import io.toast.tk.dao.domain.impl.test.block.IBlock;
import io.toast.tk.dao.domain.impl.test.block.VariableBlock;
import io.toast.tk.dao.domain.impl.test.block.line.BlockLine;
import io.toast.tk.runtime.parse.IBlockParser;

/**
 * Parser for variable blocks.
 * Parse all lines beginning with $
 */
public class VariableBlockParser implements IBlockParser {

    private static String VARIABLE_ASSIGNATION_SEPARATOR = ":=";

    private static String FILE_REFERENCE_REGEX = "file\\(\'([\\/\\.\\w]+)\'\\)";
    
    private static Pattern FILE_REFERENCE_PATTERN = Pattern.compile(FILE_REFERENCE_REGEX);

    @Override
    public BlockType getBlockType() {
        return BlockType.VARIABLE;
    }

    @Override
    public IBlock digest(
		    final List<String> strings
    ) throws IOException {
    	final VariableBlock variableBlock = new VariableBlock();
        for(final Iterator<String> iterator = strings.iterator(); iterator.hasNext(); ) {
            final String line = iterator.next();
            if(!isFirstLineOfBlock(line)) { // line is parsable
                return variableBlock;
            }
            digestLine(variableBlock, iterator, line);
        }
        return variableBlock;
    }

	private static void digestLine(
		final VariableBlock variableBlock,
		final Iterator<String> iterator, 
		final String line
	) throws IOException {
		final String[] textLine = line.split(VARIABLE_ASSIGNATION_SEPARATOR);
		final List<String> variableParts = new ArrayList<>();
		final String variableName = textLine[0].trim();
		variableParts.add(variableName);
		if(isVarMultiLine(line)) {
		    variableParts.add(buildMultiLineValue(variableBlock, iterator, line));
		} 
		else if(isVarLine(line)) {
			final String variableValue = textLine[1].trim();
			variableParts.add(getVarValue(variableValue));
			variableBlock.addTextLine(line);
		}
		final BlockLine blockLine = new BlockLine();
		blockLine.setCells(variableParts);
		variableBlock.addline(blockLine);
	}

	private static String buildMultiLineValue(
		final VariableBlock variableBlock, 
		final Iterator<String> iterator,
		final String line
	) {
		final StringBuilder variableValue = new StringBuilder(512);
		variableBlock.addTextLine(line);
		while(iterator.hasNext()) {
			final String nextLine = iterator.next();
			variableBlock.addTextLine(nextLine);
		    if(!nextLine.startsWith("\"\"\"")) {
		    	variableValue.append(nextLine.replace("\n", " ").replace("\t", " ")).append(" ");
		    } 
		    else {
		        break;
		    }
		}
		return variableValue.toString();
	}

    private static String getVarValue(final String variableValue) throws IOException {
    	if(isFileReference(variableValue)) {
    		final String fileRef = getFileReference(variableValue);
    		try(final InputStream resourceAsStream = VariableBlockParser.class.getClassLoader().getResourceAsStream(fileRef);) {
    			return IOUtils.toString(resourceAsStream);
    		}
    	}
		return variableValue;
	}

	private static String getFileReference(final String variableValue) {
		final Matcher matcher = getFileRefMather(variableValue);
		matcher.find();
		return matcher.group(1);
	}

	private static Matcher getFileRefMather(final String value) {
		return FILE_REFERENCE_PATTERN.matcher(value);
	}

	private static boolean isFileReference(final String variableValue) {
		return getFileRefMather(variableValue).matches();
	}

	@Override
    public boolean isFirstLineOfBlock(final String line) {
        return isVarLine(line) || isVarMultiLine(line);
    }

    private static boolean isVarMultiLine(final String line) {
        return 
        		line != null 
        		&& 
        		line.startsWith("$")
                && 
                line.contains(VARIABLE_ASSIGNATION_SEPARATOR)
                && 
                line.contains("\"\"\"")
       ;
    }

    private static boolean isVarLine(final String line) {
        return 
        		line != null 
        		&& 
        		line.startsWith("$")
                && 
                line.contains(VARIABLE_ASSIGNATION_SEPARATOR)
                && 
                !line.contains("\"\"\"")
        ;
    }
}
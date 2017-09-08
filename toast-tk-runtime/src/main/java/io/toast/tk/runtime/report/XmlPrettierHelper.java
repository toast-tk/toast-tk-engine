package io.toast.tk.runtime.report;

import java.util.ArrayList;
import java.util.List;

public class XmlPrettierHelper {

	private XmlPrettierHelper() {
		
	}
	
	static String getPrettySentenceText(String str) {
		if(str.contains("*")) {
			String separator = "\\*";
			String[] lines = str.split(separator);
			List<String> res = getPrettyXmlText(lines);
			String resultat = String.join("*", res);
			if(lines.length % 2  == 0) {
				resultat += "*";
			}
			return resultat;
		} else {
			return getPrettyXmlText(str);
		}
	}
	static List<String> getPrettyXmlText(String[] lines) {
		List<String> res = new ArrayList<>();
		for(String line : lines) {
			if((line.indexOf('<') < line.indexOf('>') + 1) 
				&& line.contains("<") && line.contains(">") ) {
				res.add(System.lineSeparator() + getPrettyXmlText(line)
						+ System.lineSeparator());
			} else {
				if(!"".equals(line)) {
					res.add(line);
				}
			}
		}
		return res;
	}
	static String getPrettyXmlText(String str) {
		// IF this is not an XML
		if(str.indexOf('<') >= str.indexOf('>') + 1) {
			return str;
		}
		
		String separator = System.lineSeparator();
		String[] lines = str.split(separator);
		List<String> res = new ArrayList<>();
		int tabNb = 0;
		for(String line : lines) {
			boolean isInside = true;
			boolean lastIsInside = true;
			boolean asToClose = false;
			if(line.contains("<") && line.contains(">")) {
				String lineTemp = line.trim()
						.replace("\t", "")
						.replace("\n", "")
						.replace("\r", "");
				int index = lineTemp.indexOf('<');
				String firstPart = lineTemp.substring(0, index).trim();
				if(!"".equals(firstPart.trim())) {
					res.add(firstPart);
				}
				
				String secondPart = lineTemp.substring(index);
				for(String lineSplit : secondPart.split("<|>")) {
					String lineResult = "";
					lineSplit = lineSplit.trim();
					if(lineSplit.equals("")) {
						isInside = true;
						lastIsInside = isInside;
						continue;
					} else {
						isInside = lastIsInside ? false : true;
					}
					if(lineSplit.startsWith("/")) {
						tabNb += -1;
					}
					for(int i = 1; i <= tabNb; i++) {
						lineResult = "\t" + lineResult;
					}
					if(!lineSplit.startsWith("/") && !lineSplit.endsWith("/") 
							&& !lineSplit.startsWith("?") && !lineSplit.endsWith("?")
							&& lastIsInside) {
						tabNb += +1;
					}
					lineResult += lastIsInside ? '<' + lineSplit + '>' : lineSplit;
					if(!lastIsInside || isInside || asToClose){
						String lineResultTemp = res.get(res.size()-1) + lineResult.trim();
						res.set(res.size()-1, lineResultTemp);
						asToClose = asToClose ? false : true;
					} else {
						res.add(lineResult);
					}
					lastIsInside = isInside;
				} 
			} else {
				if(!"".equals(line.trim())) {
					res.add(line);
				}
			}
			
		}
		return String.join(separator, res);
	}

}

package com.synaptix.toast.runtime.parse;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;

import io.toast.tk.runtime.parse.AbstractParser;

/**
 * Created by Nicolas Sauvage on 02/08/2016.
 */
public class ScriptHelper {

	public static List<String> getScript(String filename) {
		InputStream resourceAsStream = ScriptHelper.class.getResourceAsStream("/" + filename);

		List<String> list = new BufferedReader(new InputStreamReader(resourceAsStream,
				StandardCharsets.UTF_8)).lines().collect(Collectors.toList());

		return removeBom(list);
	}

	public static List<String> removeBom(final List<String> list) {
		final String firstLine = list.get(0);
		if(firstLine.startsWith("\uFEFF")) {
			list.set(0, firstLine.substring(1));
		}
		return list;
	}

}

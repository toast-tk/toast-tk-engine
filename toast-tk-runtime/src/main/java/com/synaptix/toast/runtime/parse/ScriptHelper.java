package com.synaptix.toast.runtime.parse;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Nicolas Sauvage on 02/08/2016.
 */
public class ScriptHelper {

	public static List<String> getScript(String filename) {
		ClassLoader classLoader = ScriptHelper.class.getClassLoader();
		URL resource = classLoader.getResource(filename);
		List<String> list = new ArrayList<>();

		Path path = Paths.get(filename);
		System.out.println("path = " + path);
		try (BufferedReader br = Files.newBufferedReader(Paths.get(resource.toURI()))) {

			//br returns as stream and convert it into a List
			list = br.lines().collect(Collectors.toList());

		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}

		return AbstractParser.removeBom(list);
	}

}

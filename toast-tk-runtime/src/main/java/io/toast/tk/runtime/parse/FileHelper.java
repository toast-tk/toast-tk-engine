package io.toast.tk.runtime.parse;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Helper class for getting files content.
 * <p>
 * Created by Nicolas Sauvage on 02/08/2016.
 */
public class FileHelper {

	public static List<String> getScript(String filename) {
		InputStream resourceAsStream = getInputStream(filename);

		List<String> list = new BufferedReader(new InputStreamReader(resourceAsStream,
				StandardCharsets.UTF_8)).lines().collect(Collectors.toList());

		return removeBom(list);
	}

	/**
	 * @param filename File name, without any preceding "/"
	 */
	public static InputStream getInputStream(String filename) {
		System.out.println("Open input stream: " + filename);
		return FileHelper.class.getResourceAsStream("/" + filename);
	}

	public static List<String> removeBom(final List<String> list) {
		final String firstLine = list.get(0);
		if (firstLine.startsWith("\uFEFF")) {
			list.set(0, firstLine.substring(1));
		}
		return list;
	}

}

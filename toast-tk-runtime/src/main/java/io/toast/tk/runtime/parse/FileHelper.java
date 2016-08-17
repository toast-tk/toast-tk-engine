package io.toast.tk.runtime.parse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;

/**
 * Helper class for getting files content.
 * <p>
 * Created by Nicolas Sauvage on 02/08/2016.
 */
public class FileHelper {

	private static final org.apache.logging.log4j.Logger LOG = LogManager.getLogger(FileHelper.class);

	public static List<String> getScript(String filename) throws IOException {
		InputStream resourceAsStream = getInputStream(filename);

		if (resourceAsStream == null) {
			throw new IOException("Could not open file " + filename);
		}

		List<String> list = new BufferedReader(new InputStreamReader(resourceAsStream,
				StandardCharsets.UTF_8)).lines().collect(Collectors.toList());

		return removeBom(list);
	}

	/**
	 * @param filename File name, without any preceding "/"
	 */
	public static InputStream getInputStream(final String filename) {
		LOG.debug("Open input stream: " + filename);
		String fullFileName = filename;
		if (!filename.startsWith("\\")) {
			fullFileName = "/" + fullFileName;
		}
		return FileHelper.class.getResourceAsStream(fullFileName);
	}

	static List<String> removeBom(final List<String> list) {
		final String firstLine = list.get(0);
		if (firstLine.startsWith("\uFEFF")) {
			list.set(0, firstLine.substring(1));
		}
		return list;
	}

}

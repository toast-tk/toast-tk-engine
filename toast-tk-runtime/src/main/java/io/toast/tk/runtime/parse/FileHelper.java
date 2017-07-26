package io.toast.tk.runtime.parse;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;

/**
 * Helper class for getting files content.
 */
public class FileHelper {

	private static final org.apache.logging.log4j.Logger LOG = LogManager.getLogger(FileHelper.class);

	private FileHelper() {

	}
	
	public static List<String> getScript(InputStream resourceAsStream) throws IOException {
		List<String> list = new BufferedReader(new InputStreamReader(resourceAsStream,
				StandardCharsets.UTF_8)).lines().collect(Collectors.toList());

		return removeBom(list);
	}

	public static List<String> getScript(String filename) throws IOException {
		InputStream resourceAsStream = getInputStream(filename);
		
		if (resourceAsStream == null) {
			String data = readFile(filename);
			resourceAsStream = new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8));		
		}
		
		return getScript(resourceAsStream);
	}

	/**
	 * @param filename File name, without any preceding "/"
	 */
	public static InputStream getInputStream(final String filename) {
		LOG.debug("Open input stream: " + filename);
		String fullFileName = filename;
		if (!(filename.startsWith("\\") || filename.startsWith("C:"))) {
			fullFileName = "/" + fullFileName;
		}
		return FileHelper.class.getResourceAsStream(fullFileName);
	}

	public static String readFile(String fileName) {
		String lines = "";
		try {
			lines = new String(Files.readAllBytes(Paths.get(fileName)), StandardCharsets.UTF_8);
			return lines;
		} 
        catch(IOException ex) {
        	String exception = "Error reading file '" + fileName + "' caused by " + ex.getMessage();
        	LOG.info(exception);
            return exception;       
        }
    }

	public static String getStringFromInputStream(InputStream is) {
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
		
		String line;
		try {
			br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
			while ((line = br.readLine()) != null) {
				sb.append(line).append(System.lineSeparator());
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return sb.toString();
	}
	
	public static void createDirectory(String arg) {
		File theDir = new File(arg);

		if (!theDir.exists()) {
			System.out.println("creating directory: " + theDir.getName());
	
		    try{
		        theDir.mkdir();
		    } 
		    catch(SecurityException se){
		        throw se;
		    }        
		    
		    System.out.println("DIR created"); 
		}
	}

	public static void writeFile(String arg, String fileName) throws IOException {
		 try {
			 FileOutputStream fileWriter =   new FileOutputStream(fileName);
	            BufferedWriter bufferedWriter = new BufferedWriter(
	            		new OutputStreamWriter(fileWriter, StandardCharsets.UTF_8));
	            bufferedWriter.write(arg);
	            bufferedWriter.close();
	        }
	        catch(IOException ex) {
	            throw new IOException("Error writing to file '" + fileName + "' cause by " + ex.getMessage());
	        }
	}

	public static String getLastModifiedFile(String directoryPath) {
		File directory = new File(directoryPath);
		File moreRecentFile = null;
		
		if (!directory.exists()) {
			LOG.warn("The file/directory '" + directoryPath + "' does not exist");
		} else if (!directory.isDirectory()) {
			LOG.warn("The path '" + directoryPath + "' does not match to a directory");
		} else {
			File[] subfiles = directory.listFiles();
			
			if( subfiles.length > 0 ){
				moreRecentFile = subfiles[0];
				for (int i = 1; i < subfiles.length; i++) {
					File subfile = subfiles[i];
					//System.out.println(subfile.getName());
					
					if (subfile.lastModified() > moreRecentFile.lastModified()) 
						moreRecentFile = subfile;
				}
				return directoryPath + File.separatorChar + moreRecentFile.getName();
			}else{
				LOG.warn("The directory '" + directoryPath + "' is empty!");
			}
		}
		return "";
	}

	public static void copyFile(String filePath, String repertory) throws IOException {
		File file = new File(filePath);
		String fileName = file.getName();
		String fileSeparator = String.valueOf(File.separatorChar);
		String fileOutputPath = repertory.endsWith(fileSeparator) ? repertory + fileName : repertory + fileSeparator + fileName;
		Files.copy(file.toPath(), (new File(fileOutputPath)).toPath());
	}
	
	static List<String> removeBom(final List<String> list) {
		final String firstLine = list.get(0);
		if (firstLine.startsWith("\uFEFF")) {
			list.set(0, firstLine.substring(1));
		}
		return list;
	}

	public static boolean existFile(String fileName) {
		File file = new File(fileName);
		return file.exists();
    }
	
	public static boolean isFile(String fileName) {
		File file = new File(fileName);
		return file.isFile();
    }
	
	public static boolean isDirectory(String fileName) {
		File file = new File(fileName);
		return file.isDirectory();
    }
}

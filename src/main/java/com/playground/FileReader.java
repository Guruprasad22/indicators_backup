package com.playground;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Guruprasad Bhaskar
 * class to read content of files from a given directory
 */
public class FileReader {
	
	private String directoryName;
	private List<File> fileNames ;
	
	public FileReader() {
		directoryName = "";
		fileNames = new ArrayList<File>();
	}
	
	public List<File> loadDataFiles() throws Exception {
		
		if(directoryName == null) {
			throw new Exception("Please provide a valid directoryName from where to pick up files..");
		}		
		File dir = new File(directoryName);
		
		File[] files = dir.listFiles(); // get all files under the directory
		for(File file : files) {
			fileNames.add(file);
		}
		return fileNames;
	}

	public String getDirectoryName() {
		return directoryName;
	}

	public void setDirectoryName(String directoryName) {
		this.directoryName = directoryName;
	}

	public List<File> getFileNames() {
		return fileNames;
	}

	public void setFileNames(List<File> fileNames) {
		this.fileNames = fileNames;
	}
}
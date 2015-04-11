package com.playground.testsuite;

import java.io.File;
import java.util.List;

import org.junit.Test;

import com.playground.FileReader;
/**
 * Unit test for simple App.
 */
public class IndicatorTest {

    /*
     * test if we can get the correct list of files
     */
	
	@Test
    public void doFilesTest() throws Exception {
    	FileReader reader = new FileReader();
    	reader.setDirectoryName("C:\\Vault\\bhav");
    	List<File> files = reader.loadDataFiles();
    	
    	for(File file: files) {
    		System.out.println(file.toString());
    	}    	
    }	
}

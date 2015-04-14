package com.playground.service;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;
import com.playground.model.Ticker;
import com.playground.utility.StringToPojo;


public class DatabaseService {
	
	private final String resource = "SqlMapConfig.xml";
	private SqlMapClient sqlMap;
	private Reader reader;
	private static Logger log = Logger.getLogger(DatabaseService.class);
	
	public DatabaseService() throws IOException {
		setUp();
	}
	
	public void setUp() throws IOException {		
		reader = Resources.getResourceAsReader(resource);
		sqlMap = SqlMapClientBuilder.buildSqlMapClient(reader);		
	}
	
	public void createDatabaseTables() throws SQLException{
		 //create ticker table if it does not exist
		 String tableExists = (String) sqlMap.queryForObject("findTable","ticker");

		 if(tableExists == null) {
			 log.info("table " + " TICKER" + " does not exist");
			 sqlMap.update("createTable");
		 } else {
			 log.info("table " + " TICKER" + " exists");
		 }
		 
		 // create file table if it does not exist
		 tableExists = (String) sqlMap.queryForObject("findTable","file");	 

		 if(tableExists == null) {
			 log.info("table " + " FILE" + " does not exist");
			 sqlMap.update("createTable_File");
		 } else {
			 log.info("table " + " FILE" + "exists ");
		 }
	}
	
	/*
	 * check if files are already committed to database
	 */
	public List<File> doValidation() throws Exception {
		 // read all the ticker files already inserted into database
		 List<String> tickerFiles =  (List<String>) sqlMap.queryForList("getFilenames");
		 
		 if(tickerFiles.isEmpty()) {
			 log.info("There are no files committed to database");
		 }
		 else {
			 log.info("The files committed to database are ");
			 for(String fileName : tickerFiles) {
				 log.info(fileName);
			 }
		 }
		 
		 List<File> filteredFiles = new ArrayList<File>();
		 FileReaderService fileReaderService = new FileReaderService();
		 fileReaderService.setDirectoryName("C:\\Vault\\bhav");
		 List<File> inputFiles = fileReaderService.loadDataFiles();
		 for(File f : inputFiles) {
			 if(!tickerFiles.contains(f.getName())) {
				 filteredFiles.add(f);
			 }
		 }		 
		 return filteredFiles;
	}
	
	// fetch the tickers and commit them to database
	public void commitRecords() throws Exception {
		
		FileReaderService reader =  new FileReaderService();
    	reader.setDirectoryName("C:\\Vault\\bhav");
    	List<File> files = reader.loadDataFiles();
    	List<Ticker> tickers = reader.readDataFiles();
	
		long startTime  = System.currentTimeMillis();
		
		sqlMap.startTransaction();
		sqlMap.startBatch();
		String line = null;
		
		StringToPojo stringToPojo = new StringToPojo();
		
		for(Ticker ticker: tickers) {
			sqlMap.insert("insertTicker",ticker);
		}	
		
		sqlMap.executeBatch();
		sqlMap.commitTransaction();
		 
		sqlMap.startTransaction();
		sqlMap.startBatch();
		 
		for(File f: doValidation()) {
		 log.info("Inserting file " + f.getName() + " into database ");
		 sqlMap.insert("insertFile",new String(f.getName()));
		}
		 
		sqlMap.executeBatch();
		sqlMap.commitTransaction();
		 
		long endTime  = System.currentTimeMillis();
		log.info("Total time taken = " + (endTime- startTime)/1000 + " seconds");
		
	}
}

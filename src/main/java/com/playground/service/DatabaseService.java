package com.playground.service;

import java.io.IOException;
import java.io.Reader;
import java.sql.SQLException;

import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;


public class DatabaseService {
	
	private final String resource = "SqlMapConfig.xml";
	private SqlMapClient sqlMap;
	private Reader reader;
	
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
			 System.out.println("table " + " TICKER" + " does not exist");
			 sqlMap.update("createTable");
		 } else {
			 System.out.println("table " + " TICKER" + " exists");
		 }
		 
		 // create file table if it does not exist
		 tableExists = (String) sqlMap.queryForObject("findTable","file");	 

		 if(tableExists == null) {
			 System.out.println("table " + " FILE" + " does not exist");
			 sqlMap.update("createTable_File");
		 } else {
			 System.out.println("table " + " FILE" + "exists ");
		 }
	}
}

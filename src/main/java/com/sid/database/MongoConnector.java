package com.sid.database;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
public class MongoConnector {
	
	private static MongoConnector INSTANCE;

	private MongoDatabase database;
	MongoCollection<Document> collection;
	
	
	public MongoConnector() {
		String db = "";
		String uri = "";
		try {
			Properties p = new Properties();
			p.load(new FileInputStream("src/main/resources/config.ini"));
			db = p.getProperty("mongo_db");
			uri =  p.getProperty("mongo_uri");
			MongoClient mongoClient = new MongoClient(new MongoClientURI(uri));
			database = mongoClient.getDatabase(db);
			collection = database.getCollection("medicoes_sensores");
			mongoClient.close();
		} catch (IOException e) {
			System.err.println("Uri : -> "+uri+"\nDb : -> "+db);
			e.printStackTrace();
		}
	}


	
	
	
	public static MongoConnector getInstance() {
		if(INSTANCE == null)
			INSTANCE = new MongoConnector();
		return INSTANCE;
	}
}

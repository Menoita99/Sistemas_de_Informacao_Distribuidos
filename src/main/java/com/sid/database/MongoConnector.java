package com.sid.database;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;
import org.bson.Document;
import org.json.JSONObject;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.ChangeStreamIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;

import lombok.Data;

@Data
public class MongoConnector {

	private static MongoConnector INSTANCE;

	private MongoDatabase database;
	private MongoCollection<Document> collection;

	private Document doc= null;

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

			initiateChangeListener(mongoClient);
		} catch (IOException e) {
			System.err.println("Uri : -> "+uri+"\nDb : -> "+db);
			e.printStackTrace();
		}
	}




	
	/**
	 * Start the Listener Thread that will watch Mongo messages
	 * and closes Mongo Client 
	 */
	private void initiateChangeListener(MongoClient mc) {
		ChangeStreamIterable<Document> changes = collection.watch(
				Arrays.asList(Aggregates.match(Filters.and(Arrays.asList( Filters.in("operationType", Arrays.asList("insert")))))));

		new Thread(() ->{
			changes.iterator().forEachRemaining(change -> {
				doc = change.getFullDocument();
				notifyRead();
			});
			mc.close();
		}).start();
	}



	

	/**
	 * returns singleton instance
	 */
	public static MongoConnector getInstance() {
		if(INSTANCE == null)
			INSTANCE = new MongoConnector();
		return INSTANCE;
	}


	
	

	/**
	 * This method stops the thread and waits for a Mongo message
	 * @return returns Mongo object read
	 */
	public synchronized JSONObject read() {
		try {
			while(doc == null) 
				wait();
			JSONObject jsonObject = new JSONObject(doc.toJson());
			doc = null;
			return jsonObject;
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	
	


	/**
	 *Notifies thread that are in waiting 
	 */
	private synchronized void notifyRead() {
		this.notify();
	}


	
	
	
	public static void main(String[] args) {
		MongoConnector.getInstance();
	}
}

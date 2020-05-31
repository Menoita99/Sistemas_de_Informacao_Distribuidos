package com.sid.database;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.JSONObject;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.ChangeStreamIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.sid.models.Alarm;
import com.sid.models.Measure;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lombok.Data;

@Data
public class MongoConnector {

	private static MongoConnector INSTANCE;

	private MongoDatabase database;
	private MongoCollection<Document> collection;
	private MongoCollection<Document> alarmsCollection;

	private Document doc = null;

	public MongoConnector() {
		String db = "";
		String uri = "";
		try {
			Properties p = new Properties();
			p.load(new FileInputStream("src/main/resources/config.ini"));
			db = p.getProperty("mongo_db");
			uri = p.getProperty("mongo_uri");
			MongoClient mongoClient = new MongoClient(new MongoClientURI(uri));
			database = mongoClient.getDatabase(db);
			collection = database.getCollection("medicoes_sensores");
			alarmsCollection = database.getCollection("backup_alarmes");

			initiateChangeListener(mongoClient);
		} catch (IOException e) {
			System.err.println("Uri : -> " + uri + "\nDb : -> " + db);
			e.printStackTrace();
		}
	}

	
	
	
	
	
	/**
	 * Start the Listener Thread that will watch Mongo messages and closes Mongo
	 * Client
	 */
	private void initiateChangeListener(MongoClient mc) {
		ChangeStreamIterable<Document> changes = collection.watch(Arrays.asList(
				Aggregates.match(Filters.and(Arrays.asList(Filters.in("operationType", Arrays.asList("insert")))))));

		new Thread(() -> {
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
		if (INSTANCE == null)
			INSTANCE = new MongoConnector();
		return INSTANCE;
	}

	
	
	
	
	
	/**
	 * This method stops the thread and waits for a Mongo message
	 * 
	 * @return returns Mongo object read
	 */
	public synchronized JSONObject read() {
		try {
			while (doc == null)
				wait();
			JSONObject jsonObject = new JSONObject(doc.toJson());
			doc = null;
			return jsonObject;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	
	
	
	
	
	/**
	 * This method deletes a mongodb entry with the given id
	 * 
	 * @param m
	 */
	public void deleteEntryWithObjectId(String id) {
		collection.deleteOne(new Document("_id", new ObjectId(id)));
	}

	
	
	
	
	
	/**
	 * Notifies thread that are in waiting
	 */
	private synchronized void notifyRead() {
		this.notify();
	}






	public List<Measure> findAllMeasures() {
		List<Measure> list = new ArrayList<>();
		MongoCursor<Measure> iterator = collection.find().map((d) -> new Measure(new JSONObject(d.toJson()))).iterator();
		
		while(iterator.hasNext()) 
			list.add(iterator.next());
		
		return list;
	}
	
	/**
	 * inserts an alarm into the collection backup_alarmes
	 * 
	 * @param alarm
	 */
	public void insertAlarm(Alarm alarm) {
		
		Document alarmDoc = new Document();
		alarmDoc.put("valorMedicao", alarm.getValorMedicao());
		alarmDoc.put("limite", alarm.getLimite());
		alarmDoc.put("tipoSensor", alarm.getTipoSensor());
		alarmDoc.put("extra", alarm.getExtra());
		alarmDoc.put("descricao", alarm.getDescricao());
		alarmDoc.put("dataHoraMedicao", alarm.getDataHoraMedicao());
		alarmDoc.put("controlo", alarm.getControlo());	
		alarmsCollection.insertOne(alarmDoc);
		System.out.println("Inserted alarm: " + alarmDoc.toJson());
	}
	
	
	/**
	 * Returns a list with all the alarms in the collection backup_alarmes
	 * @return
	 */
	public List<Alarm> findAllAlarms() {
		List<Alarm> alarms = new ArrayList<>();
		MongoCursor<Alarm> iterator = alarmsCollection.find().map( (doc)
				-> new Alarm(doc.getDouble("valorMedicao"), doc.getString("tipoSensor"), formatDate(doc.getString("dataHoraMedicao")), doc.getDouble("limite"), 
								doc.getString("descricao"), doc.getString("extra"), formatBool(doc.getInteger("controlo")))).iterator();
		while(iterator.hasNext())
			alarms.add(iterator.next());
		return alarms;
		
	}
	
	/**
	 * transforms a String date into a LocalDateTime
	 * @param date
	 * @return
	 */
	private LocalDateTime formatDate(String date) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime dateTime = LocalDateTime.parse(date, formatter);
		return dateTime;
	}
	
	/**
	 * transforms a int into a boolean 0 = true, 1 = false
	 * @param i
	 * @return
	 */
	private boolean formatBool(int i) {
		return i == 0;
	}





	/**
	 * Deletes an alarm from mongo collection - backup_alarmes
	 * 
	 * @param alarm
	 */
	public void deleteAlarm(Alarm alarm) {
		Document alarmDoc = new Document();
		Document deletedDoc;
		alarmDoc.put("valorMedicao", alarm.getValorMedicao());
		alarmDoc.put("limite", alarm.getLimite());
		alarmDoc.put("tipoSensor", alarm.getTipoSensor());
		alarmDoc.put("extra", alarm.getExtra());
		alarmDoc.put("descricao", alarm.getDescricao());
		alarmDoc.put("dataHoraMedicao", alarm.getDataHoraMedicao());
		alarmDoc.put("controlo", alarm.getControlo());
		deletedDoc = alarmsCollection.findOneAndDelete(alarmDoc);
		if(deletedDoc != null)
			System.out.println("Deleted alarm: " + deletedDoc.toJson());
		else
			System.out.println("No alarm deleted");
	}
	
	
	
}

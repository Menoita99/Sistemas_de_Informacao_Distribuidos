package com.sid.database;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.json.JSONObject;

public class AndroidSocketServer {
	
	private static AndroidSocketServer INSTANCE;
	private ServerSocket serverSocket;


	public void start() {
		try{
			serverSocket = new ServerSocket(42000);
			new Thread(() -> {
				while(true) {
					try {
						System.out.println("Looking for Android connections");
						Socket android = serverSocket.accept();
						System.out.println("Connection achived with ip "+android.getInetAddress().getHostAddress());
						
						new Thread(() -> resolveAndroidCommunicationSocket(android)).start();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}).start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	

	private void resolveAndroidCommunicationSocket(Socket android) {
		try {
			ObjectInputStream input = new ObjectInputStream(android.getInputStream());
			ObjectOutputStream output = new ObjectOutputStream(android.getOutputStream());

			//RECEBES ISTO
			//{"limiteLuminosidade":20,"margemLuminosidade":20,"limiteHumidade":30,"margemTemperatura":0,"limiteTemperatura":50,"margemHumidade":0}
			String inputSys = (String) input.readObject(); 

			JSONObject modifiedObj = new JSONObject(inputSys);

			System.out.println("Received -> "+inputSys);

			//retorna um JSONObject.toString();
			output.writeObject(modifiedObj.toString());
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * returns singleton instance
	 */
	public static AndroidSocketServer getInstance() {
		if (INSTANCE == null)
			INSTANCE = new AndroidSocketServer();
		return INSTANCE;
	}

}

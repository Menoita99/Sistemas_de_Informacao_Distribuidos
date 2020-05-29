package com.sid.launch;

import com.sid.database.AndroidSocketServer;
import com.sid.gui.PerformanceMonitor;
import com.sid.process.Processor;

public class Launch {
	public static void main(String[] args) {
		Processor.getInstance();
		AndroidSocketServer.getInstance().start();
		new Thread(() -> PerformanceMonitor.start(args)).start();
		Processor.getInstance().Process();
	}
}

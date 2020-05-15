package com.sid.gui;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.sid.models.Measure;
import com.sid.process.Processor;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ListChangeListener.Change;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;

public class PerformanceMonitor extends Application implements Initializable{

	@FXML private LineChart<String, Number> chart;
	@FXML private NumberAxis yAxis;
	@FXML private CategoryAxis xAxis;
	@FXML private ListView<?> alarmList;
	@FXML private ProgressBar progressBar;

	private static int MAX_ELEMS = 20; 

	XYChart.Series<String,Number> temSerie = new XYChart.Series<>();
	XYChart.Series<String,Number> humSerie = new XYChart.Series<>();
	XYChart.Series<String,Number> lumSerie = new XYChart.Series<>();

	private Processor processor = Processor.getInstance();






	@Override
	public void initialize(URL location, ResourceBundle resources) {
		xAxis.setLabel("Time");
		xAxis.setAnimated(false);
		yAxis.setLabel("value");
		yAxis.setAnimated(false);

		temSerie.setName("Temperature");
		humSerie.setName("Humidity");
		lumSerie.setName("Luminosity");

		chart.getData().add(temSerie);
		chart.getData().add(lumSerie);
		chart.getData().add(humSerie);
		chart.setAnimated(false);

		initializeChartListeners();
	}






	@SuppressWarnings("unchecked")
	private void initializeChartListeners() {
		processor.getMeasures().addListener((Change<? extends Measure> c) -> {
			while (c.next()) {
				if (!c.getAddedSubList().isEmpty() && !c.getList().isEmpty()) {
					Measure measure = c.getAddedSubList().get(0);

					Platform.runLater(() -> {
						List<Measure> list = new ArrayList<>();
						if(c.getFrom() < c.getList().size()) 
							list = (List<Measure>) c.getAddedSubList();
						else 
							list.add(measure);

						for (Measure item : list) {
							String time = item.getDataHoraMedicao().format(DateTimeFormatter.ofPattern("mm:ss"));
							lumSerie.getData().add(new XYChart.Data<String,Number>( time, item.getValorLumMedicao()));
							temSerie.getData().add(new XYChart.Data<String,Number>( time, item.getValorTmpMedicao()));
							humSerie.getData().add(new XYChart.Data<String,Number>( time, item.getValorHumMedicao()));

							if (lumSerie.getData().size() > MAX_ELEMS) {
								temSerie.getData().remove(0);
								humSerie.getData().remove(0);
								lumSerie.getData().remove(0);
							}
						}
					});
				} 
			}
		});
	}






	@Override
	public void start(Stage window) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("scene.fxml"));	
		Parent root = loader.load();

		window.setOnCloseRequest((event) -> {Platform.exit(); processor.close();});
		window.setTitle("Monitor");
		window.setScene(new Scene(root));
		window.show();
	}






	public static void start(String[] Args) {
		PerformanceMonitor.launch(Args);
	}
}

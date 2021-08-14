package sample;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.*;


public class Main extends Application {

    protected static class StockReader{
        final static String baseUrl = "https://www.alphavantage.co/query?function=TIME_SERIES_MONTHLY&symbol=";
        final static String api = "&apikey=H74ZYVXIYB2D1985";
        private KeyController task;
        protected String[] keySet;
        protected Double[] openSet, highSet, lowSet, closeSet;

        public StockReader( String stockTicker ) throws IOException {
            this.task = new KeyController( urlReader(stockTicker ) );
            this.keySet = this.task.grabKeys();
            this.openSet = this.task.grabOpenPrices();
            this.highSet = this.task.grabHighPrices();
            this.lowSet = this.task.grabLowPrices();
            this.closeSet = this.task.grabClosePrices();
        }

        public JsonObject urlReader(String stockTicker ) throws IOException {
            String contents = "";
            URL url = new URL( baseUrl +stockTicker + api );

            Scanner urlScanner = new Scanner(url.openStream());
            while (urlScanner.hasNextLine() ) {
                contents += urlScanner.nextLine() +"\n";
            }
            urlScanner.close();
            return (JsonObject) Jsoner.deserialize( contents, new JsonObject());
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception{

        GridPane pane = new GridPane();
        pane.setAlignment( Pos.CENTER );
        pane.setPadding(new Insets(10,10,10,10));
        pane.setHgap(5.5);
        pane.setVgap(5.5);

        //Define Button In Grid Pane
        final Button enter = new Button("Enter");
        pane.add(enter,0,0);

        //Define TextText Field In Grid Pane for Ticker
        final TextField tickerBox = new TextField();
        tickerBox.setPromptText("Ticker Symbol");
        tickerBox.setFont( Font.font("Times New Roman"));
        tickerBox.setPrefColumnCount(9);
        tickerBox.getText();
        pane.add( tickerBox, 1,0);

        //Define TextText Field In Grid Pane for Month Span
        final TextField spanBox = new TextField();
        spanBox.setPromptText("Monthly Span");
        spanBox.setFont( Font.font("Times New Roman"));
        spanBox.getText();
        pane.add( spanBox, 2,0);

        //Message
        final Label message = new Label();
        pane.add(message,0,1);
        GridPane.setColumnSpan(message, 3);

        //Align Button
        GridPane.setValignment( tickerBox, VPos.BOTTOM );
        GridPane.setValignment( enter, VPos.BOTTOM);
        GridPane.setHalignment( message, HPos.CENTER );

        //Create Button Event
        enter.setOnAction( new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if ( tickerBox.getText() != null && !tickerBox.getText().isEmpty() &&
                        spanBox.getText() != null && !spanBox.getText().isEmpty()) {
                    try {
                        //Set New Stage
                        Stage stage = new Stage();
                        stage.setTitle(tickerBox.getText().toUpperCase()+" Chart");
                        final CategoryAxis xAxis = new CategoryAxis();
                        final NumberAxis yAxis = new NumberAxis();
                        xAxis.setLabel("Month");
                        yAxis.setLabel("Price");
                        final LineChart<String,Number> lineChart = new LineChart<String,Number>(xAxis,yAxis);
                        lineChart.setTitle( tickerBox.getText().toUpperCase() +" Analysis" );

                        //Populate Series
                        StockReader read = new StockReader( tickerBox.getText().toUpperCase() );
                        XYChart.Series series1 = new XYChart.Series();
                        series1.setName("Open Price");
                        XYChart.Series series2 = new XYChart.Series();
                        series2.setName("High Price");
                        XYChart.Series series3 = new XYChart.Series();
                        series3.setName("Low Price");
                        XYChart.Series series4 = new XYChart.Series();
                        series4.setName("Close Price");

                        for (int i = 0; i < Integer.parseInt(spanBox.getText()); i++ ) {
                            String x = LocalDate.parse( read.keySet[i] ).getMonth().toString().substring(0,3)+" "+
                                       Integer.toString(LocalDate.parse( read.keySet[i] ).getYear()).substring(2)+"'";

                            series1.getData().add(new XYChart.Data( x, read.openSet[i] ) );
                            series2.getData().add(new XYChart.Data( x, read.highSet[i] ) );
                            series3.getData().add(new XYChart.Data( x, read.lowSet[i] ) );
                            series4.getData().add(new XYChart.Data( x, read.closeSet[i] ) );
                            //+Integer.toString(LocalDate.parse( read.keySet[i] ).getYear()).substring(0,2)
                        }

                        lineChart.getData().addAll(series1, series2, series3,series4 );

                        //Display Scene
                        Scene scene  = new Scene(lineChart,800,600);
                        stage.setScene(scene);
                        stage.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    tickerBox.setPromptText("Improper Entry");
                    spanBox.setPromptText("Improper Entry");
                    message.setText("Please Fill Both Text Fields");
                    ScheduledExecutorService s = Executors.newScheduledThreadPool(1);
                    Runnable action1, action2;
                    action1 = () -> tickerBox.setPromptText("Try \"IBM\"");
                    action2 = () -> spanBox.setPromptText("Try \"12\"");
                    s.schedule(action1, 2, TimeUnit.SECONDS);
                    s.schedule(action2, 2, TimeUnit.SECONDS);
                }
            }
        });
        primaryStage.setScene( new Scene(pane,400,100) );
        primaryStage.setTitle("Stock Market Tracker");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}


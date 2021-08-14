package sample;

import com.github.cliftonlabs.json_simple.JsonObject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Set;

public class KeyController {
    private JsonObject document;
    private String[] keySet;

    KeyController(JsonObject document ) {
        this.document = document;
        this.keySet = grabKeys();

    }

    public String[] grabKeys() {
        JsonObject monthlyTimeSeries = (JsonObject) this.document.get( "Monthly Time Series" );
        ArrayList<String> keys = new ArrayList<>();
        Set<String> dates = monthlyTimeSeries.keySet();
        for ( String o: dates ){
            keys.add( o );
        }
        return sortKeys(keys);
    }

    public String[] sortKeys(ArrayList<String> obj ){
        String[] keys = new String[obj.size()];
        keys = obj.toArray(keys);

        LocalDate[] dates = new LocalDate[keys.length];
        int i;
        for(i = 0; i< dates.length;i++){
            dates[i] = LocalDate.parse(keys[i]);
        }

        for(i = 1; i < dates.length; ++i) {
            LocalDate key = dates[i];
            int j = i - 1;

            while (j >= 0 && dates[j].isBefore( key )) {
                dates[j + 1] = dates[j];
                j = j - 1;
            }
            dates[j + 1] = key;
        }

        for(i = 0; i < keys.length; i++ ) {
            keys[i] = dates[i].toString();
        }
        return keys;
    }

    public Double[] grabOpenPrices() {
        ArrayList<Double> openPrice = new ArrayList<>();
        JsonObject months = (JsonObject) this.document.get( "Monthly Time Series" );
        JsonObject month = new JsonObject();
        for ( int i = 0; i < this.keySet.length; i++ ) {
            month = (JsonObject) months.get( keySet[i] );
            openPrice.add( Double.parseDouble( month.get("1. open").toString() ) );
        }
        Double[] openSet = new Double[openPrice.size()];
        openSet = openPrice.toArray(openSet);
        return openSet;
    }

    public Double[] grabHighPrices() {
        ArrayList<Double> highPrice = new ArrayList<>();
        JsonObject months = (JsonObject) this.document.get( "Monthly Time Series" );
        JsonObject month = new JsonObject();
        for ( int i = 0; i < this.keySet.length; i++ ) {
            month = (JsonObject) months.get( keySet[i] );
            highPrice.add( Double.parseDouble( month.get("2. high").toString() ) );
        }

        Double[] highSet = new Double[highPrice.size()];
        highSet = highPrice.toArray(highSet);

        return highSet;
    }

    public Double[] grabLowPrices() {
        ArrayList<Double> lowPrice = new ArrayList<>();
        JsonObject months = (JsonObject) this.document.get( "Monthly Time Series" );
        JsonObject month = new JsonObject();
        for ( int i = 0; i < this.keySet.length; i++ ) {
            month = (JsonObject) months.get( keySet[i] );
            lowPrice.add( Double.parseDouble( month.get("3. low").toString() ) );
        }
        Double[] lowSet = new Double[lowPrice.size()];
        lowSet = lowPrice.toArray(lowSet);

        return lowSet;
    }

    public Double[] grabClosePrices() {
        ArrayList<Double> closePrice = new ArrayList<>();
        JsonObject months = (JsonObject) this.document.get( "Monthly Time Series" );
        JsonObject month = new JsonObject();
        for ( int i = 0; i < this.keySet.length; i++ ) {
            month = (JsonObject) months.get( keySet[i] );
            closePrice.add( Double.parseDouble( month.get("4. close").toString() ) );
        }
        Double[] closeSet = new Double[closePrice.size()];
        closeSet = closePrice.toArray(closeSet);

        return closeSet;
    }


}

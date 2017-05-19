package gr.ihu.lab.ihuweather_01;

import android.icu.text.SimpleDateFormat;
import android.provider.Settings;
import android.text.format.Time;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

/**
 * Created by Sak on 19-May-17.
 */

public class WeatherParser {

    private static final String LOG_TAG = WeatherParser.class.getName();


    public static String[] parseWeatherFromJSON(int numDays, String jsonWeather) throws JSONException {
        String[] results = new String[numDays];

        String OWM_List = "list";
        String OWM_Weather = "weather";
        String OWM_Temp = "temp";
        String OWM_Max = "max";
        String OWM_Min = "min";
        String OWM_description = "main";

        //create a JSON object
        JSONObject forecastObj = new JSONObject(jsonWeather); //we add an exception because sth may go wrong with the parsing
        JSONArray weatherArray = forecastObj.getJSONArray(OWM_List);

        Time dayTime = new Time(); //android.text.format.time
        dayTime.setToNow();
        int julianStartDay = Time.getJulianDay(System.currentTimeMillis(), dayTime.gmtoff); //current day
        dayTime =  new Time(); //creates a date object that is in UTC format

        for (int i=0; i<numDays; i++){
            //we are going to use trhe format: Day, description, minTemp/maxTemp
            String day, description, minmax;


            JSONObject dayForecast = weatherArray.getJSONObject(i);
            long dateTime;
            dateTime = dayTime.setJulianDay(julianStartDay + i);
            SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd,  MMM");
            day = sdf.format(dateTime);


            JSONObject o = dayForecast.getJSONArray(OWM_Weather).getJSONObject(0);
            description = o.getString(OWM_description);

            JSONObject tempObj = dayForecast.getJSONObject(OWM_Temp);
            double max = tempObj.getDouble(OWM_Max);
            double min = tempObj.getDouble(OWM_Min);

            minmax = Math.round(min) + " - " + Math.round(max);

            results[i] = day + " | "+ description + " | "+minmax;

        }

        for (int i = 0; i<numDays; i++){
            Log.i("hh", results[i]);
        }


        return results;
    }

}

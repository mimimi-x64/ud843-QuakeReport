
package com.example.android.quakereport;

        import android.annotation.TargetApi;
        import android.os.Build;
        import android.text.TextUtils;
        import android.util.Log;
        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;
        import java.util.ArrayList;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class QueryUtils {

    /** Sample JSON response for a USGS query */
    public static final String USGS_REQUEST_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=6&limit=10";

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Return a list of {@link QuakeList} objects that has been built up from
     * parsing a JSON response.
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static ArrayList<QuakeList> extractEarthquakes(String jsonResponse) {
        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding earthquakes to
        ArrayList<QuakeList> earthquakes = new ArrayList<>();

        // Try to parse the USGS_REQUEST_URL. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            //Create JSON object and attach to root
            JSONObject rootJSON = new JSONObject(jsonResponse);
            JSONArray featureArrayJSON = rootJSON.getJSONArray("features");
            //Do a loop to take some values
            for (int i = 0; i < featureArrayJSON.length(); i++){
                //Start navigating on loop
                JSONObject index = featureArrayJSON.getJSONObject(i);
                JSONObject propertiesJSON = index.getJSONObject("properties");
                //Get values we need
                String place = propertiesJSON.getString("place");
                String url = propertiesJSON.getString("url");
                double mag = propertiesJSON.getInt("mag");
                long time = propertiesJSON.getInt("time");
//                //Initialize date obj
//                Date dateObj = new Date(time);
//                //Mili to date
//                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("DD MMM, yyyy");
//                String dateToDisplay = simpleDateFormat.format(dateObj) ;
//                //Mili to hour
//                SimpleDateFormat simpleHourFormat = new SimpleDateFormat("h:mm a");
//                String hourToDisplay = simpleDateFormat.format(simpleHourFormat);

//                if (place.contains(" of ")) split = place.split("of");
//                String concatOf = split[0].concat("of");

                //Fill the list
                earthquakes.add(new QuakeList(mag, place, time, url));
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return earthquakes;
    }

}
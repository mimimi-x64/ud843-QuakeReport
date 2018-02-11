
package com.example.android.quakereport;

        import android.annotation.TargetApi;
        import android.os.Build;
        import android.text.TextUtils;
        import android.util.Log;
        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;

        import java.io.BufferedReader;
        import java.io.IOException;
        import java.io.InputStream;
        import java.io.InputStreamReader;
        import java.net.HttpURLConnection;
        import java.net.MalformedURLException;
        import java.net.URL;
        import java.nio.charset.Charset;
        import java.util.ArrayList;
        import java.util.List;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class QueryUtils {

    public static final String LOG_TAG = QueryUtils.class.getName();

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

    public static List<QuakeList> fetchEarthquakeData( String requestUrl ) {
        URL url = createUrl(requestUrl);

        try {
            Thread.sleep(1111);
        } catch (InterruptedException e) {
            e.printStackTrace( );
        }

        String jsonResponse = "";
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e){
            Log.e(LOG_TAG, "Error on HttpResquest");
        }
        Log.e(LOG_TAG, "fetchEarthQuakeData executed");
        return QueryUtils.extractEarthquakes(jsonResponse);
    }

    private static URL createUrl( String stringUrl ){
        URL url;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException exception){
            Log.e(LOG_TAG, "Error with creating URL", exception);
            return null;
        }
        return url;
    }

    private static String makeHttpRequest( URL url ) throws IOException {
        String jsonResponse = "";
        if (jsonResponse == null){
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(1000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200){
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code " + urlConnection.getResponseCode());
            }
        } catch (IOException e){
            Log.e(LOG_TAG, "Problem with Internet Connection.");
        } finally {
            if (urlConnection != null){
                urlConnection.disconnect();
            }
            if (inputStream != null){
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream( InputStream inputStream ) throws IOException{
        StringBuilder output = new StringBuilder();
        if (inputStream != null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
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
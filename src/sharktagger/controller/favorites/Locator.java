package sharktagger.controller.favorites;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONArray;
import org.json.JSONObject;

import api.jaws.Location;

public class Locator {
    public static final String GOOGLEAPI_URL_PREFIX = "http://maps.googleapis.com/maps/api/geocode/json?sensor=false&latlng=";

    private String constructUrl(Location location) {
        String latlng = location.getLongitude() + "," + location.getLatitude();
        return GOOGLEAPI_URL_PREFIX + latlng;
    }

    private JSONObject makeJsonRequest(String url) {
        URLConnection connection;
        try {
            connection = new URL(url).openConnection();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        BufferedReader in;
        String json = "";
        try {
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                json += inputLine + "\n";
            }
            json = json.trim();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return new JSONObject(json);
    }

    public boolean isOnLand(Location location) {
        String url = constructUrl(location);
        JSONObject json = makeJsonRequest(url);

        if (json == null) {
            return false;
        }

        JSONArray results = json.getJSONArray("results");
        if (results.length() == 0) {
            return false;
        }

        return false;
    }
}

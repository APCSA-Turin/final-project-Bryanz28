package com.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;


public class Api {
    //filters the json string to return maneuver, message, and combined message
    public static void filterData(String json) {
    try {
        //String to json obj
        JSONObject obj = new JSONObject(json);

        //Digs into the JSON hierarchy
        JSONArray instructions = obj
            .getJSONArray("routes")
            .getJSONObject(0)
            .getJSONObject("guidance")
            .getJSONArray("instructions");//contains the neccecary instructions
            
        //Loops through each instruction
        for (int i = 0; i < instructions.length(); i++) {
            JSONObject step = instructions.getJSONObject(i);
            
            //extracts the neccecary parts from each instruction
            String maneuver = step.optString("maneuver", "N/A");
            String message = step.optString("message", "N/A");
            String combinedMessage = step.optString("combinedMessage", "N/A");
            
            //Prints each step
            System.out.println("Step " + (i + 1) + ":"); //step counter
            System.out.println("  Maneuver: " + maneuver);
            System.out.println("  Message: " + message);
            System.out.println("  Combined Message: " + combinedMessage);
            System.out.println();
        }
        } catch (Exception e) {
            System.out.println("Failed to parse maneuvers: " + e.getMessage());
        }
    }
    // method to get latitude and longitude from an address
    public static double[] getCoordinatesFromAddress(String address) {
        String apiKey = "bKVdiFAnNfojAwK9p01YVIaG0gOqn79F";
            try {

                //build the url for the api
                String encodedAddress = URLEncoder.encode(address, "UTF-8"); //debugged with Copilot
                String endpoint = String.format("https://api.tomtom.com/search/2/geocode/%s.json?key=%s", encodedAddress, apiKey);

                //create a url object and open an http connection
                URL url = new URL(endpoint);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                // read response
                BufferedReader buff = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder content = new StringBuilder();
                String inputLine;

                while ((inputLine = buff.readLine()) != null) {
                    content.append(inputLine);
                }

                buff.close();
                connection.disconnect();

                // Parse JSON to extract coordinates
                JSONObject obj = new JSONObject(content.toString());
                JSONArray results = obj.getJSONArray("results");

                // Check if at least one result was returned
                if (results.length() > 0) {
                    // extract the position from the first result
                    JSONObject position = results.getJSONObject(0).getJSONObject("position");

                    // get latitude and longitude from the position object
                    double lat = position.getDouble("lat");
                    double lon = position.getDouble("lon");

                    // return the coordinates as an array: [latitude, longitude]
                    return new double[] { lat, lon };
                } else {
                    System.out.println("No results found for the address.");
                }
            } catch (Exception e) {
                System.out.println("Failed to get coordinates: " + e.getMessage());
            }
        return new double[] { -1, -1 }; // default/fallback if not found or error
    }


    //returns step by step instructions on how to reach a location (As shown in the Student Workbook)
      public static String getDirections(double startLat, double startLon, double endLat, double endLon) throws Exception {
        String apiKey = "bKVdiFAnNfojAwK9p01YVIaG0gOqn79F";

        // build the url for the api
         String endpoint = String.format("https://api.tomtom.com/routing/1/calculateRoute/%f,%f:%f,%f/json?key=%s&instructionsType=text",startLat, startLon, endLat, endLon, apiKey);

        //create a url object and open an http connection
        URL url = new URL(endpoint);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        // read the response
        BufferedReader buff = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder content = new StringBuilder();
        String inputLine;

        //reads lines from a buffered reader and adds them to content
        while ((inputLine = buff.readLine()) != null) {
            content.append(inputLine);
        }

        //close to avoid resource leak
        buff.close();
        connection.disconnect();

        // Return response
        return content.toString();
    }

    //returns the total distance from the json string
    public static double getTotalDistance(String json) {
            //turns string to json obj
            JSONObject obj = new JSONObject(json);

            // Extracts and returns distance
            return obj
                .getJSONArray("routes")
                .getJSONObject(0)
                .getJSONObject("summary")
                .optDouble("lengthInMeters", -1); // returns -1 if not found
    }
    //reads and returns the contents of the text file
    public static String getHistory() {
        String content = "";
        try (BufferedReader reader = new BufferedReader(new FileReader("JavaAPIProject/src/main/java/com/example/TravelData.txt"))) {
            //the content of each line in the text file
            String line; 
            while ((line = reader.readLine()) != null) {
                content += line + "\n";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }
    //returns the amount of lines in TravelData text file
    public static int getLines() {
        int lineCount = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader("JavaAPIProject/src/main/java/com/example/TravelData.txt"))) {
            while (reader.readLine() != null) { // traverses the text file with a while loop
                lineCount++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lineCount;
    }
    //saves data to text file as shown in Ms. Turin's example
    public static void saveData(String data) {
        //google ai overview showed me how to append instead of overwrite
        try (FileWriter writer = new FileWriter("JavaAPIProject/src/main/java/com/example/TravelData.txt", true)) { 
            writer.write("\n"+data);
        } catch (IOException e) {
            e.printStackTrace();
        }       
    }   
}

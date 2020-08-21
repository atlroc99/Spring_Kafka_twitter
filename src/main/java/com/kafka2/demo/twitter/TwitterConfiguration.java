package com.kafka2.demo.twitter;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.codec.binary.Base64;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Properties;

public class TwitterConfiguration {


    public static void main(String[] args) throws Exception {
        TwitterConfiguration tw = new TwitterConfiguration();
        tw.getBearerToken();
    }


    private String getBearerToken() throws Exception {
        String encodedKeys = getEncodedKeys();
        System.out.println(encodedKeys);
        String bearerToken = null;
        try {
            String authorization_endpoint = "https://api.twitter.com/oauth2/token";
            URL url = new URL(authorization_endpoint);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("host", "api.twitter.com");
            con.setRequestProperty("User-Agent", "my-kafka-twitter");
            con.setRequestProperty("Authorization", "Basic " + encodedKeys);
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
            con.setRequestProperty("Content-Length", "29");
            con.setUseCaches(false);

            writeRequest(con, "grant_type=client_credentials");
            String response = readResponse(con);
            JsonElement el = JsonParser.parseString(response);
            System.out.println(el);
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
        
        return bearerToken;
    }


    private String getEncodedKeys() throws IOException {
        final String apiKey = "twitter.api.key";
        final String secretKey = "twitter.secret.key";

        Properties properties = new Properties();
        properties.load(new FileReader(getClass().getClassLoader().getResource("application.properties").getFile()));
        final String API_KEY = URLEncoder.encode(properties.getProperty(apiKey), "UTF-8");
        final String SECRET_KEY = URLEncoder.encode(properties.getProperty(secretKey), "UTF-8");
        String fullKey = API_KEY + ":" + SECRET_KEY;
        byte[] encodedKeys = Base64.encodeBase64(fullKey.getBytes());
        return new String(encodedKeys);
    }

    private void writeRequest(HttpURLConnection connection, String body) throws Exception {
        OutputStream out;
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
        bw.write(body);
        bw.flush();
        bw.close();
    }

    private String readResponse(HttpURLConnection connection) throws Exception {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            InputStream in;
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = "";
            while ((line = br.readLine()) != null) {
                stringBuilder.append(line);
            }
            return stringBuilder.toString();
        } catch (Exception e) {
            throw e;
        }
    }
}

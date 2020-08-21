package com.kafka2.demo.twitter;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.apache.commons.httpclient.HttpHost;
import org.apache.commons.httpclient.URI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.io.FileReader;
import java.io.Reader;
import java.util.Base64;
import java.util.Properties;

public class AuthencationWithSpring {
    private Logger logger = LoggerFactory.getLogger(AuthencationWithSpring.class);

    public static void main(String[] args) throws Exception {
        AuthencationWithSpring app = new AuthencationWithSpring();
        String bearer_token = app.getBearerToken();
    }


    private String getBearerToken() throws Exception {
        ResponseEntity<String> response = new RestTemplate()
                .exchange("https://api.twitter.com/oauth2/token?grant_type=client_credentials",
                        HttpMethod.POST,
                        new HttpEntity<>(createHeaders()),
                        String.class);

        if (response.getStatusCode().is4xxClientError() || !response.getStatusCode().is2xxSuccessful()) {
            logger.error(response.getStatusCode().value() + " " + response.getStatusCode().getReasonPhrase());
        }
        return JsonParser.parseString(response.getBody()).getAsJsonObject().get("access_token").getAsString();
    }

    private HttpHeaders createHeaders() throws Exception {
        Properties props = new Properties();
        props.load(new FileReader(getClass().getClassLoader().getResource("application.properties").getFile()));
        String API_KEY = props.getProperty("twitter.api.key");
        String SECRET_KEY = props.getProperty("twitter.secret.key");
        String stringByte = API_KEY + ":" + SECRET_KEY;
        byte[] encoded = Base64.getEncoder().encodeToString(stringByte.getBytes()).getBytes();

        return new HttpHeaders() {{
            String basicAuth = "Basic " + new String(encoded);
            set("Authorization", basicAuth);
            set("User-Agent", "my-kafka-twitter");
            setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            setBasicAuth(new String(encoded));
            set("grant_type", "client_credentials");
            setContentLength(29L);
            setCacheControl("no-cache, no-store, max-age=0, must-revalidate");
            setAccessControlRequestMethod(HttpMethod.POST);
        }};
    }
}

package com.assessify.demonotificationwithonesignal.option;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PushNotificationOptions {
    private static final Logger log = LoggerFactory.getLogger(PushNotificationOptions.class);

    public static final String REST_API_KEY = "os_v2_app_bcigqvfucvaibk2agm3ai7ssykbjhmv7nnyuvmv62pfr4eisdo3tdh5yh3ha6kvq6enmn2boe3jwwunte4az7lp2emwanxu65stz3fa";
    public static final String APP_ID = "08906854-b415-4080-ab40-3336047e52c2";
    private static final RestTemplate restTemplate = new RestTemplate();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void sendMessageToAllUsers(String message) throws JsonProcessingException {
        String url = "https://onesignal.com/api/v1/notifications";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json; charset=UTF-8");
        headers.set("Authorization", "Basic " + REST_API_KEY);

        String strJsonBody = buildJsonBodyForAllUsers(message);
        HttpEntity<String> request = new HttpEntity<>(strJsonBody, headers);
        sendAndLog(url, request);
    }

    public static void sendMessageToUser(String message, String userId) throws JsonProcessingException {
        String url = "https://onesignal.com/api/v1/notifications";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json; charset=UTF-8");
        headers.set("Authorization", "Basic " + REST_API_KEY);

        String strJsonBody = buildJsonBodyForSingleUser(message, userId);
        HttpEntity<String> request = new HttpEntity<>(strJsonBody, headers);
        sendAndLog(url, request);
    }

    private static void sendAndLog(@NonNull String url, @NonNull HttpEntity<String> request) {
        try {
            ResponseEntity<String> response = Objects.requireNonNull(
                    restTemplate.postForEntity(url, request, String.class)
            );
            String responseBody = response.getBody() == null ? "<empty>" : response.getBody();
            log.info("OneSignal response status: {}", response.getStatusCode().value());
            log.info("OneSignal response body: {}", responseBody);
        } catch (HttpStatusCodeException e) {
            log.error("OneSignal request failed with status: {}", e.getStatusCode().value());
            log.error("OneSignal error body: {}", e.getResponseBodyAsString());
            throw e;
        }
    }

    private static String buildJsonBodyForAllUsers(String message) throws JsonProcessingException {
        Map<String, Object> body = new HashMap<>();
        body.put("app_id", APP_ID);
        body.put("included_segments", new String[]{"All"});
        Map<String, String> data = new HashMap<>();
        data.put("foo", "bar");
        body.put("data", data);
        Map<String, String> contents = new HashMap<>();
        contents.put("en", message);
        body.put("contents", contents);

        return objectMapper.writeValueAsString(body);
    }

    private static String buildJsonBodyForSingleUser(String message, String userId) throws JsonProcessingException {
        Map<String, Object> body = new HashMap<>();
        body.put("app_id", APP_ID);
        body.put("target_channel", "push");
        Map<String, String[]> includeAliases = new HashMap<>();
        includeAliases.put("external_id", new String[]{userId});
        body.put("include_aliases", includeAliases);
        Map<String, String> data = new HashMap<>();
        data.put("foo", "bar");
        body.put("data", data);
        Map<String, String> contents = new HashMap<>();
        contents.put("en", message);
        body.put("contents", contents);

        return objectMapper.writeValueAsString(body);
    }
}

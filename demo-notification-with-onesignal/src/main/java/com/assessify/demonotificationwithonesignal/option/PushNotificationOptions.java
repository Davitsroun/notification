package com.assessify.demonotificationwithonesignal.option;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

public class PushNotificationOptions {

    public static final String REST_API_KEY = "os_v2_app_bcigqvfucvaibk2agm3ai7ssyk4butfak35ejem4nr7lc7s56zwqttd4mj3fkvmu7umyyaflf3d43lwh664f6rydt2tg5phm63ntopa";
    public static final String APP_ID = "08906854-b415-4080-ab40-3336047e52c2";
    public static final String DEFAULT_USER_ID = "39095bb2-a078-4264-b95b-3511d00f641f";
    private static final RestTemplate restTemplate = new RestTemplate();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void sendMessageToAllUsers(String message) throws JsonProcessingException {
        String url = "https://onesignal.com/api/v1/notifications";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json; charset=UTF-8");
        headers.set("Authorization", "Basic " + REST_API_KEY);

        String strJsonBody = buildJsonBodyForAllUsers(message);
        HttpEntity<String> request = new HttpEntity<>(strJsonBody, headers);
        restTemplate.postForEntity(url, request, String.class);
    }

    public static void sendMessageToUser(String message, String userId) throws JsonProcessingException {
        String url = "https://onesignal.com/api/v1/notifications";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json; charset=UTF-8");
        headers.set("Authorization", "Basic " + REST_API_KEY);

        String targetUserId = (userId == null || userId.isBlank()) ? DEFAULT_USER_ID : userId;
        String strJsonBody = buildJsonBodyForSingleUser(message, targetUserId);
        HttpEntity<String> request = new HttpEntity<>(strJsonBody, headers);
        restTemplate.postForEntity(url, request, String.class);
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

package com.tinktask.translatetask.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
public class TranslateService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Value("${libretranslate.api.url}")
    private String apiUrl;

    @Value("${libretranslate.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

    public String translateText(String text, String from, String to, String userIp) {
        String[] words = text.split(" ");
        Future<String>[] futures = new Future[words.length];

        for (int i = 0; i < words.length; i++) {
            final String word = words[i];
            futures[i] = executorService.submit(() -> translateWord(word, from, to));
        }

        StringBuilder translatedText = new StringBuilder();
        for (Future<String> future : futures) {
            try {
                translatedText.append(future.get()).append(" ");
            } catch (Exception e) {
                throw new RuntimeException("Translation error", e);
            }
        }

        saveTranslationData(text, translatedText.toString().trim(), userIp);

        StringBuilder result = new StringBuilder();
        result.append(from).append(" -> ").append(to).append("\n");
        result.append(translatedText.toString().trim());

        return result.toString();
    }

    private String translateWord(String word, String from, String to) {
        String url = apiUrl;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        Map<String, Object> requestBody = Map.of(
                "q", word,
                "source", from,
                "target", to,
                "format", "text",
                "alternatives", 3,
                "api_key", apiKey
        );

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);

        if (response.getBody() != null && response.getBody().containsKey("translatedText")) {
            return (String) response.getBody().get("translatedText");
        }

        throw new RuntimeException("Translation API error");
    }

    private void saveTranslationData(String originalText, String translatedText, String userIp) {
        String sql = "INSERT INTO translations (original_text, translated_text, user_ip) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, originalText, translatedText, userIp);
    }
}
package com.code_doctor.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class AIAnalysisService {

    @Value("${openai.api.key:}")
    private String openAIApiKey;

    public String analyzeCode(String code) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.openai.com/v1/completions";

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "text-davinci-003");
        requestBody.put("prompt", "Analyze the following code:\n\n" + code);
        requestBody.put("max_tokens", 500);

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + openAIApiKey);

        return restTemplate.postForObject(url, requestBody, String.class);
    }
}

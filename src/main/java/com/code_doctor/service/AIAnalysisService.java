package com.code_doctor.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.code_doctor.model.OllamaResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AIAnalysisService {

    @Value("${ollama.api.url:}")
    private String ollamaApiUrl;

    public String analyzeCode(String code) {
        RestTemplate restTemplate = new RestTemplate();
        String model = "codellama"; // Change to the specific model you're running locally

        // Build the request body
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", model);
        requestBody.put("prompt", "Analyze the following code and explain it:\n\n" + code);

        // Create headers
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        // Make the POST request
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> response = restTemplate.exchange(
            ollamaApiUrl + "/api/completions",
            HttpMethod.POST,
            entity,
            String.class
        );

        return response.getBody();
    }

    /**
     * 
     * {
  "model": "{{model_llama}}", //insert any models from Ollama that are on your local machine
  "messages": [
    {
      "role": "system", //"system" is a prompt to define how the model should act.
      "content": "you are a salty pirate" //system prompt should be written here
    },
    {
      "role": "user", //"user" is a prompt provided by the user.
      "content": "why is the sky blue" //user prompt should be written here
    }
  ],
  "stream": false //returns as a full message rather than a streamed response
}
     * 
     * 
     */

    public String analyzeCodeWithOllama(String code) {
        RestTemplate restTemplate = new RestTemplate();
        String model = "codellama"; // Change to the specific model you're running locally
        String ollamaApiUrl = "http://localhost:11434"; // Base URL for Ollama API
    
        // Build the request body
        Map<String, Object> requestBody = createRequestBody(model, "Extremely intelligent Senior Software Engineer", code);
    
        // Create headers
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
    
        // Make the POST request
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> response = restTemplate.exchange(
            ollamaApiUrl + "/api/chat",
            HttpMethod.POST,
            entity,
            String.class
        );
        return mapJsonToOllamaResponse(response.getBody()).getMessage().getContent();
    }



    public Map<String, Object> createRequestBody(String model, String systemPrompt, String userPrompt) {
    Map<String, Object> requestBody = new HashMap<>();
    requestBody.put("model", model);

    List<Map<String, String>> messages = new ArrayList<>();
    
    Map<String, String> systemMessage = new HashMap<>();
    systemMessage.put("role", "system");
    systemMessage.put("content", systemPrompt);
    messages.add(systemMessage);
    
    Map<String, String> userMessage = new HashMap<>();
    userMessage.put("role", "user");
    userMessage.put("content", userPrompt);
    messages.add(userMessage);
    
    requestBody.put("messages", messages);
    requestBody.put("stream", false);

    return requestBody;
}



public OllamaResponse mapJsonToOllamaResponse(String jsonResponse) {
    try {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(jsonResponse, OllamaResponse.class);
    } catch (Exception e) {
        e.printStackTrace();
        return null;
    }
}



}
package com.ssafy.client.user.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ApiService {

    @Autowired
    private RestTemplate restTemplate;

    public String callProtectedApi(String apiUrl) {
        return restTemplate.getForObject(apiUrl, String.class);
    }
}

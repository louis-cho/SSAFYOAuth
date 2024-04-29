package com.ssafy.client.user.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RestClientService {

    @Autowired
    private RestTemplate restTemplate;

    public String callTestEndpoint(String jwtToken) {
        // Http 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);

        // HttpEntity 생성
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // RestTemplate을 사용하여 GET 요청
        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:8080/user/test", HttpMethod.GET, entity, String.class);

        return response.getBody();
    }
}
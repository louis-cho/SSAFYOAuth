package com.ssafy.client.user.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.nimbusds.jose.util.Resource;
import com.ssafy.client.user.dto.ProfileInformationForUpdatesDto;

@Service
public class ApiService {

    @Autowired
    private RestTemplate restTemplate;

    public String callProtectedApi(String apiUrl) {
        return restTemplate.getForObject(apiUrl, String.class);
    }
    public String callPostApi(String apiUrl,Object data) {
        return restTemplate.postForObject(apiUrl,data ,String.class);
    }

    public String callPostFileApi(String apiUrl, ProfileInformationForUpdatesDto dto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        // Convert DTO properties to body map
        body.add("name", dto.getName());
        body.add("email", dto.getEmail());
        body.add("gender", dto.getGender());
        body.add("phoneNumber", dto.getPhoneNumber());
        body.add("studentId", dto.getStudentId());

        MultipartFile file = dto.getImage();
        if (file != null && !file.isEmpty()) {
            try {
                ByteArrayResource resource = new ByteArrayResource(file.getBytes()) {
                    @Override
                    public String getFilename() {
                        return file.getOriginalFilename(); // Needed to correctly handle the file part of the request
                    }
                };
                body.add("image", resource);
            } catch (Exception e) {
                throw new RuntimeException("Failed to convert file data", e);
            }
        }

        // Create an HttpEntity using the constructed body and headers
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // Make the HTTP POST request
        return restTemplate.postForObject(apiUrl, requestEntity, String.class);
    }
}

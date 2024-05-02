package com.ssafy.authorization.config.filter.countrylimit;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class GeoIpCountryResolver implements CountryResolver {

	private final RestTemplate restTemplate;

	@Override
	public String resolveCountry(String ip) {
		String url = String.format("https://ipapi.co/%s/json/",ip);
		try {
			ResponseEntity<IpApiResponse> response = restTemplate.getForEntity(url, IpApiResponse.class);

			if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
				if(response.getBody().isError()){
					return "Unknown";
				}
				return response.getBody().getCountry();
			}
		} catch (Exception e) {
			log.debug("API 호출 중 오류 발생: {}", e.getMessage());
		}
		return "Unknown"; // 실패 시 기본 값 반환
	}

}
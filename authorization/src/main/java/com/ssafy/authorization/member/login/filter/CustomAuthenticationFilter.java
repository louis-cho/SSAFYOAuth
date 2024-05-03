package com.ssafy.authorization.member.login.filter;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

public class CustomAuthenticationFilter extends OncePerRequestFilter {
    private final RedisTemplate<String, String> redisTemplate;


    @Autowired
    public CustomAuthenticationFilter(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private String getRequestKey(HttpServletRequest request) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map<String, Object> jsonMap = objectMapper.readValue(request.getInputStream(), Map.class);

            // JSON 형태의 데이터에서 필요한 값 추출
            String username = (String) jsonMap.get("username");
            String teamid = (String) jsonMap.get("teamid");

            return username + "_" + teamid;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 요청 URL을 가져옴
        String requestUri = request.getRequestURI();
        System.out.printf("requestUri >> " + requestUri);
// 로그인을 위한 요청이라면 내부 필터 실행
        if ("/login".equals(requestUri)) {
            try {
                String key = getRequestKey(request); // 로그인 객체로부터 사용자 이메일을 가져오는 사용자 정의 메서드
                String loginTimestamp = request.getHeader("Login-Timestamp"); // 로그인 요청 시각을 헤더에서 가져옴

                if (key != null && loginTimestamp != null) {
                    String storedTimestamp = (String) redisTemplate.opsForValue().get(key);
                    if (storedTimestamp != null) {
                        // 로그인 요청 시각과 저장된 시각이 일치하는 경우에만 통과
                        filterChain.doFilter(request, response);
                        return;
                    }
                }

                // 인증 실패 시, 적절한 응답 처리를 수행하고 필터 체인 종료
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            } catch (Exception e) {
                // 예외가 발생한 경우, 적절한 처리를 수행하고 에러 응답을 보냄
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                e.printStackTrace(); // 예외 정보를 로그에 출력
            }
        } else {
            // 로그인 요청이 아니라면 필터를 건너뜀
            filterChain.doFilter(request, response);
        }
    }
}

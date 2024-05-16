package com.ssafy.authorization.member.login.controller;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import co.elastic.clients.elasticsearch.nodes.Http;
import com.ssafy.authorization.member.login.filter.LoginRateLimiter;
import jakarta.servlet.http.HttpServletRequest;
import org.joda.time.DateTime;
import org.joda.time.Seconds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.ssafy.authorization.member.login.model.LoginRequest;
import com.ssafy.authorization.member.login.service.LoginService;
import com.ssafy.authorization.stats.login.service.LoginStatsService;

@RestController("LoginController")
@RequestMapping("/api/auth")
public class LoginController {

    private final LoginService loginService;
    private final Map<String, SseEmitter> userEmitters;

    private final Map<String, LoginRequest> loginRequestMap = new ConcurrentHashMap<>();

    private RedisTemplate<String, String> redisTemplate;
    private final LoginStatsService loginStatsService;

    private static final Long LIMIT_PER_PERIOD = 2L;


    @Autowired
    LoginController(LoginService loginService, RedisTemplate<String, String> redisTemplate
    ,LoginStatsService loginStatsService) {
        this.loginService = loginService;
        this.userEmitters = new ConcurrentHashMap<>();
        this.redisTemplate = redisTemplate;
        this.loginStatsService = loginStatsService;
    }

    private void updateLoginRate(LoginRequest request) {

        if(request.getIp() == null) {
            return;
        }

        final String loginRateKey = LoginRateLimiter.getCountIndex(request);
        final String loginIndexKey = LoginRateLimiter.getIndex(request);

        if(Boolean.TRUE.equals(redisTemplate.hasKey(loginRateKey))) {
            Long loginRate = redisTemplate.opsForValue().increment(loginRateKey);
            if(loginRate != null && loginRate.compareTo(LIMIT_PER_PERIOD) > 0) {
                redisTemplate.opsForValue().set(loginIndexKey, LocalDateTime.now().toString());
            }
        } else {
            redisTemplate.opsForValue().set(loginRateKey, "1");
            redisTemplate.expire(loginIndexKey, 10, TimeUnit.SECONDS);
            redisTemplate.expire(loginRateKey, 10, TimeUnit.SECONDS);
        }
    }

    private boolean validLoginRate(LoginRequest request) {
        final String loginIndexKey = LoginRateLimiter.getIndex(request);
        if(Boolean.TRUE.equals(redisTemplate.hasKey(loginIndexKey))) {
            try {
                if (Duration.between(LocalDateTime.now(), LocalDateTime.parse(Objects.requireNonNull(redisTemplate.opsForValue().get(loginIndexKey)))).getSeconds() > LIMIT_PER_PERIOD) {
                    return true;
                }
            } catch(Exception e) {
                return false;
            }
            return false;
        }
        return true;
    }



    @PostMapping("/waitRequest")
    public ResponseEntity<String> createLoginRequest(@RequestBody LoginRequest request, HttpServletRequest httpServletRequest) {
        String key = UUID.randomUUID().toString();
        request.setIp(httpServletRequest.getRemoteAddr());
        loginRequestMap.put(key, request);
        return ResponseEntity.ok(key);
    }

    @GetMapping("/sse/{key}")
    public SseEmitter connectSse(@PathVariable String key) {
        SseEmitter emitter = new SseEmitter(5 * 60 * 1000L);

        LoginRequest request = loginRequestMap.get(key);
        if(request == null) {
            return null;
        }


        final String emitterKey = request.toString();

        updateLoginRate(request);

        if(userEmitters.isEmpty() == false && userEmitters.get(emitterKey) != null) {
            userEmitters.remove(emitterKey);
        }

        userEmitters.put(request.toString(), emitter);

        try {
            emitter.send(SseEmitter.event().name("STATUS").data("Request received"));
            boolean enqueued = loginService.getLoginQueueManager().enqueue(request);
            if (enqueued) {
                emitter.send(SseEmitter.event().name("STATUS").data("Request enqueued"));
            } else {
                emitter.send(SseEmitter.event().name("STATUS").data("Queue is full"));
                emitter.completeWithError(new RuntimeException("Queue Exceed"));
            }
        } catch (IOException e) {
            emitter.completeWithError(e);
        }

        emitter.onCompletion(() -> userEmitters.remove(request.toString()));
        emitter.onTimeout(() -> userEmitters.remove(request.toString()));

        return emitter;
    }

//    @PostMapping("/waitSignal")
//    public SseEmitter waitSignal(@RequestBody LoginRequest request, HttpServletRequest servletRequest) {
//
//        final SseEmitter emitter = new SseEmitter(5 * 60 * 1000L);
//        final String key = request.toString();
//        request.setIp(servletRequest.getRemoteAddr());
//
//        updateLoginRate(request);
//
//        if(userEmitters.isEmpty() == false && userEmitters.get(key) != null) {
//            userEmitters.remove(key);
//        }
//
//        userEmitters.put(request.toString(), emitter);
//
//        try {
//            emitter.send(SseEmitter.event().name("STATUS").data("Request received"));
//            boolean enqueued = loginService.getLoginQueueManager().enqueue(request);
//             if (enqueued) {
//                emitter.send(SseEmitter.event().name("STATUS").data("Request enqueued"));
//            } else {
//                emitter.send(SseEmitter.event().name("STATUS").data("Queue is full"));
//                emitter.completeWithError(new RuntimeException("Queue Exceed"));
//            }
//        } catch (IOException e) {
//            emitter.completeWithError(e);
//        }
//
//        emitter.onCompletion(() -> userEmitters.remove(request.toString()));
//        emitter.onTimeout(() -> userEmitters.remove(request.toString()));
//
//        return emitter;
//    }


//    @PostMapping("/login")
//    public SseEmitter addLoginRequest(@RequestBody LoginRequest request) {
//        final SseEmitter emitter = new SseEmitter(Long.MAX_VALUE); // 긴 연결 시간 설정
//        userEmitters.put(request.getUserId(), emitter);
//
//        try {
//            emitter.send(SseEmitter.event().name("STATUS").data("Request received"));
//            boolean enqueued = loginService.getLoginQueueManager().enqueue(request);
//            if (enqueued) {
//                emitter.send(SseEmitter.event().name("STATUS").data("Request enqueued"));
//            } else {
//                emitter.send(SseEmitter.event().name("STATUS").data("Queue is full"));
//                emitter.complete();
//            }
//        } catch (IOException e) {
//            emitter.completeWithError(e);
//        }
//
//        emitter.onCompletion(() -> userEmitters.remove(request.getUserId()));
//        emitter.onTimeout(() -> userEmitters.remove(request.getUserId()));
//
//        return emitter;
//    }

    public void notifyLoginResult(LoginRequest request) {
        final String key = request.toString();
        redisTemplate.opsForValue().set(request.toString(), LocalDateTime.now().toString());
        redisTemplate.expire(key, 300, TimeUnit.SECONDS);



        SseEmitter emitter = userEmitters.get(key);
        if (emitter != null) {
            try {
                if(!validLoginRate(request)) {
                    emitter.completeWithError(new RuntimeException("되겠냐?"));
                } else {
                    emitter.send(SseEmitter.event().name("WAIT_RESULT").data("Wait successful"));
                    emitter.complete();
                }
                userEmitters.remove(key);
            } catch (IOException e) {
                emitter.completeWithError(e);
                userEmitters.remove(key);
            }
        }
    }

    @Scheduled(fixedRate = 5000)
    public void sendQueueUpdates() {
        AtomicInteger queueSize = loginService.getLoginQueueManager().getQueueSize();  // 대기열 크기를 가져옵니다

        if(userEmitters.isEmpty())
            return;

        userEmitters.forEach((userId, emitter) -> {
            try {
                emitter.send(SseEmitter.event().name("queue-update").data(queueSize));
            } catch (IOException e) {
                emitter.completeWithError(e);
                userEmitters.remove(userId);
            }
        });
    }
}

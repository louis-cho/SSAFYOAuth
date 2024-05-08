package com.ssafy.authorization.member.login.controller;

import org.joda.time.DateTime;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;


import com.ssafy.authorization.member.login.model.LoginRequest;
import com.ssafy.authorization.member.login.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@RestController("LoginController")
@RequestMapping("/api/auth")
public class LoginController {

    private final LoginService loginService;
    private final Map<String, SseEmitter> userEmitters;


    private RedisTemplate<String, String> redisTemplate;


    @Autowired
    LoginController(LoginService loginService, RedisTemplate<String, String> redisTemplate) {
        this.loginService = loginService;
        this.userEmitters = new ConcurrentHashMap<>();
        this.redisTemplate = redisTemplate;
    }

    @PostMapping("/waitSignal")
    public SseEmitter waitSignal(@RequestBody LoginRequest request) {

        final SseEmitter emitter = new SseEmitter(5 * 60 * 1000L);
        userEmitters.put(request.toString(), emitter);

        try {
            emitter.send(SseEmitter.event().name("STATUS").data("Request received"));
            boolean enqueued = loginService.getLoginQueueManager().enqueue(request);
            if (enqueued) {
                emitter.send(SseEmitter.event().name("STATUS").data("Request enqueued"));
            } else {
                emitter.send(SseEmitter.event().name("STATUS").data("Queue is full"));
                emitter.complete();
            }
        } catch (IOException e) {
            emitter.completeWithError(e);
        }

        emitter.onCompletion(() -> userEmitters.remove(request.toString()));
        emitter.onTimeout(() -> userEmitters.remove(request.toString()));

        return emitter;
    }
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
        String key = request.toString();
        redisTemplate.opsForValue().set(request.toString(), LocalDateTime.now().toString());
        redisTemplate.expire(key, 300, TimeUnit.SECONDS);

        SseEmitter emitter = userEmitters.get(request.toString());
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().name("WAIT_RESULT").data("Wait successful"));
                emitter.complete();
            } catch (IOException e) {
                emitter.completeWithError(e);
            }
        }
    }

    @Scheduled(fixedRate = 5000)
    public void sendQueueUpdates() {
        AtomicInteger queueSize = loginService.getLoginQueueManager().getQueueSize();  // 대기열 크기를 가져옵니다

        userEmitters.forEach((userId, emitter) -> {
            try {
                emitter.send(SseEmitter.event().name("queue-update").data(queueSize));
            } catch (IOException e) {
                emitter.completeWithError(e);
            }
        });
    }
}

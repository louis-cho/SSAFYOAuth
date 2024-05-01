package com.ssafy.resourceserver.member.service;

import com.ssafy.resourceserver.member.model.domain.Member;
import com.ssafy.resourceserver.member.model.handler.ScopeHandler;
import com.ssafy.resourceserver.member.model.handler.ScopeMethod;
import com.ssafy.resourceserver.member.model.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final ScopeMethod scopeMethod;
    @Override
    public Map<String, Object> getUserProfile(String email, List<String> scopes) {
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> {
            throw new RuntimeException("회원정보없음");
        });
        Map<String, Object> profileData = new HashMap<>();
        log.info("유저 서비스에서 가져온 유저정보 {}", member);
        Method[] methods = scopeMethod.getClass().getMethods();

        for (String scope : scopes) {
            log.info("scope 정보 : {}",scope);

            for (Method method : methods) {
                if (method.isAnnotationPresent(ScopeHandler.class)) {
                    ScopeHandler scopeHandler = method.getAnnotation(ScopeHandler.class);
                    System.out.println(scopeHandler.value());
                    if (scopeHandler.value().equals(scope)) {
                        try {
                            method.invoke(scopeMethod, member, profileData);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return profileData;
    }
}

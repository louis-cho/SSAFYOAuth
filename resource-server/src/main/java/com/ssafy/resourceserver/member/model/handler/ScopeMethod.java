package com.ssafy.resourceserver.member.model.handler;

import com.ssafy.resourceserver.member.model.domain.Member;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ScopeMethod {
    @ScopeHandler("profile")
    public void handleProfile(Member member, Map<String, Object> data) {
        data.putIfAbsent("email", member.getEmail());
        data.putIfAbsent("name", member.getName());
        data.putIfAbsent("image", member.getImage());
    }

    @ScopeHandler("email")
    public void handleEmail(Member member, Map<String, Object> data) {
        data.putIfAbsent("email", member.getEmail());
    }

    @ScopeHandler("name")
    public void handleName(Member member, Map<String, Object> data) {
        data.putIfAbsent("name", member.getName());
    }

    @ScopeHandler("image")
    public void handleImage(Member member, Map<String, Object> data) {
        data.putIfAbsent("image", member.getImage());
    }

    @ScopeHandler("gender")
    public void handleGender(Member member, Map<String, Object> data) {
        data.putIfAbsent("gender", member.getGender());
    }
    @ScopeHandler("phoneNumber")
    public void handlePhoneNumber(Member member, Map<String, Object> data) {
        data.putIfAbsent("phoneNumber", member.getPhoneNumber());
    }
    @ScopeHandler("studentId")
    public void handleStudentId(Member member, Map<String, Object> data) {
        data.putIfAbsent("studentId", member.getStudentId());
    }

}

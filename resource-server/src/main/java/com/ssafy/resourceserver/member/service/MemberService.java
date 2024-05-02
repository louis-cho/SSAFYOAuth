package com.ssafy.resourceserver.member.service;

import java.util.List;
import java.util.Map;

public interface MemberService {

    Map<String, Object> getUserProfile(String email, List<String> scopes);
}

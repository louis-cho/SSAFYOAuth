package com.ssafy.authorization.member.model.service;

import java.util.HashMap;
import java.util.Map;

import com.ssafy.authorization.common.utils.S3Uploader;
import com.ssafy.authorization.member.model.domain.Member;
import com.ssafy.authorization.member.model.dto.FindUserEmailDto;
import com.ssafy.authorization.member.model.dto.SignUpRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final CustomMemberManager customMemberManager;
    private final S3Uploader s3Uploader;
    private RedisTemplate<String, String> redisTemplate;

    @SneakyThrows
    @Transactional
    public Map<String, Boolean> save(Member member, SignUpRequestDto dto) {
        String s;
        Map<String, Boolean> response = new HashMap<>();
        try {
            if (!dto.getProfileImage().isEmpty()) {
                s = s3Uploader.uploadFile(dto.getProfileImage());
                member.changeProfile(s);
            }
            String isCertified = redisTemplate.opsForValue().get(member.getEmail());
            if (isCertified!=null && "true".equals(isCertified)) {
                customMemberManager.createUser(member);
                response.put("result", true);
                return response;
            } else {
                response.put("result", false);
                return response;
            }

        } catch (Exception e) {
            response.put("result", false);
            return response;
        }
    }

    public String findMemberEmail(FindUserEmailDto dto) {
        return customMemberManager.findMemberUserEmail(dto);
    }

}

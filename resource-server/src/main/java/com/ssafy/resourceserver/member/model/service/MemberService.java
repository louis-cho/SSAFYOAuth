package com.ssafy.resourceserver.member.model.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.resourceserver.common.utils.S3Uploader;
import com.ssafy.resourceserver.member.model.domain.Member;
import com.ssafy.resourceserver.member.model.dto.SignUpRequestDto;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final CustomMemberManager customMemberManager;
    private final S3Uploader s3Uploader;

    @SneakyThrows
    @Transactional
    public void save(Member member, SignUpRequestDto dto) {
        String s;
        if (!dto.getProfileImage().isEmpty())
        {
            s = s3Uploader.uploadFile(dto.getProfileImage());
            member.changeProfile(s);
        }
        customMemberManager.createUser(member);
    }

}

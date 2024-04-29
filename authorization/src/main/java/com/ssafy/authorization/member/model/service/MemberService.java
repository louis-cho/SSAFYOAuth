package com.ssafy.authorization.member.model.service;

import com.ssafy.authorization.common.utils.S3Uploader;
import com.ssafy.authorization.member.model.domain.Member;
import com.ssafy.authorization.member.model.dto.ResetPasswordDto;
import com.ssafy.authorization.member.model.dto.SignUpRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

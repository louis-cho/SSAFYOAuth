package com.ssafy.resourceserver.member.service;

import com.ssafy.resourceserver.common.utils.S3Uploader;
import com.ssafy.resourceserver.member.model.domain.Member;
import com.ssafy.resourceserver.member.model.dto.ProfileInformationForUpdatesDto;
import com.ssafy.resourceserver.member.model.handler.ScopeHandler;
import com.ssafy.resourceserver.member.model.handler.ScopeMethod;
import com.ssafy.resourceserver.member.model.repository.MemberRepository;
import com.ssafy.resourceserver.member.model.repository.ClientMemberRepository;
import com.ssafy.resourceserver.team.entity.DeveloperEntity;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final ClientMemberRepository teamMemberRepository;
    private final ScopeMethod scopeMethod;
    private final S3Uploader s3Uploader;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Override
    public void signUp(Integer seq){
        DeveloperEntity developerEntity = new DeveloperEntity(seq, false);
        teamMemberRepository.save(developerEntity);
    }
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

    @Override
    @Transactional
    public void updateUserProfile(String jwtEmail,ProfileInformationForUpdatesDto userinfo) throws IOException {
        Member member = memberRepository.findByEmail(jwtEmail).orElseThrow(() -> {
            throw new RuntimeException("회원정보없음");
        });
        System.out.println(member);

        String studentId;
        MultipartFile image;
        String phoneNumber;
        boolean gender;
        String name;
        String email;
        try {
            studentId = userinfo.getStudentId();
            image = userinfo.getImage();
            phoneNumber = userinfo.getPhoneNumber();
            gender = userinfo.getGender();
            name = userinfo.getName();
            email = userinfo.getEmail();
        } catch(Exception e) {
            log.info("요청 잘못됨");
            throw new RuntimeException("요청 잘못됨");
        }

        //유효성 검사
        //바뀌면 안되는 값이 바뀌는지
        if (!member.getStudentId().equals(studentId)) {
            log.info("StudentId 바뀌면 안되는데?");
            throw new RuntimeException("StudentId 바뀌면 안되는데?");
        }
        
        if (!member.getGender().equals(gender)) {
            log.info("성별 바뀌면 안되는데?");
            throw new RuntimeException("성별 바뀌면 안되는데?");
        }
        
        if (!member.getEmail().equals(email)) {
            log.info("이메일 바뀌면 안되는데?");
            throw new RuntimeException("이메일 바뀌면 안되는데?");
        }

        if (name == null) {
            log.info("name 없으면 안되는데?");
            throw new RuntimeException("name 없으면 안되는데?");
        }

        if (phoneNumber == null) {
            log.info("전화번호 없으면 안되는데?");
            throw new RuntimeException("전화번호 없으면 안되는데?");
        }

        String imageUrl = null;
        if (image == null) {
            imageUrl = "https://dagak.s3.ap-northeast-2.amazonaws.com/profile/youngjoo.png";
        }
        else{
            imageUrl = s3Uploader.uploadFile((MultipartFile)image);
        }

        member.changeName(name);
        member.changePhoneNumber(phoneNumber);
        member.changeProfile(imageUrl);
        log.info("update completed.");
    }

    @Override
    @Transactional
    public void updateSecurityGrade(String email, Integer grade) {
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> {
            throw new RuntimeException("회원정보없음");
        });

        member.changeGrade(grade);
    }

    @Override
    @Transactional
    public void updatePassword(String email, Map<String, String> passwords) {
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> {
            throw new RuntimeException("회원정보없음");
        });

        String password = passwords.get("password");
        String newPassword = passwords.get("newPassword");

        String origin = member.getPassword();

        if (!passwordEncoder.matches(password, origin)) {
            throw new RuntimeException("비밀번호 틀림");
        }

        member.changePassword(passwordEncoder.encode(newPassword));
    }

    @Override
    public ProfileInformationForUpdatesDto ProfileInforForUpdatesData(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        if (member.isPresent()) {
            Member existMember = member.get();
            return existMember.EntityToProfileUpdatesDto();
        }
        return null;
    }

    @Override
    public Integer checkUser(String email) {
        Optional<Member> memberOptional = memberRepository.findByEmail(email);
        if (memberOptional.isPresent()) {
            Member member = memberOptional.get();
            int count = teamMemberRepository.countByMemberSeq(member.getMemberId());
            return count > 0 ? member.getMemberId() : -1;
        }
        return -1;
    }

}

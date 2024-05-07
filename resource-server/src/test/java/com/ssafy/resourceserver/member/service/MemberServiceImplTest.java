package com.ssafy.resourceserver.member.service;

import static java.time.LocalDateTime.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.joda.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.resourceserver.common.utils.S3Uploader;
import com.ssafy.resourceserver.member.model.domain.Member;
import com.ssafy.resourceserver.member.model.domain.MemberRoleEnum;
import com.ssafy.resourceserver.member.model.dto.ProfileInformationForUpdatesDto;
import com.ssafy.resourceserver.member.model.repository.MemberRepository;

@ExtendWith(MockitoExtension.class)
public class MemberServiceImplTest {

	@Mock
	private MemberRepository memberRepository;

	@Mock
	private S3Uploader s3Uploader;

	@InjectMocks
	private MemberServiceImpl memberService;

	private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	@Test
	@DisplayName("유저 정보 업데이트 테스트 - 성공 케이스")
	void updateUserProfileTest() throws Exception {
		// 가정
		Member mockMember = new Member
			(1,
				"test@example.com",
				"Test User",
				MemberRoleEnum.USER,
				"010-1234-5678",
				"test@example.com",
				1,
				"java",
				"test",
				now(),
				false,
				"http://example.com/image.jpg",
				true,
				true,
				true,
				true,
				true);
		when(memberRepository.findByEmail(anyString())).thenReturn(Optional.of(mockMember));
		when(s3Uploader.uploadFile(any(MultipartFile.class))).thenReturn("http://example.com/newimage.jpg");

		// 입력
		MultipartFile image = mock(MultipartFile.class);
		ProfileInformationForUpdatesDto updatedTest = ProfileInformationForUpdatesDto.builder()
			.studentId("test@example.com")
			.phoneNumber("010-9876-5432")
			.name("Updated test")
			.gender(true)
			.email("")
			.image(mock(MultipartFile.class))
			.build();

		// 실행
		memberService.updateUserProfile("test@example.com", updatedTest);

		// 검증
		assertEquals("Updated test", mockMember.getName());
		assertEquals("010-9876-5432", mockMember.getPhoneNumber());
		assertEquals("http://example.com/newimage.jpg", mockMember.getImage());
		verify(memberRepository).findByEmail("test@example.com");
		verify(s3Uploader).uploadFile(image);
	}

	@Test
	@DisplayName("유저 정보 업데이트 테스트 - 실패 케이스")
	void updateUserProfileTestFail1() throws Exception {
		// 가정
		Member mockMember = new Member
			(1,
				"test@example.com",
				"Test User",
				MemberRoleEnum.USER,
				"010-1234-5678",
				"test@example.com",
				1,
				"java",
				"test",
				now(),
				false,
				"http://example.com/image.jpg",
				true,
				true,
				true,
				true,
				true);
		when(memberRepository.findByEmail(anyString())).thenReturn(Optional.of(mockMember));

		// 입력
		Map<String, Object> userInfo = new HashMap<>();
		ProfileInformationForUpdatesDto updatedTest = ProfileInformationForUpdatesDto.builder()
			.studentId("test@example.com")
			.phoneNumber("010-9876-5432")
			.name("Updated test")
			.gender(false)
			.email("test@example.com")
			.image(mock(MultipartFile.class))
			.build();
		// 실행
		// 실행 및 검증
		Exception exception = assertThrows(RuntimeException.class, () -> {
			memberService.updateUserProfile("test@example.com", updatedTest);
		});
		assertEquals("성별 바뀌면 안되는데?", exception.getMessage());

		// 검증
		assertEquals(true, mockMember.getGender());
		assertEquals("010-1234-5678", mockMember.getPhoneNumber());
		assertEquals("http://example.com/image.jpg", mockMember.getImage());
	}

	@Test
	@DisplayName("보안 단계 설정")
	void updateSecurityGradeTest() {
		// 가정
		Member mockMember = new Member(1, "test@example.com", "Test User", MemberRoleEnum.USER, "010-1234-5678",
			"test@example.com", 1, "java", "test", now(), false, "http://example.com/image.jpg", true, true, true, true,
			true);
		when(memberRepository.findByEmail(anyString())).thenReturn(Optional.of(mockMember));

		// 실행
		memberService.updateSecurityGrade("test@example.com", 5);

		// 검증
		assertEquals(5, mockMember.getGrade());
		verify(memberRepository).findByEmail("test@example.com");
	}

	@Test
	@DisplayName("비밀번호 변경 - 성공 케이스")
	public void changePasswordTest() {
		Member mockMember = new Member(1, "test@example.com", passwordEncoder.encode("1234"), MemberRoleEnum.USER,
			"010-1234-5678",
			"test@example.com", 1, "java", "test", now(), false, "http://example.com/image.jpg", true, true, true, true,
			true);
		when(memberRepository.findByEmail(anyString())).thenReturn(Optional.of(mockMember));

		Map<String, String> map = new HashMap<>();
		map.put("password", "1234");
		map.put("newPassword", "new");

		memberService.updatePassword("test@example.com", map);
		assertTrue(passwordEncoder.matches(map.get("newPassword"), mockMember.getPassword()));
	}

	@Test
	@DisplayName("비밀번호 변경 - 실패 케이스")
	public void changePasswordFailTest() {
		Member mockMember = new Member(1, "test@example.com", passwordEncoder.encode("123456789"), MemberRoleEnum.USER,
			"010-1234-5678",
			"test@example.com", 1, "java", "test", now(), false, "http://example.com/image.jpg", true, true, true, true,
			true);
		when(memberRepository.findByEmail(anyString())).thenReturn(Optional.of(mockMember));

		Map<String, String> map = new HashMap<>();
		map.put("password", "1234");
		map.put("newPassword", "new");

		assertFalse(passwordEncoder.matches(map.get("newPassword"), mockMember.getPassword()));

		RuntimeException exception = assertThrows(RuntimeException.class, () -> {
			memberService.updatePassword("test@example.com", map);
		});

		assertEquals("비밀번호 틀림", exception.getMessage());
	}
}
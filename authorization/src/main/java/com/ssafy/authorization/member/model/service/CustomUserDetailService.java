package com.ssafy.authorization.member.model.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ssafy.authorization.member.model.domain.Authority;
import com.ssafy.authorization.member.model.domain.Member;
import com.ssafy.authorization.member.model.dto.SignUpRequestDto;
import com.ssafy.authorization.member.model.repository.AuthorityRepository;
import com.ssafy.authorization.member.model.repository.MemberRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
	private final MemberRepository memberRepository;
	private final AuthorityRepository authorityRepository;
	private final PasswordEncoder passwordEncoder;
	@Transactional
	public void createUser(SignUpRequestDto dto) {
		Member member = Member.create(dto.getUserEmail(), passwordEncoder.encode(dto.getPassword()));
		memberRepository.save(member);
		authorityRepository.save(Authority.builder().authority("USER").member(member).build());
	}
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Member member = getUserByUsername(username);
		return User.builder()
			.username(member.getUsername())
			.password(member.getPassword())
			.disabled(!member.getEnabled())
			.accountExpired(!member.getAccountNonExpired())
			.accountLocked(!member.getAccountNonLocked())
			.credentialsExpired(!member.getCredentialsNonExpired())
			.authorities(member.getSimpleAuthorities())
			.build();
	}

	public Member getUserByUsername(String username) throws UsernameNotFoundException {
		Member member = memberRepository.findByUsername(username);
		if (member == null) {
			throw new UsernameNotFoundException(username);
		}
		return member;
	}
}

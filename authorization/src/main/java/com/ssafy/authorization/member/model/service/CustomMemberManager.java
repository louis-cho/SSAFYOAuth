package com.ssafy.authorization.member.model.service;

import java.util.Optional;


import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import com.ssafy.authorization.member.model.domain.Member;
import com.ssafy.authorization.member.model.repository.MemberRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomMemberManager implements UserDetailsManager {

	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;
	@Override
	@Transactional
	public void createUser(UserDetails user) {
		Member member = (Member) user;
		member.changePassword(passwordEncoder.encode(member.getPassword()));
		memberRepository.save(member);
	}

	@Override
	@Transactional
	public void updateUser(UserDetails user) {
		Member member = (Member) user;
		member.changePassword(passwordEncoder.encode(member.getPassword()));
		memberRepository.save(member);
	}

	@Override
	@Transactional
	public void deleteUser(String username) {
		memberRepository.deleteByEmail(username);
	}

	@Override
	@Transactional
	public void changePassword(String oldPassword, String newPassword) {
		UserDetails userDetails = loadUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		if (userDetails != null) {
			Member member = (Member) userDetails;
			if (passwordEncoder.matches(oldPassword, member.getPassword())) {
				member.changePassword(passwordEncoder.encode(newPassword));
				memberRepository.save(member);
			} else {
				throw new IllegalArgumentException("Old password does not match.");
			}
		}
	}

	@Override
	public boolean userExists(String username) {
		return memberRepository.findByEmail(username).isPresent();
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println(username);
		Optional<Member> member = memberRepository.findByEmail(username);
		if (member.isPresent()) {
			log.info("member {} : ", member);
			return member.get();
		} else {
			throw new UsernameNotFoundException("User not found for username: " + username);
		}
	}
}


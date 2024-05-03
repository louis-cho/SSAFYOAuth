package com.ssafy.client.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private final OAuth2AuthorizedClientService authorizedClientService;
	@Autowired
	private final RestTemplate restTemplate;

	@GetMapping("/check-auth")
	public String checkAuthentication() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) {
			return "No authentication found.";
		} else {
			return "Authentication: " + authentication.toString() + "\nPrincipal: " + authentication.getPrincipal().toString();
		}
	}


	@GetMapping("/token")
	public String getToken(Authentication authentication) {
		OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
			"client", authentication.getName());
		if (client == null) {
			return "No client data found.";
		}

		OAuth2AccessToken accessToken = client.getAccessToken();
		return "Access Token: " + accessToken.getTokenValue();
	}

	@GetMapping("test")
	public String makeOAuth2Request(@AuthenticationPrincipal OAuth2User principal,
		@RegisteredOAuth2AuthorizedClient("client") OAuth2AuthorizedClient client) {
		// 토큰 추출
		String accessToken = client.getAccessToken().getTokenValue();

		// Http 헤더 설정
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(accessToken);  // Bearer 토큰 설정

		// HttpEntity 객체 생성
		HttpEntity<String> entity = new HttpEntity<>(headers);

		// RestTemplate을 사용하여 GET 요청
		ResponseEntity<String> response = restTemplate.exchange(
			"http://127.0.0.1:8090/user/test", HttpMethod.GET, entity, String.class);

		return response.getBody();
	}
    // private final UserService userService;
    //
    // @GetMapping
    // public ResponseEntity<?> getUserInfo(Authentication authentication) {
    //     CustomOAuth2User user = (CustomOAuth2User) authentication.getPrincipal();
    //
    //     UserEntity userInfo = userService.getUserInfo(user);
    //     if (userInfo != null) {
    //         return ResponseEntity.ok(userInfo);
    //     }
    //
    //     return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("없음. 뭐지?");
    // }
    //
    // @GetMapping("/name")
    // public ResponseEntity<Boolean> duplicateUsername(@RequestParam String name) {
    //     boolean result = userRepository.findByUsername(name) != null;
    //     return ResponseEntity.ok(result);
    // }
    //
    // @PatchMapping("/name")
    // public ResponseEntity<String> updateUserName(Authentication authentication, @RequestParam String name) {
    //     CustomOAuth2User user = (CustomOAuth2User) authentication.getPrincipal();
    //     if (user == null) {
    //         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("권한 없음");
    //     }
    //
    //     UserEntity userInfo = userService.getUserInfo(user);
    //     userService.updateUserName(userInfo, name);
    //
    //     return ResponseEntity.ok("success");
    // }
    //
    // @GetMapping("/records")
    // public ResponseEntity<?> getGameRecords(@RequestParam String nickname,
    //                                                        Authentication authentication) {
    //     List<GameRecord> gameRecords = null;
    //     CustomOAuth2User principal = (CustomOAuth2User) authentication.getPrincipal();
    //     if (principal == null) {
    //         return ResponseEntity.status(HttpStatus.FORBIDDEN).body("회원이 아닙니다.");
    //     }
    //
    //     Optional<GameRecordMember> gameRecordMember = gameMemberRepository.findByUsername(nickname);
    //     if (gameRecordMember.isPresent()) {
    //         gameRecords = gameRecordMember.get().getGameRecords();
    //     }
    //
    //
    //     return ResponseEntity.ok(gameRecords);
    // }
    //
    // @GetMapping("/piece")
    // public ResponseEntity<?> getMyDefaultPiece(Authentication authentication) {
    //     CustomOAuth2User user = (CustomOAuth2User) authentication.getPrincipal();
    //
    //     UserEntity userInfo = userService.getUserInfo(user);
    //     Product myDefaultPiece = userService.getMyDefaultPiece(userInfo);
    //
    //     return ResponseEntity.ok(myDefaultPiece);
    // }
    //
    // @GetMapping("/pieces")
    // public ResponseEntity<?> getPiecesList(Authentication authentication) {
    //     CustomOAuth2User user = (CustomOAuth2User) authentication.getPrincipal();
    //
    //     UserEntity userInfo = userService.getUserInfo(user);
    //     List<Product> purchasesByUser = userService.findPurchasesByUser(userInfo);
    //
    //     return ResponseEntity.ok(purchasesByUser);
    // }
    //
    // @PostMapping("/piece")
    // public ResponseEntity<?> choicePiece(Authentication authentication, @RequestBody Map<String, String> productName) {
    //     CustomOAuth2User user = (CustomOAuth2User) authentication.getPrincipal();
    //
    //     UserEntity userInfo = userService.getUserInfo(user);
    //     String name = productName.get("productName");
    //     System.out.println(name);
    //     boolean result = userService.choicePiece(userInfo, name);
    //
    //     if (result) {
    //         return ResponseEntity.status(HttpStatus.OK).body(userInfo);
    //     }
    //
    //     return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("상품 없음");
    // }
    //
    // @PostMapping("/wallet")
    // public ResponseEntity<?> updateWalletAddress(Authentication authentication, @RequestBody Map<String, String> walletAddress) {
    //     CustomOAuth2User user = (CustomOAuth2User) authentication.getPrincipal();
    //
    //     UserEntity userInfo = userService.getUserInfo(user);
    //     userService.updateWallet(userInfo, walletAddress.get("address"));
    //     return ResponseEntity.status(HttpStatus.OK).body(userInfo);
    // }
}

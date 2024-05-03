package com.ssafy.authorization.config;

import static org.springframework.security.config.Customizer.*;

import java.util.List;
import java.util.function.Function;

import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobHandler;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;

@Configuration
@EnableWebSecurity
public class AuthorizationServerConfig {

	// @Bean
	// public CorsConfigurationSource corsConfigurationSource() {
	// 	CorsConfiguration configuration = new CorsConfiguration();
	// 	configuration.setAllowedOrigins(Arrays.asList("http://localhost:9000")); // 허용할 오리진
	// 	configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
	// 	configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
	//
	// 	UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	// 	source.registerCorsConfiguration("/**", configuration);
	// 	return source;
	// }
	@Bean
	@Order(1)
	SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http)
		throws Exception {
		OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
		http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
			.authorizationEndpoint(auth -> auth
				.consentPage("/oauth2/consent"));
		//				.oidc(withDefaults());
		http
			.exceptionHandling((exceptions) -> exceptions
				.defaultAuthenticationEntryPointFor(
					new LoginUrlAuthenticationEntryPoint("/login"),
					new MediaTypeRequestMatcher(MediaType.TEXT_HTML)
				)
			)
			.oauth2ResourceServer((resourceServer) -> resourceServer
				.jwt(withDefaults()));

		return http.build();
	}


	@Bean
	@Order(2)
	SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http)
			throws Exception {
		http.csrf(csrf -> csrf.disable());
		http
				.authorizeHttpRequests((authorize) -> authorize
						.requestMatchers("/css/**", "/favicon.ico", "/error","/image/**","/vendor/**",
							"/test/**","/login","/signup", "/sendemail","/certify","/forgot_password","/forgot_user","/find_user"
							,".well-known/jwks.json").permitAll()
						.anyRequest().authenticated()
				)
				.formLogin(formLogin -> formLogin
						.loginPage("/login")
				);
		
		return http.build();
	}

	 // @Bean
	 // @Order(2)
	 // SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http)
	 // 		throws Exception {
		//  http
		// 		 .csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable())
		// 		 .authorizeHttpRequests(authorize -> authorize
		// 				 .requestMatchers("/login_test", "/css/**", "/api/auth/login", "/favicon.ico", "/error", "/test/**", "/login", "/sign_up", ".well-known/jwks.json").permitAll()
		// 				 .anyRequest().authenticated())
		// 		 .formLogin(formLogin -> formLogin
		// 				 .loginPage("/login_test")
		// 				 .successHandler((request, response, authentication) -> {
		// 					 System.out.println("Login success");
		// 				 })
		// 				 .failureHandler((request, response, exception) -> {
		// 					 System.out.println("Login failed");
		// 				 })
		// 		 );
	 // 	return http.build();
	 // }
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	// @Bean
	// RegisteredClientRepository jdbcRegisteredClientRepository(JdbcTemplate template) {
	// 	return new JdbcRegisteredClientRepository(template);
	// }
	//
	// @Bean
	// OAuth2AuthorizationService jdbcOAuth2AuthorizationService(
	// 	JdbcOperations jdbcOperations,
	// 	RegisteredClientRepository registeredClientRepository) {
	//
	// 	// RegisteredClient registeredClient = registeredClientRepository.findById(id);
	//
	// 	RegisteredClient registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
	// 		.clientId("client")
	// 		.clientName("client")
	// 		.clientSecret(passwordEncoder().encode("secret"))
	// 		.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
	// 		.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
	// 		.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
	// 		.authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
	// 		.authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
	// 		.redirectUri("http://localhost:8080/login/oauth2/code/client")
	// 		.postLogoutRedirectUri("http://localhost:8080/logged-out")
	// 		.scope(OidcScopes.OPENID)
	// 		.scope(OidcScopes.PROFILE)
	// 		.scope("read")
	// 		.scope("write")
	// 		.clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
	// 		.build();
	//
	// 	RegisteredClient deviceClient = RegisteredClient.withId(UUID.randomUUID().toString())
	// 		.clientId("device-client")
	// 		.clientAuthenticationMethod(ClientAuthenticationMethod.NONE)
	// 		.authorizationGrantType(AuthorizationGrantType.DEVICE_CODE)
	// 		.authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
	// 		.scope("message.read")
	// 		.scope("message.write")
	// 		.build();
	//
	// 	//			 registeredClientRepository.save(registeredClient);
	// 	//			 registeredClientRepository.save(deviceClient);
	//
	// 	return new JdbcOAuth2AuthorizationService(jdbcOperations, registeredClientRepository);
	// }

	@Bean
	OAuth2AuthorizationConsentService jdbcOAuth2AuthorizationConsentService(
		JdbcOperations jdbcOperations,
		RegisteredClientRepository registeredClientRepository) {
		return new JdbcOAuth2AuthorizationConsentService(jdbcOperations, registeredClientRepository);
	}

	@Bean
	public LobHandler lobHandler() {
		return new DefaultLobHandler();
	}

	@Bean
	public RowMapper<OAuth2Authorization> authorizationRowMapper(RegisteredClientRepository registeredClientRepository) {
		return new OAuth2AuthorizationServiceImpl.OAuth2AuthorizationRowMapper(registeredClientRepository);
	}

	@Bean
	public Function<OAuth2Authorization, List<SqlParameterValue>> authorizationParametersMapper() {
		return new OAuth2AuthorizationServiceImpl.OAuth2AuthorizationParametersMapper();
	}
}
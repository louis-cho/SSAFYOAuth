package com.ssafy.authorization.config;

import com.ssafy.authorization.member.login.filter.CustomAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobHandler;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.web.cors.CorsUtils;

import java.util.List;
import java.util.function.Function;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class AuthorizationServerConfig {

	private final RedisTemplate<String, String> redisTemplate;

	@Value("${oauth2.client.redirect-uri}")
	private String redirectBaseUrl;

	@Autowired
	public AuthorizationServerConfig(RedisTemplate<String, String> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

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

				.addFilterBefore(new CustomAuthenticationFilter(redisTemplate), OAuth2AuthorizationRequestRedirectFilter.class)
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
				.authorizeHttpRequests((request) -> request.requestMatchers(CorsUtils::isPreFlightRequest).permitAll());

	 	http
	 			.authorizeHttpRequests((authorize) -> authorize
	 					.requestMatchers("/js/**","/api/auth/waitSignal", "/css/**", "/favicon.ico", "/error","/image/**","/vendor/**",
	 						"/test/**","/login","/signup", "/sendemail","/certify","/forgot_password","/forgot_user","/find_user"
	 						,".well-known/jwks.json").permitAll()
	 					.anyRequest().authenticated()
	 			)
	 			.formLogin(formLogin -> formLogin
	 					.loginPage("/login")
	 			);

	 	return http.build();
	 }

	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	@Bean
	public LobHandler lobHandler() {
		return new DefaultLobHandler();
	}

	@Bean
	public RowMapper<OAuth2Authorization> authorizationRowMapper(RegisteredClientRepository registeredClientRepository) {
		return new TestOauth2ServiceImpl.OAuth2AuthorizationRowMapper(registeredClientRepository);
	}

	@Bean
	public Function<OAuth2Authorization, List<SqlParameterValue>> authorizationParametersMapper() {
		return new TestOauth2ServiceImpl.OAuth2AuthorizationParametersMapper();
	}


}
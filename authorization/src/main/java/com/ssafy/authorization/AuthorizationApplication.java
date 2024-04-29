package com.ssafy.authorization;

import com.ssafy.authorization.member.login.management.LoginQueueManager;
import org.apache.tomcat.util.threads.VirtualThreadExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableJpaAuditing
public class AuthorizationApplication implements ApplicationRunner {

	private final VirtualThreadExecutor virtualThreadExecutor;

	private final LoginQueueManager loginQueueManager;

	@Autowired
    public AuthorizationApplication(LoginQueueManager loginQueueManager) {
		this.virtualThreadExecutor = new VirtualThreadExecutor("login");
		this.loginQueueManager = loginQueueManager;
    }

    public static void main(String[] args) {
		SpringApplication.run(AuthorizationApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		virtualThreadExecutor.execute(loginQueueManager);
	}
}

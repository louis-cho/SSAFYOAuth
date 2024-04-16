package com.ssafy.test.com.ssafy.authorization.secure;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.ssafy.authorization.secure.BcryptEncoder;

public class BcryptEncoderTest {

	@Test
	void test() {

		try {
			String text = "sample text with request client info";
			String bcrypted = BcryptEncoder.getInstance().encode(text);

			System.out.println("text >> " + text);
			System.out.println("bcrypted >> " + bcrypted);

			boolean ret = BcryptEncoder.getInstance().verify(text, bcrypted);
			if (ret) {
				System.out.println("클라이언트에서 보낸 내용이 위변조되지 않았습니다");
			} else {
				System.out.println("클라이언트에서 보낸 내용이 위변조되었습니다.");
			}
			assertTrue(ret);
		} catch (NullPointerException e) {
			assert false;
		}

	}
}

package com.ssafy.test.org.example.authorization.secure;

import static org.junit.jupiter.api.Assertions.*;

import java.security.PrivateKey;
import java.security.PublicKey;

import org.junit.jupiter.api.Test;

import com.ssafy.authorization.secure.KeyManager;
import com.ssafy.authorization.secure.RSA2048;

public class KeyManagerTest {
	@Test
	void test() {
		String ip = "127.0.0.1";
		String plain = "hello hi !@#$_123";

		KeyManager.getInstance().createKeyPair(ip);
		PublicKey publicKey = KeyManager.getInstance().getPublicKey(ip);
		PrivateKey privateKey = KeyManager.getInstance().getPrivateKey(ip);
		String encrypted = RSA2048.encrypt(plain, RSA2048.keyToString(publicKey));
		String decrypted = RSA2048.decrypt(encrypted, RSA2048.keyToString(privateKey));

		try {
			boolean ret = (!plain.equals(encrypted)) && plain.equals(decrypted);
			System.out.println("plain >> " + plain);
			System.out.println("encrypted >> " + encrypted);
			System.out.println("decrypted >> " + decrypted);
			assertTrue(ret);
		} catch (NullPointerException e) {
			assert false;
		}
	}
}


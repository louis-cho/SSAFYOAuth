package org.example.authorization.secure;

import static org.junit.jupiter.api.Assertions.*;

import java.security.KeyPair;

import org.junit.jupiter.api.Test;

class RSA2048Test {

	@Test
	void test() {

		KeyPair keys = RSA2048.createKey();
		String publicKey = RSA2048.keyToString(keys.getPublic());
		String privateKey = RSA2048.keyToString((keys.getPrivate()));

		String plain = "hello123!@#hello";
		String encrypted = RSA2048.encrypt(plain, publicKey);
		String decrypted = RSA2048.decrypt(encrypted, privateKey);

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
package org.red5.core.encryption;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.logging.Logger;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;


public class KeyGen implements Runnable {
	private static final Logger log = Logger.getLogger(KeyGen.class.getName());

	private static KeyGen instance = null;
	private SecretKey key;
	private static final String MODE = "AES";
	private static final Long Timeout = Long.valueOf(1000L);
	public static int SeksToSleep = 1;

	private KeyGen() {
		new Thread(this).start();
	}
 
	public static KeyGen getInstance() {
		if (instance == null)
			instance = new KeyGen();
		return instance;
	}

	public String decrypt(byte[] secret) throws Exception {
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, this.key);
		return new String(cipher.doFinal(secret));
	}

	public byte[] encrypt(String path) throws Exception {
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, this.key);
		return cipher.doFinal(path.getBytes());
	}

	public void run() {
		KeyGenerator keygen = null;
		try {
			keygen = KeyGenerator.getInstance("AES");
		} catch (NoSuchAlgorithmException e1) {

			e1.printStackTrace();
			return;
		}
		while (true)
			try {

				SecureRandom random = new SecureRandom();
				keygen.init(random);
				setKey(keygen.generateKey());

				log.info("Waiting for new Key... for " + Timeout.longValue()
						* SeksToSleep);
				Thread.sleep(Timeout.longValue() * SeksToSleep);
				continue;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	}

	public void setKey(SecretKey key) {
		this.key = key;
	}

	public SecretKey getKey() {
		return this.key;
	}
}

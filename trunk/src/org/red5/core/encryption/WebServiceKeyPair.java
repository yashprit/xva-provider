package org.red5.core.encryption;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import sun.security.rsa.RSAPublicKeyImpl;

public class WebServiceKeyPair {
	private static final int KEYSIZE = 2048;
	private KeyPair keyPair;
	private static final String privateFormat = "PKCS#8";
	private static final String publicFormat = "X.509";
	private static final String algorithm = "RSA";
	public static String secret;

	public static void main(String[] args) {
		new WebServiceKeyPair();
	}

	public WebServiceKeyPair() {
		init();
	}

	public synchronized KeyPair getKeyPair() {
		return this.keyPair;
	}

	public synchronized void setKeyPair(KeyPair keyPair) {
		this.keyPair = keyPair;
	}

	private void init() {
		File privateFile = new File("private.key");
		File publicFile = new File("public.key");

		if ((privateFile.exists()) && (publicFile.exists())) {
			try {
				FileInputStream pfis = new FileInputStream(privateFile);
				byte[] privatKey = new byte[2048];
				int data = pfis.read();
				int count = 0;
				while (data != -1) {
					privatKey[count] = (byte) data;
					count++;
					data = pfis.read();
				}

				count = 0;
				FileInputStream pufis = new FileInputStream(publicFile);
				byte[] publicKey = new byte[2048];
				data = pufis.read();

				while (data != -1) {
					publicKey[count] = (byte) data;
					count++;
					data = pufis.read();
				}

				PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(privatKey);
				KeyFactory kf = KeyFactory.getInstance("RSA");
				PrivateKey private_key = kf.generatePrivate(spec);

				setKeyPair(new KeyPair(new RSAPublicKeyImpl(publicKey),
						private_key));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InvalidKeyException e) {
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (InvalidKeySpecException e) {
				e.printStackTrace();
			}
		} else
			try {
				KeyPairGenerator pairgen = KeyPairGenerator.getInstance("RSA");
				SecureRandom random = new SecureRandom();
				pairgen.initialize(2048, random);
				this.keyPair = pairgen.generateKeyPair();

				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				byte[] privateKey = this.keyPair.getPrivate().getEncoded();
				byteArrayOutputStream.write(privateKey, 0, privateKey.length);
				FileOutputStream fileOutputStream = new FileOutputStream(
						"private.key");
				byteArrayOutputStream.writeTo(fileOutputStream);
				fileOutputStream.close();
				byteArrayOutputStream.reset();
				fileOutputStream = null;

				byte[] publicKey = this.keyPair.getPublic().getEncoded();
				System.out.println(publicKey.length);
				byteArrayOutputStream.write(publicKey);
				fileOutputStream = new FileOutputStream("public.key");
				byteArrayOutputStream.writeTo(fileOutputStream);
				fileOutputStream.close();
				byteArrayOutputStream.reset();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

	public boolean check(byte[] encrypted) {
		try {
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(2, this.keyPair.getPrivate());
			byte[] decrypted = cipher.doFinal(encrypted);
			String dec = new String(decrypted);
			System.out.println("Decrypted:" + dec);
			return dec.equals(secret);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return false;
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
			return false;
		} catch (InvalidKeyException e) {
			e.printStackTrace();
			return false;
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
			return false;
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return false;
	}

	public class KeyImpl implements Key, PublicKey, PrivateKey {
		private String algorithm;
		private byte[] encoded;
		private String format;

		public KeyImpl(String algorithm, byte[] encoded, String format) {
			this.algorithm = algorithm;
			this.encoded = encoded;
			this.format = format;
		}

		public String getAlgorithm() {
			return this.algorithm;
		}

		public byte[] getEncoded() {
			return this.encoded;
		}

		public String getFormat() {
			return this.format;
		}
	}
}

/*     */ package org.red5.core.encryption;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.security.InvalidKeyException;
/*     */ import java.security.Key;
/*     */ import java.security.KeyFactory;
/*     */ import java.security.KeyPair;
/*     */ import java.security.KeyPairGenerator;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.PrivateKey;
/*     */ import java.security.PublicKey;
/*     */ import java.security.SecureRandom;
/*     */ import java.security.spec.InvalidKeySpecException;
/*     */ import java.security.spec.PKCS8EncodedKeySpec;
/*     */ import javax.crypto.BadPaddingException;
/*     */ import javax.crypto.Cipher;
/*     */ import javax.crypto.IllegalBlockSizeException;
/*     */ import javax.crypto.NoSuchPaddingException;
/*     */ import sun.security.rsa.RSAPublicKeyImpl;
/*     */ 
/*     */ public class WebServiceKeyPair
/*     */ {
/*     */   private static final int KEYSIZE = 2048;
/*     */   private KeyPair keyPair;
/*     */   private static final String privateFormat = "PKCS#8";
/*     */   private static final String publicFormat = "X.509";
/*     */   private static final String algorithm = "RSA";
/*     */   public static String secret;
/*     */ 
/*     */   public static void main(String[] args)
/*     */   {
/*  37 */     new WebServiceKeyPair();
/*     */   }
/*     */ 
/*     */   public WebServiceKeyPair() {
/*  41 */     init();
/*     */   }
/*     */ 
/*     */   public synchronized KeyPair getKeyPair()
/*     */   {
/*  49 */     return this.keyPair;
/*     */   }
/*     */ 
/*     */   public synchronized void setKeyPair(KeyPair keyPair) {
/*  53 */     this.keyPair = keyPair;
/*     */   }
/*     */ 
/*     */   private void init()
/*     */   {
/*  62 */     File privateFile = new File("private.key");
/*  63 */     File publicFile = new File("public.key");
/*     */ 
/*  65 */     if ((privateFile.exists()) && (publicFile.exists())) {
/*     */       try {
/*  67 */         FileInputStream pfis = new FileInputStream(privateFile);
/*  68 */         byte[] privatKey = new byte[2048];
/*  69 */         int data = pfis.read();
/*  70 */         int count = 0;
/*  71 */         while (data != -1) {
/*  72 */           privatKey[count] = (byte)data;
/*  73 */           count++;
/*  74 */           data = pfis.read();
/*     */         }
/*     */ 
/*  77 */         count = 0;
/*  78 */         FileInputStream pufis = new FileInputStream(publicFile);
/*  79 */         byte[] publicKey = new byte[2048];
/*  80 */         data = pufis.read();
/*     */ 
/*  82 */         while (data != -1) {
/*  83 */           publicKey[count] = (byte)data;
/*  84 */           count++;
/*  85 */           data = pufis.read();
/*     */         }
/*     */ 
/*  91 */         PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(privatKey);
/*  92 */         KeyFactory kf = KeyFactory.getInstance("RSA");
/*  93 */         PrivateKey private_key = kf.generatePrivate(spec);
/*     */ 
/*  95 */         setKeyPair(new KeyPair(new RSAPublicKeyImpl(publicKey), private_key));
/*     */       }
/*     */       catch (FileNotFoundException e)
/*     */       {
/*  99 */         e.printStackTrace();
/*     */       }
/*     */       catch (IOException e) {
/* 102 */         e.printStackTrace();
/*     */       }
/*     */       catch (InvalidKeyException e) {
/* 105 */         e.printStackTrace();
/*     */       }
/*     */       catch (NoSuchAlgorithmException e) {
/* 108 */         e.printStackTrace();
/*     */       }
/*     */       catch (InvalidKeySpecException e) {
/* 111 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */     else
/*     */       try
/*     */       {
/* 117 */         KeyPairGenerator pairgen = KeyPairGenerator.getInstance("RSA");
/* 118 */         SecureRandom random = new SecureRandom();
/* 119 */         pairgen.initialize(2048, random);
/* 120 */         this.keyPair = pairgen.generateKeyPair();
/*     */ 
/* 122 */         ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
/* 123 */         byte[] privateKey = this.keyPair.getPrivate().getEncoded();
/* 124 */         byteArrayOutputStream.write(privateKey, 0, privateKey.length);
/* 125 */         FileOutputStream fileOutputStream = new FileOutputStream("private.key");
/* 126 */         byteArrayOutputStream.writeTo(fileOutputStream);
/* 127 */         fileOutputStream.close();
/* 128 */         byteArrayOutputStream.reset();
/* 129 */         fileOutputStream = null;
/*     */ 
/* 131 */         byte[] publicKey = this.keyPair.getPublic().getEncoded();
/* 132 */         System.out.println(publicKey.length);
/* 133 */         byteArrayOutputStream.write(publicKey);
/* 134 */         fileOutputStream = new FileOutputStream("public.key");
/* 135 */         byteArrayOutputStream.writeTo(fileOutputStream);
/* 136 */         fileOutputStream.close();
/* 137 */         byteArrayOutputStream.reset();
/*     */       }
/*     */       catch (NoSuchAlgorithmException e)
/*     */       {
/* 141 */         e.printStackTrace();
/*     */       }
/*     */       catch (FileNotFoundException e)
/*     */       {
/* 147 */         e.printStackTrace();
/*     */       }
/*     */       catch (IOException e) {
/* 150 */         e.printStackTrace();
/*     */       }
/*     */   }
/*     */ 
/*     */   public boolean check(byte[] encrypted)
/*     */   {
/*     */     try
/*     */     {
/* 159 */       Cipher cipher = Cipher.getInstance("RSA");
/* 160 */       cipher.init(2, this.keyPair.getPrivate());
/* 161 */       byte[] decrypted = cipher.doFinal(encrypted);
/* 162 */       String dec = new String(decrypted);
/* 163 */       System.out.println("Decrypted:" + dec);
/* 164 */       return dec.equals(secret);
/*     */     }
/*     */     catch (NoSuchAlgorithmException e)
/*     */     {
/* 168 */       e.printStackTrace();
/* 169 */       return false;
/*     */     }
/*     */     catch (NoSuchPaddingException e) {
/* 172 */       e.printStackTrace();
/* 173 */       return false;
/*     */     }
/*     */     catch (InvalidKeyException e) {
/* 176 */       e.printStackTrace();
/* 177 */       return false;
/*     */     }
/*     */     catch (IllegalBlockSizeException e) {
/* 180 */       e.printStackTrace();
/* 181 */       return false;
/*     */     }
/*     */     catch (BadPaddingException e) {
/* 184 */       e.printStackTrace();
/*     */     }
/* 186 */     return false;
/*     */   }
/*     */   public class KeyImpl implements Key, PublicKey, PrivateKey {
/*     */     private String algorithm;
/*     */     private byte[] encoded;
/*     */     private String format;
/*     */ 
/* 196 */     public KeyImpl(String algorithm, byte[] encoded, String format) { this.algorithm = algorithm;
/* 197 */       this.encoded = encoded;
/* 198 */       this.format = format;
/*     */     }
/*     */ 
/*     */     public String getAlgorithm()
/*     */     {
/* 204 */       return this.algorithm;
/*     */     }
/*     */ 
/*     */     public byte[] getEncoded()
/*     */     {
/* 210 */       return this.encoded;
/*     */     }
/*     */ 
/*     */     public String getFormat()
/*     */     {
/* 216 */       return this.format;
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/philippklose/red5-1.0.0-RC1/webapps/XVAProvider/WEB-INF/classes/
 * Qualified Name:     org.red5.core.encryption.WebServiceKeyPair
 * JD-Core Version:    0.6.0
 */
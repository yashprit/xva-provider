/*     */ package org.red5.core;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import org.apache.log4j.Logger;
/*     */ import org.apache.log4j.Priority;
/*     */ import org.red5.core.blacklist.BlackList;
/*     */ import org.red5.core.encryption.KeyGen;
/*     */ import org.red5.core.filenameresolver.AFileNameResolver;
/*     */ import org.red5.core.filenameresolver.CrypticFileNameResolver;
/*     */ import org.red5.core.filenameresolver.NormalFileNameResolver;
/*     */ import org.red5.server.api.IConnection;
/*     */ import org.red5.server.api.IScope;
/*     */ import org.red5.server.api.Red5;
/*     */ import org.red5.server.api.stream.IStreamFilenameGenerator;
/*     */ import org.red5.server.api.stream.IStreamFilenameGenerator.GenerationType;
/*     */ 
/*     */ public class FileRedirector
/*     */   implements IStreamFilenameGenerator
/*     */ {
/*  18 */   private static final Logger log = Logger.getLogger(FileRedirector.class.getName());
/*     */ 
/*  20 */   private static final HashMap<String, String> map = new HashMap(500);
/*     */   private String dirs;
/*  23 */   private static boolean inited = false;
/*     */   private boolean secured;
/*     */   private String secret;
/*     */   private int liveTime;
/*     */   private int bannTime;
/*     */   private int Tries;
/*  84 */   private AFileNameResolver resolver = null;
/*     */ 
/*     */   public String getDirs()
/*     */   {
/*  25 */     return this.dirs;
/*     */   }
/*     */ 
/*     */   public void setDirs(String dirs) {
/*  29 */     this.dirs = dirs;
/*     */   }
/*     */ 
/*     */   public synchronized boolean isSecured()
/*     */   {
/*  37 */     return this.secured;
/*     */   }
/*     */ 
/*     */   public synchronized void setSecured(boolean secured) {
/*  41 */     this.secured = secured;
/*     */   }
/*     */ 
/*     */   public synchronized String getSecret()
/*     */   {
/*  47 */     return this.secret;
/*     */   }
/*     */ 
/*     */   public synchronized void setSecret(String secret) {
/*  51 */     this.secret = secret;
/*     */   }
/*     */ 
/*     */   public synchronized int getLiveTime()
/*     */   {
/*  57 */     return this.liveTime;
/*     */   }
/*     */ 
/*     */   public synchronized void setLiveTime(int liveTime) {
/*  61 */     this.liveTime = liveTime;
/*     */   }
/*     */ 
/*     */   public synchronized int getBannTime()
/*     */   {
/*  66 */     return this.bannTime;
/*     */   }
/*     */ 
/*     */   public synchronized void setBannTime(int bannTime) {
/*  70 */     this.bannTime = bannTime;
/*     */   }
/*     */ 
/*     */   public synchronized int getTries() {
/*  74 */     return this.Tries;
/*     */   }
/*     */ 
/*     */   public synchronized void setTries(int tries) {
/*  78 */     this.Tries = tries;
/*     */   }
/*     */ 
/*     */   private void load()
/*     */   {
/*  87 */     String[] tdir = this.dirs.split(";");
/*  88 */     for (int i = 0; i < tdir.length; i++) {
/*  89 */       String[] splits = tdir[i].split(":=");
/*  90 */       map.put(splits[0], splits[1]);
/*     */     }
/*     */ 
/*  93 */     KeyGen.SeksToSleep = getLiveTime();
/*  94 */     KeyGen.getInstance();
/*     */ 
/*  96 */     BlackList.bannehours = getBannTime();
/*  97 */     BlackList.Tries = getTries();
/*     */ 
/*  99 */     org.red5.core.encryption.WebServiceKeyPair.secret = getSecret();
/* 100 */     log.log(Priority.INFO, "Succesfully loaded with Secret");
/* 101 */     if (this.secured) {
/* 102 */       log.log(Priority.INFO, "Bind secured File Path Provider");
/* 103 */       this.resolver = new CrypticFileNameResolver(map);
/*     */     } else {
/* 105 */       log.log(Priority.INFO, "Binding unsecured File Path Provider");
/* 106 */       this.resolver = new NormalFileNameResolver(map);
/*     */     }
/*     */ 
/* 110 */     inited = true;
/*     */   }
/*     */ 
/*     */   public String generateFilename(IScope arg0, String arg1, IStreamFilenameGenerator.GenerationType arg2)
/*     */   {
/* 119 */     if (!inited) {
/* 120 */       load();
/*     */     }
/* 122 */     return generateFilename(arg0, arg1, null, arg2);
/*     */   }
/*     */ 
/*     */   public String generateFilename(IScope scope, String arg1, String arg2, IStreamFilenameGenerator.GenerationType arg3)
/*     */   {
/* 129 */     if (!inited) {
/* 130 */       load();
/*     */     }
/* 132 */     String ret = this.resolver.getFileName(arg1);
/* 133 */     if ((ret == null) || (ret == "")) {
/* 134 */       IConnection currentConnection = Red5.getConnectionLocal();
/* 135 */       BlackList.getInstance().warnIp(currentConnection.getRemoteAddress());
/* 136 */       currentConnection.close();
/* 137 */       log.info("Closing Connection and Warning Client");
/*     */     }
/* 139 */     return ret;
/*     */   }
/*     */ 
/*     */   public boolean resolvesToAbsolutePath()
/*     */   {
/* 147 */     return true;
/*     */   }
/*     */ }

/* Location:           /Users/philippklose/red5-1.0.0-RC1/webapps/XVAProvider/WEB-INF/classes/
 * Qualified Name:     org.red5.core.FileRedirector
 * JD-Core Version:    0.6.0
 */
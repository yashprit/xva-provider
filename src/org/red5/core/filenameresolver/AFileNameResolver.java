/*    */ package org.red5.core.filenameresolver;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ 
/*    */ public abstract class AFileNameResolver
/*    */ {
/*    */   protected HashMap<String, String> dirs;
/*    */ 
/*    */   protected AFileNameResolver(HashMap<String, String> dirs)
/*    */   {
/*  9 */     this.dirs = dirs;
/*    */   }
/*    */ 
/*    */   private boolean isStringEncrypted(String inputString) {
/* 13 */     int lsharp = inputString.lastIndexOf("#");
/* 14 */     if (lsharp < 3) return false;
/* 15 */     String Secret = inputString.substring(lsharp, inputString.length());
/*    */ 
/* 17 */     return Secret.length() == 12;
/*    */   }
/*    */ 
/*    */   protected String resolve(String arg1)
/*    */   {
/* 24 */     int firstSlash = arg1.indexOf("/");
/* 25 */     String dir = arg1.substring(0, firstSlash);
/* 26 */     if ((dir == null) || (dir == "")) {
/* 27 */       return null;
/*    */     }
/* 29 */     String folder = (String)this.dirs.get(dir);
/* 30 */     if ((folder == null) || (folder == "")) {
/* 31 */       return null;
/*    */     }
/*    */ 
/* 34 */     String filePath = folder + arg1.substring(firstSlash + 1);
/*    */ 
/* 36 */     return filePath;
/*    */   }
/*    */ 
/*    */   public abstract String getFileName(String paramString);
/*    */ }

/* Location:           /Users/philippklose/red5-1.0.0-RC1/webapps/XVAProvider/WEB-INF/classes/
 * Qualified Name:     org.red5.core.filenameresolver.AFileNameResolver
 * JD-Core Version:    0.6.0
 */
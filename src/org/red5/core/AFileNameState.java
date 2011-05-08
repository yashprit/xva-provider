/*    */ package org.red5.core;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ 
/*    */ public abstract class AFileNameState
/*    */ {
/*    */   protected HashMap<String, String> dirs;
/*    */ 
/*    */   protected AFileNameState(HashMap<String, String> dirs)
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
/*    */   public abstract String getFileName(String paramString);
/*    */ }

/* Location:           /Users/philippklose/red5-1.0.0-RC1/webapps/XVAProvider/WEB-INF/classes/
 * Qualified Name:     org.red5.core.AFileNameState
 * JD-Core Version:    0.6.0
 */
/*    */ package org.red5.core.filenameresolver;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ 
/*    */ public class NormalFileNameResolver extends AFileNameResolver
/*    */ {
/*    */   public NormalFileNameResolver(HashMap<String, String> map)
/*    */   {
/*  8 */     super(map);
/*    */   }
/*    */ 
/*    */   public String getFileName(String inputString)
/*    */   {
/* 15 */     return resolve(inputString);
/*    */   }
/*    */ }

/* Location:           /Users/philippklose/red5-1.0.0-RC1/webapps/XVAProvider/WEB-INF/classes/
 * Qualified Name:     org.red5.core.filenameresolver.NormalFileNameResolver
 * JD-Core Version:    0.6.0
 */
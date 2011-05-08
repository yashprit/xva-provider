/*    */ package org.red5.core.filenameresolver;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import org.apache.log4j.Logger;
/*    */ import org.red5.core.encryption.KeyGen;
/*    */ import org.red5.server.api.IConnection;
/*    */ import org.red5.server.api.Red5;
/*    */ 
/*    */ public class CrypticFileNameResolver extends AFileNameResolver
/*    */ {
/* 16 */   private static final Logger log = Logger.getLogger(CrypticFileNameResolver.class.getName());
/*    */ 
/*    */   public CrypticFileNameResolver(HashMap<String, String> map)
/*    */   {
/* 20 */     super(map);
/*    */   }
/*    */ 
/*    */   public String getFileName(String inputString)
/*    */   {
/*    */     try
/*    */     {
/* 33 */       String path = KeyGen.getInstance().decrypt(inputString.getBytes());
/* 34 */       return path;
/*    */     }
/*    */     catch (Exception e) {
/* 37 */       IConnection currentConnection = Red5.getConnectionLocal();
/* 38 */       currentConnection.getRemoteAddress();
/* 39 */       e.printStackTrace();
/*    */     }
/* 41 */     return null;
/*    */   }
/*    */ }

/* Location:           /Users/philippklose/red5-1.0.0-RC1/webapps/XVAProvider/WEB-INF/classes/
 * Qualified Name:     org.red5.core.filenameresolver.CrypticFileNameResolver
 * JD-Core Version:    0.6.0
 */
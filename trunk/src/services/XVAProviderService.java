/*    */ package services;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ import javax.jws.WebService;
/*    */ import org.red5.core.encryption.KeyGen;
/*    */ import org.red5.core.encryption.WebServiceKeyPair;
/*    */ 
/*    */ @WebService
/*    */ public class XVAProviderService
/*    */ {
/* 15 */   private static final WebServiceKeyPair wKeyPair = new WebServiceKeyPair();
/*    */ 
/*    */   public byte[] encrypt(String url, byte[] keyphrase)
/*    */   {
/*    */     try
/*    */     {
/* 22 */       if (url != "") {
/* 23 */         byte[] bytes = KeyGen.getInstance().encrypt(url);
/*    */ 
/* 25 */         System.out.println(bytes.length);
/* 26 */         return bytes;
/*    */       }
/*    */ 
/* 38 */       return null;
/*    */     }
/*    */     catch (Exception e)
/*    */     {
/* 43 */       e.printStackTrace();
/* 44 */     }return null;
/*    */   }
/*    */ }

/* Location:           /Users/philippklose/red5-1.0.0-RC1/webapps/XVAProvider/WEB-INF/classes/
 * Qualified Name:     services.XVAProviderService
 * JD-Core Version:    0.6.0
 */
/*    */ package org.red5.core;
/*    */ 
/*    */ import javax.xml.ws.Endpoint;
/*    */ import org.red5.core.blacklist.BlackList;
/*    */ import org.red5.server.adapter.ApplicationAdapter;
/*    */ import org.red5.server.api.IConnection;
/*    */ import org.red5.server.api.IScope;
/*    */ import services.XVAProviderService;
/*    */ 
/*    */ public class Application extends ApplicationAdapter
/*    */ {
/*    */   public boolean appStart(IScope arg0)
/*    */   {
/* 41 */     Endpoint.publish(
/* 42 */       "http://localhost:5080/XVAProvider/XVAProviderService", 
/* 43 */       new XVAProviderService());
/*    */ 
/* 45 */     return super.appStart(arg0);
/*    */   }
/*    */ 
/*    */   public boolean connect(IConnection conn, IScope scope, Object[] params)
/*    */   {
/* 51 */     return !BlackList.getInstance().isBlackListed(conn.getRemoteAddress());
/*    */   }
/*    */ 
/*    */   public void disconnect(IConnection conn, IScope scope)
/*    */   {
/* 57 */     super.disconnect(conn, scope);
/*    */   }
/*    */ }

/* Location:           /Users/philippklose/red5-1.0.0-RC1/webapps/XVAProvider/WEB-INF/classes/
 * Qualified Name:     org.red5.core.Application
 * JD-Core Version:    0.6.0
 */
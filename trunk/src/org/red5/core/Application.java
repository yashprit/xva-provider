package org.red5.core;

import java.util.logging.Logger;

import javax.xml.ws.Endpoint;


import org.red5.core.blacklist.BlackList;
import org.red5.server.adapter.ApplicationAdapter;
import org.red5.server.api.IConnection;
import org.red5.server.api.IScope;
import services.XVAProviderService;

public class Application extends ApplicationAdapter {
	private static final Logger log = Logger.getLogger(Application.class.getName());
	public boolean appStart(IScope arg0) {
		Endpoint.publish(
				"http://localhost:5080/XVAProvider/XVAProviderService",
				new XVAProviderService());
		log.info("Started Server and publisehd Endpoint");
 		return super.appStart(arg0);
	}

	public boolean connect(IConnection conn, IScope scope, Object[] params) {
		return !BlackList.getInstance().isBlackListed(conn.getRemoteAddress());
	}

	public void disconnect(IConnection conn, IScope scope) {
		super.disconnect(conn, scope);
	}
}

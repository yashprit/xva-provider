package org.red5.core;

import java.util.HashMap;
import java.util.logging.Logger;

import javax.xml.ws.Endpoint;


import org.red5.core.blacklist.BlackList;
import org.red5.core.encryption.KeyGen;
import org.red5.core.encryption.WebServiceKeyPair;
import org.red5.server.adapter.ApplicationAdapter;
import org.red5.server.api.IConnection;
import org.red5.server.api.IScope;
import services.XVAProviderService;

public class Application extends ApplicationAdapter {
	
	private String ip;
	private String port;
	private int seksToSleep;
	private String secret;
	private String dirs;
	
	public static final HashMap<String, String> map = new HashMap<String,String>(500);
	
	public synchronized int getSeksToSleep() {
		return seksToSleep;
	}

	public synchronized void setSeksToSleep(int seksToSleep) {
		this.seksToSleep = seksToSleep;
	}

	private static final Logger log = Logger.getLogger(Application.class.getName());
	public boolean appStart(IScope arg0) {
		Endpoint.publish(
				"http://"+this.getIp()+":"+this.getPort()+"/XVAProvider/XVAProviderService",
				new XVAProviderService());
		KeyGen.SeksToSleep = this.getSeksToSleep();
		KeyGen.getInstance();
		WebServiceKeyPair.secret = getSecret();
		
		String[] tdir = this.getDirs().split(";");
		for (int i = 0; i < tdir.length; i++) {
			String[] splits = tdir[i].split(":=");
			log.info("Adding "+splits[0]+" to folder-map with:"+splits[1]);
			map.put(splits[0], splits[1]);
		}

		
		
		log.info( "Succesfully loaded with Secret");

		log.info("Started Server and publisehd Endpoint at:"+this.getIp()+":"+this.getPort());
 		return super.appStart(arg0);
	}

	public boolean connect(IConnection conn, IScope scope, Object[] params) {
		if(BlackList.getInstance().isBlackListed(conn.getRemoteAddress())){
		log.info("Refunsing connection: "+conn.getRemoteAddress());
		return false;
		}else {
		return true;
		}
		//return !BlackList.getInstance().isBlackListed(conn.getRemoteAddress());
	}

	public void disconnect(IConnection conn, IScope scope) {
		super.disconnect(conn, scope);
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getIp() {
		return ip;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getPort() {
		return port;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getSecret() {
		return secret;
	}

	public void setDirs(String dirs) {
		this.dirs = dirs;
	}

	public String getDirs() {
		return dirs;
	}
}

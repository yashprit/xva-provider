package org.red5.core;

import java.util.HashMap;
import java.util.logging.Logger;

import org.red5.core.blacklist.BlackList;
import org.red5.core.encryption.KeyGen;
import org.red5.core.encryption.WebServiceKeyPair;
import org.red5.core.filenameresolver.AFileNameResolver;
import org.red5.core.filenameresolver.CrypticFileNameResolver;
import org.red5.core.filenameresolver.NormalFileNameResolver;
import org.red5.server.api.IConnection;
import org.red5.server.api.IScope;
import org.red5.server.api.Red5;
import org.red5.server.api.stream.IStreamFilenameGenerator;


public class FileRedirector implements IStreamFilenameGenerator {
	private static final Logger log = Logger.getLogger(FileRedirector.class.getName());

	
	
	private static boolean inited = false;
	private boolean secured;
	private String secret;
	
	private int bannTime;
	private int Tries;
	private AFileNameResolver resolver = null;

	
	public synchronized boolean isSecured() {
		return this.secured;
	}

	public synchronized void setSecured(boolean secured) {
		this.secured = secured;
	}

	public synchronized String getSecret() {
		return this.secret;
	}

	public synchronized void setSecret(String secret) {
		this.secret = secret;
	}

	

	public synchronized int getBannTime() {
		return this.bannTime;
	}

	public synchronized void setBannTime(int bannTime) {
		this.bannTime = bannTime;
	}

	public synchronized int getTries() {
		return this.Tries;
	}

	public synchronized void setTries(int tries) {
		this.Tries = tries;
	}

	private void load() {
		HashMap<String,String> map = Application.map;
		
		KeyGen.getInstance();

		BlackList.bannehours = getBannTime();
		log.info("Bannhours :="+ this.getBannTime());
		BlackList.Tries = getTries();
		log.info("Tries till bann:= "+this.getTries());
		
		if (this.secured) {
			log.info("Bind secured File Path Provider");
			this.resolver = new CrypticFileNameResolver(map);
		} else {
			log.info( "Binding unsecured File Path Provider");
			this.resolver = new NormalFileNameResolver(map);
		}

		inited = true;
	}

	public String generateFilename(IScope arg0, String arg1,
			IStreamFilenameGenerator.GenerationType arg2) {
		if (!inited) {
			load();
		}
		return generateFilename(arg0, arg1, null, arg2);
	}

	public String generateFilename(IScope scope, String arg1, String arg2,
			IStreamFilenameGenerator.GenerationType arg3) {
		if (!inited) {
			load();
		}
		String ret = this.resolver.getFileName(arg1);
		if ((ret == null) || (ret == "")) {
			IConnection currentConnection = Red5.getConnectionLocal();
			BlackList.getInstance()
					.warnIp(currentConnection.getRemoteAddress());
			currentConnection.getScope().disconnect(currentConnection);
			currentConnection.close();
			log.info("Closing Connection and Warning Client");
			return "";
		}
		log.info("Path :="+ret);
		return ret;
	}

	public boolean resolvesToAbsolutePath() {
		return true;
	}
}

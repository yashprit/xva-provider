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

	private static final HashMap<String, String> map = new HashMap<String,String>(500);
	private String dirs;
	private static boolean inited = false;
	private boolean secured;
	private String secret;
	private int liveTime;
	private int bannTime;
	private int Tries;
	private AFileNameResolver resolver = null;

	public String getDirs() {
		return this.dirs;
	}

	public void setDirs(String dirs) {
		this.dirs = dirs;
	}

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

	public synchronized int getLiveTime() {
		return this.liveTime;
	}

	public synchronized void setLiveTime(int liveTime) {
		this.liveTime = liveTime;
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
		String[] tdir = this.dirs.split(";");
		for (int i = 0; i < tdir.length; i++) {
			String[] splits = tdir[i].split(":=");
			map.put(splits[0], splits[1]);
		}

		KeyGen.SeksToSleep = getLiveTime();
		log.info("Seks to sleep:="+ getLiveTime());
		KeyGen.getInstance();

		BlackList.bannehours = getBannTime();
		log.info("Bannhours :="+ this.getBannTime());
		BlackList.Tries = getTries();
		log.info("Tries till bann:= "+this.getTries());
		WebServiceKeyPair.secret = getSecret();
		log.info( "Succesfully loaded with Secret");
		System.out.println("Started");
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
		}
		log.info("Path :="+ret);
		return ret;
	}

	public boolean resolvesToAbsolutePath() {
		return true;
	}
}

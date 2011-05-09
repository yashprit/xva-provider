package org.red5.core.filenameresolver;

import java.util.HashMap;
import org.apache.log4j.Logger;
import org.red5.core.encryption.KeyGen;
import org.red5.server.api.IConnection;
import org.red5.server.api.Red5;

public class CrypticFileNameResolver extends AFileNameResolver {
	private static final Logger log = Logger
			.getLogger(CrypticFileNameResolver.class.getName());

	public CrypticFileNameResolver(HashMap<String, String> map) {
		super(map);
	}

	public String getFileName(String inputString) {
		try {
			String path = KeyGen.getInstance().decrypt(inputString.getBytes());
			return path;
		} catch (Exception e) {
			IConnection currentConnection = Red5.getConnectionLocal();
			currentConnection.getRemoteAddress();
			e.printStackTrace();
		}
		return null;
	}
}

package org.red5.core.filenameresolver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

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
			byte[] result = this.transformToSignedByte(inputString);
			String path = KeyGen.getInstance().decrypt(result);
			return super.resolve(path);			
		} catch (Exception e) {
			IConnection currentConnection = Red5.getConnectionLocal();
			currentConnection.getRemoteAddress();
			//log.warning(e.getStackTrace().toString());
			log.log(Level.SEVERE,null, e);
			log.info("Invalid Password");
		}
		return null;
	}
	
	private byte[] transformToSignedByte (String input){
		if(input.contains("."))
		input = input.substring(0,input.lastIndexOf("."));
		final ArrayList<Byte> list = new ArrayList<Byte>();
        for(int i=0;i<input.length()/2;i++){
            final String element = input.substring(i*2, i*2+2);
            final int k = Integer.parseInt(element, 16);
            byte val = (byte)(k-Byte.MAX_VALUE);
            list.add(val);
        }
        final byte[] ret = new byte[list.size()];
        for(int i=0;i<list.size();i++) ret[i] = list.get(i);
        return ret;
	}
}

package services;

import java.io.PrintStream;
import javax.jws.WebService;

import org.apache.log4j.Logger;
import org.red5.core.encryption.KeyGen;
import org.red5.core.encryption.WebServiceKeyPair;

@WebService
public class XVAProviderService {
	private static final WebServiceKeyPair wKeyPair = new WebServiceKeyPair();
	private static final Logger log = Logger.getLogger(XVAProviderService.class.getName());

	public byte[] encrypt(String url, byte[] keyphrase) {
		try {

			if (url != "" && wKeyPair.check(keyphrase)) {
				byte[] bytes = KeyGen.getInstance().encrypt(url);

				return bytes;
			}else {
				log.info("Keyphrase was wrong, maybe because of an old cert?");
			}

			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}

package services;

import java.io.PrintStream;
import java.util.logging.Logger;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;


import org.red5.core.encryption.KeyGen;
import org.red5.core.encryption.WebServiceKeyPair;

@WebService
public class XVAProviderService {
	private static final WebServiceKeyPair wKeyPair = new WebServiceKeyPair();
	private static final Logger log = Logger.getLogger(XVAProviderService.class.getName());

	
	@WebMethod(operationName="encrypt")
	public byte[] encrypt(@WebParam(name="arg0")String url, @WebParam(name="arg1")byte[] keyphrase) {
		log.info("Getting Request with : "+url+" and :"+keyphrase);
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

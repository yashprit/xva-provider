        package services;
        
        import java.io.PrintStream;
        import javax.jws.WebService;
        import org.red5.core.encryption.KeyGen;
        import org.red5.core.encryption.WebServiceKeyPair;
        
        @WebService
        public class XVAProviderService
        {
       private static final WebServiceKeyPair wKeyPair = new WebServiceKeyPair();
        
          public byte[] encrypt(String url, byte[] keyphrase)
          {
            try
            {
            if (url != "") {
              byte[] bytes = KeyGen.getInstance().encrypt(url);
        
              System.out.println(bytes.length);
              return bytes;
              }
        
            return null;
            }
            catch (Exception e)
            {
            e.printStackTrace();
          }return null;
          }
       }

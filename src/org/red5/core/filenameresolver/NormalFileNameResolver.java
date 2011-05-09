      package org.red5.core.filenameresolver;
      
      import java.util.HashMap;
      
      public class NormalFileNameResolver extends AFileNameResolver
      {
        public NormalFileNameResolver(HashMap<String, String> map)
        {
        super(map);
        }
      
        public String getFileName(String inputString)
        {
        return resolve(inputString);
        }
     }

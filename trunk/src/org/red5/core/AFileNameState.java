       package org.red5.core;
       
       import java.util.HashMap;
       
       public abstract class AFileNameState
       {
         protected HashMap<String, String> dirs;
       
         protected AFileNameState(HashMap<String, String> dirs)
         {
         this.dirs = dirs;
         }
       
         private boolean isStringEncrypted(String inputString) {
         int lsharp = inputString.lastIndexOf("#");
         if (lsharp < 3) return false;
         String Secret = inputString.substring(lsharp, inputString.length());
       
         return Secret.length() == 12;
         }
       
         public abstract String getFileName(String paramString);
       }




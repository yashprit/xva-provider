package org.red5.core.filenameresolver;

import java.util.HashMap;

public abstract class AFileNameResolver {
	protected HashMap<String, String> dirs;

	protected AFileNameResolver(HashMap<String, String> dirs) {
		this.dirs = dirs;
	}

	private boolean isStringEncrypted(String inputString) {
		int lsharp = inputString.lastIndexOf("#");
		if (lsharp < 3)
			return false;
		String Secret = inputString.substring(lsharp, inputString.length());

		return Secret.length() == 12;
	}

	protected String resolve(String arg1) {
		int firstSlash = arg1.indexOf("/");
		String dir = arg1.substring(0, firstSlash);
		if ((dir == null) || (dir == "")) {
			return null;
		}
		String folder = (String) this.dirs.get(dir);
		if ((folder == null) || (folder == "")) {
			return null;
		}

		String filePath = folder + arg1.substring(firstSlash + 1);

		return filePath;
	}
	
	public static String resolve(HashMap<String,String> dirs,String arg1){
		int firstSlash = arg1.indexOf("/");
		String dir = arg1.substring(0, firstSlash);
		if ((dir == null) || (dir == "")) {
			return null;
		}
		String folder = (String) dirs.get(dir);
		if ((folder == null) || (folder == "")) {
			return null;
		}

		String filePath = folder + arg1.substring(firstSlash + 1);

		return filePath;
	}

	public abstract String getFileName(String paramString);
}

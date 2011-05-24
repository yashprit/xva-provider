package servlets;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpUtils;

import org.red5.core.Application;
import org.red5.core.blacklist.BlackList;
import org.red5.core.encryption.KeyGen;
import org.red5.core.filenameresolver.AFileNameResolver;

/**
 * Servlet implementation class ProgressiveHttpStreaming
 */
public class ProgressiveHttpStreaming extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final Logger log = Logger
			.getLogger(ProgressiveHttpStreaming.class.getName());

	/**
	 * Default constructor.
	 */
	public ProgressiveHttpStreaming() {
		// TODO Auto-generated constructor stub

	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		final String query = request.getQueryString();
		log.info("Client requests " + query);
		if (query == null)
			return;
		if (query == "")
			return;
		if (query.length() < 5)
			return;

		String path = null;
		try {
			path = KeyGen.getInstance().decrypt(transformToSignedByte(query));
		} catch (Exception e) {
			BlackList.getInstance().warnIp(request.getRemoteAddr());
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.warning(e.toString());
			return;
		}
		if (path == null)
			return;
		final String fpath = AFileNameResolver.resolve(Application.map, path);
		if (fpath == null)
			return;
		final File file = new File(fpath);
		log.info("Returning: " + fpath);
		prepareResponseFor(response, file);
		streamFileTo(response, file);
	}

	private byte[] transformToSignedByte(String input) {
		if (input.contains(".")) {
			input = input.substring(0, input.lastIndexOf("."));
		}
		final ArrayList<Byte> list = new ArrayList<Byte>();
		for (int i = 0; i < input.length(); i += 2) {
			final String element = input.substring(i, i + 2);
			final int k = Integer.parseInt(element, 16);
			byte val = (byte) (k - Byte.MAX_VALUE);
			list.add(val);
		}
		final byte[] ret = new byte[list.size()];
		for (int i = 0; i < list.size(); i++)
			ret[i] = list.get(i);
		return ret;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		this.doGet(request, response);
	}

	private static final int BUFFER_SIZE = 16384;

	private void streamFileTo(HttpServletResponse response, File file) throws IOException{
		OutputStream os = null;
		FileInputStream fis = null;
		try {
		os = response.getOutputStream();
		fis = new FileInputStream(file);
		byte[] buffer = new byte[BUFFER_SIZE];
		int bytesRead = 0;
		while ((bytesRead = fis.read(buffer)) > 0) {
			os.write(buffer, 0, bytesRead);
		}
		}catch (Exception e){
			log.warning(e.toString());
		}finally {
			if(os != null){
				os.flush();				
			}
			if(fis != null){
				fis.close();
			}
			
		}
		
	}

	private static final String default_mimeType = "application/octet-stream";
	private static final String video_mimeType = "video/mp4";
	private static final String video_ogg = "video/ogg";
	private static final String video_webm ="video/webm";

	private void prepareResponseFor(HttpServletResponse response, File file) {
		StringBuilder type = new StringBuilder("attachment; filename=");
		type.append(file.getName());
		response.setContentLength((int) file.length());
		
		response.setContentType(video_mimeType);
		setMimeType(file, response);
		
		response.setHeader("Content-Disposition", type.toString());
	}
	
	private void setMimeType (File file,HttpServletResponse response){
		final String path = file.getPath().substring(file.getPath().lastIndexOf("."));
		if(path != null){
			if(path.contains("ogv")){
				response.setContentType(video_ogg);
				return;
			}if(path.contains("webm")){
				response.setContentType(video_webm);
				return;
			}if(path.contains("mp4")){
				response.setContentType(video_mimeType);
				return;
			}
		}
		
	}

}

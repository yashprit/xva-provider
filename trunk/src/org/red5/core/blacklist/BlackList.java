package org.red5.core.blacklist;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import org.apache.log4j.Logger;

public class BlackList implements Runnable {
	private static final Logger log = Logger.getLogger(BlackList.class
			.getName());

	private HashMap<String, Date> banned = new HashMap<String,Date>(10000);
	private HashMap<String, Integer> warned = new HashMap<String,Integer>(10000);

	private static BlackList instance = null;

	private static final Long Timeout = Long.valueOf(150000L);
	public static int Tries;
	public static int bannehours = 1;

	private BlackList() {
		new Thread(this).start();
	}

	public static BlackList getInstance() {
		if (instance == null)
			instance = new BlackList();
		return instance;
	}

	public boolean warnIp(String ip) {
		if (this.warned.containsKey(ip)) {
			int level = ((Integer) this.warned.get(ip)).intValue();
			level++;
			if (level <= Tries) {
				log.info("Putting ip: " + ip + " into warning map with level: "
						+ level);
				this.warned.put(ip, new Integer(level));
				return true;
			}
			log.info("Banning ip: " + ip + " after " + Tries + " tries");
			this.warned.remove(ip);
			this.banned.put(ip, new Date());
			return false;
		}

		this.warned.put(ip, Integer.valueOf(0));
		return true;
	}

	public boolean isBlackListed(String ip) {
		return this.banned.containsKey(ip);
	}

	public void run() {
		while (true)
			try {
				final int bannout = bannehours * 60 * 60 * 1000;
				final Date now = new Date();
				final Iterator iter = this.banned.keySet().iterator();
				String current;
				while (iter.hasNext()) {
					current = (String) iter.next();
					final Date entryDate = (Date) this.banned.get(current);
					final Long dif = Long
							.valueOf(now.getTime() - entryDate.getTime());
					if (dif.longValue() > bannehours) {
						log.info("Removing Ip " + current + " from Blacklist");
						this.banned.remove(current);
					}
				}

				Thread.sleep(Timeout.longValue());
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	}
}

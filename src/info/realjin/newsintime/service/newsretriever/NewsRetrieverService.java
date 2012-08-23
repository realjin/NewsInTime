package info.realjin.newsintime.service.newsretriever;

import info.realjin.newsintime.domain.Collection;
import info.realjin.newsintime.domain.NewsList;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class NewsRetrieverService {
	private NewsRetrieverServiceThread thread;
	private boolean threadRunning;

	public void sendMsg(Message msg) {
		handler.sendMessage(msg);
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// get the bundle and extract data by key
			Bundle b = msg.getData();
			String msgType = b.getString("type");
			if (msgType.equals("changeColItem") || msgType.equals("changeCol")) {
				// end the loop
				NewsRetrieverService.this.stop();

				if (msgType.equals("changeColItem")) {
					// start new retrieval looping
					// TODO: deleted!!!
					// String url = b.getString("content");
					// Log.e("===NRS===", "changeColItem: content=" + url);
					// NewsRetrieverService.this
					// .startByNewColItem(new CollectionItem(url));
				}

				// synchronized (NewsRetrieverService.this) {
				// NewsRetrieverService.this.notifyAll();
				// thread.enabled = false;
				// }

			} else {
			}

		}
	};

	/**
	 * @param nl
	 * @param url
	 *            initial url
	 */
	public NewsRetrieverService(NewsList nl, Collection coll) {
		thread = new NewsRetrieverServiceThread(this, nl, coll);
	}

	public void start() {
		threadRunning = true;
		thread.setEnabled(true);
		new Thread(thread).start();
	}

	public void restartByNewColItem(Collection coll) {
		//0. stop old thread
		stop();
		
		// 1.clear list
		thread.getNl().clearAll();

		// 2. start retrieving
		thread.setColl(coll);
		
		start();
	}

	public void stop() {
		if (!threadRunning) {
			Log.e("===NRS===", "stop error: thead not running!");
			return;
		}
		threadRunning = false;
		synchronized (this) {
			this.notifyAll();
			thread.setEnabled(false);
		}
	}

	/**
	 * @param s
	 *            pubdate
	 * @return
	 */
	public static Date parsePubDate(String s) {
		String format = null;

		// the substring before "," is Day of Week
		int nComma = s.indexOf(',');
		if (nComma != -1) {
			s = s.substring(nComma + 1);
		}

		// the substring at the end is always GMT
		int nGMT = s.indexOf("GMT");
		if (nGMT != -1) {
			s = s.substring(0, nGMT);
		}

		System.out.println("===1===");

		// trim
		s = s.trim();

		// get time part as s2
		String s2;
		int n1, n2;
		n1 = s.indexOf(':');
		n2 = s.lastIndexOf(':');
		if (n1 != -1 && n2 != -1 && n2 - n1 == 3) {
			s2 = s.substring(n1 - 2, n2 + 3);
		} else {
			System.out.println("parsePubDate: n2 and n1 not valid.");
			return null;
		}

		System.out.println("===2===");
		// get date part as s1
		String s1;
		s1 = s.substring(0, n1 - 2);
		s1 = s1.trim();
		String[] tokens = s1.split(" ");
		System.out.println("===3===token length=" + tokens.length);
		if (tokens.length == 3) {
			if (tokens[0].length() == 1) {
				tokens[0] = '0' + tokens[0];
			}
			if (tokens[1].matches(".*\\d.*")) {// contains digits
				format = "yyyy MM dd HH:mm:ss";
			} else {
				format = "yyyy MMMMM dd HH:mm:ss";
			}
		} else {
			return null;
		}
		s1 = tokens[2] + ' ' + tokens[1] + ' ' + tokens[0];
		System.out.println("===4===fmt=" + format);

		String modified = s1 + ' ' + s2;
		System.out.println("===5=== modified=" + modified);
		SimpleDateFormat _df = new SimpleDateFormat(format, Locale.US);
		// System.out.println("sample--->" + _df.format(new Date()));
		Date d = null;
		try {
			d = _df.parse(modified);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}

		return d;
	}

	public static void main(String[] args) {
		System.out.println("start");
		NewsRetrieverService nrs = new NewsRetrieverService(null, null);
		// nrs.getFeed("http://cn.wsj.com.feedsportal.com/c/33121/f/538760/index.rss");
		// nrs.getFeed("http://rss.sina.com.cn/news/marquee/ddt.xml");
		// try {
		// nrs.getFeed(new FileInputStream("I:\\Tangdun\\index.rss"));
		// } catch (FileNotFoundException e) {
		// e.printStackTrace();
		// }
	}
}

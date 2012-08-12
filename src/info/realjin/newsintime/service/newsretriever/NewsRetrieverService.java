package info.realjin.newsintime.service.newsretriever;

import info.realjin.newsintime.domain.Collection;
import info.realjin.newsintime.domain.CollectionItem;
import info.realjin.newsintime.domain.News;
import info.realjin.newsintime.domain.NewsList;
import info.realjin.newsintime.domain.RssFeed;
import info.realjin.newsintime.domain.RssItem;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

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

	class NewsRetrieverServiceThread implements Runnable {
		NewsRetrieverService nrs;
		private NewsList nl;
		private Collection coll;
		// private String url;

		private boolean enabled;

		public NewsRetrieverServiceThread(NewsRetrieverService nrs,
				NewsList nl, Collection coll) {
			this.nrs = nrs;
			this.nl = nl;
			this.coll = coll;
			// TODO: temp!!!!!!!!!!
			// this.url = coll.getItems().get(0).getUrl();
			this.enabled = true;
		}

		public void run() {
			try {
				loop();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		/**
		 * @throws InterruptedException
		 * 
		 */
		public void loop() throws InterruptedException {
			while (enabled) {
				List<News> newsList, unarrangedNewsList;
				List<RssFeed> feedList = getFeedList(coll);
				unarrangedNewsList = feedListToNews(feedList);
				newsList = filterAndRearrangeNews(unarrangedNewsList);
				nl.addNewsList(newsList);

				// RssFeed feed = getFeedByUrl(url);
				// for (Object o : feed.getAllItems()) {
				// RssItem ri = (RssItem) o;
				// Log.e("===NRSERVICE===", "all:" + feed.getAllItems().size()
				// + ", news title= " + ri.getTitle());
				//
				// String text = ri.getTitle() + " ###";
				// Date pubDate;
				// if (ri.getPubDate() == null) {
				// // TODO: !!!mmm set as now!!!
				// pubDate = new Date();
				// } else {
				// pubDate = NewsRetrieverService.parsePubDate(ri
				// .getPubDate());
				// }
				//
				// News n = new News(text, pubDate);
				// // mmmmmm
				// nl.addNews(n);
				// }
				// Thread.sleep(10 * 1000); // TODO: should be wait
				synchronized (nrs) {
					if (enabled) {
						break;
					}
					nrs.wait(10 * 1000);
				}

				// if source changed

			}
			Log.e("===NRS===", "loop() end");
		}

		private List<News> filterAndRearrangeNews(List<News> n) {
			// TODO: temp
			return n;
		}

		private List<News> feedListToNews(List<RssFeed> rfList) {
			List<News> newsList = new ArrayList<News>();
			for (RssFeed rf : rfList) {
				for (Object o : rf.getAllItems()) {
					RssItem ri = (RssItem) o;
					String text = ri.getTitle() + " ###";
					Date pubDate;
					if (ri.getPubDate() == null) {
						// TODO: !!!mmm set as now!!!
						pubDate = new Date();
					} else {
						pubDate = NewsRetrieverService.parsePubDate(ri
								.getPubDate());
					}

					News n = new News(text, pubDate);

					newsList.add(n);
				}
			}

			return newsList;
		}

		private List<RssFeed> getFeedList(Collection c) {
			List<RssFeed> rfList = new ArrayList<RssFeed>();
			if (c == null || c.getItems() == null || c.getItems().size() == 0) {
				Log.e("===NRS===", "getFeedList error: arg 0 null!");
				return null;
			}
			for (CollectionItem ci : c.getItems()) {
				// TODO: !!!!!!!!!!!!!!!!!!!!!! no exception handling
				RssFeed rf = getFeedByUrl(ci.getUrl());
				rfList.add(rf);
			}

			return rfList;
		}

		private RssFeed getFeedByUrl(String urlString) {
			URL url;
			InputStream is;
			try {
				url = new URL(urlString);
				is = url.openStream();
			} catch (MalformedURLException e) {
				Log.e("===NRSERVICE===", "getFeedByUrl url malformed! ("
						+ urlString + ")");
				return null;
				// e.printStackTrace();
			} catch (IOException e) {
				// e.printStackTrace();
				Log.e("===NRSERVICE===", "getFeedByUrl ioException! ("
						+ urlString + ")" + e.getMessage());
				return null;
			}
			return getFeed(is);
		}

		private RssFeed getFeed(InputStream is) {
			try {
				if (is == null) {
					return null;
				}

				RssFeed feed = new RssFeed();

				SAXReader reader = new SAXReader();
				Document doc = reader.read(is);

				Element root = doc.getRootElement();

				// TODO: assume one channel only!
				Element channel = root.element("channel");

				Iterator it = channel.elementIterator("item");
				while (it.hasNext()) {
					Element e = (Element) it.next();
					RssItem ri = new RssItem();
					ri.setTitle(e.elementTextTrim("title"));
					ri.setCategory(e.elementTextTrim("category"));
					ri.setDescription(e.elementTextTrim("description"));
					String sDate = e.elementTextTrim("pubDate");
					if (sDate == null || sDate.length() == 0) {
						ri.setPubDate(null);
					} else {
						ri.setPubDate(sDate);
					}
					ri.setLink(e.elementTextTrim("link"));

					feed.addItem(ri);

					System.out
							.println("==>" + e.element("title").getTextTrim());
				}

				return feed;

			} catch (Exception ee) {
				return null;
			}

		}

		class RSSHandler extends DefaultHandler {
			RssFeed rssFeed;
			RssItem rssItem;
			String lastElementName = "";
			final int RSS_TITLE = 1;
			final int RSS_LINK = 2;
			final int RSS_DESCRIPTION = 3;
			final int RSS_CATEGORY = 4;
			final int RSS_PUBDATE = 5;
			int currentstate = 0;

			public RSSHandler() {
			}

			// 调用顺序跟代码位置无关
			public RssFeed getFeed() {
				return rssFeed;
			}

			// 开始文档时调用
			public void startDocument() throws SAXException {
				System.out.println("startDocument");

				// 实例化两个对象
				rssFeed = new RssFeed();
				rssItem = new RssItem();
			}

			public void endDocument() throws SAXException {
			}

			public void startElement(String namespaceURI, String localName,
					String qName, Attributes atts) throws SAXException {
				// System.out.println("localname: " + localName + ", qName=" +
				// qName);
				if (qName.equals("channel")) {
					currentstate = 0;
					return;
				}
				if (qName.equals("item")) {
					rssItem = new RssItem();
					return;
				}
				if (qName.equals("title")) {
					currentstate = RSS_TITLE;
					return;
				}
				if (qName.equals("description")) {
					currentstate = RSS_DESCRIPTION;
					return;
				}
				if (qName.equals("link")) {
					currentstate = RSS_LINK;
					return;
				}
				if (qName.equals("category")) {
					currentstate = RSS_CATEGORY;
					return;
				}
				if (qName.equals("pubDate")) {
					currentstate = RSS_PUBDATE;
					return;
				}
				currentstate = 0;
			}

			public void endElement(String namespaceURI, String localName,
					String qName) throws SAXException {
				// 如果解析一个item节点结束，就将rssItem添加到rssFeed中。
				if (qName.equals("item")) {
					rssFeed.addItem(rssItem);
					return;
				}
			}

			public void characters(char ch[], int start, int length) {
				String theString = new String(ch, start, length);
				switch (currentstate) {
				case RSS_TITLE:
					rssItem.setTitle(theString);
					currentstate = 0;
					break;
				case RSS_LINK:
					rssItem.setLink(theString);
					currentstate = 0;
					break;
				case RSS_DESCRIPTION:
					rssItem.setDescription(theString);
					currentstate = 0;
					break;
				case RSS_CATEGORY:
					rssItem.setCategory(theString);
					currentstate = 0;
					break;
				case RSS_PUBDATE:
					rssItem.setPubDate(theString);
					currentstate = 0;
					break;
				default:
					return;
				}
			}
		}

		public Collection getColl() {
			return coll;
		}

		public void setColl(Collection coll) {
			this.coll = coll;
		}

	}

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
		new Thread(thread).start();
	}

	public void startByNewColItem(Collection coll) {
		// 1.clear list
		thread.nl.clearAll();

		// 2. start retrieving

		threadRunning = true;
		// thread.setUrl(ci.getUrl());
		thread.setColl(coll);
		new Thread(thread).start();
	}

	public void stop() {
		if (!threadRunning) {
			Log.e("===NRS===", "stop error: thead not running!");
			return;
		}
		threadRunning = false;
		synchronized (this) {
			this.notifyAll();
			thread.enabled = false;
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

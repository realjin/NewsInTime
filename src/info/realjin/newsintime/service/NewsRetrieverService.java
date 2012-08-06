package info.realjin.newsintime.service;

import info.realjin.newsintime.domain.News;
import info.realjin.newsintime.domain.NewsList;
import info.realjin.newsintime.domain.RssFeed;
import info.realjin.newsintime.domain.RssItem;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

public class NewsRetrieverService implements Runnable {
	private NewsList nl;
	private String url;
	private boolean enabled;

	/**
	 * @param nl
	 * @param url
	 *            initial url
	 */
	public NewsRetrieverService(NewsList nl, String url) {
		this.nl = nl;
		this.url = url;
		this.enabled = true;
	}

	public void run() {
		// TODO Auto-generated method stub
		loop();
	}

	/**
	 * 
	 */
	public void loop() {
		while (enabled) {
			try {
				RssFeed feed = getFeedByUrl(url);
				for (Object o : feed.getAllItems()) {
					RssItem ri = (RssItem) o;
					Log.e("===NRSERVICE===", "all:" + feed.getAllItems().size()
							+ ", news title= " + ri.getTitle());

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
					// mmmmmm
					nl.addNews(n);
				}
				Thread.sleep(10 * 1000); // TODO: should be wait
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			// if source changed

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
			Log.e("===NRSERVICE===", "getFeedByUrl ioException! (" + urlString
					+ ")" + e.getMessage());
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

				System.out.println("==>" + e.element("title").getTextTrim());
			}

			return feed;

		} catch (Exception ee) {
			return null;
		}

	}

	private RssFeed getFeedOld(InputStream is) {
		try {
			if (is == null) {
				return null;
			}
			// URL url = new URL(urlString);
			// 新建一个工厂类
			SAXParserFactory factory = SAXParserFactory.newInstance();
			// 工厂类产生出一个sax的解析类
			SAXParser parser = factory.newSAXParser();
			XMLReader xmlreader = parser.getXMLReader();

			RSSHandler rssHandler = new RSSHandler();
			xmlreader.setContentHandler(rssHandler);
			// InputSource is = new InputSource(url.openStream());
			InputSource isrc = new InputSource(is);
			xmlreader.parse(isrc);
			// // // 调用解析的类
			// return rssHandler.getFeed();
			RssFeed feed = rssHandler.getFeed();
			System.out.println("after get feed: " + feed.getAllItems().size());
			return feed;
		} catch (Exception ee) {
			return null;
		}
	}

	public static void main(String[] args) {
		System.out.println("start");
		NewsRetrieverService nrs = new NewsRetrieverService(null, null);
		// nrs.getFeed("http://cn.wsj.com.feedsportal.com/c/33121/f/538760/index.rss");
		// nrs.getFeed("http://rss.sina.com.cn/news/marquee/ddt.xml");
		try {
			nrs.getFeed(new FileInputStream("I:\\Tangdun\\index.rss"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
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

}

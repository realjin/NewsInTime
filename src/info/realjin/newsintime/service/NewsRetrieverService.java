package info.realjin.newsintime.service;

import info.realjin.newsintime.domain.NewsList;
import info.realjin.newsintime.domain.RssFeed;
import info.realjin.newsintime.domain.RssItem;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

public class NewsRetrieverService extends Thread {
	private NewsList nl;
	private String url;
	private boolean enabled;

	public NewsRetrieverService(NewsList nl) {
		this.nl = nl;
		this.enabled = true;
	}

	/**
	 * @param ulr
	 *            initial url
	 */
	public void start(String ulr) {
		this.url = url;
		loop();
	}

	/**
	 * 
	 */
	public void loop() {
		while (enabled) {
			try {
				this.wait(10 * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			// if source changed

		}
	}

	private RssFeed getFeedByUrl(String urlString) {
		URL url;
		InputStream is;
		try {
			url = new URL(urlString);
			is = url.openStream();
		} catch (MalformedURLException e) {
			System.out.println("getFeedByUrl url malformed! (" + urlString
					+ ")");
			return null;
			// e.printStackTrace();
		} catch (IOException e) {
			// e.printStackTrace();
			System.out.println("getFeedByUrl ioException! (" + urlString + ")");
			return null;
		}
		return getFeed(is);
	}

	private RssFeed getFeed(InputStream is) {
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
		NewsRetrieverService nrs = new NewsRetrieverService();
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

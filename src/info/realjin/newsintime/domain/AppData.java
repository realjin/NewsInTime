package info.realjin.newsintime.domain;

import info.realjin.newsintime.NewsInTimeApp;
import info.realjin.newsintime.dao.CollectionDao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppData {
	private NewsList newsList;
	private NewsInTimeApp app;
	private int scrWidth;
	private int scrHeight;
	// private PredefinedDataManagerService pdData;

	// predefined data
	private List<PredefinedCategory> pdCategoryList;
	private Map<String, String> pdCategoryMap;
	private List<PredefinedCollectionItem> pdCiList;

	public AppData(NewsInTimeApp app) {
		// this.pdData = pdData;
		newsList = new NewsList();
		this.app = app;

		// init predefined data
		pdCategoryList = new ArrayList<PredefinedCategory>();
		pdCategoryMap = new HashMap<String, String>();
		pdCiList = new ArrayList<PredefinedCollectionItem>();
	}

	// proxy methods
	// TODO: DB!!!
	public List<Collection> getCollectionListNewDeprecated() {
		CollectionDao dao = app.getDbmService().getCollectionDao();
		List<Collection> collList = dao.getAllCollectionsWithItems();
		return collList;
	}

	public List<Collection> getCollectionListOld() {
		List<Collection> collList = new ArrayList<Collection>();
		Collection coll1 = new Collection();
		coll1.setId(1);
		coll1.setName("sina general");
		CollectionItem ci1 = new CollectionItem(
				"http://rss.sina.com.cn/roll/sports/hot_roll.xml");
		ci1.setName("sina general");
		coll1.getItems().add(ci1);

		Collection coll2 = new Collection();
		coll2.setId(2);
		coll2.setName("sina sports");
		CollectionItem ci2 = new CollectionItem(
				"http://rss.sina.com.cn/roll/sports/hot_roll.xml");
		ci2.setName("sina sports");
		coll2.getItems().add(ci2);

		Collection coll3 = new Collection();
		coll3.setId(3);
		coll3.setName("sina finance");
		CollectionItem ci3 = new CollectionItem(
				"http://rss.sina.com.cn/roll/finance/hot_roll.xml");
		ci3.setName("sina finance");
		coll3.getItems().add(ci3);

		Collection coll4 = new Collection();
		coll4.setId(4);
		coll4.setName("nyt global");
		CollectionItem ci4 = new CollectionItem(
				"http://www.nytimes.com/services/xml/rss/nyt/International.xml");
		ci4.setName("nyt global");
		coll4.getItems().add(ci4);

		Collection coll5 = new Collection();
		coll5.setId(5);
		coll5.setName("wsj china");
		CollectionItem ci5 = new CollectionItem(
				"http://chinese.wsj.com/gb/rssbch.xml");
		ci5.setName("wsj china");
		coll5.getItems().add(ci5);

		Collection coll6 = new Collection();
		coll6.setId(6);
		coll6.setName("wsj finance");
		CollectionItem ci6 = new CollectionItem(
				"http://chinese.wsj.com/gb/rss01.xml");
		ci6.setName("wsj finance");
		coll6.getItems().add(ci6);

		collList.add(coll1);
		collList.add(coll2);
		collList.add(coll3);
		collList.add(coll4);
		collList.add(coll5);
		collList.add(coll6);
		return collList;
	}

	// proxy methods
	// TODO: DB!!!
	/*
	 * public Collection getCollectionById(String id, List<Collection> collList)
	 * { for (Collection c : collList) { if (c.getId().equals(id)) { return c; }
	 * } return null; }
	 */

	public List<PredefinedCollectionItem> getPredefinedCollectionItemList() {
		return pdCiList;
	}

	public List<PredefinedCollectionItem> getPredefinedCollectionItemListByCat(
			String catId) {
		List<PredefinedCollectionItem> listByCat = new ArrayList<PredefinedCollectionItem>();
		for (PredefinedCollectionItem ci : pdCiList) {
			if (ci.getCategoryId().equals(catId)) {
				listByCat.add(ci);
			}
		}

		return listByCat;
	}

	// proxy methods
	// TODO: DB!!!
	public List<CollectionItem> getPredefinedCollectionItemListOld() {
		List<CollectionItem> list = new ArrayList<CollectionItem>();
		PredefinedCollectionItem a = new PredefinedCollectionItem();
		a.setUrl("http://news.qq.com/newsgn/rss_newsgn.xml");
		a.setName("腾讯国内新闻");
		PredefinedCollectionItem b = new PredefinedCollectionItem();
		b.setUrl("http://news.163.com/special/00011K6L/rss_hotnews.xml");
		b.setName("网易深度新闻");
		list.add(a);
		list.add(b);

		return list;
	}

	// ----- setters and getters
	public NewsList getNewsList() {
		return newsList;
	}

	public void setNewsList(NewsList newsList) {
		this.newsList = newsList;
	}

	public int getScrWidth() {
		return scrWidth;
	}

	public void setScrWidth(int scrWidth) {
		this.scrWidth = scrWidth;
	}

	public int getScrHeight() {
		return scrHeight;
	}

	public void setScrHeight(int scrHeight) {
		this.scrHeight = scrHeight;
	}

	public String toString() {
		return "[APPDATA] scr: " + scrWidth + " X " + scrHeight;
	}

	public List<PredefinedCategory> getPdCategoryList() {
		return pdCategoryList;
	}

	public void setPdCategoryList(List<PredefinedCategory> pdCategoryList) {
		this.pdCategoryList = pdCategoryList;
	}

	public Map<String, String> getPdCategoryMap() {
		return pdCategoryMap;
	}

	public void setPdCategoryMap(Map<String, String> pdCategoryMap) {
		this.pdCategoryMap = pdCategoryMap;
	}

	public List<PredefinedCollectionItem> getPdCiList() {
		return pdCiList;
	}

	public void setPdCiList(List<PredefinedCollectionItem> pdCiList) {
		this.pdCiList = pdCiList;
	}

}

package info.realjin.newsintime.domain;

import java.util.ArrayList;
import java.util.List;

public class AppData {
	private NewsList newsList;
	int scrWidth;
	int scrHeight;

	public AppData() {
		newsList = new NewsList();
	}

	// proxy methods
	public List<Collection> getCollectionList() {
		List<Collection> collList = new ArrayList<Collection>();
		Collection coll1 = new Collection();
		coll1.setId("1");
		coll1.setName("sina general");
		CollectionItem ci1 = new CollectionItem(
				"http://rss.sina.com.cn/roll/sports/hot_roll.xml");
		ci1.setName("sina general");
		coll1.getItems().add(ci1);
		

		Collection coll2 = new Collection();
		coll2.setId("2");
		coll2.setName("sina sports");
		CollectionItem ci2 = new CollectionItem(
				"http://rss.sina.com.cn/roll/sports/hot_roll.xml");
		ci2.setName("sina sports");
		coll2.getItems().add(ci2);

		Collection coll3 = new Collection();
		coll3.setId("3");
		coll3.setName("sina finance");
		CollectionItem ci3 = new CollectionItem(
				"http://rss.sina.com.cn/roll/finance/hot_roll.xml");
		ci3.setName("sina finance");
		coll3.getItems().add(ci3);
		
		
		collList.add(coll1);
		collList.add(coll2);
		collList.add(coll3);
		return collList;
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

}

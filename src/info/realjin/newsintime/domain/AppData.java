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
		coll1.setName("cu1");
		Collection coll2 = new Collection();
		coll2.setId("2");
		coll2.setName("cstomized2");
		Collection coll3 = new Collection();
		coll3.setId("3");
		coll3.setName("cu3");
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

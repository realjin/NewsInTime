package info.realjin.newsintime.domain;

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

package info.realjin.newsintime.domain;

public class AppData {
	private NewsList newsList;

	public AppData() {
		newsList = new NewsList();
	}

	// ----- setters and getters
	public NewsList getNewsList() {
		return newsList;
	}

	public void setNewsList(NewsList newsList) {
		this.newsList = newsList;
	}

}

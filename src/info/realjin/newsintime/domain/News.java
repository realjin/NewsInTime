package info.realjin.newsintime.domain;

import java.util.Comparator;

public class News implements Comparable<News> {
	private long id;
	private String text;

	private enum Category {

	}

	Category cat;

	private static long idCounter;

	static {
		idCounter = 1;
	}

	public News(String text) {
		id = idCounter++;
		this.text = text;
	}

	public String toString() {
		return text;
	}

	// -------setters and getters
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Category getCat() {
		return cat;
	}

	public void setCat(Category cat) {
		this.cat = cat;
	}

	public int compareTo(News another) {
		long diff;
		diff = this.getId() - another.getId();
		if (diff == 0) {
			return 0;
		} else if (diff > 0) {
			return 1;
		} else {
			return -1;
		}
	}
}

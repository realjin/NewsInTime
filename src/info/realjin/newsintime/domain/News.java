package info.realjin.newsintime.domain;

import java.util.Date;

import android.util.Log;

public class News implements Comparable<News> {
	private String id; // id = time+subid
	private String text;

	private Date time;
	private long subid;

	private enum Category {

	}

	Category cat;

	// private static long idCounter;

	static {
		// idCounter = 1;
	}

	public News(String text, Date time) {
		// id = idCounter++;
		this.text = text;
		this.time = time;
	}

	public String toString() {
		return text;
	}

	// -------setters and getters
	public String getId() {
		return id;
	}

	public void setId(String id) {
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

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public long getSubid() {
		return subid;
	}

	public void setSubid(long subid) {
		this.subid = subid;
	}

	public int compareTo(News another) {
		if (this.time.before(another.time)) {
			return -1;
		} else if (this.time.after(another.time)) {
			return 1;
		} else {
			long diff = this.subid - another.subid;
			if (diff == 0) {
				Log.e("===DOMAIN.news===", "sub id the same!!! [1] "
						+ this.text + ", [2] " + another.text + ", [time]"
						+ this.time + "|" + this.subid);
			}
			if (diff > 0) {
				return 1;
			} else if (diff < 0) {
				return -1;
			} else {
				return 0;
			}
		}
	}
}

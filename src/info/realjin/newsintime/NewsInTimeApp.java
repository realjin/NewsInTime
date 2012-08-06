package info.realjin.newsintime;

import info.realjin.newsintime.domain.AppConfig;
import info.realjin.newsintime.domain.AppData;
import info.realjin.newsintime.domain.AppStatus;
import info.realjin.newsintime.domain.News;
import info.realjin.newsintime.domain.NewsList;

import java.util.List;

import android.app.Application;
import android.util.Log;

public class NewsInTimeApp extends Application {
	private AppData data;
	private AppConfig config;

	private AppStatus status;

	public NewsInTimeApp() {
		Log.i("===APP===", "1");
		data = new AppData();
		Log.i("===APP===", "2");
		config = new AppConfig();
		Log.i("===APP===", "3");
		status = new AppStatus();
	}

	// ----- method for animation
	public List<News> getSubList(News least, int size) {
		long i = 0;
		int j;
		List<News> sl;
		NewsList nl = data.getNewsList();
		if (least == null) { // no news was played yet
			sl = nl.getAllCatFirstSeveral(size);
		} else {
			sl = nl.getAllCatByIdCyclicly(least, size);
		}

		Log.i("===App===", "get " + (i - 1) + " news");

		return sl;
	}

	// ----- setters and getters
	public AppData getData() {
		return data;
	}

	public void setData(AppData data) {
		this.data = data;
	}

	public AppConfig getConfig() {
		return config;
	}

	public void setConfig(AppConfig config) {
		this.config = config;
	}

	public AppStatus getStatus() {
		return status;
	}

	public void setStatus(AppStatus status) {
		this.status = status;
	}
}

package info.realjin.newsintime;

import info.realjin.newsintime.activity.MainActivity;
import info.realjin.newsintime.domain.AppConfig;
import info.realjin.newsintime.domain.AppData;
import info.realjin.newsintime.domain.AppStatus;
import info.realjin.newsintime.domain.Collection;
import info.realjin.newsintime.domain.CollectionItem;
import info.realjin.newsintime.domain.News;
import info.realjin.newsintime.domain.NewsList;
import info.realjin.newsintime.service.newsretriever.NewsRetrieverService;

import java.util.List;

import android.app.Application;
import android.util.Log;
import android.widget.TextView;

public class NewsInTimeApp extends Application {
	private AppData data;
	private AppConfig config;
	private AppStatus status;

	// activity pointers
	MainActivity mainActivity;

	private NewsRetrieverService nrService;

	public NewsInTimeApp() {
		// init data
		Log.i("===APP===", "1");
		data = new AppData();
		Log.i("===APP===", "2");
		config = new AppConfig();
		Log.i("===APP===", "3");
		status = new AppStatus();

		// start retrieving news rss
		// TODO: temp!!!
		Collection tempColl = new Collection();
		tempColl.getItems().add(
				new CollectionItem(
						"http://rss.sina.com.cn/news/marquee/ddt.xml"));
		nrService = new NewsRetrieverService(data.getNewsList(), tempColl);
//		nrService.start();
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

	public void updateProgress(News curNews) {
		NewsList nl = data.getNewsList();
		long pos = nl.getPosition(curNews);
		long size = nl.size();

		// for test
		String s = pos + "/" + size;
		TextView tv = (TextView) mainActivity.findViewById(R.id.tvMain);
		tv.setText(s);
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

	public MainActivity getMainActivity() {
		return mainActivity;
	}

	public void setMainActivity(MainActivity mainActivity) {
		this.mainActivity = mainActivity;
	}
}

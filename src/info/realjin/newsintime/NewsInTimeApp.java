package info.realjin.newsintime;

import info.realjin.newsintime.activity.MainActivity;
import info.realjin.newsintime.dao.CollectionDao;
import info.realjin.newsintime.domain.AppConfig;
import info.realjin.newsintime.domain.AppData;
import info.realjin.newsintime.domain.AppMessage;
import info.realjin.newsintime.domain.AppStatus;
import info.realjin.newsintime.domain.Collection;
import info.realjin.newsintime.domain.CollectionItem;
import info.realjin.newsintime.domain.News;
import info.realjin.newsintime.domain.NewsList;
import info.realjin.newsintime.service.DbManagerService;
import info.realjin.newsintime.service.newsretriever.NewsRetrieverService;
import info.realjin.newsintime.view.VerticalSlider;

import java.util.List;

import android.app.Application;
import android.util.Log;

public class NewsInTimeApp extends Application {
	private AppData data;
	private AppConfig config;
	private AppStatus status;
	private AppMessage msg;

	// activity pointers
	MainActivity mainActivity;

	// services
	private DbManagerService dbmService;

	private NewsRetrieverService nrService;

	public NewsInTimeApp() {
		// init data
		Log.i("===APP===", "1");
		data = new AppData();
		Log.i("===APP===", "2");
		config = new AppConfig();
		Log.i("===APP===", "3");
		status = new AppStatus();
		Log.i("===APP===", "4");
		msg = new AppMessage();

		// start retrieving news rss
		// TODO: temp!!!
		Collection tempColl = new Collection();
		tempColl.getItems().add(
				new CollectionItem(
						"http://rss.sina.com.cn/news/marquee/ddt.xml"));
		nrService = new NewsRetrieverService(data.getNewsList(), tempColl);
//		nrService.start();
	}

	public void notifyFirstActivityStart(MainActivity a) {
		this.mainActivity = a;
		// init db
		dbmService = new DbManagerService(mainActivity);
		dbmService.initDatabase();
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

		// update progress text
		// String s = pos + "/" + size;
		// TextView tv = mainActivity.getTvProgress();
		// tv.setText(s);

		// update slider
		VerticalSlider vsMain = mainActivity.getVsMain();
		float max = vsMain.getMax();
		float min = vsMain.getMin();
		float position = min + (1 - pos * 1.0f / size) * (max - min);
		Log.d("===ANIM===", "position=" + position + ", min=" + vsMain.getMin()
				+ ", max=" + vsMain.getMax());
		vsMain.setPosition(position);

	}

	// ----- helper methods
	public CollectionDao getCollectionDao() {
		return dbmService.getCollectionDao();
	}

	public void setCollectionDao(CollectionDao collectionDao) {
		dbmService.setCollectionDao(collectionDao);
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

	public NewsRetrieverService getNrService() {
		return nrService;
	}

	public void setNrService(NewsRetrieverService nrService) {
		this.nrService = nrService;
	}

	public DbManagerService getDbmService() {
		return dbmService;
	}

	public void setDbmService(DbManagerService dbmService) {
		this.dbmService = dbmService;
	}

	public void putMessage(String name, Object m) {
		msg.getMsgMap().put(name, m);
	}

	public Object getMessage(String name) {
		return msg.getMsgMap().get(name);
	}

}

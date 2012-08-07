package info.realjin.newsintime.domain;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.locks.ReentrantLock;

import android.util.Log;

public class NewsList {
	private static final SimpleDateFormat sdf = new SimpleDateFormat(
			"yyyy.MM.dd;HH:mm:ss");
	private static final String ID_SPLITTER = "|";

	private List<News> newsList;
	private ReentrantLock lock;

	/**
	 * mmm: test only
	 */
	private void testInitNews() {
		lock.lock();
		Date d = new Date();
		try {
			d = sdf.parse("2012.08.06;11:23:25");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		addNews(new News(
				"1. aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa.", d));
		addNews(new News(
				"2. bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb.",
				d));
		addNews(new News(
				"3. cccccccccccccccccccccccccccccccccccccccccccccccccccccccc.",
				d));
		lock.unlock();
	}

	// public-------------
	public NewsList() {
		newsList = new ArrayList<News>();
		lock = new ReentrantLock();
		// testInitNews();
	}

	public int size() {
		return newsList.size();
	}

	// --------- methods for newsretriever service to put news ----
	private long allocateSubId(News n) {
		long maxSubId = 0;
		lock.lock();

		for (News m : newsList) {
			Date dm = m.getTime();
			Date dn = n.getTime();
			// if (d1.getYear() == d2.getYear() && d1.getMonth() ==
			// d2.getMonth()
			// && d1.getDay() == d2.getDay()
			// && d1.getHours() == d2.getHours()
			// && d1.getMinutes() == d2.getMinutes()
			// && d1.getSeconds() == d2.getSeconds()) {
			Log.e("===TEMP===", "dm=" + dm + ", dn=" + dn);
			Log.e("===TEMP===", "dn text=" + n.getText());
			Log.e("===TEMP===", "dmgettime=" + dm.getTime() + ", dngettime="
					+ dn.getTime());
			if (dm.getTime() == dn.getTime()) {
				if (m.getSubid() > maxSubId) {
					maxSubId = m.getSubid();
				}
			}
		}

		lock.unlock();

		// if not found same time, then maxSubId remains zero
		return maxSubId + 1;
	}

	public void clearAll() {
		lock.lock();
		newsList.clear();
		lock.unlock();
	}

	/**
	 * @param n
	 *            time must be set already
	 */
	public void addNews(News n) {
		if (n == null) {
			return;
		}

		// check if exist
		if (existNews(n)) {
			Log.e("===domain.NEWSLIST===", "news exist: " + n.getText());
			return;
		}

		// allocate id
		long subid = allocateSubId(n);
		lock.lock();
		n.setSubid(subid);

		String sTime = sdf.format(n.getTime());
		String id = sTime + ID_SPLITTER + subid;
		n.setId(id);

		newsList.add(n);
		lock.unlock();

	}

	private boolean existNews(News n) {
		lock.lock();
		for (News m : newsList) {
			if (m.getText().equalsIgnoreCase(n.getText())) {
				lock.unlock();
				return true;
			}
		}
		lock.unlock();
		return false;
	}

	// ------------------------ methods of get news ------------------
	public News getById(String id) {
		lock.lock();
		for (News n : newsList) {
			if (n.getId().equals(id)) {
				lock.unlock();
				return n;
			}
		}

		lock.unlock();
		return null;
	}

	public long getPosition(News n) {
		SortedSet<News> ss = new TreeSet<News>();

		lock.lock();
		for (News m : newsList) {
			ss.add(m);
		}
		lock.unlock();

		long pos = 0;
		for (News m : ss) {
			if (m.getId().equalsIgnoreCase(n.getId())) {
				break;
			}
			pos++;
		}

		return pos + 1;
	}

	public List<News> getAllCatFirstSeveral(int size) {
		List<News> result = new ArrayList<News>();
		SortedSet<News> ss = new TreeSet<News>();

		lock.lock();
		for (News n : newsList) {
			ss.add(n);
		}

		News[] temp = new News[ss.size()];
		ss.toArray(temp);

		result.addAll(Arrays.asList(temp));

		if (result.size() >= size) {
			result = result.subList(0, size);
		} else {
			Log.i("===NewsLIst===",
					"getAllCatFirstSeveral: not so many(expecting size:" + size
							+ ", actual size:" + result.size());
		}
		lock.unlock();

		return result;
	}

	/**
	 * get all news of all cat whose id is at most of the argument (size of
	 * result is specified)
	 * 
	 * @param max
	 *            not including
	 * @param size
	 * @return result is sorted by id
	 */
	public List<News> getAllCatByIdMax(News max, int size) {
		List<News> result = new ArrayList<News>();
		SortedSet<News> ss = new TreeSet<News>();

		lock.lock();
		for (News n : newsList) {
			if (n.compareTo(max) < 0) { // TODO: cmp!!
				ss.add(n);
			}
		}

		// TODO: what if size==0? test in Test.java!!
		News[] temp = new News[ss.size()];
		ss.toArray(temp);

		result.addAll(Arrays.asList(temp));

		if (result.size() >= size) {
			result = result.subList(0, size);
		} else {
			Log.i("===NewsLIst===",
					"getAllCatByIdMax: not so many(expecting size:" + size
							+ ", actual size:" + result.size());
		}

		lock.unlock();
		return result;
	}

	/**
	 * get all news of all cat whose id is at least of the argument (size of
	 * result is specified)
	 * 
	 * @param min
	 * @param size
	 * @return result is sorted by id
	 */
	public List<News> getAllCatById(News min, int size) {
		List<News> result = new ArrayList<News>();
		SortedSet<News> ss = new TreeSet<News>();

		lock.lock();
		for (News n : newsList) {
			// if (n.getId() >= min) {
			if (n.compareTo(min) >= 0) {
				ss.add(n);
			}
		}

		News[] temp = new News[ss.size()];
		ss.toArray(temp);

		result.addAll(Arrays.asList(temp));

		if (result.size() >= size) {
			result = result.subList(0, size);
		} else {
			Log.i("===NewsLIst===",
					"getAllCatById: not so many(expecting size:" + size
							+ ", actual size:" + result.size());
		}

		lock.unlock();
		return result;
	}

	/**
	 * @param min
	 * @return
	 */
	public List<News> getAllCatById(News min) {
		SortedSet<News> ss = new TreeSet<News>();

		lock.lock();
		for (News n : newsList) {
			// if (n.getId() >= min) {
			if (n.compareTo(min) >= 0) {
				ss.add(n);
			}
		}
		News[] temp = new News[ss.size()];
		ss.toArray(temp);

		lock.unlock();

		return Arrays.asList(temp);
	}

	/**
	 * @param least
	 *            including
	 * @param size
	 * @return
	 */
	public List<News> getAllCatByIdCyclicly(News least, int size) {
		List<News> result = new ArrayList<News>();
		SortedSet<News> ss = new TreeSet<News>();

		lock.lock();
		for (News n : newsList) {
			// if (n.getId() >= least) {
			if (n.compareTo(least) >= 0) {
				ss.add(n);
			}
		}

		News[] temp = new News[ss.size()];
		ss.toArray(temp);

		result.addAll(Arrays.asList(temp));

		if (result.size() >= size) {
			result = result.subList(0, size);

			lock.unlock();
			return result;
		} else {
			int left = size - result.size();
			Log.i("===NewsLIst===",
					"getAllCatByIdCyclicly: not so many(expecting size:" + size
							+ ", actual size:" + result.size()
							+ ", so starting from beginning");

			// List<News> leftNews = getAllCatByIdMax(result.get(0).getId(),
			// left);
			List<News> leftNews = getAllCatByIdMax(result.get(0), left);
			Log.i("===NewsLIst===",
					"getAllCatByIdCyclicly: debug(expecting size:" + size
							+ ", actual size:" + result.size() + ", left:"
							+ left + ", leftNews size=" + leftNews.size());
			if (leftNews.size() < left) {
				Log.i("===NewsLIst===",
						"getAllCatByIdCyclicly: not so many finnaly(expecting size:"
								+ size + ", actual size:" + result.size()
								+ left);
			}
			Log.e("==addall===", "result size=" + result.size() + ", leftnews="
					+ leftNews.size());
			{
				for (News n : result) {
					Log.e("===111===", n.getId() + "");
				}
				for (News n : leftNews) {
					Log.e("===222===", n.getId() + "");
				}
			}
			result.addAll(leftNews);

			lock.unlock();
			return result;
		}

	}

	// ------------------------ news of get news ------------------ end

	// ---- vip methods

	public String getMaxId() {
		News m;
		if (newsList.size() == 0) {
			return null;
		}
		m = newsList.get(0);
		lock.lock();
		for (News n : newsList) {
			// if (n.getId() > m) {
			if (n.compareTo(m) > 0) {
				m = n;
			}
		}
		lock.unlock();
		return m.getId();
	}

	// ---- util methods
	public static String newsListToString(List<News> nl) {
		StringBuilder sb = new StringBuilder();
		for (News n : nl) {
			sb.append(n.toString()).append(' ');
		}
		sb.deleteCharAt(sb.length() - 1);

		return sb.toString();
	}

	// ----- setters and getters
	public List<News> getNewsList() {
		return newsList;
	}

	public void setNewsList(List<News> newsList) {
		this.newsList = newsList;
	}
}

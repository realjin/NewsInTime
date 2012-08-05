package info.realjin.newsintime.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import android.util.Log;

public class NewsList {
	private List<News> newsList;

	/**
	 * mmm: test only
	 */
	private void testInitNews() {
		newsList.add(new News(
				"1. aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa."));
		newsList.add(new News(
				"2. bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb."));
		newsList.add(new News(
				"3. cccccccccccccccccccccccccccccccccccccccccccccccccccccccc."));
	}

	// public-------------
	public NewsList() {
		newsList = new ArrayList<News>();
		testInitNews();
	}

	public int size() {
		return newsList.size();
	}

	// ------------------------ methods of get news ------------------
	public News getById(long id) {
		for (News n : newsList) {
			if (n.getId() == id) {
				return n;
			}
		}

		return null;
	}

	public List<News> getAllCatFirstSeveral(int size) {
		List<News> result = new ArrayList<News>();
		SortedSet<News> ss = new TreeSet<News>();

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
	public List<News> getAllCatByIdMax(long max, int size) {
		List<News> result = new ArrayList<News>();
		SortedSet<News> ss = new TreeSet<News>();

		for (News n : newsList) {
			if (n.getId() < max) {
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
	public List<News> getAllCatById(long min, int size) {
		List<News> result = new ArrayList<News>();
		SortedSet<News> ss = new TreeSet<News>();

		for (News n : newsList) {
			if (n.getId() >= min) {
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

		return result;
	}

	/**
	 * @param min
	 * @return
	 */
	public List<News> getAllCatById(long min) {
		SortedSet<News> ss = new TreeSet<News>();

		for (News n : newsList) {
			if (n.getId() >= min) {
				ss.add(n);
			}
		}
		News[] temp = new News[ss.size()];
		ss.toArray(temp);

		return Arrays.asList(temp);
	}

	public List<News> getAllCatByIdCyclicly(long least, int size) {
		List<News> result = new ArrayList<News>();
		SortedSet<News> ss = new TreeSet<News>();

		for (News n : newsList) {
			if (n.getId() >= least) {
				ss.add(n);
			}
		}

		News[] temp = new News[ss.size()];
		ss.toArray(temp);

		result.addAll(Arrays.asList(temp));

		if (result.size() >= size) {
			result = result.subList(0, size);

			return result;
		} else {
			int left = size - result.size();
			Log.i("===NewsLIst===",
					"getAllCatByIdCyclicly: not so many(expecting size:" + size
							+ ", actual size:" + result.size()
							+ ", so starting from beginning");

			List<News> leftNews = getAllCatByIdMax(result.get(0).getId(), left);
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

			return result;
		}

	}

	// ------------------------ news of get news ------------------ end

	// ---- vip methods

	public long getMaxId() {
		long m;
		m = 0;
		for (News n : newsList) {
			if (n.getId() > m) {
				m = n.getId();
			}
		}
		return m;
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

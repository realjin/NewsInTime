package info.realjin.newsintime.dao;

import info.realjin.newsintime.domain.Collection;
import info.realjin.newsintime.domain.CollectionItem;
import info.realjin.newsintime.service.DbManagerService;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

/**
 * collection, collectionitem, etc.
 * 
 * @author realjin
 * 
 */
public class CollectionDao extends GenericDao {

	public CollectionDao(DbManagerService dbmService) {
		super(dbmService);
	}

	/**
	 * without collection item!!
	 * 
	 * @param coll
	 */
	public void addCollectionWithoutItems(Collection coll) {
		ContentValues cv = new ContentValues();
		// cv.put(DbManagerService.Table_Collection.IMEI, "123456789012345");
		Log.e("CDAO", "addCollectionWithoutItems: name=" + coll.getName());
		cv.put(DbManagerService.Table_Collection.CNAME_NAME, coll.getName());
		getDb().insert(DbManagerService.Table_Collection.TNAME, null, cv);
	}

	public void addCollectionWithItems(Collection coll) {
		// 1. save coll
		ContentValues cvColl = new ContentValues();
		cvColl.put(DbManagerService.Table_Collection.CNAME_NAME, coll.getName());
		Long collId = getDb().insert(DbManagerService.Table_Collection.TNAME,
				null, cvColl);

		// 2. save collitems and coll_collitem relations
		for (CollectionItem ci : coll.getItems()) {
			ContentValues cvCollItem = new ContentValues();
			cvCollItem.put(DbManagerService.Table_CollectionItem.CNAME_NAME,
					ci.getName());
			cvCollItem.put(DbManagerService.Table_CollectionItem.CNAME_URL,
					ci.getUrl());
			Long collItemId = getDb().insert(
					DbManagerService.Table_CollectionItem.TNAME, null,
					cvCollItem);

			ContentValues cvColl_CollItem = new ContentValues();
			cvColl_CollItem
					.put(DbManagerService.Table_CollectionCollectionItem.CNAME_COLLID,
							collId.intValue());
			cvColl_CollItem
					.put(DbManagerService.Table_CollectionCollectionItem.CNAME_COLLITEMID,
							collItemId.intValue());
			getDb().insert(
					DbManagerService.Table_CollectionCollectionItem.TNAME,
					null, cvColl_CollItem);
		}

	}

	public List<Collection> getAllCollectionsWithoutItems() {
		Cursor c = getDb().rawQuery(
				"SELECT * FROM " + DbManagerService.Table_Collection.TNAME
						+ " WHERE 1=1", null);
		List<Collection> collList = new ArrayList<Collection>();
		if (c != null) {
			while (c.moveToNext()) {
				Integer id = c
						.getInt(c
								.getColumnIndex(DbManagerService.Table_Collection.CNAME_ID));
				String name = c
						.getString(c
								.getColumnIndex(DbManagerService.Table_Collection.CNAME_NAME));

				Collection coll = new Collection();
				coll.setId(id);
				coll.setName(name);

				collList.add(coll);
			}
		}
		c.close();
		return collList;
	}

	// get one complete
	public Collection getCollectionWithItems(Integer collId) {
		Cursor c = getDb().rawQuery(
				"SELECT * FROM " + DbManagerService.Table_Collection.TNAME
						+ " WHERE "
						+ DbManagerService.Table_Collection.CNAME_ID + "="
						+ collId, null);
		Collection coll = null;
		if (c != null) {
			if (c.moveToNext()) {
				Integer id = c
						.getInt(c
								.getColumnIndex(DbManagerService.Table_Collection.CNAME_ID));
				String name = c
						.getString(c
								.getColumnIndex(DbManagerService.Table_Collection.CNAME_NAME));

				coll = new Collection();
				coll.setId(id);
				coll.setName(name);

				List<CollectionItem> collItemList = getCollectionItemsByCollId(id);
				coll.setItems(collItemList);
			}
		}
		c.close();
		return coll;
	}

	// get many complete
	public List<Collection> getAllCollectionsWithItems() {
		Cursor c = getDb().rawQuery(
				"SELECT * FROM " + DbManagerService.Table_Collection.TNAME
						+ " WHERE 1=1", null);
		List<Collection> collList = new ArrayList<Collection>();
		if (c != null) {
			while (c.moveToNext()) {
				Integer id = c
						.getInt(c
								.getColumnIndex(DbManagerService.Table_Collection.CNAME_ID));
				String name = c
						.getString(c
								.getColumnIndex(DbManagerService.Table_Collection.CNAME_NAME));

				Collection coll = new Collection();
				coll.setId(id);
				coll.setName(name);

				List<CollectionItem> collItemList = getCollectionItemsByCollId(id);
				coll.setItems(collItemList);

				collList.add(coll);
			}
		}
		c.close();
		return collList;
	}

	// not tested
	public Collection getCollectionWithoutItems(Long collId) {
		Cursor c = getDb().rawQuery(
				"SELECT * FROM " + DbManagerService.Table_Collection.TNAME
						+ " WHERE id=" + collId, null);
		Collection coll = null;

		if (c != null) {
			if (c.moveToNext()) {
				coll = new Collection();

				Integer id = c
						.getInt(c
								.getColumnIndex(DbManagerService.Table_Collection.CNAME_ID));
				String name = c
						.getString(c
								.getColumnIndex(DbManagerService.Table_Collection.CNAME_NAME));

				coll.setId(id);
				coll.setName(name);
			}
		}

		return coll;
	}

	public void removeCollection() {

	}

	/*---------------
	 * collection_collectionitem operations 
	 *---------------*/

	// TODO: using complex sql!
	public List<CollectionItem> getCollectionItemsByCollId(Integer collId) {
		List<Integer> collItemIdList = getCollectionItemIdsByCollId(collId);

		List<CollectionItem> collItemList = new ArrayList<CollectionItem>();
		for (Integer collItemId : collItemIdList) {
			CollectionItem ci = getCollectionItem(collItemId);
			collItemList.add(ci);
		}

		return collItemList;
	}

	public List<Integer> getCollectionItemIdsByCollId(Integer collId) {
		List<Integer> collItemIdsList = new ArrayList<Integer>();

		Cursor c = getDb()
				.rawQuery(
						"SELECT * FROM "
								+ DbManagerService.Table_CollectionCollectionItem.TNAME
								+ " WHERE "
								+ DbManagerService.Table_CollectionCollectionItem.CNAME_COLLID
								+ "=" + collId, null);
		List<Collection> collList = new ArrayList<Collection>();
		if (c != null) {
			while (c.moveToNext()) {
				Integer collItemId = c
						.getInt(c
								.getColumnIndex(DbManagerService.Table_CollectionCollectionItem.CNAME_COLLITEMID));

				collItemIdsList.add(collItemId);
			}
		}
		c.close();
		return collItemIdsList;
	}

	/*---------------
	 * collection item operations 
	 *---------------*/

	public void addCollectionItem(Collection coll, CollectionItem collItem) {
		// add in CI table
		ContentValues cvCi = new ContentValues();
		cvCi.put(DbManagerService.Table_CollectionItem.CNAME_NAME,
				collItem.getName());
		cvCi.put(DbManagerService.Table_CollectionItem.CNAME_URL,
				collItem.getUrl());
		getDb().insert(DbManagerService.Table_CollectionItem.TNAME, null, cvCi);
		// cv.put(DbManagerService.Table_CollectionItem.CNAME_NAME,
		// collItem.getName());

		// add in C-CI table
		ContentValues cvC_Ci = new ContentValues();
		cvCi.put(DbManagerService.Table_CollectionCollectionItem.CNAME_COLLID,
				coll.getId());
		cvCi.put(
				DbManagerService.Table_CollectionCollectionItem.CNAME_COLLITEMID,
				"");
	}

	public CollectionItem getCollectionItem(Integer collItemId) {
		Cursor c = getDb().rawQuery(
				"SELECT * FROM " + DbManagerService.Table_CollectionItem.TNAME
						+ " WHERE id=" + collItemId, null);
		CollectionItem collItem = null;

		if (c != null) {
			if (c.moveToNext()) {
				collItem = new CollectionItem();

				Integer id = c
						.getInt(c
								.getColumnIndex(DbManagerService.Table_CollectionItem.CNAME_ID));
				String name = c
						.getString(c
								.getColumnIndex(DbManagerService.Table_CollectionItem.CNAME_NAME));
				String url = c
						.getString(c
								.getColumnIndex(DbManagerService.Table_CollectionItem.CNAME_URL));

				collItem.setId(id);
				collItem.setName(name);
				collItem.setUrl(url);
			}
		}

		return collItem;
	}
}

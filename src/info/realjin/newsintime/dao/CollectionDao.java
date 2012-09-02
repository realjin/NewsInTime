package info.realjin.newsintime.dao;

import info.realjin.newsintime.domain.Collection;
import info.realjin.newsintime.domain.CollectionItem;
import info.realjin.newsintime.service.DbManagerService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
		cv.put(DbManagerService.Table_Collection.CNAME_ENABLED,
				DbManagerService.Table_Collection.DEFAULTVALUE_ENABLED_TRUE);
		getDb().insert(DbManagerService.Table_Collection.TNAME, null, cv);
	}

	public Integer addCollectionWithItems(Collection coll) {
		Integer collId = null;

		// 1. save coll
		ContentValues cvColl = new ContentValues();
		cvColl.put(DbManagerService.Table_Collection.CNAME_NAME, coll.getName());
		cvColl.put(DbManagerService.Table_Collection.CNAME_ENABLED,
				DbManagerService.Table_Collection.DEFAULTVALUE_ENABLED_TRUE);
		collId = new Long(getDb().insert(
				DbManagerService.Table_Collection.TNAME, null, cvColl))
				.intValue();

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

		return collId;
	}

	public List<Collection> getAllCollectionsWithoutItems() {
		Cursor c = getDb()
				.rawQuery(
						"SELECT * FROM "
								+ DbManagerService.Table_Collection.TNAME
								+ " WHERE "
								+ DbManagerService.Table_Collection.CNAME_ENABLED
								+ "="
								+ DbManagerService.Table_Collection.DEFAULTVALUE_ENABLED_TRUE,
						null);
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
		Cursor c = getDb()
				.rawQuery(
						"SELECT * FROM "
								+ DbManagerService.Table_Collection.TNAME
								+ " WHERE "
								+ DbManagerService.Table_Collection.CNAME_ID
								+ "="
								+ collId
								+ " AND "
								+ DbManagerService.Table_Collection.CNAME_ENABLED
								+ "="
								+ DbManagerService.Table_Collection.DEFAULTVALUE_ENABLED_TRUE,
						null);
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
		Cursor c = getDb()
				.rawQuery(
						"SELECT * FROM "
								+ DbManagerService.Table_Collection.TNAME
								+ " WHERE "
								+ DbManagerService.Table_Collection.CNAME_ENABLED
								+ "="
								+ DbManagerService.Table_Collection.DEFAULTVALUE_ENABLED_TRUE,
						null);
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
		Cursor c = getDb()
				.rawQuery(
						"SELECT * FROM "
								+ DbManagerService.Table_Collection.TNAME
								+ " WHERE id="
								+ collId
								+ " AND "
								+ DbManagerService.Table_Collection.CNAME_ENABLED
								+ "="
								+ DbManagerService.Table_Collection.DEFAULTVALUE_ENABLED_TRUE,
						null);
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

	// TODO: only read coll that flag is true too!
	public void deleteCollection(Integer collId) {
		// 1. delete collection
		int delResult = getDb()
				.delete(DbManagerService.Table_Collection.TNAME,
						DbManagerService.Table_Collection.CNAME_ID + "="
								+ collId, null);
		if (delResult == 0) {
			Log.e("CDAO", "deleteCollection: delResult zero");
		}

		// 2. delete collectionitems
		Set<Integer> collItemIds = getCollectionItemIdsByCollId(collId);
		for (Integer collItemId : collItemIds) {
			deleteCollectionItem(collItemId);
		}

		// 3. delete collection_collectionitem relation
		deleteCollection_CollectionItem(collId);
	}

	public void hideCollection(Integer collId) {
		Cursor c = getDb()
				.rawQuery(
						"UPDATE "
								+ DbManagerService.Table_Collection.TNAME
								+ " SET "
								+ DbManagerService.Table_Collection.CNAME_ENABLED
								+ "="
								+ DbManagerService.Table_Collection.DEFAULTVALUE_ENABLED_FALSE
								+ " WHERE "
								+ DbManagerService.Table_Collection.CNAME_ID
								+ "=" + collId, null);
		c.moveToFirst();
		c.close();
		// mmm: no check of cursor
	}

	public void unhideCollection(Integer collId) {
		Cursor c = getDb()
				.rawQuery(
						"UPDATE "
								+ DbManagerService.Table_Collection.TNAME
								+ " SET "
								+ DbManagerService.Table_Collection.CNAME_ENABLED
								+ "="
								+ DbManagerService.Table_Collection.DEFAULTVALUE_ENABLED_TRUE
								+ " WHERE "
								+ DbManagerService.Table_Collection.CNAME_ID
								+ "=" + collId, null);
		c.moveToFirst();
		c.close();
		// mmm: no check of cursor
	}

	/*---------------
	 * collection_collectionitem operations 
	 *---------------*/

	// TODO: using complex sql!
	public List<CollectionItem> getCollectionItemsByCollId(Integer collId) {
		Set<Integer> collItemIdList = getCollectionItemIdsByCollId(collId);

		List<CollectionItem> collItemList = new ArrayList<CollectionItem>();
		for (Integer collItemId : collItemIdList) {
			CollectionItem ci = getCollectionItem(collItemId);
			collItemList.add(ci);
		}

		return collItemList;
	}

	public Set<Integer> getCollectionItemIdsByCollId(Integer collId) {
		Set<Integer> collItemIdsList = new HashSet<Integer>();

		// no need for flag filter here!
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

	public void deleteCollection_CollectionItem(Integer collId) {
		int delResult = getDb().delete(
				DbManagerService.Table_CollectionCollectionItem.TNAME,
				DbManagerService.Table_CollectionCollectionItem.CNAME_COLLID
						+ "=" + collId, null);
		if (delResult == 0) {
			Log.e("CDAO", "deleteCollection_CollectionItem: delResult zero");
		}
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

	public void deleteCollectionItem(Integer collItemId) {
		int delResult = getDb().delete(
				DbManagerService.Table_CollectionItem.TNAME,
				DbManagerService.Table_CollectionItem.CNAME_ID + "="
						+ collItemId, null);
		if (delResult == 0) {
			Log.e("CDAO", "deleteCollectionItem: delResult zero");
		}
	}
}

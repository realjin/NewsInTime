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
		long collId = getDb().insert(DbManagerService.Table_Collection.TNAME,
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
							collId);
			cvColl_CollItem
					.put(DbManagerService.Table_CollectionCollectionItem.CNAME_COLLITEMID,
							collItemId);
			getDb().insert(
					DbManagerService.Table_CollectionCollectionItem.TNAME,
					null, cvColl_CollItem);
		}

	}

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

	public List<Collection> getAllCollections() {
		Cursor c = getDb().rawQuery(
				"SELECT * FROM " + DbManagerService.Table_Collection.TNAME
						+ " WHERE 1=1", null);
		List<Collection> collList = new ArrayList<Collection>();
		if (c != null) {
			while (c.moveToNext()) {
				Log.e("CDAO", "getAllCollections ITERATION");
				Log.e("CDAO",
						"getAllCollections id columnid="
								+ c.getColumnIndex(DbManagerService.Table_Collection.CNAME_ID));
				Log.e("CDAO",
						"getAllCollections name columnid="
								+ c.getColumnIndex(DbManagerService.Table_Collection.CNAME_NAME));
				String id = ""
						+ c.getInt(c
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

	public void removeCollection() {

	}

}

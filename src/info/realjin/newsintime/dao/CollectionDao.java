package info.realjin.newsintime.dao;

import info.realjin.newsintime.domain.Collection;
import info.realjin.newsintime.domain.CollectionItem;
import info.realjin.newsintime.service.DbManagerService;
import android.content.ContentValues;

/**
 * collection, collectionitem, etc.
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
		cv.put(DbManagerService.Table_Collection.CNAME_NAME, coll.getName());
		getDb().insert(DbManagerService.Table_Collection.TNAME, null, cv);
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

	/**
	 * with collection item!!
	 * 
	 * @param coll
	 */
	public void addCollection(Collection coll) {
	}

	public void removeCollection() {

	}

}

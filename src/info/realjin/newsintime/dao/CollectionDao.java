package info.realjin.newsintime.dao;

import info.realjin.newsintime.domain.Collection;
import info.realjin.newsintime.service.DbManagerService;
import android.content.ContentValues;

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

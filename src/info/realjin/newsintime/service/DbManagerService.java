package info.realjin.newsintime.service;

import info.realjin.newsintime.dao.CollectionDao;
import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

/*
 * http://stackoverflow.com/questions/3684678/best-practices-of-working-with-multiple-sqlite-db-tables-in-android
 */

public class DbManagerService {

	private static final String LOG_TAG = DbManagerService.class
			.getSimpleName();

	private SQLiteDatabase db;
	private static MySQLiteOpenHelper helper;
	private Application application;

	private CollectionDao collectionDao;

	public DbManagerService(Application app) {
		application = app;
		helper = new MySQLiteOpenHelper(app);
		db = helper.getWritableDatabase();

		collectionDao = new CollectionDao(this);
	}

	/**
	 * init database tables
	 */
	public void initDatabase() {
		checkDbState();
		Log.i("===DBM===", "** init db");
		db.execSQL("CREATE TABLE IF NOT EXISTS" + Table_Collection.TNAME + " ("
				+ Table_Collection.CNAME_ID + " INTEGER PRIMARY KEY,"
				+ Table_Collection.CNAME_NAME + " TEXT,"
				+ Table_Collection.CNAME_UPDATETIME + " TEXT);");
		db.execSQL("CREATE TABLE IF NOT EXISTS" + Table_CollectionItem.TNAME
				+ " (" + Table_CollectionItem.CNAME_ID
				+ " INTEGER PRIMARY KEY," + Table_CollectionItem.CNAME_NAME
				+ " TEXT," + Table_CollectionItem.CNAME_URL + " TEXT,"
				+ Table_CollectionItem.CNAME_COLLID + " TEXT,"
				+ Table_CollectionItem.CNAME_TYPE + " TEXT,"
				+ Table_CollectionItem.CNAME_PREDEFINED + " TEXT,"
				+ Table_CollectionItem.CNAME_UPDATETIME + " TEXT);");
	}

	public static final class Table_Collection implements BaseColumns {
		public static final String TNAME = "NEWSINTIME_COLLECTION";
		public static final String CNAME_ID = "id";
		public static final String CNAME_NAME = "name";
		public static final String CNAME_UPDATETIME = "updatetime";
	}

	public static final class Table_CollectionItem implements BaseColumns {
		public static final String TNAME = "NEWSINTIME_COLLECTIONITEM";
		public static final String CNAME_ID = "id";
		public static final String CNAME_NAME = "name";
		public static final String CNAME_URL = "url";
		public static final String CNAME_COLLID = "collid";
		public static final String CNAME_TYPE = "type";
		public static final String CNAME_PREDEFINED = "predefined";
		public static final String CNAME_UPDATETIME = "updatetime";
	}

	public boolean isOpen() {
		return db != null && db.isOpen();
	}

	public void open() {
		helper = new MySQLiteOpenHelper(application);
		if (!isOpen()) {
			db = helper.getWritableDatabase();
		}
	}

	public void close() {
		if (isOpen()) {
			db.close();
			db = null;
			if (helper != null) {
				helper.close();
				helper = null;
			}
		}
	}

	private boolean checkDbState() {
		if (db == null || !db.isOpen()) {
			// throw new
			// IllegalStateException("The database has not been opened");
			return false;
		}

		return true;
	}

	/**
	 * return db that is opened!!
	 */
	public SQLiteDatabase getDb() {
		if (checkDbState()) {
			return db;
		} else {
			open();
			if (!checkDbState()) {
				throw new IllegalStateException(
						"The database has not been opened");
			}
			return db;
		}
	}

	private static class MySQLiteOpenHelper extends SQLiteOpenHelper {
		private static final String DATABASE_NAME = "dbname";
		private static final int DATABASE_VERSION = 7;

		private MySQLiteOpenHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		public void onCreate(SQLiteDatabase db) {
		}

		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(LOG_TAG, "Upgrading database from version " + oldVersion
					+ " to " + newVersion + "!");
		}

		private void dropDatabase(SQLiteDatabase db) {
			db.execSQL("DROP TABLE IF EXISTS " + Table_Collection.TNAME);
			db.execSQL("DROP TABLE IF EXISTS " + Table_CollectionItem.TNAME);
		}

	}

	public CollectionDao getCollectionDao() {
		return collectionDao;
	}

	public void setCollectionDao(CollectionDao collectionDao) {
		this.collectionDao = collectionDao;
	}

}

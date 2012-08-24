package info.realjin.newsintime.dao;

import info.realjin.newsintime.service.DbManagerService;
import android.database.sqlite.SQLiteDatabase;

public class GenericDao {

	protected DbManagerService dbmService;

	public GenericDao(DbManagerService dbmService) {
		this.dbmService = dbmService;
	}

	public SQLiteDatabase getDb() {
		return dbmService.getDb();
	}

}

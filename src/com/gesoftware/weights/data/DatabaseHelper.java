package com.gesoftware.weights.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

	public static final String TABLE_PEOPLE = "PEOPLE";
	public static final String COLUMN_PEOPLE_ID = "_id";
	public static final String COLUMN_PEOPLE_FIRST_NAME = "FirstName";

	public static final String TABLE_WEIGHTS = "WEIGHTS";
	public static final String COLUMN_WEIGHTS_ID = "_id";
	public static final String COLUMN_WEIGHTS_PERSON_ID = "PersonId";
	public static final String COLUMN_WEIGHTS_WEIGHT = "Weight";

	private static final String DATABASE_NAME = "WeightTracker.db";
	private static final int DATABASE_VERSION = 1;

	// Database creation sql statement
	private static final String TABLE_PEOPLE_CREATE = "create table "
			+ TABLE_PEOPLE + "(" + COLUMN_PEOPLE_ID
			+ " integer primary key autoincrement, " + COLUMN_PEOPLE_FIRST_NAME
			+ " text not null);";

	// Database creation sql statement
	private static final String TABLE_WEIGHTS_CREATE = "create table "
			+ TABLE_WEIGHTS + " (" + COLUMN_WEIGHTS_ID
			+ " integer primary key autoincrement, " + COLUMN_WEIGHTS_PERSON_ID
			+ " integer,"/*
						 * + " FOREIGN KEY(" + COLUMN_WEIGHTS_PERSON_ID +
						 * ") REFERENCES " + TABLE_PEOPLE + "(" +
						 * COLUMN_PEOPLE_ID + ") "
						 */+ COLUMN_WEIGHTS_WEIGHT + " DECIMAL(5,2));";

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(TABLE_PEOPLE_CREATE);
		database.execSQL(TABLE_WEIGHTS_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(DatabaseHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PEOPLE);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_WEIGHTS);
		onCreate(db);
	}

}
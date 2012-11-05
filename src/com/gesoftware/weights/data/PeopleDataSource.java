package com.gesoftware.weights.data;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class PeopleDataSource {

	// Database fields
		private SQLiteDatabase database;
		private DatabaseHelper dbHelper;
		private String[] allColumns = { DatabaseHelper.COLUMN_PEOPLE_ID,
				DatabaseHelper.COLUMN_PEOPLE_FIRST_NAME };

		public PeopleDataSource(Context context) {
			dbHelper = new DatabaseHelper(context);
		}

		public void open() throws SQLException {
			database = dbHelper.getWritableDatabase();
		}

		public void close() {
			dbHelper.close();
		}

		public Person createPerson(String firstName) {
			ContentValues values = new ContentValues();
			values.put(DatabaseHelper.COLUMN_PEOPLE_FIRST_NAME, firstName);
			long insertId = database.insert(DatabaseHelper.TABLE_PEOPLE, null,
					values);
			Cursor cursor = database.query(DatabaseHelper.TABLE_PEOPLE,
					allColumns, DatabaseHelper.COLUMN_PEOPLE_ID + " = " + insertId, null,
					null, null, null);
			cursor.moveToFirst();
			Person newPerson = cursorToPerson(cursor);
			cursor.close();
			return newPerson;
		}

		public void deletePerson(Person person) {
			long id = person.getId();
			System.out.println("Person deleted with id: " + id);
			database.delete(DatabaseHelper.TABLE_PEOPLE, DatabaseHelper.COLUMN_PEOPLE_ID
					+ " = " + id, null);
		}

		public List<Person> getAllPeople() {
			List<Person> people = new ArrayList<Person>();

			Cursor cursor = database.query(DatabaseHelper.TABLE_PEOPLE,
					allColumns, null, null, null, null, null);

			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				Person person = cursorToPerson(cursor);
				people.add(person);
				cursor.moveToNext();
			}
			// Make sure to close the cursor
			cursor.close();
			return people;
		}

		private Person cursorToPerson(Cursor cursor) {
			Person person = new Person();
			person.setId(cursor.getLong(0));
			person.setFirstName(cursor.getString(1));
			return person;
		}

		public int getNumberOfPeople() {
			Cursor cursor = database.query(DatabaseHelper.TABLE_PEOPLE,
					allColumns, null, null, null, null, null);
			
			return cursor.getCount();
		}

		public Person modifyPerson(long id, String firstName) {
			String strFilter = "_id=" + id;
			ContentValues values = new ContentValues();
			values.put(DatabaseHelper.COLUMN_PEOPLE_FIRST_NAME, firstName);
			database.update(DatabaseHelper.TABLE_PEOPLE, values, strFilter, null);

			Cursor cursor = database.query(DatabaseHelper.TABLE_PEOPLE,
					allColumns, DatabaseHelper.COLUMN_PEOPLE_ID + " = " + id, null,
					null, null, null);
			cursor.moveToFirst();
			Person updatedPerson = cursorToPerson(cursor);
			cursor.close();
			return updatedPerson;
		}
}

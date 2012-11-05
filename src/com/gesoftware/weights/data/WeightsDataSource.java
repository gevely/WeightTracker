package com.gesoftware.weights.data;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class WeightsDataSource {

	// Database fields
		private SQLiteDatabase database;
		private DatabaseHelper dbHelper;
		private String[] allColumns = { DatabaseHelper.COLUMN_WEIGHTS_ID,
				DatabaseHelper.COLUMN_WEIGHTS_PERSON_ID,
				DatabaseHelper.COLUMN_WEIGHTS_WEIGHT};

		public WeightsDataSource(Context context) {
			dbHelper = new DatabaseHelper(context);
		}

		public void open() throws SQLException {
			database = dbHelper.getWritableDatabase();
		}

		public void close() {
			dbHelper.close();
		}

		public Weight addWeight(long personId, double weightKg) {
			ContentValues values = new ContentValues();
			values.put(DatabaseHelper.COLUMN_WEIGHTS_PERSON_ID, personId);
			values.put(DatabaseHelper.COLUMN_WEIGHTS_WEIGHT, weightKg);
			long insertId = database.insert(DatabaseHelper.TABLE_WEIGHTS, null,
					values);
			Cursor cursor = database.query(DatabaseHelper.TABLE_WEIGHTS,
					allColumns, DatabaseHelper.COLUMN_WEIGHTS_ID + " = " + insertId, null,
					null, null, null);
			cursor.moveToFirst();
			Weight newWeight = cursorToWeight(cursor);
			cursor.close();
			return newWeight;
		}

		public void deleteWeight(Weight weight) {
			long id = weight.getId();
			System.out.println("Weight deleted with id: " + id);
			database.delete(DatabaseHelper.TABLE_WEIGHTS, DatabaseHelper.COLUMN_WEIGHTS_ID
					+ " = " + id, null);
		}

		public List<Weight> getAllWeightsForPerson(long personId) {
			List<Weight> weights = new ArrayList<Weight>();

			Cursor cursor = database.query(DatabaseHelper.TABLE_WEIGHTS,
					allColumns, 
					DatabaseHelper.COLUMN_WEIGHTS_PERSON_ID + " = " + personId, 
					null, 
					null, 
					null, 
					null);

			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				Weight weight = cursorToWeight(cursor);
				weights.add(weight);
				cursor.moveToNext();
			}
			// Make sure to close the cursor
			cursor.close();
			return weights;
		}

		private Weight cursorToWeight(Cursor cursor) {
			Weight weight = new Weight();
			weight.setId(cursor.getLong(0));
			weight.setPersonId(cursor.getLong(1));
			weight.setWeight(cursor.getDouble(2));
			return weight;
		}

		public int getNumberOfWeightsForPerson(long personId) {
			Cursor cursor = database.query(DatabaseHelper.TABLE_WEIGHTS,
					allColumns, 
					DatabaseHelper.COLUMN_WEIGHTS_PERSON_ID + " = " + personId, 
					null, 
					null, 
					null, 
					null);
			
			return cursor.getCount();
		}

		public Weight modifyWeight(long id, double weightKg) {
			String strFilter = "_id=" + id;
			ContentValues values = new ContentValues();
			values.put(DatabaseHelper.COLUMN_WEIGHTS_WEIGHT, weightKg);
			database.update(DatabaseHelper.TABLE_WEIGHTS, values, strFilter, null);

			Cursor cursor = database.query(DatabaseHelper.TABLE_WEIGHTS,
					allColumns, DatabaseHelper.COLUMN_WEIGHTS_ID + " = " + id, null,
					null, null, null);
			cursor.moveToFirst();
			Weight updatedWeight = cursorToWeight(cursor);
			cursor.close();
			return updatedWeight;
		}
}

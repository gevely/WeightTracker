package com.gesoftware.weights;

import java.util.List;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.gesoftware.weights.data.PeopleDataSource;
import com.gesoftware.weights.data.Person;

@TargetApi(11)
public class MainActivity extends ListActivity {

	protected PeopleDataSource peopleDatasource;
	protected ArrayAdapter<Person> mAdapter;
	protected Object mActionMode;
	public int mSelectedItem = -1;
	public final static String PERSON_ID = "PERSON_ID_EXTRA";

	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		peopleDatasource = new PeopleDataSource(getApplicationContext());
		peopleDatasource.open();

		List<Person> values = peopleDatasource.getAllPeople();

		// Use the SimpleCursorAdapter to show the
		// elements in a ListView
		mAdapter = new ArrayAdapter<Person>(getApplicationContext(),
				android.R.layout.simple_list_item_1, values);
		setListAdapter(mAdapter);

		ListView listView = getListView();
		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				
				mSelectedItem = getListView().getCheckedItemPosition();

				// Start the CAB using the ActionMode.Callback defined above
				mActionMode = MainActivity.this.startActionMode(mActionModeCallback);
				arg1.setSelected(true);
				
				return false;
			}
		});
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				mSelectedItem = getListView().getCheckedItemPosition();

				if ((mAdapter.getCount() > 0) && (mAdapter.getCount() >= mSelectedItem)) {
					final Person person = (Person) mAdapter.getItem(mSelectedItem);
					Intent intent = new Intent();
					intent.setClass(getApplicationContext(), PersonDetailsActivity.class);
				    Long personId = person.getId();
				    intent.putExtra(PERSON_ID, personId);
				    startActivity(intent);
				}
				
			}
		});
	
	}

	private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

		// Called when the action mode is created; startActionMode() was called
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			// Inflate a menu resource providing context menu items
			MenuInflater inflater = mode.getMenuInflater();
			// Assumes that you have "contexual.xml" menu resources
			inflater.inflate(R.menu.main_activity_context_actions, menu);
			return true;
		}

		// Called each time the action mode is shown. Always called after
		// onCreateActionMode, but
		// may be called multiple times if the mode is invalidated.
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			return false; // Return false if nothing is done
		}

		// Called when the user selects a contextual menu item
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			switch (item.getItemId()) {
			case R.id.editPerson:
				editPersonDetails();
				// Action picked, so close the CAB
				mode.finish();
				return true;
			case R.id.deletePerson:
				deletePerson();
				// Action picked, so close the CAB
				mode.finish();
				return true;
			default:
				return false;
			}
		}

		// Called when the user exits the action mode
		public void onDestroyActionMode(ActionMode mode) {
			mActionMode = null;
			mSelectedItem = -1;
		}
	};

	public final void addPersonDetails() {

		// Preparing views
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.dialog_person_details, null);
		// layout_root should be the name of the "top-level" layout node in the
		// dialog_layout.xml file.
		final EditText firstNameBox = (EditText) layout
				.findViewById(R.id.first_name_box);

		// Building dialog
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setView(layout);
		builder.setPositiveButton("Save",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// save info where you want it
						Person newPerson = peopleDatasource
								.createPerson(firstNameBox.getText().toString());

						mAdapter.add(newPerson);
						mAdapter.notifyDataSetChanged();

						dialog.dismiss();
					}
				});

		builder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		AlertDialog dialog = builder.create();
		dialog.show();
	}
	
	public final void editPersonDetails() {

		// Preparing views
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.dialog_person_details, null);
		// layout_root should be the name of the "top-level" layout node in the
		// dialog_layout.xml file.
		final EditText firstNameBox = (EditText) layout
				.findViewById(R.id.first_name_box);
		
		if ((mAdapter.getCount() > 0) && (mAdapter.getCount() >= mSelectedItem)) {
			final Person person = (Person) mAdapter.getItem(mSelectedItem);
			
			firstNameBox.setText(person.getFirstName());

			// Building dialog
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setView(layout);
			builder.setPositiveButton("Save",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// save info where you want it
							Person updatedPerson = peopleDatasource
									.modifyPerson(person.getId(), firstNameBox.getText().toString());

							mAdapter.remove(person);
							mAdapter.add(updatedPerson);
							mAdapter.notifyDataSetChanged();

							dialog.dismiss();
						}
					});

			builder.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			AlertDialog dialog = builder.create();
			dialog.show();
			
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.addPerson:
			addPersonDetails();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void deletePerson() {
		Person person = null;
		if ((mAdapter.getCount() > 0) && (mAdapter.getCount() >= mSelectedItem)) {
			person = (Person) mAdapter.getItem(mSelectedItem);
			peopleDatasource.deletePerson(person);
			mAdapter.remove(person);
		}

		mAdapter.notifyDataSetChanged();
	}
}

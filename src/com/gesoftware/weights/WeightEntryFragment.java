package com.gesoftware.weights;

import java.util.List;

import com.gesoftware.weights.data.PeopleDataSource;
import com.gesoftware.weights.data.Person;
import com.gesoftware.weights.data.Weight;
import com.gesoftware.weights.data.WeightsDataSource;

import android.app.AlertDialog;
import android.app.Fragment;
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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class WeightEntryFragment extends Fragment {
	
	protected Context appContext;
	protected WeightsDataSource weightsDatasource;
	protected ArrayAdapter<Weight> mAdapter;
	protected Object mActionMode;
	public int mSelectedItem = -1;
	private long personId;
	
	public WeightEntryFragment(Context appContext, long id) {
		this.appContext = appContext;
		this.personId = id;
		
		weightsDatasource = new WeightsDataSource(appContext);
		weightsDatasource.open();
				
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View layout = inflater.inflate(R.layout.weight_entry_fragment, container, false);
		// layout_root should be the name of the "top-level" layout node in the
		// dialog_layout.xml file.
		
		final Button btnSubmit = (Button) layout.findViewById(R.id.btnSubmit);
		btnSubmit.setOnClickListener(new Button.OnClickListener() {  
        public void onClick(View v)
            {
        		addWeightDetails();
            }
         });

		
		final ListView listWeights = (ListView) layout
				.findViewById(R.id.listWeights);
		
		// Get weights for selected person
		List<Weight> values = weightsDatasource.getAllWeightsForPerson(personId);

		// Use the SimpleCursorAdapter to show the
		// elements in a ListView
		mAdapter = new ArrayAdapter<Weight>(appContext,
				android.R.layout.simple_list_item_1, values);
		listWeights.setAdapter(mAdapter);

		listWeights.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		listWeights.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				
				mSelectedItem = listWeights.getCheckedItemPosition();

				// Start the CAB using the ActionMode.Callback defined above
				//mActionMode = PersonDetailsActivity.this.startActionMode(mActionModeCallback);
				//arg1.setSelected(true);
				
				return false;
			}
		});
		
		listWeights.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				mSelectedItem = listWeights.getCheckedItemPosition();

				if ((mAdapter.getCount() > 0) && (mAdapter.getCount() >= mSelectedItem)) {
					final Weight weight = (Weight) mAdapter.getItem(mSelectedItem);
					/*Intent intent = new Intent();
					intent.setClass(appContext, PersonDetailsActivity.class);
				    Long personId = person.getId();
				    intent.putExtra(PERSON_ID, personId);
				    startActivity(intent);*/
				}
				
			}
		});
		
		return layout;
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
				//editPersonDetails();
				// Action picked, so close the CAB
				mode.finish();
				return true;
			case R.id.deletePerson:
				//deletePerson();
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

	public final void addWeightDetails() {

		// Preparing views
		LayoutInflater inflater = (LayoutInflater) appContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.weight_entry_fragment, null);
		// layout_root should be the name of the "top-level" layout node in the
		// dialog_layout.xml file.
		final EditText weightBox = (EditText) getActivity().findViewById(R.id.edtWeight);

		// save info where you want it
		Weight newWeight = weightsDatasource.addWeight(personId, Double.parseDouble(weightBox.getText().toString()));

		mAdapter.add(newWeight);
		mAdapter.notifyDataSetChanged();

	}
	
	public final void editPersonDetails() {
/*
		// Preparing views
		LayoutInflater inflater = (LayoutInflater) appContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
							Person updatedPerson = weightsDatasource
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
	*/	
	}

	public void deleteWeight() {
		Weight weight = null;
		if ((mAdapter.getCount() > 0) && (mAdapter.getCount() >= mSelectedItem)) {
			weight = (Weight) mAdapter.getItem(mSelectedItem);
			weightsDatasource.deleteWeight(weight);
			mAdapter.remove(weight);
		}

		mAdapter.notifyDataSetChanged();
	}
}



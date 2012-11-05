package com.gesoftware.weights;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

public class PersonDetailsActivity extends Activity {

	public static Context appContext;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_person_details);

		appContext = this.getApplicationContext();

		// Get the message from the intent
		Intent intent = getIntent();
		long id = intent.getLongExtra(MainActivity.PERSON_ID, -1);

		ActionBar bar = getActionBar();
		bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME, ActionBar.DISPLAY_SHOW_HOME);

		ActionBar.Tab weightEntryTab = bar.newTab().setText("Entry");
		ActionBar.Tab weightGraphTab = bar.newTab().setText("Graph");

		// create the two fragments we want to use for display content
		Fragment weightEntryFragment = new WeightEntryFragment(appContext, id);
		Fragment weightGraphFragment = new WeightGraphFragment(/*appContext, id*/);

		// set the Tab listener. Now we can listen for clicks.
		weightEntryTab.setTabListener(new TabsListener(weightEntryFragment));
		weightGraphTab.setTabListener(new TabsListener(weightGraphFragment));

		// add the two tabs to the actionbar
		bar.addTab(weightEntryTab);
		bar.addTab(weightGraphTab);

		if (savedInstanceState != null) {

			bar.setSelectedNavigationItem(savedInstanceState.getInt("tab", 0));
		}

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// app icon in action bar clicked; go Location selection
			Intent intent = new Intent(PersonDetailsActivity.this,
					MainActivity.class);
			PersonDetailsActivity.this.startActivityForResult(intent, 0);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("tab", getActionBar().getSelectedNavigationIndex());
	}

	class TabsListener implements ActionBar.TabListener {
		public Fragment fragment;

		public TabsListener(Fragment fragment) {
			this.fragment = fragment;
		}

		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft) {
			
		}

		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			ft.replace(R.id.fragment_container, fragment);
		}

		@Override
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			ft.remove(fragment);
		}

	}

}

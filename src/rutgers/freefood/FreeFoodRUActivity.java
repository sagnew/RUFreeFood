package rutgers.freefood;

import java.util.ArrayList;
import java.util.List;

import rutgers.freefood.model.Event;
import rutgers.freefood.model.EventScraper;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class FreeFoodRUActivity extends Activity {

	private ListView listView = null;
	private ArrayList<Event> events = new ArrayList<Event>(25);

	/** Called when the activity is first created. */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lists);
		
		listView = (ListView) findViewById(R.id.list);
		
		// Gets event information and display it.
		(new LoadEvents()).execute();

		// Opens a display to show an individual event
		listView.setClickable(true);
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> adapter, View v,
					int selected, long id) {
				Log.v("Event Clicked", selected + ": "
								+ events.get(selected).toString());
				
				Intent intent = new Intent(FreeFoodRUActivity.this,
						EventDisplayActivity.class);
				intent.putExtra("ChosenEvent", selected);
				intent.putExtra("Events", events);
				startActivity(intent);
			}
		});
		
		registerForContextMenu(listView);

		// Opens texting program.
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent smsIntent = new Intent(Intent.ACTION_SENDTO, Uri
						.parse("smsto:40404"));
				String smsString = events.get(position).getSmsString();
				smsIntent.putExtra("sms_body", smsString);
				startActivity(smsIntent);

				return false;
			}

		});

	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu m){
		MenuInflater inf = this.getMenuInflater();
		inf.inflate(R.menu.refresh_menu, m);
		MenuItem mi = m.findItem(R.id.refresh);
		
		mi.setOnMenuItemClickListener(new OnMenuItemClickListener() {	
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				events = new ArrayList<Event>(25);
				(new LoadEvents()).execute();				
				return false;
			}
		});
		return true;
	}

	/**
	 * Loads event information in a background thread, displaying a dialog box
	 * during the load.
	 * */
	private class LoadEvents extends AsyncTask<Void, Void, Void> {

		ProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(FreeFoodRUActivity.this, null,
					"Loading...", true, false);
			dialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
				events.addAll(EventScraper.getEvents());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Log.e("Loading events", e.getMessage());
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// Creates items in list
			listView.setAdapter(new ArrayAdapter<Event>(
					getApplicationContext(),
					R.layout.list_item, events));
			dialog.dismiss();
		}

	}

}
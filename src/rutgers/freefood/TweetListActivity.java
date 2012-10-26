package rutgers.freefood;

import java.util.ArrayList;
import java.util.List;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Tweet;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
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
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/** This activity is called when looking up the list of food tweets.
 * 
 * @author Student
 *
 */
public class TweetListActivity extends Activity {
	
	private ListView listView = null;
	
	private List<Tweet> tweets;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lists);        
        
        listView = (ListView) findViewById(R.id.list);

	    try{
	    	
		    (new LoadTweets()).execute();
	    	
		    //Send text
		    listView.setOnItemLongClickListener(new OnItemLongClickListener(){
	        	@Override
				public boolean onItemLongClick(AdapterView<?> parent, View view,
						int position, long id) {
					Intent smsIntent = new Intent(Intent.ACTION_SENDTO,Uri.parse("smsto:"));
					String smsString = tweets.get(position).getText();
					smsIntent.putExtra("sms_body", smsString);
					startActivity(smsIntent);
					return false;
				}	
	        });
	    	
	    } catch(Exception e){
	    	Log.e("TweetList", "Error retrieving tweets.");
	    	Toast.makeText(getApplicationContext(), "Error: No internet connection.", 
	    			Toast.LENGTH_SHORT).show();
	    }
	    
        registerForContextMenu(listView);
    }
    
    /** 
     * Performs Twitter search for specified objects
     * @return
     */
    @SuppressWarnings("unused")
	public static List<Tweet> findTweets(){
		
		Twitter tw = new TwitterFactory().getInstance();
		QueryResult result, result2, result3;
		result = result2 = result3 = null;
		try {
			result = tw.search(new Query("Rutgers Free Food"));
			result2 = tw.search(new Query("#RUFreeFood"));
			result3 = tw.search(new Query("#RutgersFreeFood"));
		} catch (TwitterException e) {
			Log.e("TwitterError", e.getMessage());
		}
		
		
		if(result != null){
		    List<Tweet> tweets = result.getTweets();
		    return tweets;
		}
		return null;
	}
    
    
    @Override
	public boolean onCreateOptionsMenu(Menu m){
		MenuInflater inf = this.getMenuInflater();
		inf.inflate(R.menu.refresh_menu, m);
		MenuItem mi = m.findItem(R.id.refresh);
		
		mi.setOnMenuItemClickListener(new OnMenuItemClickListener() {	
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				(new LoadTweets()).execute();				
				return true;
			}
		});
		return true;
	}
    
    
    /**
	 * Loads event information in a background thread, displaying a dialog box
	 * during the load.
	 * */
	private class LoadTweets extends AsyncTask<Void, Void, Void> {

		List<String> tweetTexts;
		ProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(TweetListActivity.this, null,
					"Loading...", true, false);
			dialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {

			tweets = findTweets();
			
		    tweetTexts = new ArrayList<String>();
		    if(tweets != null){
		    	for(Tweet tweet : tweets){
		    		tweetTexts.add(tweet.getText());
		    	}
		    }
		    
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			
			if(tweetTexts.size() != 0){
				// Creates items in list
				listView.setAdapter(
			    		new ArrayAdapter<String>(getApplicationContext(),
			    				R.layout.list_item, tweetTexts));
			}
			dialog.dismiss();

		}

	}
    
    
}
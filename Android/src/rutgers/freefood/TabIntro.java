package rutgers.freefood;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class TabIntro extends TabActivity {
	
	
	/** Called when the activity is first created. */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		TabHost tabs = this.getTabHost();
		
		TabSpec eventTab = tabs.newTabSpec("events tab");
		eventTab.setIndicator("Events").setContent(new Intent(this, FreeFoodRUActivity.class));
		tabs.addTab(eventTab);
		
		TabSpec tweetTab = tabs.newTabSpec("twitter tab");
		tweetTab.setIndicator("Twitter").setContent(new Intent(this, TweetListActivity.class));
		tabs.addTab(tweetTab);
		
		
		int numTabs = tabs.getTabWidget().getChildCount();
		for(int i = 0; i < numTabs; i++){
		  tabs.getTabWidget().getChildAt(i).getLayoutParams().height /= 2;
		}
		tabs.setCurrentTab(0);
	}

}

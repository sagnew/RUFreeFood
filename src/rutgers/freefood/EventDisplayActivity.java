package rutgers.freefood;

import java.util.ArrayList;
import java.util.List;

import rutgers.freefood.model.Event;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class EventDisplayActivity extends Activity {
	private List<Event> events;
	private ViewPager vp;
	
	/** Called when the activity is first created. */

	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.eventdisplay);
		
		
		
		vp = (ViewPager) findViewById(R.id.pager);
		vp.setAdapter(new EventPagerAdapter(this));

		Intent intent = getIntent();
		events = (ArrayList<Event>) intent.getSerializableExtra("Events");
		int chosen = intent.getIntExtra("ChosenEvent", 0);
		vp.setCurrentItem(chosen);
		
		
	}
	
	

	private class EventPagerAdapter extends PagerAdapter {

		private Context context;

		public EventPagerAdapter(Context c) {
			this.context = c;
		}

		@Override
		public int getCount() {
			return events.size();
		}

		@Override
		public Object instantiateItem(ViewGroup parent, int position) {			
			ScrollView scroll;
			LinearLayout lin;
			Event e = events.get(position);
			Log.i("Instantiate", e.getName());
			
			if(parent.getChildAt(position) != null){
				scroll = (ScrollView) parent.getChildAt(position);
				lin = (LinearLayout) scroll.getChildAt(0);
			} else {
				scroll = new ScrollView(context);
				LayoutInflater inf = EventDisplayActivity.this.getLayoutInflater();
				lin = (LinearLayout) inf.inflate(R.layout.eventdisplay_item, null);
				scroll.addView(lin);
				parent.addView(scroll);
			}
			
			((TextView)lin.getChildAt(1)).setText(e.getName());
			((TextView)lin.getChildAt(3)).setText(e.getDateString());
			((TextView)lin.getChildAt(5)).setText(e.getDescription());
			((TextView)lin.getChildAt(7)).setText(e.getCampus());
			return scroll;
		}

		@Override
		public void destroyItem(ViewGroup parent, int position, Object view) {
//			parent.removeViewAt(position);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

	}
	
	

}
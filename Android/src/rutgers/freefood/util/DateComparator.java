package rutgers.freefood.util;

import java.util.Comparator;

import rutgers.freefood.model.Event;


public class DateComparator implements Comparator<Event> {

	@Override
	public int compare(Event e1, Event e2) {
		if(e1 == null)
			return 1;
		if(e2 == null)
			return -1;
		
		if (e1.startDate.getTimeInMillis() > e2.startDate.getTimeInMillis())
			return 1;
		else
			return -1;
	}

}
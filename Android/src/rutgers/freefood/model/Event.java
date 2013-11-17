package rutgers.freefood.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Event implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String name;
	
	public GregorianCalendar startDate;
	public GregorianCalendar endDate;
	
	public String description;
	public String campus;
	public String building;
	public String foodType;
	
	public Event(GregorianCalendar start, GregorianCalendar end, String description, String campus, String name)
	{
		this.name = name;
		this.startDate = start;
		this.endDate = end;
		this.description = description;
		this.campus = campus;
		this.foodType = "--";
	}
	
	public Event(GregorianCalendar start, GregorianCalendar end, String description, String campus, String name, String foodtype)
	{
		this.name = name;
		this.startDate = start;
		this.endDate = end;
		this.description = description;
		this.campus = campus;
		this.foodType = foodtype;
	}	
	
	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}

	public GregorianCalendar getStartDate() {
		return startDate;
	}
	
	public void setStartDate(GregorianCalendar startDate) {
		this.startDate = startDate;
	}
	
	public GregorianCalendar getEndDate() {
		return endDate;
	}
	
	public void setEndDate(GregorianCalendar endDate) {
		this.endDate = endDate;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getCampus() {
		return campus;
	}
	
	public void setCampus(String campus) {
		this.campus = campus;
	}
	
	public String getBuilding() {
		return building;
	}
	
	public void setBuilding(String building) {
		this.building = building;
	}
	
	public String getFoodType() {
		return foodType;
	}
	
	public String getDateString(){
		return "" + (startDate.get(Calendar.MONTH)+1) + "/" + 
				startDate.get(Calendar.DATE) + "/" +
				startDate.get(Calendar.YEAR);
	}
	
	@Override
	public String toString() {
		return this.name;
	}
	
	public String getSmsString(){
		String smsMessage = "Hey! #RUFreeFood @ " + this.name + " on " 
		+ this.getDateString() + ": " + this.description;
		return smsMessage;
	}

}

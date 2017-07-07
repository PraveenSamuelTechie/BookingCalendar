package com.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Booking {
	
	private String name;
	private Calendar startDateTime;
	private Calendar endDateTime;
	private String repeatType;
	private String bookingNote;
	private String startTime;
	private String endTime;
	
	public Booking(String name, Calendar startDateTime, Calendar endDateTime, String repeatType, String bookingNote) {		
		super();
		this.name = name;
		this.startDateTime = startDateTime;
		this.endDateTime = endDateTime;
		this.repeatType = repeatType;
		this.bookingNote = bookingNote;		
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");		
		startTime = sdf.format(startDateTime.getTime());
		sdf = new SimpleDateFormat("HH:mm");
		endTime = sdf.format(endDateTime.getTime());
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Calendar getStartDateTime() {
		return startDateTime;
	}
	public void setStartDateTime(Calendar startDateTime) {
		this.startDateTime = startDateTime;
	}
	public Calendar getEndDateTime() {
		return endDateTime;
	}
	public void setEndDateTime(Calendar endDateTime) {
		this.endDateTime = endDateTime;
	}
	public String getRepeatType() {
		return repeatType;
	}
	public void setRepeatType(String repeatType) {
		this.repeatType = repeatType;
	}
	public String getBookingNote() {
		return bookingNote;
	}
	public void setBookingNote(String bookingNote) {
		this.bookingNote = bookingNote;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	
}

package com.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class Resource {

	private String name;
	private Calendar startTime;
	private Calendar endTime;
	private List<Booking> bookingList;
	private String startTm;
	private String endTm;
	
	public Resource(String name, Calendar startTime, Calendar endTime) {
		super();
		this.name = name;
		this.startTime = startTime;
		this.endTime = endTime;
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");		
		startTm = sdf.format(startTime.getTime());
		sdf = new SimpleDateFormat("HH:mm");
		endTm = sdf.format(endTime.getTime());
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Calendar getStartTime() {
		return startTime;
	}

	public void setStartTime(Calendar startTime) {
		this.startTime = startTime;
	}

	public Calendar getEndTime() {
		return endTime;
	}

	public void setEndTime(Calendar endTime) {
		this.endTime = endTime;
	}

	public List<Booking> getBookingList() {
		return bookingList;
	}

	public void setBookingList(List<Booking> bookingList) {
		this.bookingList = bookingList;
	}

	public String getStartTm() {
		return startTm;
	}

	public void setStartTm(String startTm) {
		this.startTm = startTm;
	}

	public String getEndTm() {
		return endTm;
	}

	public void setEndTm(String endTm) {
		this.endTm = endTm;
	}
}

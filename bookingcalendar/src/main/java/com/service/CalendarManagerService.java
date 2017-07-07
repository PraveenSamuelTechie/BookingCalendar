package com.service;

import java.util.Calendar;
import java.util.List;
import java.util.Map;


public interface CalendarManagerService {	
	
	//Functionality to fetch Booking details of the resources for a specific date and time interval
	public Map<Object, List<Object>> getAvailabilityDayByResourceHtmlTable(Calendar date, Integer interval);

	//Functionality to fetch Booking details of the resources for a date range and time interval : Method overloading
	public Map<Object, List<Object>> getAvailabilityDayByResourceHtmlTable(Calendar startDate, Calendar endDate, Integer interval);
}

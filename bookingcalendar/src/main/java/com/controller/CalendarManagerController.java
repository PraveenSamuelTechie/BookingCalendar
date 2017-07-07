package com.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.service.CalendarManagerService;

@RestController
public class CalendarManagerController {
	
	@Autowired
    CalendarManagerService calendarManagerService;
	
	//Controller to load the initial booking page
	@RequestMapping("/booking")
	public ModelAndView bookingCalendar(HttpServletRequest request)
    {
        return new ModelAndView("booking");
	}
	
	//Controller to load the initial booking advanced page
	@RequestMapping("/bookingAdvanced")
	public ModelAndView bookingCalendarAdvanced(HttpServletRequest request)
    {
        return new ModelAndView("bookingAdvanced");
	}
	
	//Controller to load the results of the booking
	@RequestMapping("/bookingResults")
	public ModelAndView calendarQuery(HttpServletRequest request)
    {
		Map<Object,List<Object>> map = new LinkedHashMap<Object,List<Object>>();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
		Date startDate = null;
		try
		{			
			String datePicker = request.getParameter("datepicker");
			String interval = request.getParameter("interval");
			sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
			startDate = null;
			startDate = sdf.parse(datePicker);
			Calendar startDtTime = sdf.getCalendar();
			startDtTime.setTime(startDate);
			//Function which calculates resources availability for a specific date
			map = calendarManagerService.getAvailabilityDayByResourceHtmlTable(startDtTime, new Integer(interval));
		}
		catch(Exception ex)
		{
			//Setting default test just in case of any issues in the front end
			try {
				startDate = sdf.parse("13/05/2017");
				Calendar startDtTime = sdf.getCalendar();
				startDtTime.setTime(startDate);
				map = calendarManagerService.getAvailabilityDayByResourceHtmlTable(startDtTime, new Integer(30));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("getAvailabilityDayByResourceHtmlTable for a specific date"+e.getMessage());
			}			
		}
        return new ModelAndView("booking","bookingMap", map);
	}
	
	//Controller to load the results of the booking advanced page
	@RequestMapping("/bookingAdvancedResults")
	public ModelAndView calendarAdvancedQuery(HttpServletRequest request)
    {
		Map<Object,List<Object>> map = new LinkedHashMap<Object,List<Object>>();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
		Date startDate = null;
		Date endDate = null;
		try
		{			
			//Fetching the data inputs from the user
			String startDatePicker = request.getParameter("startDatepicker");
			String endDatePicker = request.getParameter("endDatepicker");
			String interval = request.getParameter("interval");
			sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
			startDate = null;
			endDate = null;
			startDate = sdf.parse(startDatePicker);
			Calendar startDtTime = sdf.getCalendar();
			startDtTime.setTime(startDate);
			sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
			endDate = sdf.parse(endDatePicker);
			Calendar endDtTime = sdf.getCalendar();
			endDtTime.setTime(endDate);
			//Function which calculates resources availability for a given time range
			map = calendarManagerService.getAvailabilityDayByResourceHtmlTable(startDtTime, endDtTime, new Integer(interval));
		}
		catch(Exception ex)
		{
			//Setting default test just in case of any issues in the front end
			try {
				startDate = sdf.parse("13/01/2017");
				Calendar startDtTime = sdf.getCalendar();
				startDtTime.setTime(startDate);
				sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
				endDate = sdf.parse("16/05/2017");
				Calendar endDtTime = sdf.getCalendar();
				endDtTime.setTime(endDate);
				map = calendarManagerService.getAvailabilityDayByResourceHtmlTable(startDtTime, endDtTime, new Integer(30));
			} catch (Exception e) {
				System.out.println("getAvailabilityDayByResourceHtmlTable for a specific date range"+e.getMessage());
			}			
		}
        return new ModelAndView("bookingAdvanced","bookingMap", map);
	}
	
}

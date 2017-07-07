package com.serviceimpl;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.model.Booking;
import com.model.CalendarManager;
import com.model.Resource;
import com.service.CalendarManagerService;

@Service
public class CalendarManagerServiceImpl implements CalendarManagerService {

    @Override
    public Map < Object, List < Object >> getAvailabilityDayByResourceHtmlTable(Calendar date, Integer interval) {
        CalendarManager calendarManager = LoadTestData();
        long rowCount = 0;

        //Used LinkedHashMap to preserve the order of insertion and to populate the table
        Map < Object, List < Object >> bookingMap = new LinkedHashMap < Object, List < Object >> ();
        List < Object > yAxisList = new ArrayList < Object > ();
        try {
            Date d = date.getTime();

            Calendar startTime = Calendar.getInstance();

            //Setting the start time of the company (Working Hours)
            startTime.setTime(d);
            startTime.set(Calendar.HOUR_OF_DAY, 8);
            startTime.set(Calendar.MINUTE, 00);
            startTime.set(Calendar.SECOND, 00);

            Calendar endTime = Calendar.getInstance();

            //Setting the end time of the company (Working Hours)
            endTime.setTime(d);
            endTime.set(Calendar.HOUR_OF_DAY, 17);
            endTime.set(Calendar.MINUTE, 00);
            endTime.set(Calendar.SECOND, 00);

            Calendar currentStartTime = Calendar.getInstance();


            //Setting the initial time interval of the input date
            currentStartTime.setTime(d);
            currentStartTime.set(Calendar.HOUR_OF_DAY, 8);
            currentStartTime.set(Calendar.MINUTE, 00);
            currentStartTime.set(Calendar.SECOND, 00);

            Calendar currentEndTime = Calendar.getInstance();

            currentEndTime.setTime(d);
            currentEndTime.set(Calendar.HOUR_OF_DAY, 8);
            currentEndTime.set(Calendar.MINUTE, 00);
            currentEndTime.set(Calendar.SECOND, 00);
            currentEndTime.add(Calendar.MINUTE, interval);


            String timeFromTo = null;

            //Getting the list of resources to populate the table header. Used Java 8 stream utility to improve the performance
            Object[] resourceObj = calendarManager.getResourceList().stream().map(b-> b.getName()).toArray();

            List < Object > resourceList = Arrays.asList(resourceObj);
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

            bookingMap.put(df.format(currentStartTime.getTime()), resourceList);

            //Iterating over the time interval until it is within the working hours of the company
            while ((currentStartTime.after(startTime) && currentEndTime.before(endTime) ||
                    (currentStartTime.equals(startTime) && currentEndTime.before(endTime)) ||
                    (currentStartTime.after(startTime) && currentEndTime.equals(endTime)))) {
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                String startTm = sdf.format(currentStartTime.getTime());
                sdf = new SimpleDateFormat("HH:mm");
                String endTm = sdf.format(currentEndTime.getTime());
                sdf = new SimpleDateFormat("HH:mm");
                long startTimeMilliseconds = currentStartTime.getTimeInMillis();
                long endTimeMilliseconds = currentEndTime.getTimeInMillis();
                timeFromTo = sdf.format(new Date(startTimeMilliseconds)) + " - " + sdf.format(new Date(endTimeMilliseconds));
                yAxisList = new ArrayList < Object > ();

                //Iterating over each resource to fetch their booking details
                for (Resource resource: calendarManager.getResourceList()) {
                    rowCount = 0;
                    SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");

                    //Checking if the current time interval is within the resource's working hours
                    if ((LocalTime.parse(resource.getStartTm()).equals(LocalTime.parse(dateFormat.format(currentStartTime.getTime()))) &&
                            LocalTime.parse(resource.getEndTm()).equals(LocalTime.parse(dateFormat.format(currentEndTime.getTime())))) ||
                        (LocalTime.parse(resource.getStartTm()).equals(LocalTime.parse(dateFormat.format(currentStartTime.getTime()))) &&
                            LocalTime.parse(resource.getEndTm()).isAfter(LocalTime.parse(dateFormat.format(currentEndTime.getTime())))) ||
                        (LocalTime.parse(resource.getStartTm()).isBefore(LocalTime.parse(dateFormat.format(currentStartTime.getTime()))) &&
                            LocalTime.parse(resource.getEndTm()).isAfter(LocalTime.parse(dateFormat.format(currentEndTime.getTime())))) ||
                        (LocalTime.parse(resource.getStartTm()).isBefore(LocalTime.parse(dateFormat.format(currentStartTime.getTime()))) &&
                            LocalTime.parse(resource.getEndTm()).equals(LocalTime.parse(dateFormat.format(currentEndTime.getTime()))))) {
                        //Checking if the time interval has any relevant bookings - Used Java 8 stream utility to improve performance.
                        rowCount = resource.getBookingList().
                        stream().filter(b ->
                            (b.getStartDateTime().after(currentStartTime) && b.getStartDateTime().before(currentEndTime) ||
                                b.getEndDateTime().after(currentStartTime) && b.getEndDateTime().before(currentEndTime) ||
                                currentStartTime.after(b.getStartDateTime()) && currentStartTime.before(b.getEndDateTime()) ||
                                currentEndTime.after(b.getStartDateTime()) && currentEndTime.before(b.getEndDateTime())) ||
                            (null != b.getRepeatType() && b.getRepeatType().equalsIgnoreCase("REPEATS DAILY") &&
                                ((LocalTime.parse(b.getStartTime()).isAfter(LocalTime.parse(startTm)) && LocalTime.parse(b.getStartTime()).isBefore(LocalTime.parse(startTm)) ||
                                    (LocalTime.parse(b.getEndTime()).isAfter(LocalTime.parse(startTm)) && LocalTime.parse(b.getEndTime()).isBefore(LocalTime.parse(endTm)) ||
                                        LocalTime.parse(startTm).isAfter(LocalTime.parse(b.getStartTime())) && LocalTime.parse(startTm).isBefore(LocalTime.parse(b.getEndTime())) ||
                                        LocalTime.parse(endTm).isAfter(LocalTime.parse(b.getStartTime())) && LocalTime.parse(endTm).isBefore(LocalTime.parse(b.getEndTime()))))))).count();

                        if (rowCount > 0) {
                            yAxisList.add("");
                        } else {
                            yAxisList.add("Free");
                        }
                    } else {
                        yAxisList.add("");
                    }
                }
                bookingMap.put(timeFromTo, yAxisList);

                //Incrementing the time interval for the next iteration
                currentStartTime.add(Calendar.MINUTE, interval);
                currentEndTime.add(Calendar.MINUTE, interval);

            }

        } catch (Exception ex) {
            System.out.println("Error in getAvailabilityDayByResourceHtmlTable "+ex.getMessage());
        }
        return bookingMap;
    }

    @Override
    public Map < Object, List < Object >> getAvailabilityDayByResourceHtmlTable(Calendar startDate, Calendar endDate, Integer interval) {
        CalendarManager calendarManager = LoadTestData();
        long rowCount = 0;
        long resourceCount = 0;
        long totalResources = 0;
        List < Object > datesList = new ArrayList < Object > ();
        //Used LinkedHashMap to preserve the order of insertion and to populate the table
        Map < Object, List < Object >> bookingMap = new LinkedHashMap < Object, List < Object >> ();
        List < Object > xAxisList = new ArrayList < Object > ();
        try {
            //Getting the total numbers of resources in the company
            if (null != calendarManager && null != calendarManager.getResourceList()) {
                totalResources = calendarManager.getResourceList().size();
            }
            Date d = startDate.getTime();

            //Setting the start time of the company (Working Hours)
            Calendar startTime = Calendar.getInstance();

            startTime.setTime(d);
            startTime.set(Calendar.HOUR_OF_DAY, 8);
            startTime.set(Calendar.MINUTE, 00);
            startTime.set(Calendar.SECOND, 00);

            //Setting the end time of the company (Working Hours)
            Calendar endTime = Calendar.getInstance();

            endTime.setTime(d);
            endTime.set(Calendar.HOUR_OF_DAY, 17);
            endTime.set(Calendar.MINUTE, 00);
            endTime.set(Calendar.SECOND, 00);

            Calendar currentStartTime = Calendar.getInstance();


            currentStartTime.setTime(d);
            currentStartTime.set(Calendar.HOUR_OF_DAY, 8);
            currentStartTime.set(Calendar.MINUTE, 00);
            currentStartTime.set(Calendar.SECOND, 00);

            Calendar currentEndTime = Calendar.getInstance();

            currentEndTime.setTime(d);
            currentEndTime.set(Calendar.HOUR_OF_DAY, 8);
            currentEndTime.set(Calendar.MINUTE, 00);
            currentEndTime.set(Calendar.SECOND, 00);
            currentEndTime.add(Calendar.MINUTE, interval);


            String timeFromTo = null;

            int counter = 0;

            //Iterating over the time interval until it is within the working hours of the company
            while ((currentStartTime.after(startTime) && currentEndTime.before(endTime) ||
                    (currentStartTime.equals(startTime) && currentEndTime.before(endTime)) ||
                    (currentStartTime.after(startTime) && currentEndTime.equals(endTime)))) {
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                String startTm = sdf.format(currentStartTime.getTime());
                sdf = new SimpleDateFormat("HH:mm");
                String endTm = sdf.format(currentEndTime.getTime());
                sdf = new SimpleDateFormat("HH:mm");
                long startTimeMilliseconds = currentStartTime.getTimeInMillis();
                long endTimeMilliseconds = currentEndTime.getTimeInMillis();

                //Getting the time interval to be displayed on the table 
                timeFromTo = sdf.format(new Date(startTimeMilliseconds)) + " - " + sdf.format(new Date(endTimeMilliseconds));
                xAxisList = new ArrayList < Object > ();
                rowCount = 0;

                Calendar innerStartTime = Calendar.getInstance();
                Calendar innerEndTime = Calendar.getInstance();

                innerStartTime.setTimeInMillis(currentStartTime.getTimeInMillis());
                innerEndTime.setTimeInMillis(currentEndTime.getTimeInMillis());

                Calendar innerStartDate = Calendar.getInstance();
                Calendar innerEndDate = Calendar.getInstance();

                innerStartDate.setTimeInMillis(startDate.getTimeInMillis());
                innerEndDate.setTimeInMillis(startDate.getTimeInMillis());


                //Inner Loop which loops over all the dates for one particular time interval 
                while ((innerStartDate.getTime().after(startDate.getTime()) && innerEndDate.getTime().before(endDate.getTime())) ||
                    (innerStartDate.getTime().equals(startDate.getTime()) && innerEndDate.getTime().before(endDate.getTime())) ||
                    (innerStartDate.getTime().after(startDate.getTime()) && innerEndDate.getTime().equals(endDate.getTime())) ||
                    (innerStartDate.getTime().equals(startDate.getTime()) && innerEndDate.getTime().equals(endDate.getTime()))) {
                    resourceCount = 0;

                    //Iterating over each resource 
                    for (Resource resource: calendarManager.getResourceList()) {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");

                        //Checking if the current time interval is within the resource's working hours
                        if ((LocalTime.parse(resource.getStartTm()).equals(LocalTime.parse(dateFormat.format(currentStartTime.getTime()))) &&
                                LocalTime.parse(resource.getEndTm()).equals(LocalTime.parse(dateFormat.format(currentEndTime.getTime())))) ||
                            (LocalTime.parse(resource.getStartTm()).equals(LocalTime.parse(dateFormat.format(currentStartTime.getTime()))) &&
                                LocalTime.parse(resource.getEndTm()).isAfter(LocalTime.parse(dateFormat.format(currentEndTime.getTime())))) ||
                            (LocalTime.parse(resource.getStartTm()).isBefore(LocalTime.parse(dateFormat.format(currentStartTime.getTime()))) &&
                                LocalTime.parse(resource.getEndTm()).isAfter(LocalTime.parse(dateFormat.format(currentEndTime.getTime())))) ||
                            (LocalTime.parse(resource.getStartTm()).isBefore(LocalTime.parse(dateFormat.format(currentStartTime.getTime()))) &&
                                LocalTime.parse(resource.getEndTm()).equals(LocalTime.parse(dateFormat.format(currentEndTime.getTime()))))) {

                            //Checking if the time interval has any relevant bookings - Used Java 8 stream utility to improve performance.
                            rowCount = resource.getBookingList().
                            stream().filter(b ->
                                (b.getStartDateTime().after(innerStartTime) && b.getStartDateTime().before(innerEndTime) ||
                                    b.getEndDateTime().after(innerStartTime) && b.getEndDateTime().before(innerEndTime) ||
                                    innerStartTime.after(b.getStartDateTime()) && innerStartTime.before(b.getEndDateTime()) ||
                                    innerEndTime.after(b.getStartDateTime()) && innerEndTime.before(b.getEndDateTime())) ||
                                (null != b.getRepeatType() && b.getRepeatType().equalsIgnoreCase("REPEATS DAILY") &&
                                    ((LocalTime.parse(b.getStartTime()).isAfter(LocalTime.parse(startTm)) && LocalTime.parse(b.getStartTime()).isBefore(LocalTime.parse(startTm)) ||
                                        (LocalTime.parse(b.getEndTime()).isAfter(LocalTime.parse(startTm)) && LocalTime.parse(b.getEndTime()).isBefore(LocalTime.parse(endTm)) ||
                                            LocalTime.parse(startTm).isAfter(LocalTime.parse(b.getStartTime())) && LocalTime.parse(startTm).isBefore(LocalTime.parse(b.getEndTime())) ||
                                            LocalTime.parse(endTm).isAfter(LocalTime.parse(b.getStartTime())) && LocalTime.parse(endTm).isBefore(LocalTime.parse(b.getEndTime()))))))).count();

                            if (rowCount > 0) {
                                resourceCount++;
                            }
                        } else {
                            resourceCount++;
                        }
                    }
                    if (totalResources - resourceCount == 0) {
                        xAxisList.add("");
                    } else {
                        xAxisList.add(totalResources - resourceCount + " Free");
                    }
                    if (counter == 0) {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        datesList.add(dateFormat.format(innerStartTime.getTime()));
                    }

                    //Incrementing the Date
                    innerStartTime.add(Calendar.DATE, 1);
                    innerEndTime.add(Calendar.DATE, 1);
                    innerStartDate.add(Calendar.DATE, 1);
                    innerEndDate.add(Calendar.DATE, 1);

                }
                bookingMap.put("Dates", datesList);
                bookingMap.put(timeFromTo, xAxisList);
                //Incrementing the Time interval for a particular date
                currentStartTime.add(Calendar.MINUTE, interval);
                currentEndTime.add(Calendar.MINUTE, interval);
                counter++;

            }

        } catch (Exception ex) {
            System.out.println("Error in getAvailabilityDayByResourceHtmlTable for a specified date frame "+ex.getMessage());
        }

        return bookingMap;
    }

    //A method which has all the test data : In real world scenario, the data will be populated from a database
    public CalendarManager LoadTestData() {
        Date startDateTime = null;
        Date endDateTime = null;
        CalendarManager calendarManager = new CalendarManager();
        Resource resource = null;
        List < Resource > resourceList = new ArrayList < Resource > ();

        Booking booking = null;
        List < Booking > bookingList = null;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
        try {
            //Resource 1
            Calendar startDtTime = sdf.getCalendar();
            Calendar endDtTime = sdf.getCalendar();
            bookingList = new ArrayList < Booking > ();
            Calendar startTime = Calendar.getInstance();
            startTime.set(Calendar.HOUR_OF_DAY, 9);
            startTime.set(Calendar.MINUTE, 00);
            Calendar endTime = Calendar.getInstance();
            endTime.set(Calendar.HOUR_OF_DAY, 17);
            endTime.set(Calendar.MINUTE, 00);

            resource = new Resource("Joe", startTime, endTime);

            //Booking 1
            startDateTime = null;
            endDateTime = null;
            booking = null;
            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
            startDateTime = sdf.parse("2017-05-13 10:05");
            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
            endDateTime = sdf.parse("2017-05-13 13:00");

            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
            startDtTime = sdf.getCalendar();
            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
            endDtTime = sdf.getCalendar();

            startDtTime.setTime(startDateTime);
            endDtTime.setTime(endDateTime);
            booking = new Booking("Joe", startDtTime, endDtTime, null, "Booking 56");
            bookingList.add(booking);
            resource.setBookingList(bookingList);

            resourceList.add(resource);

            //Resource 2
            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
            startDtTime = sdf.getCalendar();
            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
            endDtTime = sdf.getCalendar();

            bookingList = new ArrayList < Booking > ();
            startTime = Calendar.getInstance();
            startTime.set(Calendar.HOUR_OF_DAY, 9);
            startTime.set(Calendar.MINUTE, 00);
            endTime = Calendar.getInstance();
            endTime.set(Calendar.HOUR_OF_DAY, 16);
            endTime.set(Calendar.MINUTE, 00);
            resource = new Resource("Bob", startTime, endTime);

            //Booking 1
            startDateTime = null;
            endDateTime = null;
            booking = null;
            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
            startDateTime = sdf.parse("2017-05-13 08:15");
            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
            endDateTime = sdf.parse("2017-05-13 10:45");

            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
            startDtTime = sdf.getCalendar();
            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
            endDtTime = sdf.getCalendar();

            startDtTime.setTime(startDateTime);
            endDtTime.setTime(endDateTime);
            booking = new Booking("Bob", startDtTime, endDtTime, null, "Booking 87");
            bookingList.add(booking);

            //Booking 2
            startDateTime = null;
            endDateTime = null;
            booking = null;
            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
            startDateTime = sdf.parse("2017-05-13 13:15");
            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
            endDateTime = sdf.parse("2017-05-13 14:00");

            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
            startDtTime = sdf.getCalendar();
            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
            endDtTime = sdf.getCalendar();

            startDtTime.setTime(startDateTime);
            endDtTime.setTime(endDateTime);
            booking = new Booking("Bob", startDtTime, endDtTime, null, "Booking 23");
            bookingList.add(booking);
            resource.setBookingList(bookingList);
            resourceList.add(resource);

            //Resource 3
            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
            startDtTime = sdf.getCalendar();
            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
            endDtTime = sdf.getCalendar();
            bookingList = new ArrayList < Booking > ();
            startTime = Calendar.getInstance();
            startTime.set(Calendar.HOUR_OF_DAY, 10);
            startTime.set(Calendar.MINUTE, 00);
            endTime = Calendar.getInstance();
            endTime.set(Calendar.HOUR_OF_DAY, 17);
            endTime.set(Calendar.MINUTE, 00);
            resource = new Resource("Jane", startTime, endTime);

            //Booking 1
            startDateTime = null;
            endDateTime = null;
            booking = null;
            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
            startDateTime = sdf.parse("2016-01-13 11:45");
            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
            endDateTime = sdf.parse("2016-01-13 12:45");

            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
            startDtTime = sdf.getCalendar();
            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
            endDtTime = sdf.getCalendar();

            startDtTime.setTime(startDateTime);
            endDtTime.setTime(endDateTime);
            booking = new Booking("Jane", startDtTime, endDtTime, "REPEATS DAILY", "Early Lunch");
            bookingList.add(booking);
            resource.setBookingList(bookingList);
            resourceList.add(resource);
            calendarManager.setResourceList(resourceList);
            
          //Resource 4
            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
            startDtTime = sdf.getCalendar();
            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
            endDtTime = sdf.getCalendar();
            bookingList = new ArrayList < Booking > ();
            startTime = Calendar.getInstance();
            startTime.set(Calendar.HOUR_OF_DAY, 8);
            startTime.set(Calendar.MINUTE, 00);
            endTime = Calendar.getInstance();
            endTime.set(Calendar.HOUR_OF_DAY, 17);
            endTime.set(Calendar.MINUTE, 00);
            resource = new Resource("Praveen", startTime, endTime);

            //Booking 1
            startDateTime = null;
            endDateTime = null;
            booking = null;
            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
            startDateTime = sdf.parse("2017-07-07 11:05");
            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
            endDateTime = sdf.parse("2017-07-07 14:31");

            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
            startDtTime = sdf.getCalendar();
            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
            endDtTime = sdf.getCalendar();

            startDtTime.setTime(startDateTime);
            endDtTime.setTime(endDateTime);
            booking = new Booking("Praveen", startDtTime, endDtTime, null, "Booking 445");
            bookingList.add(booking);
            resource.setBookingList(bookingList);
            resourceList.add(resource);
            calendarManager.setResourceList(resourceList);


        } catch (Exception ex) {
            // TODO Auto-generated catch block
            System.out.println("Error in Load Test Data "+ex.getMessage());
        }
        return calendarManager;
    }
}
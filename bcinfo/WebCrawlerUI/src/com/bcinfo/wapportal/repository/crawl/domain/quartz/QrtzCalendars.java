package com.bcinfo.wapportal.repository.crawl.domain.quartz; 
public class QrtzCalendars { 
  public static final String TABLE_NAME = "QRTZ_CALENDARS";
  public static final String CALENDAR_NAME = "CALENDAR_NAME";
  private String calendarName; 

  public String getCalendarName() {
    return calendarName;
  }
  public void setCalendarName(String calendarName) {
    this.calendarName = calendarName;
  }
  public static final String CALENDAR = "CALENDAR";
  private String calendar; 

  public String getCalendar() {
    return calendar;
  }
  public void setCalendar(String calendar) {
    this.calendar = calendar;
  }
} 

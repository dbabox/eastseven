package com.bcinfo.wapportal.repository.crawl.domain.quartz; 
public class QrtzCronTriggers { 
  public static final String TABLE_NAME = "QRTZ_CRON_TRIGGERS";
  public static final String TRIGGER_NAME = "TRIGGER_NAME";
  private String triggerName; 

  public String getTriggerName() {
    return triggerName;
  }
  public void setTriggerName(String triggerName) {
    this.triggerName = triggerName;
  }
  public static final String TRIGGER_GROUP = "TRIGGER_GROUP";
  private String triggerGroup; 

  public String getTriggerGroup() {
    return triggerGroup;
  }
  public void setTriggerGroup(String triggerGroup) {
    this.triggerGroup = triggerGroup;
  }
  public static final String CRON_EXPRESSION = "CRON_EXPRESSION";
  private String cronExpression; 

  public String getCronExpression() {
    return cronExpression;
  }
  public void setCronExpression(String cronExpression) {
    this.cronExpression = cronExpression;
  }
  public static final String TIME_ZONE_ID = "TIME_ZONE_ID";
  private String timeZoneId; 

  public String getTimeZoneId() {
    return timeZoneId;
  }
  public void setTimeZoneId(String timeZoneId) {
    this.timeZoneId = timeZoneId;
  }
} 

package com.bcinfo.wapportal.repository.crawl.domain.quartz; 
public class QrtzSimpleTriggers { 
  public static final String TABLE_NAME = "QRTZ_SIMPLE_TRIGGERS";
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
  public static final String REPEAT_COUNT = "REPEAT_COUNT";
  private long repeatCount; 

  public long getRepeatCount() {
    return repeatCount;
  }
  public void setRepeatCount(long repeatCount) {
    this.repeatCount = repeatCount;
  }
  public static final String REPEAT_INTERVAL = "REPEAT_INTERVAL";
  private long repeatInterval; 

  public long getRepeatInterval() {
    return repeatInterval;
  }
  public void setRepeatInterval(long repeatInterval) {
    this.repeatInterval = repeatInterval;
  }
  public static final String TIMES_TRIGGERED = "TIMES_TRIGGERED";
  private long timesTriggered; 

  public long getTimesTriggered() {
    return timesTriggered;
  }
  public void setTimesTriggered(long timesTriggered) {
    this.timesTriggered = timesTriggered;
  }
} 

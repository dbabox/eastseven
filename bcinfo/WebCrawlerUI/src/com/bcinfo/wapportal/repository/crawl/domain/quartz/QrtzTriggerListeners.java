package com.bcinfo.wapportal.repository.crawl.domain.quartz; 
public class QrtzTriggerListeners { 
  public static final String TABLE_NAME = "QRTZ_TRIGGER_LISTENERS";
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
  public static final String TRIGGER_LISTENER = "TRIGGER_LISTENER";
  private String triggerListener; 

  public String getTriggerListener() {
    return triggerListener;
  }
  public void setTriggerListener(String triggerListener) {
    this.triggerListener = triggerListener;
  }
} 

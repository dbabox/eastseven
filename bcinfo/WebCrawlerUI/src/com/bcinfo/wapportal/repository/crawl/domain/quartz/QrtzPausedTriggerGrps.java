package com.bcinfo.wapportal.repository.crawl.domain.quartz; 
public class QrtzPausedTriggerGrps { 
  public static final String TABLE_NAME = "QRTZ_PAUSED_TRIGGER_GRPS";
  public static final String TRIGGER_GROUP = "TRIGGER_GROUP";
  private String triggerGroup; 

  public String getTriggerGroup() {
    return triggerGroup;
  }
  public void setTriggerGroup(String triggerGroup) {
    this.triggerGroup = triggerGroup;
  }
} 

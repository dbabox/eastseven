package com.bcinfo.wapportal.repository.crawl.domain.quartz; 
public class QrtzSchedulerState { 
  public static final String TABLE_NAME = "QRTZ_SCHEDULER_STATE";
  public static final String INSTANCE_NAME = "INSTANCE_NAME";
  private String instanceName; 

  public String getInstanceName() {
    return instanceName;
  }
  public void setInstanceName(String instanceName) {
    this.instanceName = instanceName;
  }
  public static final String LAST_CHECKIN_TIME = "LAST_CHECKIN_TIME";
  private long lastCheckinTime; 

  public long getLastCheckinTime() {
    return lastCheckinTime;
  }
  public void setLastCheckinTime(long lastCheckinTime) {
    this.lastCheckinTime = lastCheckinTime;
  }
  public static final String CHECKIN_INTERVAL = "CHECKIN_INTERVAL";
  private long checkinInterval; 

  public long getCheckinInterval() {
    return checkinInterval;
  }
  public void setCheckinInterval(long checkinInterval) {
    this.checkinInterval = checkinInterval;
  }
} 

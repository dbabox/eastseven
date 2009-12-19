package com.bcinfo.wapportal.repository.crawl.domain.quartz; 
public class QrtzFiredTriggers { 
  public static final String TABLE_NAME = "QRTZ_FIRED_TRIGGERS";
  public static final String ENTRY_ID = "ENTRY_ID";
  private String entryId; 

  public String getEntryId() {
    return entryId;
  }
  public void setEntryId(String entryId) {
    this.entryId = entryId;
  }
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
  public static final String IS_VOLATILE = "IS_VOLATILE";
  private String isVolatile; 

  public String getIsVolatile() {
    return isVolatile;
  }
  public void setIsVolatile(String isVolatile) {
    this.isVolatile = isVolatile;
  }
  public static final String INSTANCE_NAME = "INSTANCE_NAME";
  private String instanceName; 

  public String getInstanceName() {
    return instanceName;
  }
  public void setInstanceName(String instanceName) {
    this.instanceName = instanceName;
  }
  public static final String FIRED_TIME = "FIRED_TIME";
  private long firedTime; 

  public long getFiredTime() {
    return firedTime;
  }
  public void setFiredTime(long firedTime) {
    this.firedTime = firedTime;
  }
  public static final String PRIORITY = "PRIORITY";
  private long priority; 

  public long getPriority() {
    return priority;
  }
  public void setPriority(long priority) {
    this.priority = priority;
  }
  public static final String STATE = "STATE";
  private String state; 

  public String getState() {
    return state;
  }
  public void setState(String state) {
    this.state = state;
  }
  public static final String JOB_NAME = "JOB_NAME";
  private String jobName; 

  public String getJobName() {
    return jobName;
  }
  public void setJobName(String jobName) {
    this.jobName = jobName;
  }
  public static final String JOB_GROUP = "JOB_GROUP";
  private String jobGroup; 

  public String getJobGroup() {
    return jobGroup;
  }
  public void setJobGroup(String jobGroup) {
    this.jobGroup = jobGroup;
  }
  public static final String IS_STATEFUL = "IS_STATEFUL";
  private String isStateful; 

  public String getIsStateful() {
    return isStateful;
  }
  public void setIsStateful(String isStateful) {
    this.isStateful = isStateful;
  }
  public static final String REQUESTS_RECOVERY = "REQUESTS_RECOVERY";
  private String requestsRecovery; 

  public String getRequestsRecovery() {
    return requestsRecovery;
  }
  public void setRequestsRecovery(String requestsRecovery) {
    this.requestsRecovery = requestsRecovery;
  }
} 

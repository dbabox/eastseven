package com.bcinfo.wapportal.repository.crawl.domain.quartz; 
public class QrtzTriggers { 
  public static final String TABLE_NAME = "QRTZ_TRIGGERS";
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
  public static final String IS_VOLATILE = "IS_VOLATILE";
  private String isVolatile; 

  public String getIsVolatile() {
    return isVolatile;
  }
  public void setIsVolatile(String isVolatile) {
    this.isVolatile = isVolatile;
  }
  public static final String DESCRIPTION = "DESCRIPTION";
  private String description; 

  public String getDescription() {
    return description;
  }
  public void setDescription(String description) {
    this.description = description;
  }
  public static final String NEXT_FIRE_TIME = "NEXT_FIRE_TIME";
  private long nextFireTime; 

  public long getNextFireTime() {
    return nextFireTime;
  }
  public void setNextFireTime(long nextFireTime) {
    this.nextFireTime = nextFireTime;
  }
  public static final String PREV_FIRE_TIME = "PREV_FIRE_TIME";
  private long prevFireTime; 

  public long getPrevFireTime() {
    return prevFireTime;
  }
  public void setPrevFireTime(long prevFireTime) {
    this.prevFireTime = prevFireTime;
  }
  public static final String PRIORITY = "PRIORITY";
  private long priority; 

  public long getPriority() {
    return priority;
  }
  public void setPriority(long priority) {
    this.priority = priority;
  }
  public static final String TRIGGER_STATE = "TRIGGER_STATE";
  private String triggerState; 

  public String getTriggerState() {
    return triggerState;
  }
  public void setTriggerState(String triggerState) {
    this.triggerState = triggerState;
  }
  public static final String TRIGGER_TYPE = "TRIGGER_TYPE";
  private String triggerType; 

  public String getTriggerType() {
    return triggerType;
  }
  public void setTriggerType(String triggerType) {
    this.triggerType = triggerType;
  }
  public static final String START_TIME = "START_TIME";
  private long startTime; 

  public long getStartTime() {
    return startTime;
  }
  public void setStartTime(long startTime) {
    this.startTime = startTime;
  }
  public static final String END_TIME = "END_TIME";
  private long endTime; 

  public long getEndTime() {
    return endTime;
  }
  public void setEndTime(long endTime) {
    this.endTime = endTime;
  }
  public static final String CALENDAR_NAME = "CALENDAR_NAME";
  private String calendarName; 

  public String getCalendarName() {
    return calendarName;
  }
  public void setCalendarName(String calendarName) {
    this.calendarName = calendarName;
  }
  public static final String MISFIRE_INSTR = "MISFIRE_INSTR";
  private long misfireInstr; 

  public long getMisfireInstr() {
    return misfireInstr;
  }
  public void setMisfireInstr(long misfireInstr) {
    this.misfireInstr = misfireInstr;
  }
  public static final String JOB_DATA = "JOB_DATA";
  private String jobData; 

  public String getJobData() {
    return jobData;
  }
  public void setJobData(String jobData) {
    this.jobData = jobData;
  }
} 

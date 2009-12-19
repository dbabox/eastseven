package com.bcinfo.wapportal.repository.crawl.domain.quartz; 
public class QrtzJobListeners { 
  public static final String TABLE_NAME = "QRTZ_JOB_LISTENERS";
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
  public static final String JOB_LISTENER = "JOB_LISTENER";
  private String jobListener; 

  public String getJobListener() {
    return jobListener;
  }
  public void setJobListener(String jobListener) {
    this.jobListener = jobListener;
  }
} 

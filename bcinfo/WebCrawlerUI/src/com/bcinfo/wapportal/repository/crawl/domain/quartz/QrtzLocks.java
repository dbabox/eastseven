package com.bcinfo.wapportal.repository.crawl.domain.quartz; 
public class QrtzLocks { 
  public static final String TABLE_NAME = "QRTZ_LOCKS";
  public static final String LOCK_NAME = "LOCK_NAME";
  private String lockName; 

  public String getLockName() {
    return lockName;
  }
  public void setLockName(String lockName) {
    this.lockName = lockName;
  }
} 

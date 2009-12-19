package com.bcinfo.wapportal.repository.crawl.domain.quartz;

public class QrtzBlobTriggers {
	public static final String TABLE_NAME = "QRTZ_BLOB_TRIGGERS";
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

	public static final String BLOB_DATA = "BLOB_DATA";
	private String blobData;

	public String getBlobData() {
		return blobData;
	}

	public void setBlobData(String blobData) {
		this.blobData = blobData;
	}
}

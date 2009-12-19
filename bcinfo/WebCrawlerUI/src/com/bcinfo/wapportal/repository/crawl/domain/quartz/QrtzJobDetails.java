package com.bcinfo.wapportal.repository.crawl.domain.quartz;

public class QrtzJobDetails {
	public static final String TABLE_NAME = "QRTZ_JOB_DETAILS";
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

	public static final String DESCRIPTION = "DESCRIPTION";
	private String description;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public static final String JOB_CLASS_NAME = "JOB_CLASS_NAME";
	private String jobClassName;

	public String getJobClassName() {
		return jobClassName;
	}

	public void setJobClassName(String jobClassName) {
		this.jobClassName = jobClassName;
	}

	public static final String IS_DURABLE = "IS_DURABLE";
	private String isDurable;

	public String getIsDurable() {
		return isDurable;
	}

	public void setIsDurable(String isDurable) {
		this.isDurable = isDurable;
	}

	public static final String IS_VOLATILE = "IS_VOLATILE";
	private String isVolatile;

	public String getIsVolatile() {
		return isVolatile;
	}

	public void setIsVolatile(String isVolatile) {
		this.isVolatile = isVolatile;
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

	public static final String JOB_DATA = "JOB_DATA";
	private String jobData;

	public String getJobData() {
		return jobData;
	}

	public void setJobData(String jobData) {
		this.jobData = jobData;
	}
}

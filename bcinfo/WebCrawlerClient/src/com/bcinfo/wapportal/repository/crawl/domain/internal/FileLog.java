/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.domain.internal;

/**
 * @author dongq
 * 
 *         create time : 2009-11-16 ÉÏÎç11:08:15
 */
public final class FileLog {

	private String fileName;
	private String createTime;
	private String flag;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	@Override
	public String toString() {
		return "FileLog [createTime=" + createTime + ", fileName=" + fileName
				+ ", flag=" + flag + "]";
	}

}

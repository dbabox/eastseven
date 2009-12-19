/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.domain.wap;

/**
 * @author dongq
 * 
 *         create time : 2009-12-16 ÏÂÎç12:23:38<br>
 */
public class WapFilter {

	private String keyValue;

	public WapFilter(String keyValue) {
		this.keyValue = keyValue;
	}

	public String getKeyValue() {
		return keyValue;
	}

	public void setKeyValue(String keyValue) {
		this.keyValue = keyValue;
	}

	@Override
	public String toString() {
		return "¹Ø¼ü×Ö [keyValue=" + keyValue + "]";
	}

}

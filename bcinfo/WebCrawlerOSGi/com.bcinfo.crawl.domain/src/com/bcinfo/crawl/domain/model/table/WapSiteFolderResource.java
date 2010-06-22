/**
 * 
 */
package com.bcinfo.crawl.domain.model.table;

import java.io.Serializable;

/**
 * @author dongq
 * 
 *         create time : 2010-5-26 …œŒÁ10:10:48
 */
public class WapSiteFolderResource implements Serializable {

	private static final long serialVersionUID = 1L;

	private String status;

	/**
	 * 1-“—…Û;0-Œ¥…Û
	 * @return
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * 1-“—…Û;0-Œ¥…Û
	 * @return
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "WapSiteFolderResource [status=" + status + "]";
	}

}

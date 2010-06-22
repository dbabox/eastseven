/**
 * 
 */
package com.bcinfo.crawl.domain.model;

import com.bcinfo.crawl.domain.util.CharacterSet;

/**
 * @author dongq
 * 
 *         create time : 2010-5-24 ионГ10:07:35
 */
public class LotteryResource extends Resource {

	private static final long serialVersionUID = 3326485797203499157L;

	public LotteryResource() {
		setCharset(CharacterSet.UTF8);
		setStatus("1");
	}
}

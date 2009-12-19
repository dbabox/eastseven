/**
 * 
 */
package com.bcinfo.wapportal.repository.crawl.core;

import java.util.concurrent.ConcurrentLinkedQueue;

import com.bcinfo.wapportal.repository.crawl.domain.bo.FolderBO;

/**
 * @author dongq
 * 
 *         create time : 2009-12-11 обнГ03:56:57
 */
public class CacheQueue {

	private static ConcurrentLinkedQueue<FolderBO> queue = null;
	
	public static synchronized ConcurrentLinkedQueue<FolderBO> getQueue() {
		if(queue == null){
			queue = new ConcurrentLinkedQueue<FolderBO>();
		}
		return queue;
	}
}

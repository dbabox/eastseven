/**
 * 
 */
package com.bcinfo.crawl.domain.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author dongq
 * 
 *         create time : 2010-5-17 ÏÂÎç10:46:35
 */
public class CrawlUrl implements Serializable {

	private static final long serialVersionUID = -3545222844494296347L;

	private Long id;
	private String url;
	private String charset;
	private Channel channel;

	private List<Resource> resources = new ArrayList<Resource>();

	public CrawlUrl() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public List<Resource> getResources() {
		return resources;
	}

	public void setResources(List<Resource> resources) {
		this.resources = resources;
	}

	public void addResource(Resource resource) {
		this.resources.add(resource);
	}
	
	public void removeResource(Resource resource) {
		this.resources.remove(resource);
	}
	
}

/**
 * 
 */
package org.dongq.analytics.model;

import java.util.List;

/**
 * @author eastseven
 * 
 */
public class Question {

	public static final int TYPE_NORMAL = 1;
	public static final int TYPE_MATRIX = 2;

	private long id;

	private String title;
	private String content;
	private long optionId;
	private long version;
	private int type;

	private List<Option> options;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public long getOptionId() {
		return optionId;
	}

	public void setOptionId(long optionId) {
		this.optionId = optionId;
	}

	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public List<Option> getOptions() {
		return options;
	}

	public void setOptions(List<Option> options) {
		this.options = options;
	}

	public String getSelect() {
		String html = "";
		if(this.options != null && !this.options.isEmpty()) {
			/*
			<select name="">
				<option value="">1</option>
			</select> 
			*/
			html = "<select name='question_"+this.id+"'>";
			for(Option o : this.options) {
				html += "<option value='"+o.getKey()+"'>"+o.getValue()+"</option>";
			}
			html += "</select>";
		}
		return html;
	}
	
	@Override
	public String toString() {
		return "Question [id=" + id + ", title=" + title + ", content="
				+ content + ", optionId=" + optionId + ", version=" + version
				+ ", type=" + type + "]";
	}

}

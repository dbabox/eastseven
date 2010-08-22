/**
 * 
 */
package org.dongq.database.model;

import java.util.HashSet;
import java.util.Set;

/**
 * @author dongq
 * 
 *         create time : 2010-8-8 上午11:35:46
 */
public class Table {

	private String name;
	private Set<Column> columns = new HashSet<Column>();

	public Table(String name) {
		this.name = name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setColumns(Set<Column> columns) {
		this.columns = columns;
	}

	public Set<Column> getColumns() {
		return columns;
	}

	@Override
	public String toString() {
		return "Table [name=" + name + ", columns=" + columns + "]";
	}

}

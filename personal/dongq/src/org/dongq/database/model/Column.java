/**
 * 
 */
package org.dongq.database.model;

/**
 * @author dongq
 * 
 *         create time : 2010-8-8 上午11:36:01
 */
public class Column {

	public static final int NULL = 0;
	public static final int NOT_NULL = 1;

	private String name;
	private String type;
	private int size;
	private int nullable;

	public Column(String name) {
		super();
		this.name = name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getNullable() {
		return nullable;
	}

	public void setNullable(int nullable) {
		this.nullable = nullable;
	}

	@Override
	public boolean equals(Object obj) {
		Column col = (Column) obj;
		return this.name.equals(col.getName());
	}

	@Override
	public String toString() {
		return "Column [name=" + name + ", type=" + type + ", size=" + size
				+ ", nullable=" + nullable + "]";
	}

}

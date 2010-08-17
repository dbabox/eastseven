/**
 * 
 */
package org.dongq.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.dongq.database.model.Column;
import org.dongq.database.model.ColumnType;
import org.dongq.database.model.Table;

/**
 * @author dongq
 * 
 *         create time : 2010-8-8 上午11:00:00<br>
 *         
 */
public final class DatabaseDemo {

	public static void main(String[] args) throws Exception {
		
		String className = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@127.0.0.1:1521:xe";
		String user = "hr";
		String password = "hr";
		
		Class.forName(className);
		Connection conn = DriverManager.getConnection(url, user, password);
		ResultSet rs = null;
		
		System.out.println(conn);
		String catalog = conn.getCatalog();
		System.out.println("Catalog:" + catalog);
		
		//数据库信息
		DatabaseMetaData databaseMetaData = conn.getMetaData();
		
		//取得表信息
		List<Table> tables = new ArrayList<Table>();
		String schemaPattern = "HR";
		String tableNamePattern = null;
		String columnNamePattern = null;
		String[] types = {"TABLE"};
		rs = databaseMetaData.getTables(catalog, schemaPattern, tableNamePattern, types);
		while(rs.next()) {
			String name = rs.getString("TABLE_NAME");
			Table table = new Table(name);
			tableNamePattern = name;
			ResultSet _rs = databaseMetaData.getColumns(catalog, schemaPattern, tableNamePattern, columnNamePattern);
			while(_rs.next()) {
				String colName = _rs.getString("COLUMN_NAME");
				String colType = _rs.getString("TYPE_NAME");
				int colSize = _rs.getInt("COLUMN_SIZE");
				int colNullable = _rs.getInt("NULLABLE");
				Column column = new Column(colName);
				column.setNullable(colNullable);
				column.setSize(colSize);
				column.setType(colType);
				table.getColumns().add(column);
			}
			tables.add(table);
			_rs.close();
		}
		
		System.out.println(tables);
		//建表脚本
		if(!tables.isEmpty()) {
			for(Table table : tables) {
				String createTableScript = "create table "+table.getName()+"(\n";
				int index = 1;
				for(Column col : table.getColumns()) {
					createTableScript += "  "+col.getName()+"  ";
					createTableScript += col.getType() + ("DATE".equalsIgnoreCase(col.getType()) ? "" : "("+col.getSize()+")  ");
					createTableScript += (col.getNullable()==Column.NOT_NULL?"NOT NULL":"");
					createTableScript += (index != table.getColumns().size()) ? ",\n" : "\n";
					index++;
				}
				createTableScript += ");\n";
				System.out.println("建表脚本:"+createTableScript);
			}
		}
		
		//表数据
		if(!tables.isEmpty()) {
			for(Table table : tables) {
				System.out.println(table.getName() + "表数据：");
				String sql = "select * from " + table.getName();
				PreparedStatement pst = conn.prepareStatement(sql);
				ResultSet _rs = pst.executeQuery();
				while(_rs.next()) {
					String insertSQL = "insert into " + table.getName() + "(";
					String data = "";
					for(Column col : table.getColumns()) {
						insertSQL += "," + col.getName();
						Object value = _rs.getObject(col.getName());
						if(value == null || "null".equals(value)) {
							data += ",null";
						} else if(col.getType().equals(ColumnType.CHAR)
								|| col.getType().equals(ColumnType.VARCHAR)
								|| col.getType().equals(ColumnType.VARCHAR2)) {
							data += ",'" + _rs.getObject(col.getName()) + "'";
						} else if(col.getType().equals(ColumnType.DATE)) {
							data += ",to_date('" + _rs.getObject(col.getName()) + "','yyyy-mm-dd')";
						} else {
							data += "," + _rs.getObject(col.getName());
						}
					}
					insertSQL = insertSQL.replaceFirst(",", "") + ") values(";
					insertSQL += data.replaceFirst(",", "");
					insertSQL += ");";
					System.out.println(insertSQL);
				}
				
				_rs.close();
				pst.close();
			}
		}
		
		if(rs != null) rs.close();
		conn.close();
		System.out.println("done...");
	}

}

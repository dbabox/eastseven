/**
 * 
 */
package org.dongq.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.dongq.database.model.Column;
import org.dongq.database.model.Table;

/**
 * @author dongq
 * 
 *         create time : 2010-8-8 上午11:00:00
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
		//建表
		if(!tables.isEmpty()) {
			String parentCatalog = null;
			String parentSchema = "HR";
			String parentTable = null;
			String foreignCatalog = null;
			String foreignSchema = "HR";
			String foreignTable = null;
//			String script = "script";
//			String tableScript = "table.sql";
//			File file = new File(script);
//			file.mkdir();
//			File tableScriptFile = new File(script+"/"+tableScript);
			for(Table table : tables) {
				parentTable = table.getName();
				rs = databaseMetaData.getCrossReference(parentCatalog, parentSchema, parentTable, foreignCatalog, foreignSchema, foreignTable);
				while(rs.next()) {
					System.out.println(rs.getString("PKTABLE_NAME") + "." + rs.getString("PKCOLUMN_NAME") + "<-->" + rs.getString("FKTABLE_NAME") + "." + rs.getString("FKCOLUMN_NAME"));
				}
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
		
		
		//外键关系
		String parentCatalog = null;
		String parentSchema = "HR";
		String parentTable = null;
		String foreignCatalog = null;
		String foreignSchema = "HR";
		String foreignTable = null;
		rs = databaseMetaData.getCrossReference(parentCatalog, parentSchema, parentTable, foreignCatalog, foreignSchema, foreignTable);
		while(rs.next()) {
			System.out.println(rs.getString("PKTABLE_NAME") + "." + rs.getString("PKCOLUMN_NAME") + "<-->" + rs.getString("FKTABLE_NAME") + "." + rs.getString("FKCOLUMN_NAME"));
		}
		
		
		if(rs != null) rs.close();
		conn.close();
		System.out.println("done...");
	}

}

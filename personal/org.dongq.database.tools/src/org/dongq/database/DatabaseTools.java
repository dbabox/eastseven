/**
 * 
 */
package org.dongq.database;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import oracle.sql.TIMESTAMP;

import org.dongq.database.model.Column;
import org.dongq.database.model.ColumnType;
import org.dongq.database.model.Table;

/**
 * @author dongq
 * 
 *         create time : 2010-8-8 上午11:00:00
 */
public final class DatabaseTools {

	private String schema = "";
	
	private static String className = "oracle.jdbc.driver.OracleDriver";
	private static String url = "jdbc:oracle:thin:@127.0.0.1:1521:xe";
	private static String user = "hr";
	private static String password = "hr";
	
	private static String schemaPattern = null;
	private static String tableNamePattern = null;
	private static String columnNamePattern = null;
	
	private static boolean isGenerateTable = true;
	private static boolean isGenerateDate = false;
	
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public DatabaseTools() {
		Properties config = new Properties();
		try {
			config.load(new FileInputStream("conf/jdbc.properties"));
			className = config.getProperty("jdbc.className");
			url = config.getProperty("jdbc.url");
			user = config.getProperty("jdbc.user");
			password = config.getProperty("jdbc.password");

			schemaPattern = config.getProperty("jdbc.schema.pattern");
			tableNamePattern = config.getProperty("jdbc.tableName.pattern");
			columnNamePattern = config.getProperty("jdbc.columnName.pattern");
			
			if(schemaPattern == null || "null".equalsIgnoreCase(schemaPattern)) schemaPattern = null;
			else schemaPattern = schemaPattern.toUpperCase();
			System.out.println("jdbc.schema.pattern : " + schemaPattern);
			
			if(tableNamePattern == null || "null".equalsIgnoreCase(tableNamePattern)) tableNamePattern = null;
			else tableNamePattern = tableNamePattern.toUpperCase();
			System.out.println("jdbc.tableName.pattern : " + tableNamePattern);
			
			if(columnNamePattern == null || "null".equalsIgnoreCase(columnNamePattern)) columnNamePattern = null;
			else columnNamePattern = columnNamePattern.toUpperCase();
			System.out.println("jdbc.columnName.pattern : " + columnNamePattern);
			
			isGenerateDate = Boolean.parseBoolean(config.getProperty("jdbc.generate.data.script"));
			isGenerateTable = Boolean.parseBoolean(config.getProperty("jdbc.generate.table.script"));
			
			Class.forName(className);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	private Connection getConnection() {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	public void start() throws Exception {
		System.out.println("程序开始");
		Connection conn = getConnection();
		assert conn != null;
		
		//数据库信息
		DatabaseMetaData databaseMetaData = conn.getMetaData();
		schema = databaseMetaData.getUserName();
		List<Table> tables = getTables(databaseMetaData);
		
		if(tables.isEmpty()) return;
		
		System.out.println(tables);
		
		if(isGenerateTable) generateTableScript(tables);
		if(isGenerateDate) generateDataScript(tables, conn);
		
		if(conn != null) conn.close();
		
		System.out.println("程序结束");
	}
	
	/**
	 * 取得表信息
	 * @param databaseMetaData
	 * @return
	 * @throws Exception
	 */
	private List<Table> getTables(DatabaseMetaData databaseMetaData) throws Exception {
		List<Table> tables = new ArrayList<Table>();
		
		ResultSet rs = null;
		String catalog = null;
		
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
		
		return tables;
	}
	
	private void generateTableScript(List<Table> tables) throws Exception {
		String script = "";
		for(Table table : tables) {
			String createTableScript = "create table "+table.getName()+"(\n";
			int index = 1;
			for(Column col : table.getColumns()) {
				createTableScript += "  "+col.getName()+"  ";
				createTableScript += col.getType() + ("DATE".equalsIgnoreCase(col.getType()) ? " " : " ("+col.getSize()+")  ");
				createTableScript += (col.getNullable()==Column.NOT_NULL?" NOT NULL":" ");
				createTableScript += (index != table.getColumns().size()) ? ",\n" : "\n";
				index++;
			}
			createTableScript += ");\n";
			System.out.println("table script :"+createTableScript);
			script += createTableScript + "\n";
		}
		File file = new File(schema + ".sql");
		if(!file.exists()) file.createNewFile();
		FileOutputStream out = new FileOutputStream(file, true);
		out.write(script.getBytes());
		out.close();
	}
	
	private void generateDataScript(List<Table> tables, Connection conn) throws Exception {
		for(Table table : tables) {
			
			String sql = "select * from " + table.getName();
			PreparedStatement pst = conn.prepareStatement(sql);
			ResultSet _rs = pst.executeQuery();
			int count = 0;
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
						
						data += ",'" + value + "'";
						
					} else if(col.getType().equals(ColumnType.DATE)) {
						data += ",to_date('" + value + "','yyyy-mm-dd hh24:mi:ss')";
					} else if(col.getType().equals(ColumnType.TIMESTAMP)) {
						//System.out.println(ColumnType.TIMESTAMP + " = " + value);
						TIMESTAMP timestamp = (TIMESTAMP)value;
						Date date = new Date(timestamp.dateValue().getTime());
						data += ",to_date('" + sdf.format(date) + "','yyyy-mm-dd hh24:mi:ss')";
					} else {
						data += "," + value;
					}
					//data += "," + value;
				}
				insertSQL = insertSQL.replaceFirst(",", "") + ") values(";
				insertSQL += data.replaceFirst(",", "");
				insertSQL += ");";
				System.out.println(insertSQL);
				count ++;
			}
			System.out.println(table.getName() + "表数据：" + count);
			_rs.close();
			pst.close();
		}
	}
	
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

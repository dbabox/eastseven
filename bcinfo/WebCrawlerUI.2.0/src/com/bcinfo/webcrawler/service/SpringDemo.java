/**
 * 
 */
package com.bcinfo.webcrawler.service;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author dongq
 * 
 *         create time : 2010-8-20 上午09:03:18
 */
public final class SpringDemo {

	private static final Log log = LogFactory.getLog(SpringDemo.class);
	
	private JdbcTemplate jdbcTemplate;
	
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	public void start() {
		log.info("\n Springframework is start");
		final String sql = "select to_char(sysdate, 'yy/mm/dd hh24:mi:ss') from dual";
		String currentTime = (String)jdbcTemplate.queryForObject(sql, String.class);
		log.info("\n "+currentTime+" \n");
	}
	
	public void end() {
		log.info("\n Springframework is end \n");
	}
}

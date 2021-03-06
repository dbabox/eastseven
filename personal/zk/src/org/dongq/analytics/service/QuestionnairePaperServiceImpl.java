package org.dongq.analytics.service;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.dongq.analytics.model.Option;
import org.dongq.analytics.model.Question;
import org.dongq.analytics.model.QuestionGroup;
import org.dongq.analytics.model.Questionnaire;
import org.dongq.analytics.model.QuestionnairePaper;
import org.dongq.analytics.model.Responder;
import org.dongq.analytics.model.ResponderProperty;
import org.dongq.analytics.utils.DbHelper;

public class QuestionnairePaperServiceImpl implements QuestionnairePaperService {

	final static Log logger = LogFactory.getLog(QuestionnairePaperServiceImpl.class);
	
	public Questionnaire getQuestionnaire(long id) {
		Questionnaire blankPaper = new Questionnaire();
		Connection conn = DbHelper.getConnection();
		QueryRunner queryRunner = new QueryRunner();
		
		try {
			String sql = "select * from responder a where a.responder_id = " + id;
			Responder responder = queryRunner.query(conn, sql, new ResultSetHandler<Responder>() {
				@Override
				public Responder handle(ResultSet rs) throws SQLException {
					if(rs.next()) {
						Responder responder = new Responder();
						responder.setId(rs.getLong("responder_id"));
						responder.setName(rs.getString("responder_name"));
						responder.setVersion(rs.getLong("version"));
						return responder;
					}
					return null;
				}
			});
			
			blankPaper.setResponder(responder);
			//Group
			blankPaper.setGroup(getQuestionGroupOfVersion(responder.getVersion()));
			//Matrix
			blankPaper.setMatrix(getQuestionsOfVersion(responder.getVersion()));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return blankPaper;
	}

	List<QuestionGroup> getQuestionGroupOfVersion(long version) throws SQLException {
		List<QuestionGroup> list = new ArrayList<QuestionGroup>();
		String sql = "select a.title, a.option_group_id from question a where a.version = " + version + " and a.type = " + Question.TYPE_NORMAL + " group by a.title, a.option_group_id";
		list = new QueryRunner().query(DbHelper.getConnection(), sql, new ResultSetHandler<List<QuestionGroup>>() {
			@Override
			public List<QuestionGroup> handle(ResultSet rs) throws SQLException {
				List<QuestionGroup> list = new ArrayList<QuestionGroup>();
				while(rs.next()) {
					QuestionGroup e = new QuestionGroup();
					e.setTitle(rs.getString("title"));
					e.setId(rs.getLong("option_group_id"));
					e.setOptions(getOptionsForQuestion(e.getId()));
					e.setQuestions(getQuestionsOfOptionGroupId(e.getId()));
					list.add(e);
				}
				return list;
			}
			
		});
		
		return list;
	}
	
	List<Question> getQuestionsOfVersion(long version) throws SQLException {
		List<Question> list = new ArrayList<Question>();
		String sql = "select * from question a where a.version = " + version + " and a.type = " + Question.TYPE_MATRIX;
		logger.debug(sql);
		list = new QueryRunner().query(DbHelper.getConnection(), sql, new ResultSetHandler<List<Question>>() {
			@Override
			public List<Question> handle(ResultSet rs) throws SQLException {
				List<Question> list = new ArrayList<Question>();
				while(rs.next()) {
					Question e = new Question();
					e.setId(rs.getLong("question_id"));
					e.setTitle(rs.getString("title"));
					e.setContent(rs.getString("content"));
					e.setOptionId(rs.getLong("option_group_id"));
					e.setVersion(rs.getLong("version"));
					e.setType(rs.getInt("type"));
					//e.setOptions(getOptionsForQuestion(e.getOptionId()));
					logger.debug(e);
					list.add(e);
				}
				return list;
			}
			
		});
		
		return list;
	}
	
	List<Question> getQuestionsOfOptionGroupId(long optionGroupId) throws SQLException {

		List<Question> list = new ArrayList<Question>();
		String sql = "select * from question a where a.option_group_id = " + optionGroupId;
		list = new QueryRunner().query(DbHelper.getConnection(), sql, new ResultSetHandler<List<Question>>() {
			@Override
			public List<Question> handle(ResultSet rs) throws SQLException {
				List<Question> list = new ArrayList<Question>();
				while(rs.next()) {
					Question e = new Question();
					e.setId(rs.getLong("question_id"));
					e.setTitle(rs.getString("title"));
					e.setContent(rs.getString("content"));
					e.setOptionId(rs.getLong("option_group_id"));
					e.setVersion(rs.getLong("version"));
					e.setType(rs.getInt("type"));
					e.setOptions(getOptionsForQuestion(e.getOptionId()));
					list.add(e);
				}
				return list;
			}
			
		});
		
		return list;
	
	}
	
	List<Option> getOptionsForQuestion(long id) throws SQLException {
		List<Option> list = new ArrayList<Option>();
		String sql = "select * from question_option a where a.option_group_id = " + id;
		list = new QueryRunner().query(DbHelper.getConnection(), sql, new ResultSetHandler<List<Option>>() {
			@Override
			public List<Option> handle(ResultSet rs) throws SQLException {
				List<Option> list = new ArrayList<Option>();
				while(rs.next()) {
					Option e = new Option();
					e.setId(rs.getLong("option_group_id"));
					e.setKey(rs.getInt("option_key"));
					e.setValue(rs.getString("option_value"));
					e.setVersion(rs.getLong("version"));
					list.add(e);
				}
				return list;
			}
			
		});
		return list;
	}
	
	public boolean saveQuestionnairePaper(QuestionnairePaper paper) {
		boolean bln = false;
		
//		try {
//			Connection conn = DbHelper.getConnection();
//			QueryRunner queryRunner = new QueryRunner();
//			
//			Responder responder = paper.getResponder();
//			long responderId = System.currentTimeMillis();
//			String sql = "insert into responder(responder_id,name,gender) values("+responderId+",'"+responder.getName()+"','"+responder.getGender()+"')";
//			int record = queryRunner.update(conn, sql);
//			logger.info(record);
//			
//			long paperId = System.currentTimeMillis()+1;
//			sql = "insert into questionnaire_paper(paper_id,responder_id,questionnaire_id,sign_date) values("+paperId+","+responderId+","+paper.getId()+",CURRENT_DATE)";
//			record = queryRunner.update(conn, sql);
//			logger.info(record);
//			
//			sql = "insert into questionnaire_paper_answer(paper_id,answer_key,answer_value) values(?,?,?)";
//			Map<Long, Integer> answers = paper.getAnswers();
//			Object[][] params = new Object[answers.size()][3];
//			int index = 0;
//			for(Iterator<Long> iter = answers.keySet().iterator(); iter.hasNext();) {
//				long key = iter.next();
//				int value = answers.get(key);
//				params[index][0] = paperId;
//				params[index][1] = key;
//				params[index][2] = value;
//				index++;
//			}
//			int[] records = queryRunner.batch(conn, sql, params);
//			logger.info(index + "=" + records.length);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
		return bln;
	}

	@Override
	public boolean parseQuestionnaireTemplate(InputStream excel) {
		boolean bln = false;
		final long version = System.currentTimeMillis();
		try {
			logger.info(version);
			Workbook workbook = WorkbookFactory.create(excel);
			int numberOfSheets = workbook.getNumberOfSheets();
			for(int index = 0; index < numberOfSheets; index++) {
				Sheet sheet = workbook.getSheetAt(index);
				logger.debug(sheet.getSheetName());
			}
			
			//｛参与调查的人员信息属性｝
			Sheet responderProperty = workbook.getSheetAt(0);
			logger.debug(responderProperty.getSheetName());
			parseResponderProperty(responderProperty, version);
			
			//｛参与调查的人员信息｝
			Sheet responders = workbook.getSheetAt(1);
			logger.info(responders.getSheetName());
			parseResponders(responders, version);
			
			//{问卷题目}
			Sheet requestions = workbook.getSheetAt(2);
			logger.info(requestions.getSheetName());
			parseRequestions(requestions, version);
			
			//{矩阵题}
			Sheet matrix = workbook.getSheetAt(3);
			logger.info(matrix.getSheetName());
			parseRequestionsOfMatrix(matrix, version);
			
			bln = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return bln;
	}
	
	void parseResponderProperty(Sheet responderProperty, long version) throws SQLException {
		int index = 0;
		for(Iterator<Row> rowIter = responderProperty.iterator(); rowIter.hasNext();) {
			Row row = rowIter.next();
			if(row.getCell(0) != null && index > 0) {
				logger.info(index + " : " + row.getCell(0));
				//属性名称
				if(row.getCell(1) != null && !StringUtils.isBlank(row.getCell(1).toString())) {
					String name = row.getCell(0).getStringCellValue();
					//选项值
					int value = 1;
					int columnIndex = 1;
					for(Iterator<Cell> cellIter = row.cellIterator(); cellIter.hasNext();) {
						Cell cell = cellIter.next();
						if(!StringUtils.isBlank(cell.getStringCellValue()) && columnIndex > 1) {
							ResponderProperty p = new ResponderProperty();
							p.setId(System.currentTimeMillis() + columnIndex * Math.round(System.currentTimeMillis()) * 1000);
							p.setDisplay(cell.getStringCellValue());
							p.setName(name);
							p.setValue(value);
							p.setVersion(version);
							
							QueryRunner query = new QueryRunner();
							final String sql = "insert into responder_property values(?,?,?,?,?)";
							int record = query.update(DbHelper.getConnection(), sql, p.getId(), p.getName(), p.getDisplay(), p.getValue(), p.getVersion());
							logger.debug(record);
							value++;
						}
						columnIndex++;
					}
				} else {
					logger.debug(row);
				}
				
			}
			index++;
		}
	}
	
	void parseResponders(Sheet responders, long version) throws Exception {
		QueryRunner query = new QueryRunner();
		String sql = "select * from responder_property where version = " + version;
		List<ResponderProperty> list = query.query(DbHelper.getConnection(), sql, new ResultSetHandler<List<ResponderProperty>>() {
			@Override
			public List<ResponderProperty> handle(ResultSet rs) throws SQLException {
				List<ResponderProperty> list = new ArrayList<ResponderProperty>();
				while(rs.next()) {
					list.add(new ResponderProperty(rs.getLong(1), rs.getString(2), rs.getString(3), rs.getInt(4), rs.getLong(5)));
				}
				return list;
			}
		});
		logger.info(list.size());
		//取表头属性名
		int attributeIndex = 0;
		for(Iterator<Cell> iter = responders.getRow(0).cellIterator(); iter.hasNext(); ) {
			Cell c = iter.next();
			if(c != null && !StringUtils.isBlank(c.getStringCellValue())) {
				attributeIndex++;
			}
		}
		int index = 0;
		for(Iterator<Row> rowIter = responders.iterator(); rowIter.hasNext();) {
			Row row = rowIter.next();
			if(index == 0) {
				int _index = 1;
				for(Iterator<Cell> iter = row.cellIterator(); iter.hasNext();) {
					Cell c = iter.next();
					logger.info(_index + "." + c);
					_index++;
				}
			} else {
				Cell firstCell = row.getCell(0);
				if(firstCell != null && !StringUtils.isBlank(firstCell.getStringCellValue())) {
					//Responder Object loop
					Responder responder = new Responder();
					for(int columnIndex = 0; columnIndex < attributeIndex; columnIndex++) {
						Cell column = row.getCell(columnIndex);
						switch (columnIndex) {
						case 0:
							logger.info(columnIndex+"-"+column.getStringCellValue());
							responder.setName(column.getStringCellValue());
							break;
						default:
							logger.info(columnIndex+"="+column.getStringCellValue());
							break;
						}
					}
					sql = "insert into responder(responder_id,responder_name,version) values(?,?,?)";
					long id = System.currentTimeMillis() + index * Math.round(System.currentTimeMillis()) * 1000;
					query.update(DbHelper.getConnection(), sql, id, responder.getName(), version);
				}
			}
			index++;
		}
	}
	
	void parseRequestions(Sheet requestions, long version) throws Exception {
		int index = 0;
		for(Iterator<Row> rowIter = requestions.iterator(); rowIter.hasNext();) {
			Row row = rowIter.next();
			if(row.getCell(0) != null && !StringUtils.isBlank(row.getCell(0).getStringCellValue()) && index > 0) {
				logger.info(index + " : " + row.getCell(0));
				String title = row.getCell(0).getStringCellValue();
				String optionString = row.getCell(1).getStringCellValue();
				long optionGroupId = parseOptions(optionString, version).get(0).getId();
				
				QueryRunner query = new QueryRunner();
				//迭代小题
				int columnIndex = 0;
				for(Iterator<Cell> iter = row.cellIterator(); iter.hasNext();) {
					Cell cell = iter.next();
					
					if(cell == null) continue;
					if(StringUtils.isBlank(cell.getStringCellValue())) continue;
					
					if(columnIndex > 1) {
						Question q = new Question();
						q.setId(System.currentTimeMillis() + columnIndex * Math.round(1000) * 1000);
						q.setContent(cell.getStringCellValue());
						q.setOptionId(optionGroupId);
						q.setTitle(title);
						q.setVersion(version);
						q.setType(Question.TYPE_NORMAL);
						saveQuestion(q, query);
						logger.info(q);
					}
					columnIndex++;
				}
			}
			index++;
		}
	}
	
	void parseRequestionsOfMatrix(Sheet matrix, long version) throws Exception {
		int index = 0;
		for(Iterator<Row> rowIter = matrix.iterator(); rowIter.hasNext();) {
			Row row = rowIter.next();
			if(row.getCell(0) != null && !StringUtils.isBlank(row.getCell(0).getStringCellValue()) && index > 0) {
				Question q = new Question();
				q.setId(System.currentTimeMillis() + index * Math.round(1000) * 1000);
				q.setTitle(row.getCell(0).getStringCellValue());
				q.setVersion(version);
				q.setType(Question.TYPE_MATRIX);
				saveQuestion(q, new QueryRunner());
				logger.info(q);
			}
			
			index++;
		}
	}
	
	List<Option> parseOptions(String optionString, long version) throws Exception {
		long optionGroupId = System.currentTimeMillis();
		List<Option> list = new ArrayList<Option>();
		
		logger.info(optionString);
		QueryRunner query = new QueryRunner();
		String insert = "insert into question_option values(?,?,?,?)";
		String[] array = optionString.split(",");
		for(String e : array) {
			String[] opt = e.split("=");
			Option o = new Option();
			o.setId(optionGroupId);
			o.setVersion(version);
			o.setKey(Integer.parseInt(opt[0]));
			o.setValue(opt[1]);
			list.add(o);
			
			logger.info(o);
			query.update(DbHelper.getConnection(), insert, o.getId(), o.getKey(), o.getValue(), o.getVersion());
		}
		
		return list;
	}
	
	int saveQuestion(Question q, QueryRunner query) throws Exception {
		String insert = "insert into question values(?,?,?,?,?,?)";
		int update = query.update(DbHelper.getConnection(), insert, q.getId(), q.getTitle(), q.getContent(), q.getOptionId(), q.getVersion(), q.getType());
		return update;
	}
	
	public static void main(String[] arg) throws Exception {
		QuestionnairePaperService service = new QuestionnairePaperServiceImpl();
		final String name = System.getProperty("user.dir") + "/src/main/webapp/template.xls";
		System.out.println(name);
		InputStream excel = new FileInputStream(name);
		service.parseQuestionnaireTemplate(excel);
	}
}

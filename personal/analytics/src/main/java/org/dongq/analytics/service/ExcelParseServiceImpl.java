package org.dongq.analytics.service;

import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Workbook;
import org.dongq.analytics.model.Question;
import org.dongq.analytics.model.Questionnaire;
import org.dongq.analytics.model.QuestionnairePaper;
import org.dongq.analytics.model.Responder;
import org.springframework.stereotype.Service;

@Service("excelParseService")
public class ExcelParseServiceImpl implements QuestionnairePaperService {

	final static Log logger = LogFactory.getLog(ExcelParseServiceImpl.class);
	
	@Override
	public boolean hasAnswered(long responderId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Responder login(String no, String pwd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getQuestionnaireTitle(Object version) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Responder> getRespondersOfVersion(long version) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Questionnaire getQuestionnaire(long responderId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Questionnaire getBlankQuestionnaire(long version) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Question> getQuestionsOfVersion(long version, int type,
			Long responderId) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean saveQuestionnairePaper(QuestionnairePaper paper) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean saveQuestionnairePaper(Responder responder,
			Map<String, Object> answer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean parseQuestionnaireTemplate(InputStream excel, String type) {
		logger.debug("===============");
		boolean bln =false;
		
		try {
			
			
			
		} catch (Exception e) {
			logger.error(e);
		}
		
		return bln;
	}

	@Override
	public Object[][] calculate(long version) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Object, Object[][]> calculateForMatrix(long version) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Workbook generateExcelForQuestionnaire(long version) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Workbook generateExcelForQuestionnaireMatrixNet(long version) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Workbook generateExcelForQuestionnaire(Object[][] normalQuestion,
			Map<Object, Object[][]> matrixQuestion) {
		// TODO Auto-generated method stub
		return null;
	}

}

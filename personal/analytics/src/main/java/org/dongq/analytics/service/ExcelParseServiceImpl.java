package org.dongq.analytics.service;

import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.dongq.analytics.model.Question;
import org.dongq.analytics.model.Questionnaire;
import org.dongq.analytics.model.QuestionnairePaper;
import org.dongq.analytics.model.Responder;
import org.dongq.analytics.model.domain.QuestionnaireTemplate;
import org.dongq.analytics.repository.QuestionnaireTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("excelParseService")
public class ExcelParseServiceImpl implements QuestionnairePaperService {

	final static Log logger = LogFactory.getLog(ExcelParseServiceImpl.class);
	
	@Autowired
	private QuestionnaireTemplateRepository templateRepository;
	
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
		boolean bln = false;
		final long version = System.currentTimeMillis();
		try {
			logger.info(version);
			
			QuestionnaireTemplate template = new QuestionnaireTemplate();
			template.setVersion(version);
			
			Workbook workbook = WorkbookFactory.create(excel);
			int numberOfSheets = workbook.getNumberOfSheets();
			for(int index = 0; index < numberOfSheets; index++) {
				Sheet sheet = workbook.getSheetAt(index);
				logger.debug(sheet.getSheetName());
			}
			
			//｛参与调查的人员信息属性｝
			Sheet responderProperty = workbook.getSheetAt(0);
			logger.debug(responderProperty.getSheetName());
			//parseResponderProperty(responderProperty, version);
			
			//｛参与调查的人员信息｝
			if(type.equals(QuestionnairePaperService.TYPE_CLOSE)) {
				Sheet responders = workbook.getSheetAt(1);
				logger.info(responders.getSheetName());
				//parseResponders(responders, version);
			}
			
			//{问卷题目}
			Sheet requestions = workbook.getSheetAt(2);
			logger.info(requestions.getSheetName());
			//parseRequestions(requestions, version);
			
			//{矩阵题}
			Sheet matrix = workbook.getSheetAt(3);
			logger.info(matrix.getSheetName());
			//parseRequestionsOfMatrix(matrix, version);
			
			//{网络题}
			Sheet matrixNet = workbook.getSheetAt(4);
			logger.info(matrixNet.getSheetName());
			//parseRequestonsOfMatrixNet(matrixNet, version);
			
			templateRepository.save(template);
			bln = true;
		} catch (Exception e) {
			e.printStackTrace();
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

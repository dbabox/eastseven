package org.dongq.analytics.service;

import java.io.InputStream;

import org.dongq.analytics.model.Questionnaire;
import org.dongq.analytics.model.QuestionnairePaper;

public interface QuestionnairePaperService {

	public Questionnaire getQuestionnaire(long id);
	
	public boolean saveQuestionnairePaper(QuestionnairePaper paper);
	
	public boolean parseQuestionnaireTemplate(InputStream excel);
}

package org.dongq.analytics.repository;

import org.dongq.analytics.BasicTestTemplate;
import org.dongq.analytics.model.domain.QuestionnaireTemplate;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class QTemplateRepository extends BasicTestTemplate {

	@Autowired
	private QuestionnaireTemplateRepository templateRepository;

	@Test
	public void insert() {
		templateRepository.deleteAll();
		Assert.assertEquals(0, templateRepository.count());
		
		QuestionnaireTemplate template = new QuestionnaireTemplate();
		template.setName("test-name");
		template.setTitle("test-title");
		template.setVersion(System.currentTimeMillis());
		
		templateRepository.save(template);
		
		Assert.assertNotNull(template.getId());
		Assert.assertTrue(template.getVersion() < System.currentTimeMillis());
		Assert.assertTrue(template.getId() > 0);
		Assert.assertEquals(1, templateRepository.count());
	}
}

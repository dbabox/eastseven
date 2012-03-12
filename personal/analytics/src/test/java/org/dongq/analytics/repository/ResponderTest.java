package org.dongq.analytics.repository;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import junit.framework.Assert;

import org.dongq.analytics.BasicTestTemplate;
import org.dongq.analytics.model.Responder;
import org.dongq.analytics.model.ResponderProperty;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ResponderTest extends BasicTestTemplate {

	@Autowired
	private ResponderRepository responderRepository;

	@Test
	public void insert() {
		Assert.assertEquals(0, responderRepository.count());
		
		final long version = System.currentTimeMillis();
		Set<ResponderProperty> items = new HashSet<ResponderProperty>(new Random().nextInt(5));
		for(int index = 0; index < items.size(); index++) {
			ResponderProperty e = new ResponderProperty();
			e.setDisplay("display-" + index);
			e.setName("name-"+index);
			e.setValue(index);
			e.setVersion(version);
			
			items.add(e);
		}
		
		Responder user = new Responder();
		user.setProperties(items);
		user.setName("dongq");
		user.setNo("NO.001");
		user.setPwd("123");
		user.setVersion(version);
		
		user = responderRepository.save(user);
		Assert.assertNotNull(user.getId());
		Assert.assertTrue(user.getId() > 0);
		Assert.assertEquals(1, responderRepository.count());
	}
}

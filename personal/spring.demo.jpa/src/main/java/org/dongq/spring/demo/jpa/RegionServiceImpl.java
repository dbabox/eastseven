package org.dongq.spring.demo.jpa;

import java.util.List;

import org.dongq.spring.demo.jpa.model.SysUser;
import org.dongq.spring.demo.jpa.repositories.RegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegionServiceImpl implements RegionService {

	@Autowired
	private RegionRepository regionRepository;
	
	public void test() {
		System.out.println(regionRepository);
		List<SysUser> users = regionRepository.findAll();
		for (SysUser e : users) {
			System.out.println(e);
		}
		
		SysUser user = new SysUser();
		user.setEmail("201207091604@test.com");
		user.setMobile("201207091604");
		user.setRealname("201207091604");
		user.setUsername("201207091604");
		
		System.out.println("before:"+user);
		regionRepository.save(user);
		System.out.println("after :"+user);
	}
}

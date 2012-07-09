package org.dongq.spring.demo.jpa.repositories;

import org.dongq.spring.demo.jpa.model.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegionRepository extends JpaRepository<SysUser, Long> {

}

package com.ee2.mail_worker.dao.repositories;

import com.ee2.mail_worker.dao.entities.SubSystemEntity;
import com.ee2.mail_worker.dao.entities.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubSystemRepository extends JpaRepository<SubSystemEntity, Integer> {
}

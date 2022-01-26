package com.ee2.mail_worker.dao.repositories;


import com.ee2.mail_worker.dao.entities.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository
        extends JpaRepository<UsersEntity, Long> {
    //  extends MongoRepository<UsersEntity, Long> {
}

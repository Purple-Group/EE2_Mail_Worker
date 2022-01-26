package com.ee2.mail_worker.dao.entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "Users")
public class UsersEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "UserId", nullable = false)
    private long userId;
    @Basic @Column(name = "UserLevelID", nullable = false)
    private long userLevelId;
    @Basic@ Column(name = "PortfolioID", nullable = false)
    private long portfolioId;
    @Basic @Column(name = "eMail", nullable = false)
    private String email;
    @Basic @Column(name = "SubSystemID", nullable = false)
    private int subSystemId;
    @Basic@Column(name = "userRoleID", nullable = false)
    private long userRoleId;
    @Basic @Column(name = "countryID", nullable = false)
    private int countryId;
    @Basic @Column(name = "archived", nullable = false)
    private boolean archived;
    @Basic @Column(name = "contractingEntityID", nullable = false)
    private int contractingEntityId;
    @Basic @Column(name = "userStatusID", nullable = false)
    private int userStatusId;
    @Basic @Column(name = "CustomerCode", nullable = false)
    private String customerCode;
    @Basic @Column(name = "FirstName", nullable = false)
    private String firstName;
    @Basic @Column(name = "LastName", nullable = false)
    private String lastName;


//    private SubSystemEntity subSystem;

}

package com.ee2.mail_worker.dao.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "SubSystem")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class SubSystemEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SubSystemID", nullable = false)
    private Integer subSystemId;
    @Column(name = "SubSystem", nullable = false)
    private String subSystem;


}


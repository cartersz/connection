package com.orvibo.cloud.connection.common.db.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by sunlin on 2017/10/25.
 */
@Entity
@Table(name = "user")
public class User {

    @javax.persistence.Id
    private String Id;

    private String name;

    @Override
    public String toString() {
        return "User{" +
                "Id='" + Id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}

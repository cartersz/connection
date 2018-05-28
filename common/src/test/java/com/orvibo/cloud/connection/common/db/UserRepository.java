package com.orvibo.cloud.connection.common.db;

import com.orvibo.cloud.connection.common.db.entity.User;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by sunlin on 2017/10/25.
 */
public interface UserRepository extends CrudRepository<User, String> {
}

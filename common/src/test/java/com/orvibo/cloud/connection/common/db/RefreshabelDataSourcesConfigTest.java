package com.orvibo.cloud.connection.common.db;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by sunlin on 2017/10/25.
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {RefreshabelDataSourcesConfig.class})
//@ContextConfiguration(classes = {NewRefreshableDataSourcesConfig.class})
public class RefreshabelDataSourcesConfigTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testGetUser() throws Exception {
        System.out.println("####=> " + userRepository.findAll());

        Thread.sleep(1000 * 60 *1);

        System.out.println("####=> " + userRepository.findAll());
    }
}
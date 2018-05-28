package com.orvibo.cloud.connection.common.elasticjob;

import com.dangdang.ddframe.job.lite.api.JobScheduler;
import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;

/**
 * Created by sunlin on 2017/11/17.
 */
public class ElasticJobDemo {

    private final static String ZK_SERVER_LIST = "192.168.2.201:2181,192.168.2.202:2181,192.168.2.192:2181";

    public static void main(String[] args) {
        new JobScheduler(createRegistryCenter(), MyElasticJob.configSimpleJob()).init();
    }

    private static CoordinatorRegistryCenter createRegistryCenter() {
        CoordinatorRegistryCenter regCenter = new ZookeeperRegistryCenter(new ZookeeperConfiguration(ZK_SERVER_LIST, "elastic-job-demo"));
        regCenter.init();
        return regCenter;
    }
}

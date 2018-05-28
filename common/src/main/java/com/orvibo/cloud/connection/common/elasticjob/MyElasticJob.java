package com.orvibo.cloud.connection.common.elasticjob;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by sunlin on 2017/11/17.
 */
public class MyElasticJob implements SimpleJob {

    private static final Logger logger = LoggerFactory.getLogger(MyElasticJob.class);

    @Override
    public void execute(ShardingContext shardingContext) {
        switch (shardingContext.getShardingItem()) {
            case 0:
                // do something by sharding item 0
                logger.info("excute job sharding item 0, ShardingParameter={}", shardingContext.getShardingParameter());
                break;
            case 1:
                // do something by sharding item 1
                logger.info("excute job sharding item 1, ShardingParameter={}", shardingContext.getShardingParameter());
                break;
            case 2:
                // do something by sharding item 2
                logger.info("excute job sharding item 2, ShardingParameter={}", shardingContext.getShardingParameter());
                break;
        }
    }

    public static LiteJobConfiguration configSimpleJob() {
        JobCoreConfiguration simpleCoreConfig = JobCoreConfiguration.newBuilder("mySimpleJob", "0/15 * * * * ?", 3)
                .shardingItemParameters("0=A,1=B,2=C").build();

        // 定义SIMPLE类型配置
        SimpleJobConfiguration simpleJobConfig = new SimpleJobConfiguration(simpleCoreConfig, MyElasticJob.class.getCanonicalName());
        // 定义Lite作业根配置
        LiteJobConfiguration simpleJobRootConfig = LiteJobConfiguration.newBuilder(simpleJobConfig).build();
        return simpleJobRootConfig;
    }
}

package com.jimmy.batch.configuration;

import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;

import javax.sql.DataSource;

public class BatchConfiguration extends DefaultBatchConfiguration {

    @Override
    protected DataSource getDataSource() {
        return super.getDataSource();
    }
}

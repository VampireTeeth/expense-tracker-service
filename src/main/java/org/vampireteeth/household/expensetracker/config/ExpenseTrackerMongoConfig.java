package org.vampireteeth.household.expensetracker.config;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

/**
 * Created by steven on 2/04/18.
 */
@EnableReactiveMongoRepositories
public class ExpenseTrackerMongoConfig extends AbstractReactiveMongoConfiguration{

    @Override
    public MongoClient reactiveMongoClient() {
        return MongoClients.create();
    }

    @Override
    protected String getDatabaseName() {
        return "expense-tracker";
    }
}

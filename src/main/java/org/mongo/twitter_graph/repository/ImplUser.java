package org.mongo.twitter_graph.repository;

import org.apache.log4j.Logger;
import org.mongo.twitter_graph.domain.User;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import javax.annotation.Resource;

import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * Created by Lassoued on 08/05/2015.
 */
public abstract class ImplUser implements UserRepository {

    protected static Logger logger = Logger.getLogger("service");

    @Resource(name="mongoTemplate")
    private MongoTemplate mongoTemplate;

    @Override
    public User findOne(String s) {
        logger.debug("Retrieving all persons");
            Query query = new Query(where("id").exists(true)).limit(1);
            if (mongoTemplate == null) {
                throw new IllegalStateException("mongoTemplate is null");
            }
            return (User) mongoTemplate.find(query, User.class);
    }
}

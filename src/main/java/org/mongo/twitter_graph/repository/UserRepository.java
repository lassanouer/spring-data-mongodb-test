package org.mongo.twitter_graph.repository;

import org.mongo.twitter_graph.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the User entity.
 */
public interface UserRepository extends MongoRepository<User,String> {

    @Override
    User findOne(String s);
}

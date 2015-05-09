package org.mongo.twitter_graph.repository;

import org.mongo.twitter_graph.domain.Topic;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the Topic entity.
 */
public interface TopicRepository extends MongoRepository<Topic,String> {

}

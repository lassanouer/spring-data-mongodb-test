package org.mongo.twitter_graph.repository;

import org.mongo.twitter_graph.domain.Hashtag;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the Hashtag entity.
 */
public interface HashtagRepository extends MongoRepository<Hashtag,String> {

}

package org.mongo.twitter_graph.repository;

import org.mongo.twitter_graph.domain.Smileys;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the Smileys entity.
 */
public interface SmileysRepository extends MongoRepository<Smileys,String> {

}

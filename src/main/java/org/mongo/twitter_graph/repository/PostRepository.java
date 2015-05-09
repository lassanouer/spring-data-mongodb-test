package org.mongo.twitter_graph.repository;

import org.mongo.twitter_graph.domain.Post;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the Post entity.
 */
public interface PostRepository extends MongoRepository<Post,String> {

}

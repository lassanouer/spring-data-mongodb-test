package org.mongo.twitter_graph.service;

import org.mongo.twitter_graph.domain.Post;
import org.mongo.twitter_graph.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TweetService {

    private UserRepository<Post> postRepository;

    private final Logger log = LoggerFactory.getLogger(TweetService.class);

    public void create(Post post) {
        Post postExistant = postRepository.findOne(post.getId());
        if(postExistant == null)
        // We must save both separately since there is no cascading feature
        // in Spring Data MongoDB (for now)
        postRepository.save(post);
    }

    public Post read(Post post) {
        return post;
    }

    public List<Post> readAll() {
        return postRepository.findAll();
    }

    public void update(Post post) {
        Post existingPost = postRepository.findOne(post.getId());
        if (existingPost == null) {
            postRepository.save(post);
        }
        existingPost.setText(post.getText());
        existingPost.setDate(post.getDate());
        existingPost.setScore(post.getScore());
        existingPost.setHashtag(post.getHashtag());
        existingPost.setLanguage(post.getLanguage());
        existingPost.setIdLongPost(post.getIdLongPost());

        postRepository.save(existingPost);
    }

    public Boolean delete(Post post) {
        Post existingPost = postRepository.findOne(post.getId());
        if (existingPost == null) {
            return false;
        }
        postRepository.delete(existingPost);
        return true;
    }
}

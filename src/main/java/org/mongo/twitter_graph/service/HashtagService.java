package org.mongo.twitter_graph.service;

import org.mongo.twitter_graph.domain.Hashtag;
import org.mongo.twitter_graph.domain.User;
import org.mongo.twitter_graph.repository.HashtagRepository;
import org.mongo.twitter_graph.repository.PostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HashtagService {

    @Autowired
    private HashtagRepository hashtagRepository;

    private final Logger log = LoggerFactory.getLogger(HashtagService.class);

    public Hashtag create(Hashtag hashtag) {
        Hashtag existingHashtag =  hashtagRepository.findOne(hashtag.getId());
        if (existingHashtag != null) {
            existingHashtag.setHashtag(hashtag.getHashtag());
            throw new RuntimeException("Record already exists!");
        }
        return hashtagRepository.save(hashtag);
    }

    public Hashtag read(Hashtag hashtag) {
        return hashtag;
    }

    public List<Hashtag> readAll() {
        List<Hashtag> hashtags = new ArrayList<Hashtag>();
        List<Hashtag> results = hashtagRepository.findAll();
        for (Hashtag r: results) {
            hashtags.add(r);
        }
        return hashtags;
    }

    public Hashtag update(Hashtag hashtag) {
        Hashtag existingHashtags = hashtagRepository.findOne(hashtag.getId());
        if (existingHashtags == null) {
            return null;
        }
        existingHashtags.setHashtag(hashtag.getHashtag());
        existingHashtags.setCount(hashtag.getCount());
        return hashtagRepository.save(existingHashtags);
    }

    public Boolean delete(Hashtag hashtag) {
        Hashtag existingHashtag = hashtagRepository.findOne(hashtag.getId());
        if (existingHashtag == null) {
            return false;
        }
        hashtagRepository.delete(existingHashtag);
        return true;
    }
}

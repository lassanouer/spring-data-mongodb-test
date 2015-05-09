package org.mongo.twitter_graph.service;

import org.mongo.twitter_graph.domain.User;
import org.mongo.twitter_graph.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    public User findOne(String id){
        return userRepository.findOne(id);
    }

    public User create(User user) {
        User existingUser =  userRepository.findOne(user.getId());
        if (existingUser != null) {
            existingUser.setPost(user.getPost());
            throw new RuntimeException("Record already exists!");
        }
        return userRepository.save(user);
    }

    public User read(User user) {
        return user;
    }

    public List<User> readAll() {
        List<User> users = new ArrayList<User>();
        List<User> results = userRepository.findAll();
        for (User r: results) {
            users.add(r);
        }
        return users;
    }

    public User update(User user) {
        User existingUser = userRepository.findOne(user.getId());
        if (existingUser == null) {
            return null;
        }
        existingUser.setUser(user.getUser());
        existingUser.getPost().addAll(user.getPost());
        return userRepository.save(existingUser);
    }

    public Boolean delete(User user) {
        User existingUser = userRepository.findOne(user.getId());
        if (existingUser == null) {
            return false;
        }
        userRepository.delete(existingUser);
        return true;
    }
}

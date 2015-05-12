package org.mongo.twitter_graph.repository;

import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Spring Data MongoDB repository for the User entity.
 */
public class UserRepository<T> implements MongoRepository<T, String> {

    public static Logger logger = Logger.getLogger(UserRepository.class);

    private MongoTemplate mongoTemplate;
    private Class<T> clazz = null;

    public T findById(String _id) {
        if(clazz != null){
            T resource = mongoTemplate.findOne(new BasicQuery("{ id : \"" + _id + "\"}"), clazz);
            if(resource == null){
//				resource = mongoTemplate.findById(new ObjectId(_id), clazz);
            }
            clazz = null;
            return resource;
        } else {
            return null;
        }
    }

    public List<T> findByEMRId(String emrId) {
        if(clazz != null){
            // http://examples.lishman.com/spring/data-mongodb/mongotemplate-queries.html
            List<T> resource = mongoTemplate.find(new BasicQuery("{ \"emrId\" : \"" + emrId + "\"}"), clazz);
            clazz = null;
            return resource;
        } else {
            return null;
        }
    }

    private static int tryDetermineRealSizeOrReturn(Iterable<?> iterable, int defaultSize) {
        return iterable == null ? 0 : (iterable instanceof Collection) ? ((Collection<?>) iterable).size() : defaultSize;
    }

    private static <T> List<T> convertIterableToList(Iterable<T> entities) {

        if (entities instanceof List) {
            return (List<T>) entities;
        }

        int capacity = tryDetermineRealSizeOrReturn(entities, 10);

        if (capacity == 0 || entities == null) {
            return Collections.<T> emptyList();
        }

        List<T> list = new ArrayList<T>(capacity);
        for (T entity : entities) {
            list.add(entity);
        }

        return list;
    }

    @Override
    public <S extends T> List<S> save(Iterable<S> iterable) {
        List<S> result = convertIterableToList(iterable);
        boolean allNew = true;

        for (S entity : iterable) {
            if (allNew ) {
                allNew = false;
            }
        }

        if (allNew) {
            mongoTemplate.insertAll(result);
        } else {

            for (S entity : result) {
                save(entity);
            }
        }

        return result;
    }

    @Override
    public T findOne(String s) {
        return null;
    }

    @Override
    public List<T> findAll() {
        logger.info(clazz);
        if(clazz != null){
            logger.info(clazz.getName());
            List<T> list = mongoTemplate.findAll(clazz);
            clazz = null;
            return list;
        } else {
            logger.info("You miss the class!");
            return null;
        }
    }

    @Override
    public Iterable<T> findAll(Iterable<String> iterable) {
        return null;
    }

    @Override
    public List<T> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<T> findAll(Pageable pageable) {
        // TODO Auto-generated method stub
        clazz = null;
        return null;
    }

    @Override
    public long count() {
        if(clazz != null){
            Long count = mongoTemplate.count(new Query(), clazz);
            clazz = null;
            return count;
        } else {
            return 0;
        }
    }

    @Override
    public void delete(String s) {

    }

    @Override
    public <String extends T> String save(String entity) {
        if(!mongoTemplate.collectionExists(entity.getClass())){
            logger.info("mongodb \t" + entity.getClass());
            mongoTemplate.createCollection(entity.getClass());
            mongoTemplate.insert(entity);
        } else {
            logger.info("mongodb \t" + entity.getClass().getSimpleName());
            mongoTemplate.save(entity);
        }
        clazz = null;
        return entity;
    }


    @Override
    public void delete(Iterable<? extends T> iterable){
    }

    @Override
    public void delete(T t) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public boolean exists(String _id) {
        T resource = mongoTemplate.findById(new ObjectId(_id), clazz);
        clazz = null;
        if(resource == null) return false;
        return true;
    }

    public Class<T> getClazz() {
        return this.clazz;
    }

    public void setClazz(Class<T> clazz) {
        this.clazz = clazz;
    }

    public T findByUsername(String _username) {
        T resource = mongoTemplate.findOne(new BasicQuery("{\"username\" : \"" + _username + "\"}"), clazz);
        clazz = null;
        return resource;
    }
}

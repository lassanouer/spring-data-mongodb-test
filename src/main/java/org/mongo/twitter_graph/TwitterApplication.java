package org.mongo.twitter_graph;

import com.mongodb.Mongo;
import facebook4j.FacebookException;
import org.mongo.twitter_graph.crowler.Execution;
import org.mongo.twitter_graph.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
//import org.springframework.boot.actuate.autoconfigure.MetricFilterAutoConfiguration;
//import org.springframework.boot.actuate.autoconfigure.MetricRepositoryAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.SimpleCommandLinePropertySource;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.IOException;
import java.util.Arrays;

@ComponentScan(basePackages = "org.mongo.twitter_graph.service")
@EnableAutoConfiguration
public class TwitterApplication {

    private static final Logger log = LoggerFactory.getLogger(TwitterApplication.class);

    @Inject
    private Environment env;

    @PostConstruct
    public void initApplication() throws IOException {
        if (env.getActiveProfiles().length == 0) {
            log.warn("No Spring profile configured, running with default configuration");
        } else {
            log.info("Running with Spring profile(s) : {}", Arrays.toString(env.getActiveProfiles()));
        }
    }

    //collection
    @Bean
    public MongoTemplate mongoTemplate (MongoDbFactory mongoDbFactory) {
        return  new MongoTemplate(mongoDbFactory);
    }

	public static void main(String[] args) throws IOException, FacebookException, InterruptedException {
        //SpringApplication app = new SpringApplication(TwitterApplication.class);
        SpringApplication.run(TwitterApplication.class, args);
        Execution exe = new Execution();
        //app.setShowBanner(false);
        SimpleCommandLinePropertySource source = new SimpleCommandLinePropertySource(args);

	}
}

package twitter_graph.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mongo.twitter_graph.TwitterApplication;
import org.mongo.twitter_graph.controler.DatabaseConfiguration;
import org.mongo.twitter_graph.repository.UserRepository;
import org.mongo.twitter_graph.service.UserService;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.inject.Inject;


/**
 * Test class for the UserResource REST controller.
 *
 * @see org.mongo.twitter_graph.service.UserService
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TwitterApplication.class)
@WebAppConfiguration
@IntegrationTest
@Import(DatabaseConfiguration.class)
public class UserServiceTest {

    @Inject
    private UserRepository userRepository;

    @Inject
    private UserService userService;

    @Test
    public void testRemoveOldPersistentTokens() {
       /* User admin = userRepository.findOneByLogin("admin").get();
        int existingCount = persistentTokenRepository.findByUser(admin).size();
        generateUserToken(admin, "1111-1111", new LocalDate());
        LocalDate now = new LocalDate();
        generateUserToken(admin, "2222-2222", now.minusDays(32));
        assertThat(persistentTokenRepository.findByUser(admin)).hasSize(existingCount + 2);
        userService.removeOldPersistentTokens();
        assertThat(persistentTokenRepository.findByUser(admin)).hasSize(existingCount + 1);*/
    }

    @Test
    public void testFindNotActivatedUsersByCreationDateBefore() {
        /*userService.removeNotActivatedUsers();
        DateTime now = new DateTime();
        List<User> users = userRepository.findAllByActivatedIsFalseAndCreatedDateBefore(now.minusDays(3));
        assertThat(users).isEmpty();*/
    }

}

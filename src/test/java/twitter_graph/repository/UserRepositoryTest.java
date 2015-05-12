package twitter_graph.repository;

/**
 * Created by Lassoued on 12/05/2015.
 */
import static org.junit.Assert.*;

import static org.hamcrest.Matchers.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mongo.twitter_graph.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.query.MongoEntityInformation;
import org.springframework.data.mongodb.repository.support.SimpleMongoRepository;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author <a href="mailto:kowsercse@gmail.com">A. B. M. Kowser</a>
 * @author Thomas Darimont
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:TwitterGraph-server.xml")
public class UserRepositoryTest {

    @Autowired private MongoTemplate template;

    private User oliver, dave;
    private List<User> all;

    private MongoEntityInformation<User, String> personEntityInformation = new CustomizedPersonInformation();
    private SimpleMongoRepository<User, String> repository;

    @Before
    public void setUp() {
        repository = new SimpleMongoRepository<User, String>(personEntityInformation, template);
        repository.deleteAll();

        oliver = new User();
        oliver.setId("test0");
        oliver.setReseau("test0");
        dave = new User();
        dave.setId("test1");
        dave.setReseau("Matthews");
        all = repository.save(Arrays.asList(oliver, dave));
    }

    @Test
    public void findALlFromCustomCollectionName() {
        List<User> result = repository.findAll();
        assertThat(result, hasSize(all.size()));
    }

    @Test
    public void findOneFromCustomCollectionName() {
        User result = repository.findOne(dave.getId());
        assertThat(result, is(dave));
    }

    @Test
    public void deleteFromCustomCollectionName() {
        repository.delete(dave);
        List<User> result = repository.findAll();

        assertThat(result, hasSize(all.size() - 1));
        assertThat(result, not(hasItem(dave)));
    }

    @Test
    public void deleteByIdFromCustomCollectionName() {
        repository.delete(dave.getId());
        List<User> result = repository.findAll();

        assertThat(result, hasSize(all.size() - 1));
        assertThat(result, not(hasItem(dave)));
    }

    /**
     * @see
     */
    @Test
    public void shouldInsertSingle() {

        String randomId = UUID.randomUUID().toString();

        User person1 = new User();
        person1.setId("randomId");
        person1.setReseau("test0");
        person1 = repository.save(person1);

        User saved = repository.findOne(person1.getId());

        assertThat(saved, is(equalTo(person1)));
    }

    /**
     * @see
     */
    @Test
    public void shouldInsertMutlipleFromList() {

        String randomId = UUID.randomUUID().toString();
        Map<String, User> idToPerson = new HashMap<String, User>();
        List<User> persons = new ArrayList<User>();

        for (int i = 0; i < 10; i++) {
            User person = new User();
            person.setId(randomId+i);
            person.setReseau("test0");
            idToPerson.put(person.getId(), person);
            persons.add(person);
        }

        List<User> saved = repository.save(persons);

        assertThat(saved, hasSize(persons.size()));
        assertThatAllReferencePersonsWereStoredCorrectly(idToPerson, saved);
    }

    /**
     * @see
     */
    @Test
    public void shouldInsertMutlipleFromSet() {

        String randomId = UUID.randomUUID().toString();
        Map<String, User> idToPerson = new HashMap<String, User>();
        Set<User> persons = new HashSet<User>();

        for (int i = 0; i < 10; i++) {
            User person = new User();
            person.setId(randomId+i);
            person.setReseau("test0");
            idToPerson.put(person.getId(), person);
            persons.add(person);
        }

        List<User> saved = repository.save(persons);

        assertThat(saved, hasSize(persons.size()));
        assertThatAllReferencePersonsWereStoredCorrectly(idToPerson, saved);
    }

    private void assertThatAllReferencePersonsWereStoredCorrectly(Map<String, User> references, List<User> saved) {

        for (User person : saved) {
            User reference = references.get(person.getId());
            assertThat(person, is(equalTo(reference)));
        }
    }

    private static class CustomizedPersonInformation implements MongoEntityInformation<User, String> {

        @Override
        public boolean isNew(User entity) {
            return entity.getId() == null;
        }

        @Override
        public String getId(User entity) {
            return entity.getId();
        }

        @Override
        public Class<String> getIdType() {
            return String.class;
        }

        @Override
        public Class<User> getJavaType() {
            return User.class;
        }

        @Override
        public String getCollectionName() {
            return "customizedPerson";
        }

        @Override
        public String getIdAttribute() {
            return "id";
        }
    }

}
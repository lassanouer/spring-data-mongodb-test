package org.mongo.twitter_graph.Crowler;

import com.google.common.base.Optional;
import com.optimaize.langdetect.LanguageDetector;
import com.optimaize.langdetect.LanguageDetectorBuilder;
import com.optimaize.langdetect.ngram.NgramExtractors;
import com.optimaize.langdetect.profiles.LanguageProfile;
import com.optimaize.langdetect.profiles.LanguageProfileReader;
import com.optimaize.langdetect.text.CommonTextObjectFactories;
import com.optimaize.langdetect.text.TextObject;
import com.optimaize.langdetect.text.TextObjectFactory;
import facebook4j.*;
import facebook4j.ResponseList;
import org.mongo.twitter_graph.domain.*;
import org.mongo.twitter_graph.domain.Post;
import org.mongo.twitter_graph.domain.Topic;
import org.mongo.twitter_graph.domain.User;
import org.mongo.twitter_graph.repositories.TweetRepository;
import org.mongo.twitter_graph.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Lassoued on 18/03/2015.
 */

public class Feed {

    @Autowired
    UserRepository userRepository;

    @Autowired
    TweetRepository tweetRepository;

    @Autowired
    MongoTemplate mongoTemplate;

    // The Facebook and Twitter clients
    private static Facebook facebookclient;
    private static Twitter twitterclient;

    // The oauth key and secret for app
    private static final String fbkey = "1584014868510703";
    private static final String fbsecret = "rCUa-FHrVUdfAGiAD1ugdRQ8Z6o";
    private static final String twkey = "2759852136-3Eb1lVNI1kFjgwwdmLVpuPPA6go9ZVnSZk8jQ5v";
    private static final String twsecret = "ffWcsC1ggAeIA4gMi0F4nX3aCisbh0HNpLzIdl6r8N98g";

    // The list that will contain the results
    private static HashMap<String, Post> twfeedlist;

    private static int limitresults;

    private static int done = 0;

    public void init() {
        // Configuration
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey("J2IYsfXCA3LqAWbBlEZSQoaHW")
                .setOAuthConsumerSecret("aWQlHV09TY14W7m5RaIfBBJcrWXJoktFhx8fAnsunfeuO5Ih2B")
                .setOAuthAccessToken("2759852136-3Eb1lVNI1kFjgwwdmLVpuPPA6go9ZVnSZk8jQ5v")
                .setOAuthAccessTokenSecret("ffWcsC1ggAeIA4gMi0F4nX3aCisbh0HNpLzIdl6r8N98g");
        TwitterFactory twitterFactory = new TwitterFactory(cb.build());

        // Setup
        twitterclient = twitterFactory.getInstance();
        facebookclient = new FacebookFactory().getInstance();

        // Facebook OAuth
        facebookclient.setOAuthAppId(fbkey,fbsecret);
        facebookclient.setOAuthAccessToken(new facebook4j.auth.AccessToken(fbkey + "|" + fbsecret, null));

        // Twitter OAuth
        //twitterclient.setOAuthConsumer(twkey, twsecret);

        // For the results
        twfeedlist = new HashMap<String, Post>();
    }

    public HashMap<String, Post> getTWfeedlist()
    {
        return twfeedlist;
    }

    /**
     * fastTwitter can be ran concurrently with fastFacebook for performance gains
     * @author Lassoued
     *
     */
    public class FastTwitter implements Runnable {
        private final String arg;

        public FastTwitter(String arg) throws IOException {
            this.arg =arg;
        }

        @Override
        public void run() {
            //load all languages:
            List<LanguageProfile> languageProfiles = null;
            try {
                languageProfiles = new LanguageProfileReader().readAll();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //build language detector:
            LanguageDetector languageDetector = LanguageDetectorBuilder.create(NgramExtractors.standard())
                    .withProfiles(languageProfiles)
                    .build();
            //create a text object factory
            TextObjectFactory textObjectFactory = CommonTextObjectFactories.forDetectingOnLargeText();
            Reseau reseau = new Reseau("Twitter");
            int i =0;
            ArrayList<Post> tmplist = new ArrayList<Post>();
            try {
                Query query = new Query(arg);
                QueryResult result;
                do {
                    result = twitterclient.search(query);
                    List<Status> tweets = result.getTweets();
                    for (Status tweet : tweets) {
                        TextObject textObject = textObjectFactory.forText(tweet.getText());
                        Optional<String> lang = languageDetector.detect(textObject);
                        System.out.println(lang + " # " + "@" + tweet.getUser().getName() + " - " + tweet.getText());
                        org.mongo.twitter_graph.domain.Post tmp = new Post(tweet.getId()+"",tweet.getUser().getName(),tweet.getCreatedAt().toString(),reseau);
                        tmp.setIdFromTwitter(tweet.getId());
                        User source = new User(tweet.getUser().getName(), tweet.getUser().getId(), reseau);
                        tmp.addMention(source);
                        //userRepository.save(source);
                        tmp.setLang(lang.toString());
                        tmp.addTag(new Topic(arg));
                        tweetRepository.save(tmp);
                        mongoTemplate.save(tmp);
                        tmplist.add(tmp);
                        i++;
                        try {
                            String txt = " ";
                            try {
                                txt = lang.get().intern().toString();
                            } catch (Exception e) {
                                txt = "msg";
                            }
                            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("output/" + txt + ".txt", true)));
                            out.println(arg + "   @" + tweet.getUser().getScreenName() + " - " + tweet.getText());
                            out.close();
                        } catch (IOException e) {

                        }
                    }
                } while ((query = result.nextQuery()) != null);

                Thread.currentThread().interrupt();
            } catch (TwitterException ty) {
                try {
                    TimeUnit.MINUTES.sleep(15);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            twfeedlist.clear();
            //twfeedlist.putAll(************,tmplist); key list??
            tmplist = null;
            ++done;
        }
    } // End of fastTwitter class


    /**
     * fastFacebook can be ran concurrently with fastTwitter for performance gains
     * @author Lassoued
     *
     */
    public class FastFacebook implements Runnable {
    //public class FastFacebook  {

        private final String arg;

        // Gets the latest facebook statuses
        public FastFacebook(String arg) throws FacebookException, IOException {
            this.arg = arg;
        }

        @Override
        public void run() {

            //load all languages:
            List<LanguageProfile> languageProfiles = null;
            try {
                languageProfiles = new LanguageProfileReader().readAll();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //build language detector:
            LanguageDetector languageDetector = LanguageDetectorBuilder.create(NgramExtractors.standard())
                    .withProfiles(languageProfiles)
                    .build();
            //create a text object factory
            TextObjectFactory textObjectFactory = CommonTextObjectFactories.forDetectingOnLargeText();
            ArrayList<Post> tmplist = new ArrayList<>();
            ResponseList<Page>results = null;
            Reseau reseau = new Reseau("Facebook");
            try {
                results = facebookclient.searchPages(arg);
            } catch (FacebookException e) {
                e.printStackTrace();
            }
            for (Page result : results) {
                System.out.println(result.getName());
                ResponseList<facebook4j.Post> feeds = null;
                try {
                    feeds = facebookclient.getPosts(result.getId());
                } catch (FacebookException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < feeds.size(); i++) {
                    System.out.println("Page :  " + feeds.get(i).getName() + " liked By :   ");
                    PagableList<Like> likePage = feeds.get(i).getLikes();
                    for (Like likeP : likePage) {
                        User follower = new User();
                        System.out.println("____________________" + likeP.getId() + "  " + likeP.getName());
                        follower.setId(likeP.getId());
                        follower.setUser(likeP.getName());
                        follower.setReseau(reseau);
                        userRepository.save(follower);
                        //FaceLike(0)
                        //tmp.addMention(follower);
                        follower = null;
                    }
                    PagableList<Comment> posts = feeds.get(i).getComments();
                    for (Comment cmnt : posts) {
                        TextObject textObject = textObjectFactory.forText(cmnt.getMessage());
                        Optional<String> lang = languageDetector.detect(textObject);
                        System.out.println(lang + " # " + cmnt.getId() + "  " + cmnt.getMessage() + " Liked By " + cmnt.getLikeCount() + " persons");
                        Post tmp1 = new Post();
                        tmp1.setDate(cmnt.getCreatedTime().toString());
                        tmp1.setText(cmnt.getMessage());
                        tmp1.setId(cmnt.getId());
                        tmp1.setReseau(reseau);
                        tmp1.setLang(lang.toString());
                        tmp1.addTag(new Topic(arg));
                        try{
                            String txt = " ";
                            try {
                                txt = lang.get().intern().toString();
                            }catch(Exception e){
                                txt = "msg";
                            }
                            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("output/"+txt+".txt", true)));
                            out.println(arg+"    @" + cmnt.getId() + " : " + cmnt.getMessage());
                            out.close();
                        }catch (IOException e){

                        }
                        PagableList<Like> cmntLikes = null;
                        try {
                            cmntLikes = facebookclient.getPostLikes(cmnt.getId().toString());
                        } catch (FacebookException e) {
                            e.printStackTrace();
                        }
                        for (Like cmntLike : cmntLikes) {
                            User follower = new User();
                            follower.setId(cmntLike.getId());
                            follower.setUser(cmntLike.getName());
                            follower.setReseau(reseau);
                            tmp1.addMention(follower);
                            userRepository.save(follower);
                            tweetRepository.save(tmp1);
                            mongoTemplate.save(tmp1);
                            tmp1 = null;
                            follower = null;
                            System.out.println(cmntLike.getId() + " " + cmntLike.getName() + " ");
                        }
                    }
                }
                ResponseList<Photo> photos = null;
                try {
                    photos = facebookclient.getPhotos(result.getId());
                } catch (FacebookException e) {
                    e.printStackTrace();
                }
                for (Photo photo : photos) {
                    PagableList<Like> likePhotos = photo.getLikes();
                    for (Like likePhoto : likePhotos) {
                        User follower0 = new User();
                        follower0.setId(likePhoto.getId());
                        follower0.setUser(likePhoto.getName());
                        follower0.setReseau(reseau);
                        userRepository.save(follower0);
                        //tmp0.addMention(follower0);
                        follower0 = null;
                        System.out.println("____________________" + likePhoto.getId() + "  " + likePhoto.getName());
                    }
                    PagableList<Comment> commentsPhoto = photo.getComments();
                    for (Comment commentPhoto : commentsPhoto) {
                        Post tmp0 = new Post();
                        tmp0.setDate(commentPhoto.getCreatedTime().toString());
                        tmp0.setText(commentPhoto.getMessage());
                        tmp0.setId(commentPhoto.getId());
                        tmp0.setReseau(reseau);
                        tmp0.addTag(new Topic(arg));
                        TextObject textObject = textObjectFactory.forText(commentPhoto.getMessage());
                        Optional<String> lang = languageDetector.detect(textObject);
                        tmp0.setLang(lang.toString());
                        System.out.println(lang + " # " + commentPhoto.getId() + "  " + commentPhoto.getMessage() + " Liked By " + commentPhoto.getLikeCount() + " persons");
                        try{
                            String txt = " ";
                            try {
                                txt = lang.get().intern().toString();
                            }catch(Exception e){
                                txt = "msg";
                            }
                            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("output/"+txt+".txt", true)));
                            out.println(arg+"    @" + commentPhoto.getId() + " : " + commentPhoto.getMessage());
                            out.close();
                        }catch (IOException e){

                        }
                        PagableList<Like> cmntPhotoLikes = null;
                        try {
                            cmntPhotoLikes = facebookclient.getPostLikes(commentPhoto.getId().toString());
                        } catch (FacebookException e) {
                            e.printStackTrace();
                        }
                        for (Like cmntPhotoLike : cmntPhotoLikes) {
                            User follower0 = new User();
                            follower0.setId(cmntPhotoLike.getId());
                            follower0.setUser(cmntPhotoLike.getName());
                            follower0.setReseau(reseau);
                            tmp0.addMention(follower0);
                            userRepository.save(follower0);
                            //tmp0.addMention(follower0);
                            follower0 = null;
                            System.out.println(cmntPhotoLike.getId() + " " + cmntPhotoLike.getName() + " ");
                        }
                        tweetRepository.save(tmp0);
                        mongoTemplate.save(tmp0);
                        tmp0 = null;
                    }
                }
                ResponseList<Video> videos = null;
                try {
                    videos = facebookclient.getVideos(result.getId());
                } catch (FacebookException e) {
                    e.printStackTrace();
                }
                for (Video video : videos) {
                    System.out.println("Video:  " + video.getName());
                    PagableList<Comment> commentsVideos = video.getComments();
                    for (Comment commentVideo : commentsVideos) {
                        Post tmp1 = new Post();
                        tmp1.setDate(commentVideo.getCreatedTime().toString());
                        tmp1.setText(commentVideo.getMessage());
                        tmp1.setId(commentVideo.getId());
                        tmp1.setReseau(reseau);
                        tmp1.addTag(new Topic(arg));
                        TextObject textObject = textObjectFactory.forText(commentVideo.getMessage());
                        Optional<String> lang = languageDetector.detect(textObject);
                        tmp1.setLang(lang.toString());
                        System.out.println(lang + " # " + commentVideo.getId() + "  " + commentVideo.getMessage() + " Liked By " + commentVideo.getLikeCount() + " persons");
                        try{
                            String txt = " ";
                            try {
                                txt = lang.get().intern().toString();
                            }catch(Exception e){
                                txt = "msg";
                            }
                            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("output/"+txt+".txt", true)));
                            out.println(arg+"    @" + commentVideo.getId() + " : " + commentVideo.getMessage());
                            out.close();
                        }catch (IOException e){

                        }
                        PagableList<Like> cmntVideoLikes = null;
                        try {
                            cmntVideoLikes = facebookclient.getPostLikes(commentVideo.getId().toString());
                        } catch (FacebookException e) {
                            e.printStackTrace();
                        }
                        for (Like cmntVideoLike : cmntVideoLikes) {
                            User follower1 = new User();
                            follower1.setId(cmntVideoLike.getId());
                            follower1.setUser(cmntVideoLike.getName());
                            follower1.setReseau(reseau);
                            follower1.addTweet(tmp1);
                            tmp1.addMention(follower1);
                            userRepository.save(follower1);
                            //tmp1.addMention(follower1);
                            follower1 = null;
                            System.out.println(cmntVideoLike.getId() + " " + cmntVideoLike.getName() + " ");
                        }
                        tweetRepository.save(tmp1);
                        mongoTemplate.save(tmp1);
                        tmp1 = null;
                    }
                }
            }
            Thread.currentThread().interrupt();
        }
    }
        public void getLatest(int howmany, String arg) throws IOException, FacebookException {
        done = 0;
        limitresults = howmany;

        //Thread fbt = new Thread(new FastFacebook(arg));
        Thread twt = new Thread(new FastTwitter(arg));
        //fbt.start();
        twt.start();
        int secs = 0;
        while (done < 2) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            System.out.println("Waiting, second number " + ++secs);
            System.out.println("Done status = " + done);
        }
    }

    public void refresh(String arg) throws IOException, FacebookException {
        getLatest(100,arg);
    }

}
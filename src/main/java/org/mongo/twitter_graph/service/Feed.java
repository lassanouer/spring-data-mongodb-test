package org.mongo.twitter_graph.service;

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
import facebook4j.auth.AccessToken;
import org.mongo.twitter_graph.domain.Post;
import org.mongo.twitter_graph.domain.User;
import org.mongo.twitter_graph.repository.PostRepository;
import org.mongo.twitter_graph.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import twitter4j.*;
import twitter4j.Query;
import twitter4j.conf.ConfigurationBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Lassoued
 * on 18/03/2015.
 */
@Controller
public class Feed {

    @Autowired
    UserRepository<User> userRepository;

    @Autowired
    PostRepository tweetRepository;

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
        facebookclient.setOAuthAccessToken(new AccessToken(fbkey + "|" + fbsecret, null));

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
                        Post tmp = new Post();
                        //tmp.setIdLongPost(tweet.getId());
                        if(tweet.getId() != 0) tmp.setId(tweet.getId() + "");
                        if(lang != null) tmp.setLanguage(lang + "");
                        if(tweet.getCreatedAt() != null) tmp.setDate(tweet.getCreatedAt());
                        if(tweet.getText() != null) tmp.setText(tweet.getText());
                        tmp.setReseau("Twitter");
                        //tmp.setHashtag(tweet.getText());
                        User source = new User();
                        //if(userRepository.findOne(tweet.getUser().getId()+"")==null) {
                            source.setReseau("Twitter");
                            source.setUser(tweet.getUser().getName());
                            source.setIdFromTwitter(tweet.getUser().getId());
                            source.setId(tweet.getUser().getId() + "");
                            userRepository.save(source);
                        //}
                        source.addPost(tmp);
                        //userRepository.save(source);
                        tmp.setTopic(arg);
                        tweetRepository.save(tmp);
                        tmplist.add(tmp);
                        i++;
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
                        User follower = new User();// = userRepository.findOne(likeP.getId());
                        System.out.println("____________________" + likeP.getId() + "  " + likeP.getName());
                        if(follower==null) {
                            follower.setReseau("Facebook");
                            follower.setUser(likeP.getName());
                            follower.setId(likeP.getId());
                            userRepository.save(follower);
                        }
                        follower = null;
                    }
                    PagableList<Comment> posts = feeds.get(i).getComments();
                    for (Comment cmnt : posts) {
                        TextObject textObject = textObjectFactory.forText(cmnt.getMessage());
                        Optional<String> lang = languageDetector.detect(textObject);
                        System.out.println(lang + " # " + cmnt.getId() + "  " + cmnt.getMessage() + " Liked By " + cmnt.getLikeCount() + " persons");
                        Post tmp1 = new Post();
                        tmp1.setDate(cmnt.getCreatedTime());
                        tmp1.setText(cmnt.getMessage());
                        tmp1.setId(cmnt.getId());
                        tmp1.setReseau("Facebook");
                        tmp1.setLanguage(lang.toString());
                        tmp1.setTopic(arg);
                        PagableList<Like> cmntLikes = null;
                        try {
                            cmntLikes = facebookclient.getPostLikes(cmnt.getId().toString());
                        } catch (FacebookException e) {
                            e.printStackTrace();
                        }
                        for (Like cmntLike : cmntLikes) {
                            System.out.println(cmntLike.getId() + " " + cmntLike.getName() + " ");
                            User follower = new User();//= userRepository.findOne(cmntLike.getId());;
                            if(follower==null) {
                                follower.setReseau("Facebook");
                                follower.setUser(cmntLike.getName());
                                follower.setId(cmntLike.getId());
                            }
                            follower.addPost(tmp1);
                            userRepository.save(follower);
                            follower = null;
                            tweetRepository.save(tmp1);
                            tmp1 = null;
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
                        User follower0 = new User();//userRepository.findOne(likePhoto.getId());;
                        if(follower0==null) {
                            follower0.setReseau("Facebook");
                            follower0.setUser(likePhoto.getName());
                            follower0.setId(likePhoto.getId());
                        }
                        userRepository.save(follower0);
                        //tmp0.addMention(follower0);
                        follower0 = null;
                        System.out.println("____________________" + likePhoto.getId() + "  " + likePhoto.getName());
                    }
                    PagableList<Comment> commentsPhoto = photo.getComments();
                    for (Comment commentPhoto : commentsPhoto) {
                        Post tmp0 = new Post();
                        tmp0.setDate(commentPhoto.getCreatedTime());
                        tmp0.setText(commentPhoto.getMessage());
                        tmp0.setId(commentPhoto.getId());
                        tmp0.setReseau("Facebook");
                        tmp0.setTopic(arg);
                        TextObject textObject = textObjectFactory.forText(commentPhoto.getMessage());
                        Optional<String> lang = languageDetector.detect(textObject);
                        tmp0.setLanguage(lang.toString());
                        System.out.println(lang + " # " + commentPhoto.getId() + "  " + commentPhoto.getMessage() + " Liked By " + commentPhoto.getLikeCount() + " persons");
                        PagableList<Like> cmntPhotoLikes = null;
                        try {
                            cmntPhotoLikes = facebookclient.getPostLikes(commentPhoto.getId().toString());
                        } catch (FacebookException e) {
                            e.printStackTrace();
                        }
                        for (Like cmntPhotoLike : cmntPhotoLikes) {
                            User follower0= new User();// = userRepository.findOne(cmntPhotoLike.getId());;
                            if(follower0==null) {
                                follower0.setReseau("Facebook");
                                follower0.setUser(cmntPhotoLike.getName());
                                follower0.setId(cmntPhotoLike.getId());
                            }
                            follower0.addPost(tmp0);
                            userRepository.save(follower0);
                            follower0 = null;
                            System.out.println(cmntPhotoLike.getId() + " " + cmntPhotoLike.getName() + " ");
                        }
                        tweetRepository.save(tmp0);
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
                        tmp1.setDate(commentVideo.getCreatedTime());
                        tmp1.setText(commentVideo.getMessage());
                        tmp1.setId(commentVideo.getId());
                        tmp1.setReseau("Facebook");
                        tmp1.setTopic(arg);
                        TextObject textObject = textObjectFactory.forText(commentVideo.getMessage());
                        Optional<String> lang = languageDetector.detect(textObject);
                        tmp1.setLanguage(lang.toString());
                        System.out.println(lang + " # " + commentVideo.getId() + "  " + commentVideo.getMessage() + " Liked By " + commentVideo.getLikeCount() + " persons");
                        PagableList<Like> cmntVideoLikes = null;
                        try {
                            cmntVideoLikes = facebookclient.getPostLikes(commentVideo.getId().toString());
                        } catch (FacebookException e) {
                            e.printStackTrace();
                        }
                        for (Like cmntVideoLike : cmntVideoLikes) {
                            User follower1 = new User();//= userRepository.findOne(cmntVideoLike.getId());;
                            if(follower1==null) {
                                follower1.setReseau("Facebook");
                                follower1.setUser(cmntVideoLike.getName());
                                follower1.setId(cmntVideoLike.getId());
                            }
                            follower1.addPost(tmp1);
                            userRepository.save(follower1);
                            follower1 = null;
                            System.out.println(cmntVideoLike.getId() + " " + cmntVideoLike.getName() + " ");
                        }
                        tweetRepository.save(tmp1);
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

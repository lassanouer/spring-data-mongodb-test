package org.mongo.twitter_graph.crowler;

import facebook4j.FacebookException;
import org.mongo.twitter_graph.service.Feed;

import java.io.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Created by Lassoued on 21/04/2015.
 */
public class Execution {
    public Execution() throws InterruptedException, IOException, FacebookException {
        Feed feed = new Feed();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(new File("minution.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));
        feed.init();
        String line = null;
        ExecutorService executor = Executors.newSingleThreadExecutor();
        try {
            int proc = Runtime.getRuntime().availableProcessors();
            while ((line = br.readLine()) != null) {
                for(int i = 0; i<proc ;i++) {
                    // executor = Executors.newSingleThreadExecutor();
                    Runnable worker = feed.new FastTwitter(line);
                    Runnable runner = feed.new FastFacebook(line);
                    executor.execute(worker);
                    executor.execute(runner);
                }
            }
        } catch (IOException e) {
            executor.shutdown();
        }
          //  Thread.sleep(600);
          //  executor.shutdown();
          //  executor.awaitTermination
           //         (600, TimeUnit.MILLISECONDS);
     //       e.printStackTrace();
  /*      } catch (FacebookException e) {
            e.printStackTrace();
        }
        try {
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }
}

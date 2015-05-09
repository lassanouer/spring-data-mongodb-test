package org.mongo.twitter_graph.crowler;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import facebook4j.FacebookException;
import org.mongo.twitter_graph.service.Feed;
import twitter4j.*;

import java.io.*;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

class CrowlSpout extends BaseRichSpout {

    SpoutOutputCollector _collector;
    LinkedBlockingQueue<Status> queue = null;
    Feed _postsStream;
    boolean completed;
    int i =0;

    public CrowlSpout() throws IOException, FacebookException {

    }

    @Override
    public void open(Map map, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector) {
        queue = new LinkedBlockingQueue<Status>(1000);
        _collector = spoutOutputCollector;
        StatusListener listener = new StatusListener() {

            @Override
            public void onStatus(Status status) {
                queue.offer(status);
            }

            @Override
            public void onDeletionNotice(StatusDeletionNotice sdn) {
            }

            @Override
            public void onTrackLimitationNotice(int i) {
            }

            @Override
            public void onScrubGeo(long l, long l1) {
            }

            @Override
            public void onStallWarning(StallWarning stallWarning) {

            }

            @Override
            public void onException(Exception e) {
            }

        };
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(new File("minution.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));
        _postsStream.init();
        String line = null;
        try {
            while ((line = br.readLine()) != null) {
                ExecutorService executor = Executors.newFixedThreadPool(1);
                Runnable worker = _postsStream.new FastTwitter(line);
                executor.execute(worker);
                Runnable runner = _postsStream.new FastFacebook(line);
                executor.execute(runner);
                try {
                    Thread.sleep(600);
                    executor.shutdown();
                    executor.awaitTermination
                            (600, TimeUnit.MILLISECONDS);
                } catch (InterruptedException ignored) {
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FacebookException e) {
            e.printStackTrace();
        }
        try {
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //_postsStream = fact.getInstance();
        //_postsStream.addListener(listener);
        //Magic filter
        FilterQuery filter = new FilterQuery();
        filter.track(new String[]{line});
        //_postsStream.filter(filter);
    }


    @Override
    public void close() {

    }

    @Override
    public void activate() {

    }

    @Override
    public void deactivate() {

    }

    @Override
    public void nextTuple() {
        if(!completed)
        {
            if(i < 100000)
            {
                String item = "Tag" + Integer.toString(i++);
                System.out.println(item);
                this._collector.emit(new Values(item), item);
            }
            else
            {
                completed = true;
            }
        }
        else
        {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                Logger.getLogger(CrowlSpout.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void ack(Object o) {

    }

    @Override
    public void fail(Object o) {

    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("word"));
    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        return null;
    }

   /* private static final int RECORDS_PER_BATCH = 10;

    LinkedBlockingQueue<Status> queue = null;


    @Override
    public void open(Map conf, TopologyContext context) {
        queue = new LinkedBlockingQueue<Status>(1000);

        StatusListener listener = new StatusListener() {

            @Override
            public void onStatus(Status status) {

                queue.offer(status);
            }

            @Override
            public void onDeletionNotice(StatusDeletionNotice sdn) {
            }

            @Override
            public void onTrackLimitationNotice(int i) {
            }

            @Override
            public void onScrubGeo(long l, long l1) {
            }

            @Override
            public void onException(Exception ex) {
            }

            @Override
            public void onStallWarning(StallWarning arg0) {
                // TODO Auto-generated method stub

            }

        };

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(new File("minution.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));
        Feed feed = new Feed();
        feed.init();
        String line = null;
        try {
            while ((line = br.readLine()) != null) {
                ExecutorService executor = Executors.newFixedThreadPool(1);
                ScheduledExecutorService execute = Executors.newSingleThreadScheduledExecutor();
                Runnable worker = feed.new FastTwitter(line);
                executor.execute(worker);
                Runnable runner = feed.new FastFacebook(line);
                //feed.new FastFacebook("tunisie telecom");
                executor.execute(runner);
                try {
                    Thread.sleep(600);
                    executor.shutdown();
                    executor.awaitTermination
                            (600, TimeUnit.MILLISECONDS);
                } catch (InterruptedException ignored) {
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FacebookException e) {
            e.printStackTrace();
        }
        try {
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void emitBatch( long batchId, TridentCollector collector ) {
        int emitted=0;
        while(emitted<RECORDS_PER_BATCH) {
            Status ret = queue.poll();
            if (ret == null) {
                Utils.sleep(50);
            } else {
                collector.emit(new Values(ret));
                emitted++;
            }
        }
    }


    @Override
    public void close() {
        //_twitterStream.shutdown();
    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        Config ret = new Config();
        ret.setMaxTaskParallelism(1);
        return ret;
    }

    @Override
    public void ack(long id) {
        //System.out.println("Twitter spout ack = "+id.toString());
    }

    //@Override
    public void fail(Object id) {
        //System.out.println("Twitter spout fail = "+id.toString());
    }

    @Override
    public Fields getOutputFields() {
        return (new Fields("tweet"));
    }*/
}


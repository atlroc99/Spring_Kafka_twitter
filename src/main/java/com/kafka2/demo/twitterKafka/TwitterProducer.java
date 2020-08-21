package com.kafka2.demo.twitterKafka;

import com.google.common.collect.Lists;
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.Hosts;
import com.twitter.hbc.core.HttpHosts;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

public class TwitterProducer {
    Logger logger = LoggerFactory.getLogger(TwitterProducer.class.getName());
    public TwitterProducer() {}

    public static void main(String[] args) throws Exception{
        new TwitterProducer().run();
    }

    public void run() throws Exception{
        logger.info("Setup");
        BlockingQueue<String> msgQueue = new LinkedBlockingDeque<String>(10000);
        //create a twitter client
        Client twitterClient = createTwitterClient(msgQueue);
        twitterClient.connect();

        while(!twitterClient.isDone()) {
            String msg = null;
            try{
                msg = msgQueue.poll(5, TimeUnit.SECONDS);
            }catch (InterruptedException ie) {
                ie.printStackTrace(System.err);
                twitterClient.stop();
            }

            if (msg != null) {
                logger.info(msg);
            }
        }

        logger.info("End of application");
    }

    public Client createTwitterClient(BlockingQueue<String> msgQueue) throws Exception{
        /** Set up your blocking queues: Be sure to size these properly based on expected TPS of your stream */
        msgQueue = new LinkedBlockingDeque<String>(10000);

        /*Declare the Host you want to connect to, the endpoint, and authentication (basic or auth)*/
        Hosts hoseBirdsHosts = new HttpHosts(Constants.STREAM_HOST);
        StatusesFilterEndpoint hoseBirdEndPoint = new StatusesFilterEndpoint();
        List<String> terms = Lists.newArrayList("COVID-19");
        hoseBirdEndPoint.trackTerms(terms);

        Properties props = new Properties();
        props.load(new FileReader(getClass().getClassLoader().getResource("application.properties").getFile()));
        final String consumerKey = props.getProperty("twitter.api.key");
        final String consumerSecret = props.getProperty("twitter.api.secret.key");
        final String accessToken = props.getProperty("twitter.access.token");
        final String tokenSecret = props.getProperty("twitter.token.secret");

        Authentication hosebirdAuth = new OAuth1(consumerKey, consumerSecret, accessToken, tokenSecret);

        ClientBuilder builder = new ClientBuilder()
                .name("Hosebird-Client-01")
                .hosts(hoseBirdsHosts)
                .authentication(hosebirdAuth)
                .endpoint(hoseBirdEndPoint)
                .processor(new StringDelimitedProcessor(msgQueue));

        Client hosebirdClient = builder.build();
        return hosebirdClient;
    }
}


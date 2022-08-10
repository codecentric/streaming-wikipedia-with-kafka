package com.acme.kafka.connect.sample;

import com.launchdarkly.eventsource.EventHandler;
import com.launchdarkly.eventsource.EventSource;
import com.launchdarkly.eventsource.MessageEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ServerSentEventClient implements EventHandler {

    private static final Logger log = LoggerFactory.getLogger(ServerSentEventClient.class);

    private final EventSource eventSource;
    private final Queue<MessageEvent> eventQueue = new LinkedList<>();

    public ServerSentEventClient(String url) {
        try {
            EventSource.Builder builder = new EventSource.Builder(this, new URI(url));
            eventSource = builder.build();
        } catch (URISyntaxException ex) {
            throw new IllegalArgumentException("Bad URI: " + url);
        }
    }

    public void start() {
        log.info("Start to receive events");
        eventSource.start();
    }

    public void stop() {
        log.info("Stop to receive events");
        eventSource.close();
    }

    public List<MessageEvent> getEvents() {
        List<MessageEvent> records = new ArrayList<>();
        while(!eventQueue.isEmpty()) {
            MessageEvent current = eventQueue.poll();
            if (current == null || current.getData() == null) {
                log.info("Received no event");
            } else {
                records.add(current);
            }
        }
        return records;
    }

    @Override
    public void onOpen() throws Exception {
        log.info("Event handler now open");
    }

    @Override
    public void onClosed() throws Exception {
        log.info("Event handler now closed");
    }

    @Override
    public void onMessage(String s, MessageEvent messageEvent) throws Exception {
        eventQueue.add(messageEvent);
    }

    @Override
    public void onComment(String s) throws Exception {
        log.info("Received comment on event handler: {}", s);
    }

    @Override
    public void onError(Throwable throwable) {
        log.error("An exception occurred", throwable);
    }
}

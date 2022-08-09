package com.acme.kafka.connect.sample;

import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.source.SourceRecord;
import org.apache.kafka.connect.source.SourceTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.sse.InboundSseEvent;
import javax.ws.rs.sse.SseEventSource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.function.Consumer;

public class SampleSourceTask extends SourceTask {

    private static final Logger log = LoggerFactory.getLogger(SampleSourceTask.class);

    private final Queue<String> received = new LinkedList<>();

    private SampleSourceConnectorConfig config;

    private Client client;
    private SseEventSource eventSource;

    @Override
    public String version() {
        return PropertiesUtil.getConnectorVersion();
    }

    @Override
    public void start(Map<String, String> properties) {
        log.info("Starting Sample Source Task");
        config = new SampleSourceConnectorConfig(properties);
        client = ClientBuilder.newClient();

        log.info("Configure WebTarget with {}", config.getString(SampleSourceConnectorConfig.SSE_URI_PARAM_CONFIG));
        WebTarget target = client.target(config.getString(SampleSourceConnectorConfig.SSE_URI_PARAM_CONFIG));

        eventSource = SseEventSource.target(target).build();
        eventSource.register(onEvent, onError, onComplete);
        log.info("Is EventSource open: {}", eventSource.isOpen());
        eventSource.open();
        log.info("Is EventSource open: {}", eventSource.isOpen());
    }

    // A new event is received
    private final Consumer<InboundSseEvent> onEvent = (inboundSseEvent) -> {
        log.info("Reading data and adding to queue");
        String data = inboundSseEvent.readData();
        received.add(data);
    };

    //Error
    private final Consumer<Throwable> onError = (throwable) -> {
        throwable.printStackTrace();
    };

    //Connection close and there is nothing to receive
    private final Runnable onComplete = () -> {
        System.out.println("Done!");
    };

    @Override
    public List<SourceRecord> poll() {
        List<SourceRecord> records = new ArrayList<>();
        while(!received.isEmpty()) {
            String current = received.poll();
            if (current != null) {
                records.add(new SourceRecord(Collections.emptyMap(), Collections.emptyMap(), config.getString(SampleSourceConnectorConfig.TOPIC_PARAM_CONFIG), null, null, Schema.STRING_SCHEMA, current));
            }
        }
        return records;
    }

    @Override
    public void stop() {
        log.info("Stopping Task");
        if (eventSource != null) {
            eventSource.close();
        } else {
            log.info("eventsource is null");
        }
        if (client != null) {
            client.close();
        } else {
            log.info("Client is null");
        }
    }

}

package com.acme.kafka.connect.sample;

import com.launchdarkly.eventsource.MessageEvent;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.source.SourceRecord;
import org.apache.kafka.connect.source.SourceTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SampleSourceTask extends SourceTask {

    private static final Logger log = LoggerFactory.getLogger(SampleSourceTask.class);

    private SampleSourceConnectorConfig config;
    private ServerSentEventClient sseClient;

    @Override
    public String version() {
        return PropertiesUtil.getConnectorVersion();
    }

    @Override
    public void start(Map<String, String> properties) {
        log.info("Starting Sample Source Task");
        config = new SampleSourceConnectorConfig(properties);
        sseClient = new ServerSentEventClient(config.getString(SampleSourceConnectorConfig.SSE_URI_PARAM_CONFIG));
        sseClient.start();
    }

    @Override
    public List<SourceRecord> poll() {
        List<SourceRecord> records = new ArrayList<>();
        List<MessageEvent> received = sseClient.getEvents();
        for (MessageEvent event : received) {
            records.add(new SourceRecord(Collections.emptyMap(), Collections.emptyMap(), config.getString(SampleSourceConnectorConfig.TOPIC_PARAM_CONFIG), null, null, Schema.STRING_SCHEMA, event.getData()));
        }
        return records;
    }

    @Override
    public void stop() {
        log.info("Stopping Task");
        sseClient.stop();
    }

}

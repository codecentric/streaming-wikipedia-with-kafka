package com.acme.kafka.connect.sample;

import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigDef.Importance;
import org.apache.kafka.common.config.ConfigDef.Type;

import java.util.Map;

public class SampleSourceConnectorConfig extends AbstractConfig {

    public SampleSourceConnectorConfig(final Map<?, ?> originalProps) {
        super(CONFIG_DEF, originalProps);
    }

    public static final String TOPIC_PARAM_CONFIG = "topic";
    private static final String TOPIC_PARAM_DOC = "This is the topic where the events will be sent to";

    public static final String SSE_URI_PARAM_CONFIG = "sse.uri";
    private static final String SSE_URI_PARAM_DOC = "The URI where the application will fetch events";

    public static final ConfigDef CONFIG_DEF = createConfigDef();

    private static ConfigDef createConfigDef() {
        ConfigDef configDef = new ConfigDef();
        addParams(configDef);
        return configDef;
    }

    private static void addParams(final ConfigDef configDef) {
        configDef.define(TOPIC_PARAM_CONFIG, Type.STRING, Importance.HIGH, TOPIC_PARAM_DOC)
            .define(SSE_URI_PARAM_CONFIG, Type.STRING, Importance.HIGH, SSE_URI_PARAM_DOC);
    }

}

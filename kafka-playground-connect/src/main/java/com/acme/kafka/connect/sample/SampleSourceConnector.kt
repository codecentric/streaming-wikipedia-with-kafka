package com.acme.kafka.connect.sample;

import org.apache.kafka.common.config.Config;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigValue;
import org.apache.kafka.connect.connector.Task;
import org.apache.kafka.connect.errors.ConnectException;
import org.apache.kafka.connect.source.SourceConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.acme.kafka.connect.sample.SampleSourceConnectorConfig.CONFIG_DEF;
import static com.acme.kafka.connect.sample.SampleSourceConnectorConfig.SSE_URI_PARAM_CONFIG;
import static com.acme.kafka.connect.sample.SampleSourceConnectorConfig.TOPIC_PARAM_CONFIG;

public class SampleSourceConnector extends SourceConnector {

    private final Logger log = LoggerFactory.getLogger(SampleSourceConnector.class);

    private Map<String, String> originalProps;

    @Override
    public String version() {
        return PropertiesUtil.getConnectorVersion();
    }

    @Override
    public ConfigDef config() {
        return CONFIG_DEF;
    }

    @Override
    public Class<? extends Task> taskClass() {
        return SampleSourceTask.class;
    }

    @Override
    public Config validate(Map<String, String> connectorConfigs) {
        Config config = super.validate(connectorConfigs);
        List<ConfigValue> configValues = config.configValues();
        boolean missingTopicDefinition = true;
        boolean missingSseUriDefinition = true;
        for (ConfigValue configValue : configValues) {
            if (configValue.name().equals(TOPIC_PARAM_CONFIG) && configValue.value() != null) {
                missingTopicDefinition = false;
            }
            if (configValue.name().equals(SSE_URI_PARAM_CONFIG) && configValue.value() != null) {
                missingSseUriDefinition = false;
            }
            if (!missingTopicDefinition && !missingSseUriDefinition) {
                break;
            }
        }
        if (missingTopicDefinition || missingSseUriDefinition) {
            throw new ConnectException(String.format(
                "Properties '%s' and '%s' must be set in the configuration.", TOPIC_PARAM_CONFIG, SSE_URI_PARAM_CONFIG));
        }
        return config;
    }

    @Override
    public void start(Map<String, String> originalProps) {
        this.originalProps = originalProps;
        new SampleSourceConnectorConfig(originalProps);
        log.info("Starting connector");
    }

    @Override
    public List<Map<String, String>> taskConfigs(int maxTasks) {
        return Collections.singletonList(new HashMap<>(originalProps));
    }

    @Override
    public void stop() {
        log.info("Stopping connector");
    }

}

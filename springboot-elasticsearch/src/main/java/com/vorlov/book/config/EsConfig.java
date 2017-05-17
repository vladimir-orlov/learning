package com.vorlov.book.config;

import com.vorlov.book.mapper.CustomEntityMapper;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import java.net.InetAddress;

@Configuration
@EnableElasticsearchRepositories(basePackages = "com.vorlov.book.repository")
@ComponentScan(basePackages = {"com.vorlov.book.service"})
@PropertySource(name = "properties", value = "application.properties")
public class EsConfig {

    @Value("${elasticsearch.host}")
    private String esHost;
    @Value("${elasticsearch.port}")
    private Integer esPort;
    @Value("${elasticsearch.clustername}")
    private String esClusterName;

    @Bean
    public Client client() throws Exception {

        Settings esSettings = Settings.settingsBuilder()
                .put("cluster.name", esClusterName)
                .build();

        return TransportClient.builder()
                .settings(esSettings)
                .build()
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(esHost), esPort));
    }

    @Bean
    public ElasticsearchOperations elasticsearchTemplate() throws Exception {
        System.out.println("================================+++++++++++++++++++++1++++++++++++++++=================================");
        return new ElasticsearchTemplate(client(), new CustomEntityMapper());
    }
}
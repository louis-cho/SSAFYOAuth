package com.ssafy.authorization.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;

@Configuration
public class ElasticSearchConfig extends ElasticsearchConfiguration {

	@Override
	public ClientConfiguration clientConfiguration() {

		return ClientConfiguration.builder()
			.connectedTo("k10a306.p.ssafy.io:3000")
			.build();
	}
}
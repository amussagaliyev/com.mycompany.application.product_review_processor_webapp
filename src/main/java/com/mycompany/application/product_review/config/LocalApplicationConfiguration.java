package com.mycompany.application.product_review.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

import com.mycompany.api.jedis.RedisQueuePublisher;
import com.mycompany.api.jedis.SimpleRedisConfig;
import com.mycompany.service.product_review.ProductReviewQueueListener;

@Configuration
@PropertySource("classpath:application.properties")
public class LocalApplicationConfiguration
{
	@Bean(name="productReviewQueuePublisher")
	@Scope("singleton")
	public RedisQueuePublisher getProductReviewQueuePublisher()
	{
		return new RedisQueuePublisher(new SimpleRedisConfig());
	}

	@Bean
	public MessageListenerAdapter messageListener() { 
		return new MessageListenerAdapter(new ProductReviewQueueListener());
	}

	@Bean
	public RedisMessageListenerContainer redisContainer() 
	{
		RedisMessageListenerContainer container = new RedisMessageListenerContainer(); 
		container.setConnectionFactory(new JedisConnectionFactory()); 
		container.addMessageListener(messageListener(), new ChannelTopic("pubsub")); 
		return container; 
	}
}

package com.mycompany.application.product_review.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericToStringSerializer;

import com.mycompany.api.jedis.RedisMessagePublisher;
import com.mycompany.api.jedis.RedisMessageSubscriber;
import com.mycompany.service.product_review.ProductReviewService;

@Configuration
@ComponentScan("com.mycompany")
@EnableRedisRepositories(basePackages = "com.mycompany")
@PropertySource("classpath:application.properties")
public class ApplicationConfiguration
{

	@Bean
	public JedisConnectionFactory jedisConnectionFactory()
	{
		return new JedisConnectionFactory();
	}

	@Bean
	public RedisTemplate<String, Object> redisTemplate()
	{
		final RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
		template.setConnectionFactory(jedisConnectionFactory());
		template.setValueSerializer(new GenericToStringSerializer<Object>(Object.class));
		return template;
	}

	@Bean
	public MessageListenerAdapter messageListener()
	{
		return new MessageListenerAdapter(new RedisMessageSubscriber());
	}

	@Bean
	public RedisMessageListenerContainer redisContainer()
	{
		final RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		container.setConnectionFactory(jedisConnectionFactory());
		container.addMessageListener(messageListener(), topic());
		return container;
	}

	@Bean(name= {"productReviewQueuePublisher"})
	public RedisMessagePublisher redisPublisher()
	{
		return new RedisMessagePublisher(redisTemplate(), topic());
	}

	@Bean
	public ChannelTopic topic()
	{
		return new ChannelTopic(ProductReviewService.QUEUE_SUBMITTED);
	}
}
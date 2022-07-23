package br.com.sburble.risk.configuration;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class RabbitConfiguration {

    @Bean
    @Primary
    public CachingConnectionFactory connectionFactory(
            @Value("${spring.rabbitmq.host}") String host,
            @Value("${spring.rabbitmq.port}") int port,
            @Value("${spring.rabbitmq.username}") String username,
            @Value("${spring.rabbitmq.password}") String password,
            @Value("${spring.rabbitmq.virtual-host}") String virtualHost,
            @Value("${spring.rabbitmq.ssl.enabled:false}") boolean isSslEnabled) {

        var connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost(host);
        connectionFactory.setPort(port);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setVirtualHost(virtualHost);

        if (isSslEnabled) {
            try {
                connectionFactory.getRabbitConnectionFactory().useSslProtocol();
            } catch (KeyManagementException | NoSuchAlgorithmException ex) {
                throw new IllegalStateException(
                        "Não foi possivel utilizar SSL no rabbitMQ. errorMessage=" + ex.getMessage());
            }
        }
        return connectionFactory;
    }

    @Bean
    @Primary
    public RabbitTemplate rabbitTemplate(
            ConnectionFactory connectionFactory) {

        var v2RabbitTemplate = new RabbitTemplate(connectionFactory);

        v2RabbitTemplate.setMessageConverter(producerJackson2MessageConverter());

        return v2RabbitTemplate;
    }

    @Bean
    public Queue riskQueue(@Value("${ms.queue.risk-status}") String queueName) {
        return new Queue(queueName, true);
    }

    @Bean
    public Queue riskQueueDlq(@Value("${ms.queue.risk-status-dlq}") String queueName) {
        return new Queue(queueName, true);
    }
    
    @Bean
    public TopicExchange riskExchange(@Value("${ms.exchange.risk-exchange}") String exchangeName) {
        return new TopicExchange(exchangeName);
    }

    @Bean
    @Primary
    public SimpleRabbitListenerContainerFactory containerFactory(
            ConnectionFactory connectionFactory,
            @Value("${spring.rabbitmq.listener.simple.prefetch}") Integer prefetch,
            @Value("${spring.rabbitmq.listener.simple.concurrency}") Integer concurrency,
            @Value("${spring.rabbitmq.listener.simple.max-concurrency}") Integer maxConcurrency) {

        var factory = new SimpleRabbitListenerContainerFactory();
        factory.setAutoStartup(true);
        factory.setConnectionFactory(connectionFactory);
        factory.setMissingQueuesFatal(false);
        factory.setStartConsumerMinInterval(3000L);
        factory.setRecoveryInterval(15000L);
        factory.setChannelTransacted(true);
        factory.setMessageConverter(new ExperimentalJacksonJsonMapper());
        factory.setMaxConcurrentConsumers(maxConcurrency);
        factory.setConcurrentConsumers(concurrency);
        factory.setPrefetchCount(prefetch);

        return factory;
    }

    @Bean
    public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    class ExperimentalJacksonJsonMapper extends Jackson2JsonMessageConverter {

        @Override
        public Object fromMessage(Message message, Object conversionHint) {
            message.getMessageProperties().setContentType("application/json");
            return super.fromMessage(message, conversionHint);
        }
    }

    @Bean
    @Primary
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        var rabbitAdmin = new RabbitAdmin(connectionFactory);
        rabbitAdmin.setAutoStartup(true);
        return rabbitAdmin;
    }
}

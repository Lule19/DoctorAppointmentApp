package poslovne.aplikacije;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import com.fasterxml.jackson.databind.ObjectMapper;

// Removed product messaging imports

@Configuration
@EnableRabbit
public class RabbitMQConfigurator {

      
	public static final String APPOINTMENTS_TOPIC_EXCHANGE_NAME = "appointments-events-exchange";
	public static final String APPOINTMENTS_AVAILABILITY_QUEUE = "availability-checker-queue";
	public static final String APPOINTMENTS_NOTIFICATION_QUEUE = "notification-queue";

     
	  @Bean
	  public Queue availabilityQueue() {
		 
		  return new Queue(APPOINTMENTS_AVAILABILITY_QUEUE, false, false, false);
	  }

	  @Bean
	  public Queue notificationQueue() {
		  
		  return new Queue(APPOINTMENTS_NOTIFICATION_QUEUE, false, false, false);
	  }

	  @Bean
	  public TopicExchange appointmentsExchange() {
		  return new TopicExchange(APPOINTMENTS_TOPIC_EXCHANGE_NAME);
	  }

	  @Bean
	  public Binding availabilityBinding(Queue availabilityQueue, TopicExchange appointmentsExchange) {
		  return BindingBuilder.bind(availabilityQueue).to(appointmentsExchange).with("appointments.requested");
	  }

	  @Bean
	  public Binding notificationConfirmedBinding(Queue notificationQueue, TopicExchange appointmentsExchange) {
		  return BindingBuilder.bind(notificationQueue).to(appointmentsExchange).with("appointments.confirmed");
	  }

	  @Bean
	  public Binding notificationRejectedBinding(Queue notificationQueue, TopicExchange appointmentsExchange) {
		  return BindingBuilder.bind(notificationQueue).to(appointmentsExchange).with("appointments.rejected");
	  }

	 
	  @Bean
	  public MessageConverter jacksonMessageConverter(ObjectMapper objectMapper) {
		  
		  return new Jackson2JsonMessageConverter(objectMapper);
	  }

	  @Bean
	  public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
		  RabbitTemplate template = new RabbitTemplate(connectionFactory);
		  template.setMessageConverter(messageConverter);
		  return template;
	  }

	  @Bean
	  public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
			  ConnectionFactory connectionFactory,
			  MessageConverter messageConverter) {
		  SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
		  factory.setConnectionFactory(connectionFactory);
		  factory.setMessageConverter(messageConverter);
		  return factory;
	  }
}

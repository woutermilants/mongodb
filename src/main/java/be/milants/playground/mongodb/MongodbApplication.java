package be.milants.playground.mongodb;

import be.milants.playground.mongodb.config.AmqpConfig;
import be.milants.playground.mongodb.domain.Customer;
import be.milants.playground.mongodb.domain.CustomerMonogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

@SpringBootApplication
@RequiredArgsConstructor
public class MongodbApplication implements CommandLineRunner {

	private final CustomerMonogRepository customerMonogRepository;
	private final MongoTemplate mongoTemplate;
	private final RabbitTemplate rabbitTemplate;

	public static void main(String[] args) {
		SpringApplication.run(MongodbApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("customers: ");
		customerMonogRepository.findAll().forEach(System.out::println);

		customerMonogRepository.deleteAll();
		customerMonogRepository.save(new Customer("Wouter", "Milants"));
		customerMonogRepository.save(new Customer("Jan", "Janssens"));
		customerMonogRepository.save(new Customer("Pieter", "Janssens"));

		System.out.println("customers: ");
		customerMonogRepository.findAll().forEach(System.out::println);


		System.out.println("all Janssens");
		customerMonogRepository.findByLastName("Janssens").forEach(System.out::println);

		System.out.println("query via template");
		final Query query = new Query(Criteria.where("lastName").is("Janssens"));
		System.out.println("Query " + query.toString());
		System.out.println(mongoTemplate.find(query, Customer.class));

		//rabbitTemplate.setExchange("TestExchange");
		System.out.println(rabbitTemplate.getExchange());
		rabbitTemplate.convertAndSend(AmqpConfig.topicExchangeName,"foo.bar.baz", "Hello from RabbitMQ!");

	}
}

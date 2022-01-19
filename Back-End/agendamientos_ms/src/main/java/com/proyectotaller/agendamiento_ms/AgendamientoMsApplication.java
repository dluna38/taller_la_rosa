package com.proyectotaller.agendamiento_ms;




import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;



@SpringBootApplication
public class AgendamientoMsApplication {

	public static void main(String[] args) {
		SpringApplication.run(AgendamientoMsApplication.class, args);
	}
	
//	@Bean
//	CommandLineRunner init(MongoTemplate mongoTemplate){
//		return args -> {
//			Query query = new Query();
//			query.addCriteria(Criteria.where("fechaAgendamiento").gte(LocalDate.of(2021, 11, 25)));
//			
//			System.out.println(mongoTemplate.find(query, Agendamiento.class));
//			};	
//	}
}

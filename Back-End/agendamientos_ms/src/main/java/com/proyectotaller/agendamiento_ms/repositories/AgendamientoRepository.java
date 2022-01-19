package com.proyectotaller.agendamiento_ms.repositories;


import java.time.LocalDateTime;
import java.util.List;


import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.proyectotaller.agendamiento_ms.models.Agendamiento;

public interface AgendamientoRepository extends MongoRepository<Agendamiento, String>{
	
	@Query(value="{fechaAgendamiento:{$lte:?0}}",sort="{fechaAgendamiento: -1}")
	List<Agendamiento> getAgendamientoByLteFechaAgendamiento(LocalDateTime fechaAgendamiento,Pageable pageable);
	
	@Query(value="{fechaAgendamiento:{$gte:?0}}")
	List<Agendamiento> getAgendamientoByGteFechaAgendamiento(LocalDateTime fechaAgendamiento,Pageable pageable);
	
	List<Agendamiento> findByFechaAgendamiento(LocalDateTime fechaAgendamiento);
	
	@Query(value="{\"fechaAgendamiento\" : {\"$gte\" : ?0, \"$lte\" : ?1}}")
	List<Agendamiento> findAgendamientosByFechaAgendamiento(LocalDateTime fechaAgendamiento,LocalDateTime fechaFinal);
	@Query(value="{\"fechaAgendamiento\" : {\"$gte\" : ?0, \"$lte\" : ?1}}",fields = "{ 'fechaAgendamiento' : 1, 'fechaFinalAgendamiento' : 1}")
	List<Agendamiento> findAgendamientosByFechaAgendamientoOnlyFechas(LocalDateTime fechaAgendamiento,LocalDateTime fechaFinalAgendamiento);
	
	@Query(value="{\"fechaAgendamiento\" : {\"$gte\" : ?0, \"$lte\" : ?1}}")
	List<Agendamiento> findAgendamientosBetweenTwoDates(LocalDateTime fechaInicio,LocalDateTime fechaFin);
	
	List<Agendamiento> findByVehiculo(String vehiculo);
	
	Long deleteAgendamientoByid(String id);
	Long deleteAgendamientoByVehiculo(String vehiculo);
}

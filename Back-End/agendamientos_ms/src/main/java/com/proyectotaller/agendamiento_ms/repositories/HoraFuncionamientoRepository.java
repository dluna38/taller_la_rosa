package com.proyectotaller.agendamiento_ms.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.proyectotaller.agendamiento_ms.models.HoraFuncionamiento;

public interface HoraFuncionamientoRepository extends MongoRepository<HoraFuncionamiento, String>{
	
	Long deleteHoraFuncionamientoByid(String id);
	
}

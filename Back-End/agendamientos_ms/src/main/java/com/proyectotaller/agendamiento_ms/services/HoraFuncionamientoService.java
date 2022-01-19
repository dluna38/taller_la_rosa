package com.proyectotaller.agendamiento_ms.services;

import java.util.Collections;

import org.springframework.stereotype.Service;

import com.proyectotaller.agendamiento_ms.exceptions.AgendamientoNotAcceptableException;
import com.proyectotaller.agendamiento_ms.models.HoraFuncionamiento;
import com.proyectotaller.agendamiento_ms.repositories.HoraFuncionamientoRepository;

@Service
public class HoraFuncionamientoService {
	
	private  final HoraFuncionamientoRepository hFRepository;

	public HoraFuncionamientoService(HoraFuncionamientoRepository hFRepository) {
		this.hFRepository = hFRepository;

	}
	
	public HoraFuncionamiento getHora() {		
		try {
			return hFRepository.findAll().get(0);
		} catch (Exception e) {
			
		}
		return null;		
	}
	public HoraFuncionamiento newHora(HoraFuncionamiento hora) {
		if(thereIsARegister() || !isValidHora(hora)) {
			throw new AgendamientoNotAcceptableException("No es valida o ya existe una configuración de hora");
		}
		try {
			return hFRepository.insert(hora);
		} catch (Exception e) {
			throw new AgendamientoNotAcceptableException("Error al registrar la configuración");
		}
	}
	public void deleteHora() {
		if(!thereIsARegister()) {
			throw new AgendamientoNotAcceptableException("No hay configuración de hora para eliminar");
		}
		hFRepository.deleteHoraFuncionamientoByid(hFRepository.findAll().get(0).getId());
	}
	
	public HoraFuncionamiento updateHora(HoraFuncionamiento hora) {
		if(!thereIsARegister() || !isValidHora(hora)) {
			throw new AgendamientoNotAcceptableException("No hay configuración de hora para actualizar o no es valida");
		}
		try {
			hora.setId(getHora().getId());
			return hFRepository.save(hora);
		} catch (Exception e) {
			throw new AgendamientoNotAcceptableException("Error al registrar la configuración");
		}
	}
	
	private boolean thereIsARegister() {
		return hFRepository.count() >= 1;
	}
	private boolean isValidHora(HoraFuncionamiento hora) {
		if(hora.getEstado() == null) {
			hora.setEstado(true);
		}
		return hora.getHoraApertura() != null && hora.getHoraCierre() != null;
	}
}

package com.proyectotaller.agendamiento_ms.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.proyectotaller.agendamiento_ms.models.HoraFuncionamiento;
import com.proyectotaller.agendamiento_ms.services.HoraFuncionamientoService;

@RestController
@RequestMapping("api/configuracion/")
public class HoraFuncionamientoController {

	private final HoraFuncionamientoService hFService;

	@Autowired
	public HoraFuncionamientoController(HoraFuncionamientoService hFService) {
		this.hFService = hFService;
	}

	@GetMapping
	@ResponseStatus(code=HttpStatus.OK)
	public HoraFuncionamiento getHF() {
		return hFService.getHora();
	}

	@PostMapping
	@ResponseStatus(code=HttpStatus.OK)
	public HoraFuncionamiento postHF(@RequestBody HoraFuncionamiento hora) {
		return hFService.newHora(hora);
	}

	@PutMapping
	@ResponseStatus(code=HttpStatus.OK)
	public HoraFuncionamiento putHF(@RequestBody HoraFuncionamiento hora) {
		return hFService.updateHora(hora);
	}

	@DeleteMapping
	@ResponseStatus(code=HttpStatus.OK)
	public void deleteHF() {
		hFService.deleteHora();
	}

}

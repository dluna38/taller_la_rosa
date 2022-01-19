package com.proyectotaller.agendamiento_ms.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.proyectotaller.agendamiento_ms.emailService.EmailBody;
import com.proyectotaller.agendamiento_ms.models.Agendamiento;
import com.proyectotaller.agendamiento_ms.models.TwoDates;
import com.proyectotaller.agendamiento_ms.services.AgendamientoService;


@RestController
@RequestMapping("api/agendamientos/")
public class AgendamientoController {
		
	private final AgendamientoService agendamientoService;
	
	@Autowired
	public AgendamientoController(AgendamientoService agendamientoService) {
		this.agendamientoService = agendamientoService;
	}
	
	
	@GetMapping
	@ResponseStatus(code=HttpStatus.OK)
	public List<Agendamiento> getAgendamientos(){
		return agendamientoService.getAgendamientos();
	}
	
	@GetMapping("{id}")
	@ResponseStatus(code=HttpStatus.OK)
	public Agendamiento getAgendamiento(@PathVariable String id){
		return agendamientoService.getAgendamiento(id);
	}
	@GetMapping("fecha/{date}")
	@ResponseStatus(code=HttpStatus.OK)
	public List<Agendamiento> getAgendamientosOfTheDay(@PathVariable String date){
		return agendamientoService.getAgendamientosOfTheDay(date);	
	}
	@GetMapping("fecha/ocupados/{date}")
	@ResponseStatus(code=HttpStatus.OK)
	public List<Agendamiento> getAgendamientosTakenOfTheDay(@PathVariable String date){
		return agendamientoService.getAgendamientosTakenOfTheDay(date);
	}
		
	@GetMapping("vehiculo/{placa}")
	@ResponseStatus(code=HttpStatus.OK)
	public List<Agendamiento> getAgendamientosByVehiculo(@PathVariable String placa){
		return agendamientoService.getAgendamientosByVehiculo(placa);
	}
		
	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	public Agendamiento newAgendamiento(@RequestBody Agendamiento agendamiento) {
		//use a DTOAgendamiento for receive then create the new Agendamiento() and map 
		//return agendamiento;
		return agendamientoService.newAgendamiento(agendamiento);
	}
	
	@PostMapping("fecha/between")
	@ResponseStatus(code=HttpStatus.OK)
	public List<Agendamiento> getAgendamientosBetweenTwoDates(@RequestBody TwoDates fechas){
		return agendamientoService.getAgendamientosBetweenTwoDates(fechas);
	}
	@PostMapping("email")
	@ResponseStatus(code=HttpStatus.OK)
	public String sentEmail(@RequestBody EmailBody body){
		return agendamientoService.sendEmail(body) ? "Enviado": "Ocurrio algun error";
	}
	
	@RequestMapping(path = "comprobar",method = RequestMethod.POST,produces="application/json")
	@ResponseStatus(code=HttpStatus.OK)
	public String checkDates(@RequestBody Agendamiento agendamiento) {
		boolean result = agendamientoService.checkDates(agendamiento);
			return "{\"result\":"+result+"}";
	}
	
	@PutMapping("update")
	@ResponseStatus(code=HttpStatus.OK)
	public Agendamiento updateAgendamiento(@RequestBody Agendamiento agendamiento) {
		return agendamientoService.updateAgendamiento(agendamiento);
	}
	
	
	@DeleteMapping("delete/{id}")
	@ResponseStatus(code=HttpStatus.OK)
	public void deleteAgendamiento(@PathVariable String id) {
		agendamientoService.deleteAgendamiento(id);
	}
	
	@DeleteMapping("delete/v/{placa}")
	@ResponseStatus(code=HttpStatus.OK)
	public Long deleteAgendamientosByVehiculo(@PathVariable String placa) {
		return agendamientoService.deleteAgendamientosByVehiculo(placa);
	}

}



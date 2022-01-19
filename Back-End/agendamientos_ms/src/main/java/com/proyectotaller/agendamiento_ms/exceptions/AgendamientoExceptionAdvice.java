package com.proyectotaller.agendamiento_ms.exceptions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;


@ControllerAdvice
@ResponseBody
public class AgendamientoExceptionAdvice {
	
	

	@ExceptionHandler(AgendamientoNotFoundException.class)	
	ResponseEntity<Object> agendamientoNotFound(AgendamientoNotFoundException ex){
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(AgendamientoNotAcceptableException.class)
	ResponseEntity<Object> agendamientoNotAcceptable(AgendamientoNotAcceptableException ex){
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_ACCEPTABLE);
	}
	
	@ExceptionHandler(HttpMessageNotReadableException.class)
	ResponseEntity<Object> agendamientoBadRequest(HttpMessageNotReadableException ex, WebRequest request) {
	       String error = "Error leyendo el JSON enviado";
	       Map<String, Object> map = getMap("msg", error);
	       map.putAll(getMap("debug",ex.getMostSpecificCause().getMessage()));
	       return new ResponseEntity<>(map,HttpStatus.BAD_REQUEST);
	   }

	private Map<String, Object> getMap(String clave,Object valor){
		Map<String, Object> map = new HashMap<>();
		map.put(clave, valor);
		return map;
	}
}

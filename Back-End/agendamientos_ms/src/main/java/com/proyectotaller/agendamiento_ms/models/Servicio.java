package com.proyectotaller.agendamiento_ms.models;

import org.springframework.data.annotation.Id;

public class Servicio {
	
	@Id
	private int id;
	private String nombre;
	private String descripcion;
	//en minutos -> conversion necesaria
	private int tiempo;
	
	
	
	public Servicio(int id, String nombre, String descripcion) {
		this.id = id;
		this.nombre = nombre;
		this.descripcion = descripcion;
	}
	
	
	public Servicio(String nombre, String descripcion) {
		this.nombre = nombre;
		this.descripcion = descripcion;
	}


	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public int getId() {
		return id;
	}
	
	
}

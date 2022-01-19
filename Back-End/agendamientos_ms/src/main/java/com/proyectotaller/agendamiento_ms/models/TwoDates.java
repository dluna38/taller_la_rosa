package com.proyectotaller.agendamiento_ms.models;

import java.time.LocalDateTime;

public class TwoDates {
	private LocalDateTime fechaInicio;
	private LocalDateTime fechaFin;
	
	public TwoDates() {
	}
	
	public TwoDates(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
	}
	public LocalDateTime getFechaInicio() {
		return fechaInicio;
	}
	public void setFechaInicio(LocalDateTime fechaInicio) {
		this.fechaInicio = fechaInicio;
	}
	public LocalDateTime getFechaFin() {
		return fechaFin;
	}
	public void setFechaFin(LocalDateTime fechaFin) {
		this.fechaFin = fechaFin;
	}
	
}

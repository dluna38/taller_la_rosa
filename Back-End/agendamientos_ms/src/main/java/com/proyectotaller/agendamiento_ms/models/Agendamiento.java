package com.proyectotaller.agendamiento_ms.models;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Agendamiento {
	@Id
	private String id;
	private LocalDateTime fechaAgendamiento;
	private LocalDateTime fechaFinalAgendamiento;
	private String motivo;
	private Boolean cumplimiento;
	private String vehiculo;
	
	public Agendamiento() {		
	}
	public Agendamiento(LocalDateTime fechaAgendamiento, LocalDateTime fechaFinalAgendamiento, String motivo, Boolean cumplimiento,
			String vehiculo) {
		this.fechaAgendamiento = fechaAgendamiento;
		this.fechaFinalAgendamiento = fechaFinalAgendamiento;
		this.motivo = motivo;
		this.cumplimiento = cumplimiento;
		this.vehiculo = vehiculo;
		
	}
	
	public Agendamiento(String id, LocalDateTime fechaAgendamiento, LocalDateTime fechaFinalAgendamiento, String motivo,
			Boolean cumplimiento,String vehiculo) {
		this.id = id;
		this.fechaAgendamiento = fechaAgendamiento;
		this.fechaFinalAgendamiento = fechaFinalAgendamiento;
		this.motivo = motivo;
		this.cumplimiento = cumplimiento;
		this.vehiculo = vehiculo;
	}

	public Agendamiento(String id, LocalDateTime fechaAgendamiento, LocalDateTime fechaFinalAgendamiento, String motivo,
			String vehiculo) {
		this.id = id;
		this.fechaAgendamiento = fechaAgendamiento;
		this.fechaFinalAgendamiento = fechaFinalAgendamiento;
		this.motivo = motivo;
		this.cumplimiento = false;
		this.vehiculo = vehiculo;
	}
	
	

	
	public Agendamiento(LocalDateTime fechaAgendamiento, LocalDateTime fechaFinalAgendamiento, String motivo, String vehiculo) {
		this.fechaAgendamiento = fechaAgendamiento;
		this.fechaFinalAgendamiento = fechaFinalAgendamiento;
		this.motivo = motivo;
		this.cumplimiento = false;
		this.vehiculo = vehiculo;
		
	}

	public LocalDateTime getFechaAgendamiento() {
		return fechaAgendamiento;
	}

	public void setFechaAgendamiento(LocalDateTime fechaAgendamiento) {
		this.fechaAgendamiento = fechaAgendamiento;
	}

	public LocalDateTime getFechaFinalAgendamiento() {
		return fechaFinalAgendamiento;
	}

	public void setFechaFinalAgendamiento(LocalDateTime fechaFinalAgendamiento) {
		this.fechaFinalAgendamiento = fechaFinalAgendamiento;
	}

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public Boolean getCumplimiento() {
		return cumplimiento;
	}

	public void setCumplimiento(Boolean cumplimiento) {
		this.cumplimiento = cumplimiento;
	}

	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	public String getVehiculo() {
		return vehiculo;
	}

	public void setVehiculo(String vehiculo) {
		this.vehiculo = vehiculo;
	}

	@Override
	public String toString() {
		return "Agendamiento [id=" + id + ", fechaAgen=" + fechaAgendamiento
				+ ", fechaFAgen=" + fechaFinalAgendamiento + ", motivo=" + motivo + ", cumpli="
				+ cumplimiento + ", V=" + vehiculo + "]";
	}
	
	
	
	
}

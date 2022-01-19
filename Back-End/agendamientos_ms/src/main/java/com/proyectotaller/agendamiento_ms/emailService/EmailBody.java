package com.proyectotaller.agendamiento_ms.emailService;

import java.time.LocalDateTime;

public class EmailBody {
	private String enviarA;
	private String nombreUsuario;
	private LocalDateTime fechaAgendamiento;
	private LocalDateTime fechaFinalAgendamiento;
	private String vehiculo;
	
	public EmailBody() {
	}

	

	public EmailBody(String enviarA, String nombreUsuario, LocalDateTime fechaAgendamiento,
			LocalDateTime fechaFinalAgendamiento, String vehiculo) {
		this.enviarA = enviarA;
		this.nombreUsuario = nombreUsuario;
		this.fechaAgendamiento = fechaAgendamiento;
		this.fechaFinalAgendamiento = fechaFinalAgendamiento;
		this.vehiculo = vehiculo;
	}



	public String getEnviarA() {
		return enviarA;
	}



	public void setEnviarA(String enviarA) {
		this.enviarA = enviarA;
	}



	public String getNombreUsuario() {
		return nombreUsuario;
	}

	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
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

	public String getVehiculo() {
		return vehiculo;
	}

	public void setVehiculo(String vehiculo) {
		this.vehiculo = vehiculo;
	}
	
	
	
}

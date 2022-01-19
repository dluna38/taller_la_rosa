package com.proyectotaller.agendamiento_ms.models;

import java.time.LocalTime;

import org.springframework.data.annotation.Id;

public class HoraFuncionamiento {
	@Id
	private String id;
	private LocalTime horaApertura;
	private LocalTime horaCierre;
	private Boolean estado;
	
	public HoraFuncionamiento() {
	}
	public HoraFuncionamiento(String id, LocalTime horaApertura, LocalTime horaCierre) {
		this.id = id;
		this.horaApertura = horaApertura;
		this.horaCierre = horaCierre;
	}
	
	public HoraFuncionamiento(LocalTime horaApertura, LocalTime horaCierre) {
		this.horaApertura = horaApertura;
		this.horaCierre = horaCierre;
		this.estado = true;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public LocalTime getHoraApertura() {
		return horaApertura;
	}
	public void setHoraApertura(LocalTime horaApertura) {
		this.horaApertura = horaApertura;
	}
	public LocalTime getHoraCierre() {
		return horaCierre;
	}
	public void setHoraCierre(LocalTime horaCierre) {
		this.horaCierre = horaCierre;
	}

	public Boolean getEstado() {
		return estado;
	}

	public void setEstado(Boolean estado) {
		this.estado = estado;
	}
	
	@Override
	public String toString() {
		
		return "id:"+this.id+",fecha apertura: "+this.horaApertura+",fecha cierre: "+this.horaCierre+",est:"+this.estado;
	}
}

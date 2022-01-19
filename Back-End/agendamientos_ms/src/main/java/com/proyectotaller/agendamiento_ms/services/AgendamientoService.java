package com.proyectotaller.agendamiento_ms.services;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.propertyeditors.ZoneIdEditor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;

import com.proyectotaller.agendamiento_ms.emailService.EmailBody;
import com.proyectotaller.agendamiento_ms.emailService.EmailServiceTaller;
import com.proyectotaller.agendamiento_ms.exceptions.AgendamientoNotAcceptableException;
import com.proyectotaller.agendamiento_ms.exceptions.AgendamientoNotFoundException;
import com.proyectotaller.agendamiento_ms.models.Agendamiento;
import com.proyectotaller.agendamiento_ms.models.HoraFuncionamiento;
import com.proyectotaller.agendamiento_ms.models.TwoDates;
import com.proyectotaller.agendamiento_ms.repositories.AgendamientoRepository;

@Service
public class AgendamientoService {

	private final AgendamientoRepository agendamientoRepository;
	private final HoraFuncionamientoService hFService;
	private final EmailServiceTaller sendEmail;

	private final ZoneId timeZoneBogota = ZoneId.of("America/Bogota");

	public AgendamientoService(AgendamientoRepository agendamientoRepository, HoraFuncionamientoService hFService,
			EmailServiceTaller sendEmail) {
		this.agendamientoRepository = agendamientoRepository;
		this.hFService = hFService;
		this.sendEmail = sendEmail;

	}

	// Email service
	public boolean sendEmail(EmailBody emailBody) {
		if(emailBody.getFechaAgendamiento() != null && (emailBody.getNombreUsuario() != null && !emailBody.getNombreUsuario().isBlank())
				&& (emailBody.getEnviarA() != null && !emailBody.getEnviarA().isBlank()) 
				&& (emailBody.getVehiculo() != null && !emailBody.getVehiculo().isBlank())
				&& emailBody.getFechaFinalAgendamiento() != null){
					return sendEmail.sendSimpleMessage(emailBody);
				}
		return false;
	}

	// ------------------------
	public List<Agendamiento> getAgendamientosByVehiculo(String placa) {
		return agendamientoRepository.findByVehiculo(placa);
	}

	public List<Agendamiento> getAgendamientosOfTheDay(String date) {
		LocalDateTime fecha = parseDate(date);
		if (fecha != null) {
			fecha = fecha.withHour(0).withMinute(0);
			LocalDateTime fecha2 = fecha.withHour(23).withMinute(59);
			return agendamientoRepository.findAgendamientosByFechaAgendamiento(fecha, fecha2);
		}
		throw new AgendamientoNotAcceptableException("La fecha no es procesable");
	}

	public List<Agendamiento> getAgendamientosTakenOfTheDay(String date) {
		LocalDateTime fecha = parseDate(date);
		if (fecha != null) {
			fecha = fecha.withHour(0).withMinute(0);
			LocalDateTime fecha2 = fecha.withHour(23).withMinute(59);
			return agendamientoRepository.findAgendamientosByFechaAgendamientoOnlyFechas(fecha, fecha2);
		}
		throw new AgendamientoNotAcceptableException("La fecha no es procesable");
	}

	public List<Agendamiento> getAgendamientosBetweenTwoDates(TwoDates fechas) {
		if (fechas.getFechaInicio() != null && fechas.getFechaFin() != null) {
			fechas.setFechaInicio(fechas.getFechaInicio().withHour(0).withMinute(0).withSecond(0));
			fechas.setFechaFin(fechas.getFechaFin().withHour(23).withMinute(59).withSecond(59));

			return agendamientoRepository.findAgendamientosBetweenTwoDates(fechas.getFechaInicio(),
					fechas.getFechaFin());
		}
		throw new AgendamientoNotAcceptableException("Las fechas no son procesables");
	}

	// services
	public List<Agendamiento> getAgendamientos() {
		return agendamientoRepository.findAll();
	}

	public Agendamiento getAgendamiento(String id) {
		return agendamientoRepository.findById(id)
				.orElseThrow(() -> new AgendamientoNotFoundException("No se encontro el agendamiento"));
	}

	public boolean checkDates(Agendamiento fecha) {
		if (fecha.getFechaAgendamiento() != null && fecha.getFechaFinalAgendamiento() != null) {
			return isValidReserva(fecha);
		}
		throw new AgendamientoNotAcceptableException("Las fechas no son validas");
	}

	public Agendamiento newAgendamiento(Agendamiento agendamiento) {

		adjustAgendamiento(agendamiento, false);
		if (!isValidAgendamiento(agendamiento)) {
			throw new AgendamientoNotAcceptableException("Campos no validos o faltantes");
		}
		if (!isValidReserva(agendamiento)) {
			throw new AgendamientoNotAcceptableException(
					"Fechas ya reservadas o no esta dentro de la hora de operación");
		}

		return agendamientoRepository.insert(agendamiento);

	}

	// check if updated
	public Agendamiento updateAgendamiento(Agendamiento agendamiento) {
		if (agendamientoRepository.existsById(agendamiento.getId())) {
			Agendamiento agendamientoRecuperado = agendamientoRepository.findById(agendamiento.getId()).get();

			try {
				adjustAgendamiento(agendamiento, true);

				if (!isValidAgendamiento(agendamiento)) {
					throw new AgendamientoNotAcceptableException("Campos no validos o faltantes");
				}
				// si no son las mismas que fechas que ya estaban, analice conflictos con otros
				// agendamientos y si no es valida la reserva arroje error
				if (!(agendamiento.getFechaAgendamiento().equals(agendamientoRecuperado.getFechaAgendamiento())
						&& agendamiento.getFechaFinalAgendamiento()
								.equals(agendamientoRecuperado.getFechaFinalAgendamiento()))
						&& !isValidReserva(agendamiento)) {

					throw new AgendamientoNotAcceptableException(
							"Fechas ya reservadas o no esta dentro de la hora de operación");
				}

				return agendamientoRepository.save(agendamiento);
			} catch (Exception e) {
				throw new AgendamientoNotFoundException("Error al actualizar agendamiento---\n" + e.getMessage());
			}

		}
		throw new AgendamientoNotFoundException("No existe el agendamiento a actualizar");
	}

	public void deleteAgendamiento(String id) {
		if (agendamientoRepository.deleteAgendamientoByid(id) <= 0)
			throw new AgendamientoNotFoundException("No existe el agendamiento a eliminar");
	}

	public Long deleteAgendamientosByVehiculo(String vehiculo) {
		// -1 si no es valido
		return isPlacaValid(vehiculo) ? agendamientoRepository.deleteAgendamientoByVehiculo(vehiculo) : -1L;
	}

	// util functions ------
	public boolean isValidAgendamiento(Agendamiento agendamiento) {

		if (agendamiento.getFechaAgendamiento() == null)
			return false;
		if (agendamiento.getFechaFinalAgendamiento() == null)
			return false;
		if (agendamiento.getMotivo() == null || agendamiento.getMotivo().isBlank())
			return false;
		if (agendamiento.getVehiculo() == null)
			return false;
		else {
			if (!isPlacaValid(agendamiento.getVehiculo())) {
				throw new AgendamientoNotAcceptableException("Placa del vehiculo no valida");
			}
		}

		return true;
	}

	private boolean isPlacaValid(String placa) {
		return placa.matches("[A-Z]{3}[0-9]{3}") && placa.length() == 6;
	}

	private Agendamiento adjustAgendamiento(Agendamiento agendamiento, boolean isUpdate) {
		// por defecto se pone false
		if (agendamiento.getCumplimiento() == null)
			agendamiento.setCumplimiento(false);
		// La id la asigna la bd
		if (!isUpdate && agendamiento.getId() != null) {
			agendamiento.setId(null);
		}
		if (agendamiento.getVehiculo() != null)
			agendamiento.setVehiculo(agendamiento.getVehiculo().toUpperCase());
		return agendamiento;
	}

	private boolean isValidForOperationTime(Agendamiento agendamiento) {
		HoraFuncionamiento operationTime = hFService.getHora();
		if (operationTime == null || !operationTime.getEstado())
			return true;

		if (agendamiento.getFechaAgendamiento().toLocalTime().isBefore(operationTime.getHoraApertura())
				|| agendamiento.getFechaAgendamiento().toLocalTime().isAfter(operationTime.getHoraCierre())) {

			if (!agendamiento.getFechaAgendamiento().toLocalTime().equals(operationTime.getHoraApertura()))
				return false;
		}
		if (agendamiento.getFechaFinalAgendamiento().toLocalTime().isBefore(operationTime.getHoraApertura())
				|| agendamiento.getFechaFinalAgendamiento().toLocalTime().isAfter(operationTime.getHoraCierre())) {

			if (!agendamiento.getFechaFinalAgendamiento().toLocalTime().equals(operationTime.getHoraCierre()))
				return false;
		}
		return true;
	}

	public boolean isValidReserva(Agendamiento agendamiento, boolean update) {

		// se convierte la hora del servidor a la hora local de bogota
		if (agendamiento.getFechaAgendamiento().isBefore(LocalDateTime.now(timeZoneBogota))
				|| agendamiento.getFechaFinalAgendamiento().isBefore(LocalDateTime.now(timeZoneBogota))
				|| agendamiento.getFechaFinalAgendamiento().isBefore(agendamiento.getFechaAgendamiento())) {

			throw new AgendamientoNotAcceptableException("No es posible registrar un agendamiento con esas fechas");
		}
		if (!isValidForOperationTime(agendamiento))
			return false;
		if (!update) {
			if (!agendamientoRepository.findByFechaAgendamiento(agendamiento.getFechaAgendamiento()).isEmpty()) {
				return false;
			} else {
				return allowReservation(agendamiento, getClosestAgendamientos(agendamiento.getFechaAgendamiento()));
			}
		}
		return true;
	}

	public boolean isValidReserva(Agendamiento agendamiento) {
		return isValidReserva(agendamiento, false);
	}

	public List<Agendamiento> getClosestAgendamientos(LocalDateTime initialDate) {

		Pageable pageable = PageRequest.of(0, 1);

		List<Agendamiento> compareAgend = new ArrayList<>();
		List<Agendamiento> lte = agendamientoRepository.getAgendamientoByLteFechaAgendamiento(initialDate, pageable);
		List<Agendamiento> gte = agendamientoRepository.getAgendamientoByGteFechaAgendamiento(initialDate, pageable);

		if (!lte.isEmpty())
			compareAgend.add(lte.get(0));
		if (!gte.isEmpty())
			compareAgend.add(gte.get(0));
		return compareAgend;
	}

	private static LocalDateTime parseDate(String date) {
		try {
			return LocalDateTime.parse(date);
		} catch (Exception e) {
			throw new AgendamientoNotAcceptableException("Error al convertir la fecha---\n" + e.getMessage());
		}
	}

	public static boolean isInRange(LocalDateTime testDate, LocalDateTime startDate, LocalDateTime endDate) {
		return ((testDate.equals(startDate) || testDate.equals(endDate))
				|| testDate.isAfter(startDate) && testDate.isBefore(endDate));
	}

	public static boolean isSameTime(LocalDateTime testDate, LocalDateTime date) {
		// check if is the same day/date
		if (testDate.toLocalDate().equals(date.toLocalDate())) {
			return testDate.toLocalTime().equals(date.toLocalTime());
		}
		return false;
	}

	public static boolean allowReservation(Agendamiento newAgendamiento, List<Agendamiento> compareAgainst) {
		if (compareAgainst.isEmpty())
			return true;

		for (Agendamiento agenda : compareAgainst) {
			// si un de los agendamientos existentes esta dentro del nuevo agendamiento
			if (isInRange(agenda.getFechaAgendamiento(), newAgendamiento.getFechaAgendamiento(),
					newAgendamiento.getFechaFinalAgendamiento())
					&& !isSameTime(agenda.getFechaAgendamiento(), newAgendamiento.getFechaFinalAgendamiento())
					&& !isSameTime(agenda.getFechaFinalAgendamiento(), newAgendamiento.getFechaAgendamiento())) {
				return false;
			}
			// si la fecha de inicio del nuevo agendamiento esta dentro de las fechas de un
			// agendamiento viejo
			if (isInRange(newAgendamiento.getFechaAgendamiento(), agenda.getFechaAgendamiento(),
					agenda.getFechaFinalAgendamiento())
					&& !isSameTime(newAgendamiento.getFechaAgendamiento(), agenda.getFechaFinalAgendamiento())) {

				return false;

			}
			// si la fecha de fin del nuevo agendamiento esta dentro de las fechas de un
			// agendamiento viejo
			if (isInRange(newAgendamiento.getFechaFinalAgendamiento(), agenda.getFechaAgendamiento(),
					agenda.getFechaFinalAgendamiento())
					&& !isSameTime(newAgendamiento.getFechaFinalAgendamiento(), agenda.getFechaAgendamiento())) {

				return false;

			}
		}
		return true;
	}

}

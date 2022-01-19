package com.proyectotaller.agendamiento_ms.emailService;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.proyectotaller.agendamiento_ms.models.Agendamiento;

@Component
public class EmailServiceTaller {
	@Autowired
	private JavaMailSender emailSender;

	public boolean sendSimpleMessage(EmailBody body) {
		MimeMessage message = emailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);

		try {
			helper.setFrom("noreply@larosataller.com");
			helper.setTo(body.getEnviarA());
			helper.setText(buildMailText(body), true);
			helper.setSubject("Agendamiento taller la rosa");
			emailSender.send(message);

			return true;
		} catch (MessagingException|MailException e) {
			System.out.println("error enviando mensaje------------------\n"+e);
		}
		return false;
	}

	private String buildMailText(EmailBody emailBody) {
		//No se proteje con try porque en teoria el deserializer se encarga de crear un LocalDateTime valido
		DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("h:mm a");

		return String.format(new Locale("es"),"<!DOCTYPE html> <html lang=\"en\" xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:o=\"urn:schemas-microsoft-com:office:office\"> <head> <meta charset=\"UTF-8\"> <meta name=\"viewport\" content=\"width=device-width,initial-scale=1\"> <meta name=\"x-apple-disable-message-reformatting\"> <title></title> <style> table, td, div, h1, p {font-family: Arial, sans-serif;} </style> </head> <body style=\"margin:0;padding:0;\"> <table role=\"presentation\" style=\"width:100%%;border-collapse:collapse;border:0;border-spacing:0;background:#ffffff;\"> <tr> <td align=\"center\" style=\"padding:0;\"> <table role=\"presentation\" style=\"width:602px;border-collapse:collapse;border:1px solid #cccccc;border-spacing:0;text-align:left;\"> <tr> <td style=\"padding:36px 30px 42px 30px;\"> <table role=\"presentation\" style=\"width:100%%;border-collapse:collapse;border:0;border-spacing:0;\"> <tr> <td style=\"padding:0 0 36px 0;color:#153643;\"> <h1 style=\"font-size: 24px; margin: 0 0 20px 0; font-family: Arial,sans-serif;\">Hola %s, Taller la Rosa te informa:</h1> <p style=\"margin: 0 0 12px 0; font-size: 16px; line-height: 24px; font-family: Arial,sans-serif;\">Tu agendamiento para el vehiculo de placas <strong>%s</strong> ha sido creado con exito para el dia %td de %tB del %tY entre las %s y las %s.</p> <p style=\"margin: 0; font-size: 16px; line-height: 24px; font-family: Arial,sans-serif;\">Cordialmente, <a style=\"color: #ee4c50; text-decoration: underline;\" href=\"https://taller-rosa.herokuapp.com/\">Taller la Rosa.</a></p> </td> </tr> </table> </td> </tr> </table> </td> </tr> </table> </td> </tr> </table> </body> </html>"
				, emailBody.getNombreUsuario()
				,emailBody.getVehiculo()
				,emailBody.getFechaAgendamiento() //dia
				,emailBody.getFechaAgendamiento() //mes
				,emailBody.getFechaAgendamiento() //año
				,emailBody.getFechaAgendamiento().format(formatoHora) //hora
				,emailBody.getFechaFinalAgendamiento().format(formatoHora)); // hora
	}
	
}

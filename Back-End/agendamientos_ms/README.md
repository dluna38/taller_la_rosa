# Microservicio Agendamientos
## Versiones usadas
```
Java: 17.0.1 ~17
SpringBoot: 2.5.6
```

# EndPoints

## Get
`[url]/api/agendamientos` : retorna todos los agendamientos.  
`[url]/api/agendamientos/{id}` : retorna un agendamiento basado en el id.  
`[url]/api/agendamientos/fecha/{date}` : recibe una fecha (`2021-11-28T00:00:00`, pasar con hora), retorna una lista de agendamientos para esa fecha.  
`[url]/api/agendamientos/fecha/ocupados/{date}` : lo mismo que la anterior pero solo retorna el id, fechaAgendamiento y fechaFinalAgendamientos (lo demas: null); con la intención de mostrar al usuario las ocupadas(¿Disponibilidad?).  
`[url]/api/agendamientos/fecha/between` : recibe dos fechas fechaInicio, fechaFin. retorna los agendamientos entre esas dos fechas  
`[url]/api/agendamientos/vehiculo/{placa}` : recibe la placa de un vehiculo y retorna los agendamientos pertenecientes a este.

`[url]/api/configuracion/`: retorna el objeto HoraFuncionamiento, sino vacio/null
## Post
`[url]/api/agendamientos` : recibe un agendamiento y lo agrega a la base de datos. Requeridos:fechaAgendamiento, fechaFinalAgendamiento, motivo, vehiculo. Enviar sin id. Si hay conflicto con las fechas rechaza la petición  
`[url]/api/agendamientos/comprobar` : recibe un json(objeto agendamiento) con fechaAgendamiento y fechaFinalAgendamiento, comprueba si las fechas son validas para una cita -> response: {`result`:true/false}. No funciona para comprobar un update

`[url]/api/configuracion/`: recibe una horaFuncionamiento(horaApertura,horaCierre) y lo registra. Unicamente puede haber una hora registrada si ya hay una rechaza la petición  

`[url]/api/agendamientos/email`: recibe un emailBody y envia un correo a esa dirección informandole el agendamiento
## Delete
`[url]/api/agendamientos/delete/{id}` : Elimina un agendamiento basado en el id  
`[url]/api/agendamientos/delete/v/{vehiculo}`: Elimina todos los agendamientos relacionados a un vehiculo, -1 si la placa no es valida. retorna el # de registros eliminados  

`[url]/api/configuracion/`: Si hay alguna hora/configuración la elimina
## Update
`[url]/api/agendamientos/update` : recibe un agendamiento(debe existir en la BD) y actualiza al que corresponda el id, si se cambia solo una fecha la validacion no la permitira

`[url]/api/configuracion/`: recibe una horaFuncionamiento (horaApertura, horaCierre) y lo actualiza.

Notas:  
- Revisar timezone enviada por mongo ya que almacena el registro/documento como +00:00, pero al trabajarlo en java aparece local (-05:00) (cambia/adapta dependiendo de donde se ejecute?), se uso LocalDateTime


# Modelos:

## Agendamiento:
Manipulación de agendamientos/citas sin que existan dos sobre la misma fecha y hora


```
String id;                        
LocalDateTime fechaAgendamiento; (AAAA-MM-DDTHH:MM:SS / AAAA-MM-DDTHH:MM) tipo 24h    
LocalDateTime fechaFinalAgendamiento; (AAAA-MM-DDTHH:MM:SS / AAAA-MM-DDTHH:MM) tipo 24h
String motivo;                       
Boolean cumplimiento;                
String vehiculo;    
```

## HoraFuncionamiento:
Limita la hora de agendamiento a las horas especificadas del negocio, si no hay ningun registro o esta deshabilitado se permite cualquier hora de reserva, unicamente puede existir **una** configuración

```
String id;              
LocalTime horaApertura; (HH:MM:SS) tipo 24h
LocalTime horaCierre;  (HH:MM:SS) tipo 24h
Boolean estado;  
```

## EmailBody:
Estructura del correo a enviar

```
String enviarA; el correo al que se envia  
String nombreUsuario; el nombre del usuario -> hola nombreUsuario  
LocalDateTime fechaAgendamiento;  
LocalDateTime fechaFinalAgendamiento;  
String vehiculo;
```
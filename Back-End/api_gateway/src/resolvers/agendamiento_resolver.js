const agendamientoResolver = {
    Query: {
        //Realizar verificacion Solo admin
        getAgendamientos: async (_, __, { dataSources, userIdToken }) => {
            return await dataSources.agendamientoAPI.getAgendamientos();
        },
        //verificación normal si el usuario equivale al usuario del vehiculo o  admin
        getAgendamientoById: async (_, { agendamientoId }, { dataSources, userIdToken }) => {
            return await dataSources.agendamientoAPI.getAgendamientoById(agendamientoId);
        },
        //Realizar verificacion Solo admin
        getAgendamientosByDay: async (_, { fecha }, { dataSources, userIdToken }) => {
            return await dataSources.agendamientoAPI.getAgendamientosByDay(fecha);
        },
        //Realizar verificacion Solo admin
        getAgendamientosBetweenDates: async (_, { fechaInicio, fechaFin }, { dataSources, userIdToken }) => {
            fechas = {
                fechaInicio: fechaInicio,
                fechaFin: fechaFin
            };
            return await dataSources.agendamientoAPI.getAgendamientosBetweenDates(fechas);
        },
        getAgendamientosOcupadosByDay: async (_, { fecha }, { dataSources, userIdToken }) => {
            return await dataSources.agendamientoAPI.getAgendamientosOcupadosByDay(fecha);
        },
        //verificación normal si el usuario equivale al usuario del vehiculo o  admin
        getAgendamientosByVehiculo: async (_, { placa }, { dataSources, userIdToken }) => {
            return await dataSources.agendamientoAPI.getAgendamientosByVehiculo(placa);
        },
        getAgendamientosByVehiculos: async (_, { placas }, { dataSources, userIdToken }) => {
            let listaAgendamientos = [];
            for (const placa of placas) {
                let agendamientosPlaca = await dataSources.agendamientoAPI.getAgendamientosByVehiculo(placa);
                listaAgendamientos.push.apply(listaAgendamientos, agendamientosPlaca)
            };
            return listaAgendamientos;
        },
        //Realizar verificacion Solo admin
        getAgendamientoConfig: async (_, __, { dataSources, userIdToken }) => {
            return await dataSources.agendamientoConfigAPI.getAgendamientoConfig();
        },
        getHorasDisponibles: async (_, { fecha }, { dataSources, userIdToken }) => {
            let agendadas = await dataSources.agendamientoAPI.getAgendamientosOcupadosByDay(fecha);
            let horarios = await dataSources.agendamientoConfigAPI.getAgendamientoConfig();
            return cargarHoras(horarios.horaApertura, horarios.horaCierre, agendadas);
        },
    },
    Mutation: {
        //sin verificacion
        newAgendamiento: async (_, { agendamiento }, { dataSources, userIdToken }) => {
            let respuesta = await dataSources.agendamientoAPI.newAgendamiento(agendamiento);
            if (respuesta != null) {
                try {
                    let vehiculo = agendamiento.vehiculo;
                    await dataSources.authAPI.getVehiculoByPlaca(vehiculo).then((result) => {
                        let infoCliente = result.propietario;
                        console.log(infoCliente)
                        let emailBody = {
                            "enviarA": infoCliente.correo,
                            "nombreUsuario": infoCliente.nombre + ' ' + infoCliente.apellido,
                            "fechaAgendamiento": agendamiento.fechaAgendamiento,
                            "fechaFinalAgendamiento": agendamiento.fechaFinalAgendamiento,
                            "vehiculo": agendamiento.vehiculo
                        }
                        dataSources.agendamientoAPI.sendEmail(emailBody);
                    }).catch((error) =>{
                        console.log(error)
                    });

                } catch (error) {
                    console.log(error)
                }
            }
            return respuesta;
        },
        //para avisar al usuario antes de que envie el nuevo agendamiento si las fechas son correctas o incorrectas
        checkDatesAgendamiento: async (_, { comprobarAgendamiento }, { dataSources, userIdToken }) => {
            return await dataSources.agendamientoAPI.checkDatesAgendamiento(comprobarAgendamiento);
        },
        //verificación normal si el usuario equivale al usuario del vehiculo o  admin
        deleteAgendamientoById: async (_, { agendamientoId }, { dataSources, userIdToken }) => {
            return await dataSources.agendamientoAPI.deleteAgendamientoById(agendamientoId);
        },
        //solo admin o si se elimina un cliente que elimina vehiculos que elimina agendamientos que elimina revisiones
        deleteAgendamientoByVehiculo: async (_, { placa }, { dataSources, userIdToken }) => {
            return await dataSources.agendamientoAPI.deleteAgendamientoByVehiculo(placa);
        },
        //verificación normal si el usuario equivale al usuario del vehiculo o  admin
        updateAgendamiento: async (_, { agendamiento }, { dataSources, userIdToken }) => {
            return await dataSources.agendamientoAPI.updateAgendamiento(agendamiento);
        },
        //solo admin
        newAgendamientoConfig: async (_, { horaApertura, horaCierre }, { dataSources, userIdToken }) => {
            horas = {
                horaApertura: horaApertura,
                horaCierre: horaCierre
            };
            return await dataSources.agendamientoConfigAPI.newAgendamientoConfig(horas);
        },
        //solo admin
        deleteAgendamientoConfig: async (_, __, { dataSources, userIdToken }) => {
            return await dataSources.agendamientoConfigAPI.deleteAgendamientoConfig();
        },
        //solo admin
        updateAgendamientoConfig: async (_, { horaApertura, horaCierre }, { dataSources, userIdToken }) => {
            horas = {
                horaApertura: horaApertura,
                horaCierre: horaCierre
            };
            return await dataSources.agendamientoConfigAPI.updateAgendamientoConfig(horas);
        },
        //se desencadena de newAgendamiento
        sendEmail: async (_, { emailBody }, { dataSources, userIdToken }) => {
            return await dataSources.agendamientoAPI.sendEmail(emailBody);
        },
    }
};
module.exports = agendamientoResolver;

function cargarHoras(horaInicio, horaFin, ocupadas) {
    function traerSoloHora(fecha) {
        return fecha.substring(11, 16);
    }
    try {
        let horaInicioInt = parseInt(horaInicio.substring(0, 2), 10);
        let horaInicioMin = horaInicio.substring(3, 5);
        let horaFinInt = parseInt(horaFin.substring(0, 2), 10);
        let horaFinMin = horaFin.substring(3, 5);
        let horas = [];

        for (let hora = horaInicioInt; hora <= horaFinInt; hora++) {
            if (hora == horaFinInt && horaInicioMin > horaFinMin) {
                break;
            }
            if (
                ocupadas.findIndex((obj) => {
                    return (
                        traerSoloHora(obj.fechaAgendamiento).substring(0, 2) == hora
                    );
                }) == -1
                && horaFinInt != hora) {
                if (hora >= 0 && hora < 10) {
                    horas.push('0' + hora + ":" + horaInicioMin);
                } else {
                    horas.push(hora + ":" + horaInicioMin);
                }
            }
        }
        return horas;
    } catch (e) {
        console.log(e);
        return [];
    }
}

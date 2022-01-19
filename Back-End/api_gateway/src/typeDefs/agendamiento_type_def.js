const { gql } = require('apollo-server');
const agendamientoTypeDefs = gql `
type Agendamiento {
id: String!
fechaAgendamiento: String!
fechaFinalAgendamiento: String!
motivo: String!
cumplimiento: Boolean!
vehiculo: String!     
}
#Para aquellas consultas que solo reciben las fechas
type FechasAgendamiento{
id: String
fechaAgendamiento: String!
fechaFinalAgendamiento: String!
}
type TwoDates{
fechaInicio: String!
fechaFin: String!
}

type ResultComprobacion{
result: Boolean
}
input ComprobarAgendamiento{
fechaAgendamiento: String!
fechaFinalAgendamiento: String!
}

input AgendamientoInput{
id: String
fechaAgendamiento: String!
fechaFinalAgendamiento: String!
motivo: String!
cumplimiento: Boolean
vehiculo: String! 
}
input AgendamientoUpdateInput{
id: String!
fechaAgendamiento: String!
fechaFinalAgendamiento: String!
motivo: String!
cumplimiento: Boolean
vehiculo: String! 
}
#Servicio de email
type emailBody{
enviarA: String!
nombreUsuario: String!
fechaAgendamiento: String!
fechaFinalAgendamiento: String!
vehiculo: String!
}
input emailBodyInput{
enviarA: String!
nombreUsuario: String!
fechaAgendamiento: String!
fechaFinalAgendamiento: String!
vehiculo: String!
}
#Parte de la configuracion de agendamiento
type AgendamientoConfig{
horaApertura: String
horaCierre: String
estado: Boolean
}

extend type Query {
getAgendamientos : [Agendamiento!]
getAgendamientoById(agendamientoId: String!): Agendamiento
getAgendamientosByDay(fecha: String!):[Agendamiento!]
getAgendamientosBetweenDates(fechaInicio: String!,fechaFin: String!):[Agendamiento!]
getAgendamientosOcupadosByDay(fecha: String!): [FechasAgendamiento]
getAgendamientosByVehiculo(placa: String!): [Agendamiento!]
getAgendamientosByVehiculos(placas:[String!]):[Agendamiento!]
getAgendamientoConfig : AgendamientoConfig
getHorasDisponibles(fecha: String!): [String!]
}

extend type Mutation {
newAgendamiento(agendamiento: AgendamientoInput!): Agendamiento
checkDatesAgendamiento(comprobarAgendamiento: ComprobarAgendamiento!): ResultComprobacion
deleteAgendamientoById(agendamientoId: String!): String
deleteAgendamientoByVehiculo(placa: String!): Int
updateAgendamiento(agendamiento: AgendamientoUpdateInput!): Agendamiento
newAgendamientoConfig(horaApertura: String!,horaCierre: String!): AgendamientoConfig
deleteAgendamientoConfig: String
updateAgendamientoConfig(horaApertura: String!,horaCierre: String!): AgendamientoConfig
sendEmail(emailBody: emailBodyInput!):String
}
`;

module.exports = agendamientoTypeDefs;
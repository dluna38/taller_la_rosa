const { gql } = require('apollo-server');
const authTypeDefs = gql`
# gestion autenticacion
type Tokens {
refresh: String!
access: String!
}
type Access {
access: String!
}
input CredentialsInput {
documento: String!
password: String!
}
type verificarAdmin{
documento: String!
admin: Boolean!
}
# fin gestion autenticacion

input SignUpInput {
documento : String!
password: String!
nombre : String!
apellido : String!
telefono : String!
correo : String!
}
input ClienteUpdateInput{
documento : String!
password: String
nombre : String
apellido : String
telefono : String
correo : String
}

type ClienteDetalle {
documento : String!
nombre : String!
apellido : String!
telefono : String!
correo : String!
}
#soporta tipo: nombre,documento,correo
input SearchCliente {
tipo : String!
texto : String!
}
# Vehiculos
type Vehiculo{
placa : String!
propietario : String!
marca : String!
modelo : String!
tipo : String!
cilindraje : String!
combustible : String!    
}
input VehiculoInput{
placa : String!
propietario : String!
marca : String!
modelo : String!
tipo : String!
cilindraje : String!
combustible : String!        
}
input UpdateVehiculoInput{
placa : String!
propietario : String
marca : String
modelo : String
tipo : String
cilindraje : String
combustible : String     
}
type ClienteAndVehiculos{
cliente: ClienteDetalle!
vehiculos: [Vehiculo!]
}
type DetalleVehiculo{
    placa : String!
    propietario : ClienteDetalle!
    marca : String!
    modelo : String!
    tipo : String!
    cilindraje : String!
    combustible : String! 
}
# Fin Vehiculos

# Inicio Revision
type Revision{
id :Int!
idAgendamiento : String! 
idVehiculo : String! 
resultado : String!
costoTotal : String
fechaRevision : String! 
fechaActualizacion : String! 
}
input InputRevision{
idAgendamiento : String! 
idVehiculo : String! 
resultado : String!
costoTotal : String
}
# Fin Revision


type Mutation {
signUpCliente(clienteInput: SignUpInput!): Tokens!
singUpAdmin(adminInput: SignUpInput!): Tokens!
updateCliente(clienteInput: ClienteUpdateInput!): String
deleteCliente(documento: String!): String
logIn(credentials: CredentialsInput!): Tokens!
refreshToken(refresh: String!): Access!
verificarToken(token: String!): verificarAdmin
newVehiculo(vehiculo: VehiculoInput!): String
updateVehiculo(vehiculo: UpdateVehiculoInput!): String
deleteVehiculo(placa: String!): String
newRevision(revision: InputRevision!): String
updateRevision(revision: InputRevision!): String
}

type Query {
getClientes: [ClienteDetalle!]
getClienteByDocumento(documentoCliente: String!): ClienteDetalle!
getClienteAndVehiculosByDocumento(documentoCliente: String!): ClienteAndVehiculos
getClientesBySearch(argumentos: SearchCliente!): [ClienteDetalle!]
getVehiculos: [Vehiculo!]
getVehiculoByPlaca(placa : String!): DetalleVehiculo!
getVehiculosByPropietario(documento: String!): [Vehiculo!]
getRevisiones: [Revision!]
getRevisionByAgendamiento(idAgendamiento : String!): Revision
getRevisionesByVehiculo(placa:String!): [Revision!]
}
`;
module.exports = authTypeDefs;
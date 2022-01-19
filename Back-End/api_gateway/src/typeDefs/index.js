//Se llama al typedef (esquema) de cada submodulo
const authTypeDefs = require('./auth_type_defs');
const agendamientoTypeDefs = require('./agendamiento_type_def');
//Se unen
const schemasArrays = [authTypeDefs,agendamientoTypeDefs];

//Se exportan
module.exports = schemasArrays;
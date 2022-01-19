const usersResolver = {
    Query: {
        getClientes: (_, __, { dataSources, documentoToken, admin }) => {
            // console.log(documentoToken)
            // console.log(admin)
            return dataSources.authAPI.getClientes();
        },
        getClienteByDocumento: (_, {documentoCliente}, { dataSources, documentoToken, admin }) => {
            return dataSources.authAPI.getClienteByDocumento(documentoCliente);
        },

        getVehiculos: (_, __, { dataSources, documentoToken, admin }) => {
            return dataSources.authAPI.getVehiculos();
        },
        getVehiculoByPlaca: (_, {placa}, { dataSources, documentoToken, admin }) => {
            return dataSources.authAPI.getVehiculoByPlaca(placa);
        },
        getVehiculosByPropietario: (_, {documento}, { dataSources, documentoToken, admin }) => {
            return dataSources.authAPI.getVehiculosByPropietario(documento);
        },
        getClienteAndVehiculosByDocumento: (_, {documentoCliente}, { dataSources, documentoToken, admin }) => {
            let retorno= {
                cliente: dataSources.authAPI.getClienteByDocumento(documentoCliente),
                vehiculos: dataSources.authAPI.getVehiculosByPropietario(documentoCliente),
            }
            return retorno;
        },
        getClientesBySearch: (_, {argumentos}, { dataSources, documentoToken, admin }) => {
            return dataSources.authAPI.getClientesBySearch(argumentos.tipo,argumentos.texto);
        },

        getRevisiones: (_, __, { dataSources, documentoToken, admin }) => {
            return dataSources.authAPI.getRevisiones();
        },
        getRevisionByAgendamiento: (_, {idAgendamiento}, { dataSources, documentoToken, admin }) => {
            return dataSources.authAPI.getRevisionByAgendamiento(idAgendamiento);
        },
        getRevisionesByVehiculo: (_, {placa}, { dataSources, documentoToken, admin }) => {
            return dataSources.authAPI.getRevisionesByVehiculo(placa);
        },
    },
    Mutation: {
        signUpCliente: async (_, { clienteInput }, { dataSources }) => {
            return await dataSources.authAPI.createCliente(clienteInput);
        },
        singUpAdmin: async (_, { adminInput }, { dataSources }) => {
            return await dataSources.authAPI.createAdministrador(adminInput);
        },
        updateCliente: async (_, { clienteInput }, { dataSources }) => {
            return await dataSources.authAPI.updateCliente(clienteInput);
        },
        deleteCliente: async (_, { documento }, { dataSources }) => {
            return await dataSources.authAPI.deleteClienteByDocumento(documento);
        },
        logIn: (_, { credentials }, { dataSources }) => dataSources.authAPI.authRequest(credentials),
        refreshToken: (_, { refresh }, { dataSources }) => dataSources.authAPI.refreshToken(refresh),

        newVehiculo: async (_, { vehiculo }, { dataSources }) => {
            return await dataSources.authAPI.createVehiculo(vehiculo);
        },
        updateVehiculo: async (_, { vehiculo }, { dataSources }) => {
            return await dataSources.authAPI.updateVehiculo(vehiculo);
        },
        deleteVehiculo: async (_, { placa }, { dataSources }) => {
            return await dataSources.authAPI.deleteVehiculoByPlaca(placa);
        },
        verificarToken: async (_, { token }, { dataSources }) => {
            return await dataSources.authAPI.verifyToken(token);
        },
        newRevision: async (_, { revision }, { dataSources }) => {
            return await dataSources.authAPI.createRevision(revision);
        },
        updateRevision: async (_, { revision }, { dataSources }) => {
            return await dataSources.authAPI.updateRevision(revision);
        },
    }
};
module.exports = usersResolver;
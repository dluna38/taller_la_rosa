const { ApolloServer } = require('apollo-server');
const typeDefs = require('./typeDefs');
const resolvers = require('./resolvers');
const AuthAPI = require('./dataSources/auth_api');
const AgendamientoAPI = require('./dataSources/agendamiento_api');
const AgendamientoConfigAPI = require('./dataSources/agendamiento_config_api');
const authentication = require('./utils/authentication');

const server = new ApolloServer({
    context: authentication,
    typeDefs,
    resolvers,
    dataSources: () => ({
        authAPI: new AuthAPI(),
        agendamientoAPI : new AgendamientoAPI(),
        agendamientoConfigAPI: new AgendamientoConfigAPI()
    }),
    introspection: true,
    playground: true
});
server.listen(process.env.PORT || 4000).then(({ url }) => {
    console.log(`🚀 Server ready at ${url}`);
});
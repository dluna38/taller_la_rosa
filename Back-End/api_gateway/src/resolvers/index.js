const authResolver = require('./auth_resolver');
const agendamientoResolver = require('./agendamiento_resolver');
const lodash = require('lodash');
const resolvers = lodash.merge(authResolver,agendamientoResolver);

module.exports = resolvers;
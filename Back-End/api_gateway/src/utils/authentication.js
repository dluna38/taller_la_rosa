const { ApolloError } = require('apollo-server');
const serverConfig = require('../server');
const fetch = require('node-fetch');

const authentication = async ({ req }) => {
    //const token = req.headers.authorization || '';
    const token = '';
    if (token == '')
        return { documentoToken: null, admin: null }
    else {
        try {
            let requestOptions = {
                method: 'POST', headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ token }), redirect: 'follow'
            };
            let response = await fetch(
                `${serverConfig.auth_api_url}verifyToken/`,
                requestOptions)
            if (response.status != 200) {
                console.log(response)
                throw new ApolloError(`SESION INACTIVA - ${401}` + response.status, 401)
            }
            let jsonDevuelto = await response.json()
            return { documentoToken: (jsonDevuelto).documento , admin: (jsonDevuelto).admin};
        }
        catch (error) {
            throw new ApolloError(`TOKEN ERROR: ${500}: ${error}`, 500);
        }
    }
}
module.exports = authentication;
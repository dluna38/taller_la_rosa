const { RESTDataSource } = require('apollo-datasource-rest');
const serverConfig = require('../server');

class AgendamientoConfigAPI extends RESTDataSource{
    constructor() {
        super();
        this.baseURL = serverConfig.agendamiento_config_url;
    }
    //get
    async getAgendamientoConfig() {
        return await this.get('');
    }
    
    //post
    async newAgendamientoConfig(agendamientoConfig) {
        //eliminar para probar
        agendamientoConfig = new Object(JSON.parse(JSON.stringify(agendamientoConfig)));
        return await this.post('',agendamientoConfig);
    }
    
    //delete
    async deleteAgendamientoConfig() {
        return await this.delete('');
    }
   
    //update
    async updateAgendamientoConfig(agendamientoConfig) {
        agendamientoConfig = new Object(JSON.parse(JSON.stringify(agendamientoConfig)));
        return await this.put('',agendamientoConfig);
    }

}

module.exports = AgendamientoConfigAPI;
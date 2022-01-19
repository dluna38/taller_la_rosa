const { RESTDataSource } = require('apollo-datasource-rest');
const serverConfig = require('../server');

class AgendamientoAPI extends RESTDataSource{
    constructor() {
        super();
        this.baseURL = serverConfig.agendamiento_api_url;
    }
    //get
    async getAgendamientos() {
        return await this.get('');
    }
    async getAgendamientoById(agendamientoId) {
        return await this.get(`${encodeURIComponent(agendamientoId)}`);
    }
    async getAgendamientosByDay(fecha) {
        return await this.get(`fecha/${encodeURIComponent(fecha)}`);
    }
    async getAgendamientosBetweenDates(fechas) {
        fechas = new Object(JSON.parse(JSON.stringify(fechas)));
        return await this.post(`fecha/between`,fechas);
    }
    async getAgendamientosOcupadosByDay(fecha) {
        return await this.get(`fecha/ocupados/${fecha}`);
    }
    async getAgendamientosByVehiculo(placa) {
        return await this.get(`vehiculo/${encodeURIComponent(placa)}`);
    }
    
    //post
    async newAgendamiento(agendamiento) {
        //eliminar para probar
        agendamiento = new Object(JSON.parse(JSON.stringify(agendamiento)));
        return await this.post('',agendamiento);
    }
    async checkDatesAgendamiento(comprobarAgendamiento) {
        //solo envia fechas - recibe result: true/false
        comprobarAgendamiento = new Object(JSON.parse(JSON.stringify(comprobarAgendamiento)));
        return await this.post('comprobar',comprobarAgendamiento);
    }
    //delete
    async deleteAgendamientoById(agendamientoId) {
        return await this.delete(`delete/${encodeURIComponent(agendamientoId)}`);
    }
    async deleteAgendamientoByVehiculo(placa) {
        return await this.delete(`delete/v/${encodeURIComponent(placa)}`);
    }
    //update
    async updateAgendamiento(agendamiento) {
        //eliminar para probar
        agendamiento = new Object(JSON.parse(JSON.stringify(agendamiento)));
        return await this.put('update',agendamiento);
    }
    //email
    async sendEmail(emailBody) {
        emailBody = new Object(JSON.parse(JSON.stringify(emailBody)));
        return await this.post('email',emailBody);
    }

}

module.exports = AgendamientoAPI;
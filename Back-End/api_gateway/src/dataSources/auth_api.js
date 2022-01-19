const { RESTDataSource } = require('apollo-datasource-rest');
const serverConfig = require('../server');

class AuthAPI extends RESTDataSource {
    constructor() {
        super();
        this.baseURL = serverConfig.auth_api_url;
    }
    //get
    async getClientes() {
        return await this.get(`cliente/`);
    }
    async getClienteByDocumento(documento) {
        return await this.get(`cliente/detalle/${documento}`);
    }
    async getVehiculos() {
        return await this.get(`cliente/vehiculos/`);
    }
    async getVehiculoByPlaca(placa) {
        return await this.get(`cliente/vehiculo/${placa}`);
    }
    async getVehiculosByPropietario(documento) {
        return await this.get(`cliente/vehiculos/prop/${documento}`);
    }
    async getClientesBySearch(tipo,texto){
        return await this.get(`/cliente/buscar/?tipo=${tipo}&argumento=${texto}`);
    }
    async getRevisiones(){
        return await this.get(`cliente/vehiculos/revisiones/`);
    }
    async getRevisionByAgendamiento(idAgendamiento){
        return await this.get(`cliente/vehiculos/revisiones/a/${idAgendamiento}`);
    }
    async getRevisionesByVehiculo(placa){
        return await this.get(`cliente/vehiculos/revisiones/v/${placa}`);
    }
    //post
    async createCliente(cliente) {
        cliente = new Object(JSON.parse(JSON.stringify(cliente)));
        return await this.post(`cliente/`, cliente);
    }
    async createAdministrador(usuario) {
        usuario = new Object(JSON.parse(JSON.stringify(usuario)));
        return await this.post(`cliente/super/`, usuario);
    }
    async createVehiculo(vehiculo) {
        vehiculo = new Object(JSON.parse(JSON.stringify(vehiculo)));
        return await this.post(`cliente/vehiculos/`, vehiculo);
    }
    async createRevision(revision){
        revision = new Object(JSON.parse(JSON.stringify(revision)));
        return await this.post(`cliente/vehiculos/revisiones/`,revision);
    }
    //post auth
    async authRequest(credentials) {
        credentials = new Object(JSON.parse(JSON.stringify(credentials)));
        return await this.post(`login/`, credentials);
    }
    async refreshToken(token) {
        token = new Object(JSON.parse(JSON.stringify({ refresh: token })));
        return await this.post(`refresh/`, token);
    }
    async verifyToken(token) {
        token = new Object(JSON.parse(JSON.stringify({ token: token })));
        return await this.post(`verifyToken/`, token);
    }

    //put
    async updateCliente(cliente) {
        cliente = new Object(JSON.parse(JSON.stringify(cliente)));
        return await this.put('cliente/',cliente);
    }
    async updateVehiculo(vehiculo) {
        vehiculo = new Object(JSON.parse(JSON.stringify(vehiculo)));
        return await this.put('cliente/vehiculos/',vehiculo);
    }
    async updateRevision(revision){
        revision = new Object(JSON.parse(JSON.stringify(revision)));
        return await this.put(`cliente/vehiculos/revisiones/`,revision);
    }
    //delete
    async deleteClienteByDocumento(documento) {
        return await this.delete(`cliente/delete/${encodeURIComponent(documento)}`);
    }
    async deleteVehiculoByPlaca(placa) {
        return await this.delete(`cliente/vehiculos/${encodeURIComponent(placa)}`);
    }
    async deleteRevisionByAgendamiento(idAgendamiento) {
        return await this.delete(`cliente/vehiculos/${encodeURIComponent(idAgendamiento)}`);
    }
    
}
module.exports = AuthAPI;
<template>
  <div class="container">
    <div class="row">
      <div class="col-6">
        <h3>Registrate</h3>
        <form style="padding-top: 15px" v-on:submit.prevent="loadPanelUsuario">
          <h5>Mis Datos</h5>
          <label for="documentoInput" class="form-label">Documento:</label>
          <input type="text" id="documentoInput" class="form-control-sm" />
          <label for="nombresInput" class="form-label">Nombre: </label>
          <input type="text" id="nombresInput" class="form-control-sm" />
          <label for="apellidoInput" class="form-label">Apellido: </label>
          <input type="text" id="apellidoInput" class="form-control-sm" />
          <label for="telefonoInput" class="form-label">Telefono: </label>
          <input type="number" id="telefonoInput" class="form-control-sm" />
          <label for="correoInput" class="form-label">Correo: </label>
          <input type="email" id="correoInput" class="form-control-sm" />
          <label for="passwordInput" class="form-label">Contraseña: </label>
          <input type="password" id="passwordInput" class="form-control-sm" />
          <h5>Mi Vehiculo</h5>
          <label for="placaInput" class="form-label">Placa:</label>
          <input type="text" id="placaInput" class="form-control-sm" pattern="[a-zA-Z]{3}[0-9]{3}" title="Por favor introduce una placa valida: XXX111"/>
          <label for="marcaInput" class="form-label">Marca:</label>
          <input type="text" id="marcaInput" class="form-control-sm" />
          <label for="modeloInput" class="form-label">Modelo:</label>
          <input type="text" id="modeloInput" class="form-control-sm" />
          <label for="tipoInput" class="form-label">Tipo:</label>
          <input type="text" id="tipoInput" class="form-control-sm" />
          <label for="cilindrajeInput" class="form-label">Cilindraje:</label>
          <input type="text" id="cilindrajeInput" class="form-control-sm" />
          <label for="combustibleInput" class="form-label">Combustible:</label>
          <select class="form-select" id="combustibleInput">
            <option disabled value="">Seleccione un elemento</option>
            <option>Gasolina</option>
            <option>Diésel</option>
            <option>Gas natural</option>
            <option>GLP</option>
            <option>Electricidad</option>
            <option>Biodiesel</option>
          </select>
          <input type="submit" value="Registrarse" class="btn btn-success" />
        </form>
      </div>
      <div class="col-6">
        <h3>Inicia Sesión</h3>
        <form style="padding-top: 15px" v-on:submit.prevent="loadPanelUsuario">
          <div class="row">
            <div class="col-12 text-center">
              <label for="documentoInputSesion" class="form-label"
                >Documento:
              </label>
              <input
                type="text"
                id="documentoInputSesion"
                class="form-control-sm"
              />
            </div>
            <div class="col-12 text-center">
              <label for="passwordInputSesion" class="form-label"
                >Contraseña:
              </label>
              <input
                type="password"
                id="passwordInputSesion"
                class="form-control-sm"
              />
            </div>
            <div class="col-12 text-center">
              <input
                type="submit"
                value="Iniciar Sesión"
                class="btn btn-success"
              />
            </div>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>


<script>
export default {
  name: "RegistroOInicio",
  emits: ["completedLogIn", "completedSignUp", "logOut"],
  data: function () {
    return {
        //cargar datos con v-model usuarioVehiculo
    };
  },
  methods: {
      //modificar para procesar los formularios -> 2 form
      loadPanelUsuario: function (){
          this.$router.push({ name: "panelUsuario" });
      }
  }
};
</script>


<style>
input {
  margin-left: 5px;
}
</style>
<template>
    <div class="container">
      <h5>Consulta de Agendamientos</h5>
      <br />
      <div class="container">
        <fieldset id="seccionFechas" style="padding: 5px">
          <legend>Consulta Fechas</legend>
          <form v-on:submit.prevent="procesarConsultaEntreFechas">
            <div class="row">
              <div class="col-4">
                <label for="fechaInput1" class="col-form-label"
                  >Fecha Inicial:</label
                >
                <input
                  v-model="fechaConsulta"
                  type="date"
                  class="form-control"
                  id="fechaInput1"
                  required
                />
              </div>
              <div class="col-4">
                <label for="fechaInput2" class="col-form-label"
                  >Fecha Final:</label
                >
                <input
                  v-model="fechaConsultaFin"
                  type="date"
                  class="form-control"
                  id="fechaInput2"
                />
              </div>
              <div class="col-4">
                <input
                  type="submit"
                  class="btn btn-primary"
                  value="Consultar"
                />
              </div>
            </div>
          </form>

          <div class="row" id="segundaFila">
            <div class="col-4">
              <button
                type="button"
                class="btn btn-secondary"
                id="mostrarTodos"
                @click="procesarConsultaTodos()"
              >
                Mostrar Todos
              </button>
              <button
                type="button"
                class="btn btn-secondary"
                @click="procesarConsultaHoy()"
              >
                Mostrar Hoy
              </button>
            </div>
            <div class="col-4">
              <p class="text-center">
                Mostrando ahora: <strong>{{ mostrandoAhora }}</strong>
              </p>
            </div>
          </div>
        </fieldset>
        <fieldset id="seccionOtros" style="padding: 5px">
          <legend>Consultar Por</legend>

          <form v-on:submit.prevent="procesarConsultaTexto">
            <div class="row">
              <div class="col-4">
                <select v-model="opcionConsulta" class="form-select" required>
                  <option disabled value="">Seleccione una opción</option>
                  <option>Placa</option>
                  <option>Documento</option>
                </select>
              </div>
              <div class="col-4">
                  <input v-model="consultaTexto" class="form-control" type="text" placeholder="consulta" required/>
                </div>
               <div class="col-4">
                <input
                  type="submit"
                  class="btn btn-primary"
                  value="Consultar"
                />
              </div>
            </div>
          </form>
        </fieldset>
      </div>
      <div class="container">
        <div class="row"> 
        <div v-if="$apollo.loading">Cargando la Información</div>
        <h6 v-else-if="!AgendamientosAMostrar.length">Sin agendamientos</h6>
        <div id="tablaAgendamientos"> 
        <table
          class="table"
          v-if="AgendamientosAMostrar.length"
          
        >
          <thead>
            <tr>
              <th scope="col">Codigo</th>
              <th scope="col">Vehiculo</th>
              <th scope="col">Fecha cita</th>
              <th scope="col">Hora cita</th>
              <th scope="col">Cumplimiento</th>
              <th scope="col">Opciones</th>
              <th scope="col">Revisión</th>
            </tr>
          </thead>
          <tbody>
            <tr
              v-for="Agendamiento in AgendamientosAMostrar"
              v-bind:key="Agendamiento.id"
            >
              <th scope="row">{{ Agendamiento.id }}</th>
              <td>{{ Agendamiento.vehiculo }}</td>
              <td>{{ Agendamiento.fechaAgendamiento.substring(0, 10) }}</td>
              <td>
                {{
                  formatearHora(
                    Agendamiento.fechaAgendamiento.substring(11, 19)
                  )
                }}
              </td>
              <td>
                {{
                  formatearCumplimiento(
                    Agendamiento.fechaAgendamiento,
                    Agendamiento.cumplimiento
                  )
                }}
              </td>
              <td>
                <a href="#" @click="deleteAgendamientoMutation(Agendamiento.id)"
                  ><span><i id="cancelBtn" class="bi bi-calendar2-x"></i></span
                ></a>
                <a href="#" >
                  <span><i id="infoUser" class="bi bi-person-circle"></i></span
                ></a>
              </td>
              <td class="align-top">
                  <a href="#" >
                  <i id="infoRevision" class="bi bi-box-arrow-in-up-right"></i></a>
                </td>
            </tr>
          </tbody>
        </table>
        </div>
      </div>
      </div>
    </div>
  <button  @click="testFunction"></button>
  
  <div class="modal fade" id="ModalCargaAnimacion" tabindex="-1" role="dialog" aria-labelledby="exampleModalCenterTitle" aria-hidden="true" >
    <div class="modal-dialog modal-dialog-centered" role="document">       
      <RingLoader :loading="loading">
      </RingLoader>
    </div>
  </div>

  <transition name="modal">
  <RingLoader :loading="loading" v-show="$apollo.loading">
  </RingLoader>
  </transition >

</template>

<script>
import gql from "graphql-tag";
import RingLoader from "./RingLoader.vue"

export default {
  name: "PanelAdminAgendamientos",
  //referente a al spinner
  components: {
    RingLoader,
  },
  props: {
    loading: {
      type: Boolean,
      default: true
    },
  },
  emits: ["completedLogIn", "completedSignUp", "logOut"],
  data: function () {
    return {
      getAgendamientos: [],
      getAgendamientosByDay: [],
      getAgendamientosBetweenDates: [],
      getAgendamientosByVehiculos:[],
      agendamiento: {
        id: "",
        fechaAgendamiento: "",
        fechaFinalAgendamiento: "",
        motivo: "",
        cumplimiento: null,
        vehiculo: "",
      },
      fechaActual: "",
      fechaConsulta: "",
      fechaConsultaHora: "",
      fechaConsultaFin: "",
      fechaConsultaFinHora: "",
      AgendamientosAMostrar: [],
      mostrandoAhora: "",
      opcionConsulta: "",
      consultaTexto: "",
      textoHoy: "Hoy",
      textoTodos: "Todos",
      textoPersonalizado: "Personalizado",
    };
  },
  apollo: {
    getAgendamientos: {
      query: gql`
        query {
          getAgendamientos {
            id
            fechaAgendamiento
            fechaFinalAgendamiento
            motivo
            cumplimiento
            vehiculo
          }
        }
      `,
    },
    getAgendamientosByDay: {
      query: gql`
        query ($fecha: String!) {
          getAgendamientosByDay(fecha: $fecha) {
            id
            fechaAgendamiento
            fechaFinalAgendamiento
            motivo
            cumplimiento
            vehiculo
          }
        }
      `,
      variables() {
        return {
          fecha: this.fechaConsultaHora,
        };
      },
    },
    getAgendamientosBetweenDates: {
      query: gql`
        query ($fechaInicio: String!, $fechaFin: String!) {
          getAgendamientosBetweenDates(
            fechaInicio: $fechaInicio
            fechaFin: $fechaFin
          ) {
            id
            fechaAgendamiento
            fechaFinalAgendamiento
            motivo
            cumplimiento
            vehiculo
          }
        }
      `,
      variables() {
        return {
          fechaInicio: this.fechaConsultaHora,
          fechaFin: this.fechaConsultaFinHora,
        };
      },
    },
    getAgendamientosByVehiculos: {
      query: gql`
        query ($placas: [String!]) {
          getAgendamientosByVehiculos(placas: $placas) {
            id
            fechaAgendamiento
            fechaFinalAgendamiento
            motivo
            cumplimiento
            vehiculo
          }
        }
      `,
      variables() {
        return {
          placas: this.consultaTexto,
        };
      },
    },
  },
  methods: {
    deleteAgendamientoMutation: async function (id) {
      if (confirm("¿Estas seguro de cancelar el agendamiento?")) {
        await this.$apollo
          .mutate({
            mutation: gql`
              mutation ($agendamientoId: String!) {
                deleteAgendamientoById(agendamientoId: $agendamientoId)
              }
            `,
            variables: {
              agendamientoId: id,
            },
          })
          .then((result) => {
            alert("Borrado con exito");
            if (this.mostrandoAhora == this.textoHoy) {
                this.procesarConsultaHoy();
            } else if (this.mostrandoAhora == this.textoTodos) {
               this.procesarConsultaTodos();
            } else {
               this.procesarConsultaEntreFechas();
            }
            //console.log(result);
          })
          .catch((error) => {
            console.log(error);
            alert("ERROR: No se pudo eliminar el agendamiento.");
          });
      }
    },
    //---------Funciones------------
    testFunction: function () {},
    agregarHoraAFecha: function (fecha) {
      return fecha + "T00:00:00";
    },
    formatearCumplimiento: function (fecha, cumplimiento) {
      try {
        fecha = this.convertToDate(fecha);
        let fechaActual = new Date();
        if (fecha > fechaActual) {
          return "Por Asistir";
        }
        if (cumplimiento) {
          return "Cumplida";
        }
        return "No Cumplida";
      } catch (e) {
        console.log(e);
        return cumplimiento;
      }
    },
    convertToDate: function (date) {
      //let dateObj = new Date(date);
      try {
        //dateObj.setTime(Date.parse(date));
        let dateObj = new Date(date);
        return dateObj;
      } catch (e) {
        console.log(e);
        return date;
      }
    },
    formatearHora: function (hora) {
      try {
        let horaInt = parseInt(hora.substring(0, 2), 10);
        let amPm = "";
        if (horaInt > 0 && horaInt < 12) {
          amPm = "a.m.";
        } else if (horaInt == 12) {
          amPm = "p.m.";
        } else {
          amPm = "p.m.";
          horaInt = horaInt - 12;
        }
        return horaInt + hora.substring(2, 5) + " " + amPm;
      } catch (e) {
        //console.log(e);
        return hora;
      }
    },
    cambiarMostrando: function (texto) {
      this.mostrandoAhora = texto;
    },
    procesarConsultaTodos: async function () {
      if (this.mostrandoAhora != this.textoTodos) {
        this.cambiarMostrando(this.textoTodos);
      }
      if (this.$apollo.queries.getAgendamientos.skip) {
        this.$apollo.queries.getAgendamientos.skip = false;
        await this.$apollo.queries.getAgendamientos.refetch();
      } else {
        await this.$apollo.queries.getAgendamientos.refetch();
      }
      this.AgendamientosAMostrar = this.getAgendamientos;
    },
    procesarConsultaHoy: async function () {
      if (this.mostrandoAhora != this.textoHoy) {
        this.cambiarMostrando(this.textoHoy);
      }
      this.fechaConsulta = this.fechaConsultaFin = this.fechaActual;
      this.fechaConsultaHora = this.agregarHoraAFecha(this.fechaConsulta);
      await this.$apollo.queries.getAgendamientosByDay.refetch();
      this.AgendamientosAMostrar = this.getAgendamientosByDay;
    },
    procesarConsultaEntreFechas: async function () {
      if (this.mostrandoAhora != this.textoPersonalizado) {
        this.cambiarMostrando(this.textoPersonalizado);
      }
      this.fechaConsultaHora = this.agregarHoraAFecha(this.fechaConsulta);
      this.fechaConsultaFinHora = this.agregarHoraAFecha(this.fechaConsultaFin);

      if (this.$apollo.queries.getAgendamientosBetweenDates.skip) {
        this.$apollo.queries.getAgendamientosBetweenDates.skip = false;
        await this.$apollo.queries.getAgendamientosBetweenDates.refetch();
      } else {
        await this.$apollo.queries.getAgendamientosBetweenDates.refetch();
      }
      this.AgendamientosAMostrar = this.getAgendamientosBetweenDates;
    },
    procesarConsultaTexto : async function (){
        if(this.opcionConsulta == 'Placa'){
            if(!this.consultaTexto.match(/[a-zA-Z]{3}[0-9]{3}/)){alert('Oops, la placa no es valida'); return;}
            this.procesarConsultaPorPlaca();
        }
    },
    procesarConsultaPorPlaca: async function (){
        if (this.mostrandoAhora != this.textoPersonalizado) {
        this.cambiarMostrando(this.textoPersonalizado);
      }
      this.consultaTexto = this.consultaTexto.toUpperCase();
      if (this.$apollo.queries.getAgendamientosByVehiculos.skip) {
        this.$apollo.queries.getAgendamientosByVehiculos.skip = false;
        await this.$apollo.queries.getAgendamientosByVehiculos.refetch();
      } else {
        await this.$apollo.queries.getAgendamientosByVehiculos.refetch();
      }
      this.AgendamientosAMostrar = this.getAgendamientosByVehiculos;
    },
    fechaDeHoy: function () {
      return new Date().toISOString().substring(0, 10);
    },
  },
  created: function () {
    this.fechaActual = this.fechaDeHoy();

    //para que no haga la consulta sin datos validos
    this.$apollo.queries.getAgendamientos.skip = true;
    //this.$apollo.queries.getAgendamientosByDay.skip = true
    this.$apollo.queries.getAgendamientosBetweenDates.skip = true;
    this.$apollo.queries.getAgendamientosByVehiculos.skip = true;   
    this.procesarConsultaHoy();
  },
};
</script>

<style>
#segundaFila {
  padding-top: 15px;
}
#mostrarTodos {
  margin-right: 30px;
}
#seccionFechas {
  border: 2px solid #000;
}
#seccionOtros {
  border-left: 2px solid #000;
  border-right: 2px solid #000;
  border-bottom: 2px solid #000;
}
#tablaAgendamientos {
  height: 18rem;
  max-height: 15rem;
  overflow: auto;
}
a{
    padding-right: 10px;
}

/* animacion de carga */
.modal-enter-active,
.modal-leave-active {
  transition: opacity 0.5s ease;
}

.modal-enter-from,
.modal-leave-to {
  opacity: 0;
}
/* fin animacion carga */
</style>
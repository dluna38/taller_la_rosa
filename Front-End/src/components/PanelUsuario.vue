<template>
  <div class="container">
    <h4>Bienvenido {{ nombreUsuario }}</h4>
    <br />
    <h5>Mis Vehiculos</h5>
    <div id="tablaVehiculos">
    <table class="table">
      <thead>
        <tr>
          <th scope="col">Placa</th>
          <th scope="col">Marca</th>
          <th scope="col">Modelo</th>
          <th scope="col">Opciones</th>
        </tr>
      </thead>
      <tbody>
        <tr>
          <th scope="row">ABC123</th>
          <td>Mazda</td>
          <td>Ion - 2016</td>
          <td>
            <a
              href="#"
              data-bs-toggle="modal"
              data-bs-target="#ModalAgendamiento"
              @click="agendamiento.vehiculo = 'REX123'"
              ><span><i class="bi bi-calendar-check"></i></span
            ></a>
          </td>
        </tr>
      </tbody>
    </table>
    </div>
    <br />
    <h5>Mis Agendamientos</h5>
    <div v-if="$apollo.loading">Cargando la Información</div>
    <h6 v-else-if="!getAgendamientosByVehiculos.length">Sin agendamientos</h6>
    <div id="tablaAgendamientos">
    <table class="table" v-if="getAgendamientosByVehiculos.length">
      <thead>
        <tr>
          <th scope="col">Codigo</th>
          <th scope="col">Vehiculo</th>
          <th scope="col">Fecha cita</th>
          <th scope="col">Hora cita</th>
          <th scope="col">Cumplimiento</th>
          <th scope="col">Opciones</th>
        </tr>
      </thead>
      <tbody>
        <tr
          v-for="Agendamiento in getAgendamientosByVehiculos"
          v-bind:key="Agendamiento.id"
        >
          <th scope="row">{{ Agendamiento.id }}</th>
          <td>{{ Agendamiento.vehiculo }}</td>
          <td>{{ Agendamiento.fechaAgendamiento.substring(0, 10) }}</td>
          <td>
            {{
              formatearHora(Agendamiento.fechaAgendamiento.substring(11, 19))
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
          </td>
        </tr>
      </tbody>
    </table>
    </div>
  </div>
  <button value="test" v-on:click="testFunction">test</button>
  <!-- Modal para crear agendamiento -->
  <div
    class="modal fade"
    id="ModalAgendamiento"
    tabindex="-1"
    aria-labelledby="crearAgendamiento"
    aria-hidden="true"
  >
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title" id="crearAgendamiento">Crear Agendamiento</h5>
          <button
            type="button"
            class="btn-close"
            data-bs-dismiss="modal"
            aria-label="Close"
          ></button>
        </div>
        <div class="modal-body">
          <h6>
            Horario: {{ formatearHora(getAgendamientoConfig.horaApertura) }} -
            {{ formatearHora(getAgendamientoConfig.horaCierre) }}
          </h6>
          <form v-on:submit.prevent="procesarNuevoAgendamiento()">
            <div class="row g-3">
              <div class="col">
                <label for="fechaInput" class="col-form-label">Fecha:</label>
                <input
                  type="date"
                  v-bind:min="fechaActual"
                  class="form-control"
                  id="fechaInput"
                  v-model="fechaNewAgendamiento"
                  v-on:blur="ejecutarCargaDeHoras()"
                  required
                />
              </div>
              <div class="col">
                <label for="horaInput" class="col-form-label">Hora:</label>
                <select
                  v-model="agendamiento.fechaFinalAgendamiento"
                  class="form-select"
                  id="horaInput"
                  required
                >
                  <option disabled v-if="getHorasDisponibles.length" value="">Seleccione una hora</option>
                  <option
                    v-for="(hora, index) in getHorasDisponibles"
                    :key="index"
                  >
                    {{ hora }}
                  </option>
                  <option v-if="!getHorasDisponibles.length" disabled value="">
                    No hay citas disponibles, elige otro dia
                  </option>
                </select>
              </div>
            </div>
            <div class="mb-3">
              <label for="motivo-text" class="col-form-label">Motivo:</label>
              <textarea
                class="form-control"
                id="motivo-text"
                v-model="agendamiento.motivo"
                required
              ></textarea>
            </div>
            <div class="row g-3">
              <button
                type="button"
                class="btn btn-secondary"
                data-bs-dismiss="modal"
                id="closeModalAgendamiento"
              >
                Cancelar
              </button>
              <input
                type="submit"
                class="btn btn-primary"
                value="Crear agendamiento"
              />
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
  <transition name="modal">
    <RingLoader :loading="loading" v-show="$apollo.loading"> </RingLoader>
  </transition>
</template>


<script>
import gql from "graphql-tag";
import RingLoader from "./RingLoader.vue";
export default {
  name: "PanelUsuario",
  emits: ["completedLogIn", "completedSignUp", "logOut"],
  //referente a al spinner
  components: {
    RingLoader,
  },
  props: {
    loading: {
      type: Boolean,
      default: true,
    },
  },
  //cargar datos con v-model
  data: function () {
    return {
      nombreUsuario: "Pacho",
      placasUsuario: ["REX123", "BBB123"],
      getAgendamientosByVehiculos: {},
      agendamiento: {
        id: "",
        fechaAgendamiento: "",
        fechaFinalAgendamiento: "",
        motivo: "Revisión general",
        cumplimiento: null,
        vehiculo: "",
      },
      getAgendamientoConfig: {},
      getHorasDisponibles: [],
      fechaConsulta: "",
      fechaNewAgendamiento:"",
      fechaActual:"",
    };
  },
  methods: {
    sendNewAgendamientoMutation: async function () {
      await this.$apollo
        .mutate({
          mutation: gql`
            mutation ($agendamiento: AgendamientoInput!) {
              newAgendamiento(agendamiento: $agendamiento) {
                id
              }
            }
          `,
          variables: {
            agendamiento: this.agendamiento,
          },
        })
        .then((result) => {
          alert("Creado con exito");
          document.getElementById("closeModalAgendamiento").click();
          this.agendamiento.motivo = "";
          this.fechaNewAgendamiento ="";
          this.agendamiento.fechaFinalAgendamiento = "";
          //Actualizar lista de placas del usuario si agrego un nuevo vehiculo
          this.$apollo.queries.getAgendamientosByVehiculos.refetch();
        })
        .catch((error) => {
          console.log(error);
          alert("ERROR: Fallo en el registro.");
        });
    },
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
            this.$apollo.queries.getAgendamientosByVehiculos.refetch();
            //console.log(result);
          })
          .catch((error) => {
            console.log(error);
            alert("ERROR: No se pudo eliminar el agendamiento.");
          });
      }
    },
    testFunction: function () {},
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
    procesarNuevoAgendamiento: function () {
      let hora = this.agendamiento.fechaFinalAgendamiento;
      this.agendamiento.fechaAgendamiento = this.fechaNewAgendamiento+ "T" + hora
      let horaMasUno = (parseInt(hora.substring(0, 2), 10) + 1);
      horaMasUno = (horaMasUno >= 0 && horaMasUno < 10) ? '0'+horaMasUno : horaMasUno;
      this.agendamiento.fechaFinalAgendamiento = this.fechaNewAgendamiento + "T" + horaMasUno+ ":00";

      console.log(this.agendamiento)
      this.sendNewAgendamientoMutation();
    },
    ejecutarCargaDeHoras: async function () {
      //valida que haya elegido una fecha
      if (this.fechaNewAgendamiento.match(/\d{4}-[01]\d-[0-3]\d/)) {
        this.fechaConsulta = this.fechaNewAgendamiento + "T00:00:00";
        if (this.$apollo.queries.getHorasDisponibles.skip) {
          this.$apollo.queries.getHorasDisponibles.skip = false;
        } else {
          await this.$apollo.queries.getHorasDisponibles.refetch();
        }
      }
    },
  },
  //querys
  apollo: {
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
          placas: this.placasUsuario,
        };
      },
    },
    getAgendamientoConfig: {
      query: gql`
        query {
          getAgendamientoConfig {
            horaApertura
            horaCierre
            estado
          }
        }
      `,
    },
    getHorasDisponibles: {
      query: gql`
        query ($fecha: String!) {
          getHorasDisponibles(fecha: $fecha)
        }
      `,
      variables() {
        return {
          fecha: this.fechaConsulta,
        };
      },
    },
  },
  created: function () {
    //para que no haga la consulta sin datos validos
    this.fechaActual= new Date().toISOString().substring(0,10);
    this.$apollo.queries.getHorasDisponibles.skip = true;
  },
};
</script>

<style>
#cancelBtn {
  color: red;
}
#tablaAgendamientos{
  height: 18;
  max-height: 15rem;
  overflow: auto;
}
#tablaVehiculos{
  height: 18;
  max-height: 15rem;
  overflow: auto;
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
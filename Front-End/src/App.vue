<template>
  <div id="app" class="app">
    <nav class="navbar navbar-expand-lg navbar-light text-dark bg-opacity-50">
      <div class="container-fluid">
        <!-- cambiar a <a> para que aparezca la manito -->
        <h1 class="navbar-brand" v-on:click="loadInicio" href="#">
          Taller la rosa
        </h1>
        <button
          class="navbar-toggler"
          type="button"
          data-bs-toggle="collapse"
          data-bs-target="#navbarSupportedContent"
          aria-controls="navbarSupportedContent"
          aria-expanded="false"
          aria-label="Toggle navigation"
        >
          <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarSupportedContent">
          <ul class="navbar-nav me-auto mb-2 mb-lg-0">
            <li class="nav-item">
              <a class="nav-link active" href="#" v-on:click="loadInicio"
                >Inicio</a
              >
            </li>
            <li class="nav-item">
              <a class="nav-link" href="#">Quienes somos</a>
            </li>
            <!-- v-if="is_auth" -->
            <li class="nav-item">
              <a class="nav-link" href="#">Mis vehiculos</a>
            </li>
            <li class="nav-item">
              <a class="nav-link" href="#">Clientes</a>
            </li>
            <li class="nav-item">
              <a class="nav-link" href="#" @click="loadAdminAgendamientos"
                >Agendamientos</a
              >
            </li>
          </ul>
        </div>
      </div>
    </nav>
    <div class="main-component">
      <router-view
        v-on:completedLogIn="completedLogIn"
        v-on:completedSignUp="completedSignUp"
        v-on:logOut="logOut"
      >
      </router-view>
    </div>
    <!-- Footer -->
    <footer class="page-footer font-small blue pt-4">
      <!-- Footer Links -->
      <div class="container-fluid text-center text-md-left">
        <!-- Grid row -->
        <div class="row">
          <!-- Grid column -->
          <div class="col-md-3 mt-md-0 mt-3">
            <!-- Content -->
            <h5 class="text-uppercase">Taller la rosa</h5>
            <p>Para servirle</p>
          </div>
          <!-- Grid column -->
          <hr class="clearfix w-100 d-md-none pb-3" />
          <!-- Grid column -->
          <div class="col-md-6 mb-md-0 mb-6">
            <!-- Links -->
            <h5 class="text-uppercase">Mas Información</h5>

            <ul class="list-unstyled">
              <li>
                <p>Contacto: 1234567890</p>
              </li>
              <li>
                <a href="#!">Link 2</a>
              </li>
            </ul>
          </div>
          <!-- Grid column -->
        </div>
        <!-- Grid row -->
      </div>
      <!-- Footer Links -->
    </footer>
    <!-- Footer -->
  </div>
</template>




<script>
export default {
  name: "App",
  computed: {
    is_auth: {
      get: function () {
        return this.$route.meta.requiresAuth;
      },
      set: function () {},
    },
  },
  methods: {
    loadLogIn: function () {
      this.$router.push({ name: "logIn" });
    },
    loadSignUp: function () {
      this.$router.push({ name: "signUp" });
    },
    completedLogIn: function (data) {
      localStorage.setItem("username", data.username);
      localStorage.setItem("token_access", data.token_access);
      localStorage.setItem("token_refresh", data.token_refresh);
      alert("Autenticación Exitosa");
      this.loadHome();
    },
    completedSignUp: function (data) {
      alert("Registro Exitoso");
      this.completedLogIn(data);
    },
    loadInicio: function () {
      this.$router.push({ name: "inicio" });
    },
    loadHome: function () {
      this.$router.push({ name: "home" });
    },
    loadAccount: function () {
      this.$router.push({ name: "account" });
    },
    loadTransaction: function () {
      this.$router.push({ name: "transaction" });
    },
    logOut: function () {
      localStorage.clear();
      alert("Sesión Cerrada");
      this.loadLogIn();
    },
    loadAdminAgendamientos: function () {
      this.$router.push({ name: "panelAdminAgendamientos" });
    },
  },
};
</script>






<style>
nav {
  background: #abe3f7;
}
footer {
  background: #abe3f7;
}
nav a {
  font-size: 1.1em;
}
.navbar-brand {
  font-size: 32px !important;
}
.main-component {
  /* height: 75vh; */
  margin: 0%;
  padding: 0%;

  background: #fdfefe;
}

.footer {
  margin: 0;
  padding: 0;
  width: 100%;
  height: 10vh;
  min-height: 100px;

  background-color: #283747;
  color: #e5e7e9;
}

.footer h2 {
  width: 100%;
  height: 100%;

  display: flex;
  justify-content: center;
  align-items: center;
}
</style>

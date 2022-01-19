<template>
  <div class="container">
    <img
      src="../assets/pexels-andrea-piacquadio-3807799.jpg"
      class="img-fluid"
      alt="imagen taller"
    />
  </div>
  <div class="container">
     <div class="row">
        <div id ="btn" class="col-12 text-center">
            <button type="button" class="btn btn-primary" v-on:click="loadRegistro">Agendar Cita</button>
        </div>
        <div id ="btn" class="col-12 text-center">
            <button type="button" class="btn btn-primary">Consultar Agendamiento</button>
        </div>       
    </div>
  </div>
  
</template>

<script>
export default {
  name: "Inicio",
  emits: ["completedLogIn", "completedSignUp", "logOut"],
  methods: {
      loadRegistro: function (){
          this.$router.push({ name: "registroOinicio" });
      }
  }
};
</script>

<style>
img {
  padding: 15px;
  width: 100%;
  object-fit: cover;
  height: 400px !important;
}
#btn{
    margin-bottom: 15px;
}
</style>
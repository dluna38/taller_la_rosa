import gql from "graphql-tag";
import { createRouter, createWebHistory } from "vue-router";
import { ApolloClient, createHttpLink, InMemoryCache } from '@apollo/client/core'

import LogIn from './components/LogIn.vue'
import SignUp from './components/SignUp.vue'
import Home from './components/Home.vue'
import Account from './components/Account.vue'
import Transaction from './components/Transaction.vue'
import Inicio from './components/Inicio.vue'
import RegistroOInicio from './components/RegistroOInicio.vue'
import PanelUsuario from './components/PanelUsuario.vue'
import PanelAdminAgendamientos from './components/PanelAdminAgendamientos'

const routes = [{
    path: '/user/logIn',
    name: "logIn",
    component: LogIn,
    meta: { requiresAuth: false }
},
{
    path: '/user/signUp',
    name: "signUp",
    component: SignUp,
    meta: { requiresAuth: false }
},
{
    path: '/user/home',
    name: "home",
    component: Home,
    meta: { requiresAuth: true }
},
{
    path: '/user/account',
    name: "account",
    component: Account,
    meta: { requiresAuth: true }
},
{
    path: '/user/transaction',
    name: "transaction",
    component: Transaction,
    meta: { requiresAuth: true }
},
{
    path: '/inicio',
    name: "inicio",
    component: Inicio,
    meta: { requiresAuth: false }
},
{
    path: '/registro',
    name: "registroOinicio",
    component: RegistroOInicio,
    meta: { requiresAuth: false }
},
{
    path: '/panel',
    name: "panelUsuario",
    component: PanelUsuario,
    meta: { requiresAuth: false } //true
},
{
    path: '/panelAgendamientos',
    name: "panelAdminAgendamientos",
    component: PanelAdminAgendamientos,
    meta: { requiresAuth: false } //true
}

];
const router = createRouter({
    history: createWebHistory(),
    routes,
});
const apolloClient = new ApolloClient({
    link: createHttpLink({ uri: 'https://practica-tic-api-gateway.herokuapp.com/' }),
    cache: new InMemoryCache()
})
async function isAuth() {
    if (localStorage.getItem("token_access") === null ||
        localStorage.getItem("token_refresh") === null) {
        return false;
    }
    try {
        var result = await apolloClient.mutate({
            mutation: gql`
mutation ($refresh: String!) {
refreshToken(refresh: $refresh) {
access
}
}
`,
            variables: {
                refresh: localStorage.getItem("token_refresh"),
            },
        })
        localStorage.setItem("token_access", result.data.refreshToken.access);
        return true;
    } catch {
        localStorage.clear();
        alert("Su sesión expiró, por favor vuelva a iniciar sesión");
        return false;
    }
}

router.beforeEach(async (to, from) => {
    var is_auth = await isAuth();
    if (is_auth == to.meta.requiresAuth) return true
    if (is_auth) return { name: "home" };
    return { name: "logIn" };
})
export default router;
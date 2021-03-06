/**
 * Ce fichier gère l'état Vuex dans l'app de l'authentification
 */


// initial state
const state = {
  //token: localStorage.getItem("user-token") || "",

  auth: false,
  username: "",
  role: "",
  user: {}
  
};

// getters
const getters = {
  //isAuthenticated: state => !!state.token,
  //authStatus: state => state.status
  isAuthenticated: state => state.auth,
  user: state => state.user
};

// actions
const actions = {
  changeStatus: ({ commit }) => {
    commit("setAuthStatus", !state.auth);
  }
};

// mutations
const mutations = {
  setAuthStatus: (state, payload) => (state.auth = payload),
  
  login: (state, {name, role}) => {
    state.username = name
    state.role = role
    state.auth = true
    // TODO: need resp. from api for id & role!
    state.user = {nomUtilisateur: name, utilisateurID: 7, role: {roleID: 4, nomRole: role}}
  },
  logout: (state) => {
    state.username = null
    state.role = null
    state.auth = false
  }
};

export default {
  state,
  getters,
  actions,
  mutations
};

/**
 * source:
 * https://blog.sqreen.com/authentication-best-practices-vue/
 * https://stackblitz.com/edit/vue-vuex-jwt-authentication-example?file=_store%2Findex.js
 * https://scotch.io/tutorials/handling-authentication-in-vue-using-vuex
 */

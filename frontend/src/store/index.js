import { createStore } from 'vuex';

const store = createStore({
    state: {
        isAuthenticated: true,
        userFullName: '',
        selectedMenuItem: 'menu0',
        selectedCompany: '',
        boyuHistory: [],

    },
    getters: {
        isAuthenticated: state => state.isAuthenticated,
        getUserFullName: state => state.userFullName,
        getSelectedMenuItem(state) {
            return state.selectedMenuItem;
        },
        getSelectedCompany: state => state.selectedCompany,
        getBoyuHistory: (state) => state.boyuHistory,
    },
    mutations: {
        setUsername(state, username) {
            state.userFullName = username;
        },
        setAuthentication(state, isAuthenticated) {
            state.isAuthenticated = isAuthenticated;
        },
        setSelectedMenuItem(state, menu) {
            state.selectedMenuItem = menu;
        },
        setSelectedCompany(state, company) {
            state.selectedCompany = company;
        },
        addBoyuHistory(state, firstForm) {
            state.boyuHistory.push(firstForm);
        }
    },
    actions: {
        saveBoyuHistory({ commit }, formArray) {
            const firstForm = formArray[0];
            commit('addBoyuHistory', { ...firstForm });
        }
    }
});

export default store;

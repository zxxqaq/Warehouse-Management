import { createStore } from 'vuex';

const store = createStore({
    state: {
        isAuthenticated: true,
        userFullName: '',
        selectedMenuItem: '0',
        boyuHistory: [],

    },
    getters: {
        isAuthenticated: state => state.isAuthenticated,
        getUserFullName: state => state.userFullName,
        getSelectedMenuItem(state) {
            return state.selectedMenuItem;
        },
        getBoyuHistory: (state) => state.boyuHistory,
    },
    mutations: {
        setUsername(state, username) {
            state.userFullName = username;
        },
        setAuthentication(state, isAuthenticated) {
            state.isAuthenticated = isAuthenticated;
        },
        setSelectedMenuItem(state, index) {
            state.selectedMenuItem = index;
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

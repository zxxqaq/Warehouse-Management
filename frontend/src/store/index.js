import { createStore } from 'vuex';

const store = createStore({
    state: {
        isAuthenticated: true,
        userName: '',
        selectedMenuItem: 'menu0',
        selectedCompany: null,
        companyList: [],
        boyuHistory: [],
    },
    getters: {
        isAuthenticated: state => state.isAuthenticated,
        getUserFullName: state => state.userName,
        getSelectedMenuItem(state) {
            return state.selectedMenuItem;
        },
        getSelectedCompany: state => state.selectedCompany,
        getCompanyList: (state) => state.companyList,
        getBoyuHistory: (state) => state.boyuHistory,
    },
    mutations: {
        setUsername(state, userName) {
            state.userName = userName;
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
        setCompanyList(state, companyList) {
            state.companyList = companyList;
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

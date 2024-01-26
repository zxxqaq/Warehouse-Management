import { createStore } from 'vuex';

const store = createStore({
    state: {
        isAuthenticated: false,
        token: null,
        selectedMenuItem: 'menu0',
        selectedCompany: 0,
        itemId: null,
        companyList: [],
    },
    getters: {
        isAuthenticated: state => state.isAuthenticated,
        getToken: state => state.token,
        getSelectedMenuItem(state) {
            return state.selectedMenuItem;
        },
        getSelectedCompany: state => state.selectedCompany,
        getCompanyList: (state) => state.companyList,
        getItemId: state => state.itemId,
    },
    mutations: {
        setToken(state, token) {
            state.token = token;
        },
        setAuthentication(state, isAuthenticated) {
            state.isAuthenticated = isAuthenticated;
        },
        setSelectedMenuItem(state, menu) {
            state.selectedMenuItem = menu;
        },
        setSelectedCompany(state, companyId) {
            state.selectedCompany = companyId;
        },
        setCompanyList(state, companyList) {
            state.companyList = companyList;
        },
        setItemId(state, itemId) {
            state.itemId = itemId;
        },
    },
    actions: {}
});

export default store;

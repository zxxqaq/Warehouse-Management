import { createStore } from 'vuex';

const store = createStore({
    state: {
        isAuthenticated: false,
        userName: '',
        selectedMenuItem: 'menu0',
        selectedCompany: null,
        itemId: null,
        companyList: [],
    },
    getters: {
        isAuthenticated: state => state.isAuthenticated,
        getUserFullName: state => state.userName,
        getSelectedMenuItem(state) {
            return state.selectedMenuItem;
        },
        getSelectedCompany: state => state.selectedCompany,
        getCompanyList: (state) => state.companyList,
        getItemId: state => state.itemId,
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

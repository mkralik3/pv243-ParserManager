import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import App from './App';
import registerServiceWorker from './registerServiceWorker';
import {Switch, Route, HashRouter} from "react-router-dom";
import Keycloak from "keycloak-js";
import axios from 'axios'
import {NotificationManager} from "react-notifications"

const kc = Keycloak('keycloak.json');

const MyAppPage = (props) => {
    return (
        <App
            kc={kc}
            {...props}
        />
    );
};

kc.onTokenExpired = () => {
    NotificationManager.info("INFO", "Token expired");
    kc.updateToken(5).success((refreshed) => {
        NotificationManager.success("SUCCESS", "Keycloak token was refreshed = " + refreshed)
    }).error(() => {NotificationManager.error("ERROR", "Keycloak token was not refreshed"); kc.login()});
};

kc.onAuthRefreshSuccess = () => {
    NotificationManager.success("SUCCESS", "Keycloak token refreshed");
    updateLocalStorage();

    console.log("Current keycloak token");
    console.log(kc.token);
    console.log("End of token")
};

kc.onAuthSuccess = () => {
    NotificationManager.success("SUCCESS", "Keycloak authenticated");
    updateLocalStorage();

    console.log("Current keycloak token");
    console.log(kc.token);
    console.log("End of token")
};

kc.init({onLoad: 'login-required'})
    .then(authenticated => {
        NotificationManager.success("SUCCESS", "Keycloak initialized authenticated: " + authenticated);
        if (authenticated) {
            ReactDOM.render(
                <HashRouter>
                    <Switch>
                        <Route path="/" render={MyAppPage}/>
                        <Route render={() => "Path not found"}/>
                    </Switch>
                </HashRouter>,
                document.getElementById('root'));
        } else {
            NotificationManager.error("ERROR", "Not able to authenticate" + authenticated);
        }
    });

axios.interceptors.request.use(function (config) {
    config.headers.Authorization = 'Bearer ' + localStorage.getItem('kc_token');
    return config;

}, function (err) {
    return Promise.reject(err);
});


axios.interceptors.response.use(function (response) {
    return response;
}, function (error) {
    if (error.response.status === 403) {
        NotificationManager.error("ERROR", "You are not allowed to do this operation")
    } else if (error.response.status === 401) {
        kc.updateToken(30).error(() => kc.login());
    }

    return Promise.reject(error);
});

const updateLocalStorage = () => {
    localStorage.setItem('kc_token', kc.token);
    localStorage.setItem('kc_refreshToken', kc.refreshToken);
};

registerServiceWorker();

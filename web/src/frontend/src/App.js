import React, {Component} from 'react';
import logo from './logo.svg';
import './App.css';
import RestaurantComponent from './components/RestaurantComponent';
import AllRestaurantsComponent from "./components/AllRestaurantsComponent";
import {Route, Switch} from "react-router-dom";
import {toast, ToastContainer} from "react-toastify";
import 'react-toastify/dist/ReactToastify.css';

class App extends Component {
    render() {
        const { match } = this.props;

        return (
            <div className="App">
                <header className="App-header">
                    <img src={logo} className="App-logo" alt="logo"/>
                    <h1 className="App-title">Welcome to Restaurant parser manager</h1>
                </header>

                <div>
                    <ToastContainer />
                    <Switch>
                        <Route exact path={`${match.url}/`} component={AllRestaurantsComponent}/>
                        <Route path={`${match.url}/restaurant/:googlePlaceID`} component={RestaurantComponent}/>
                    </Switch>
                </div>
            </div>
        );
    }
}

export default App;

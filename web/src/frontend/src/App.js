import React, {Component} from 'react';
import logo from './logo.svg';
import './App.css';
import RestaurantComponent from './components/RestaurantComponent';
import AllRestaurantsComponent from "./components/AllRestaurantsComponent";
import {Link, Route, Switch} from "react-router-dom";
import {ToastContainer} from "react-toastify";

import 'react-toastify/dist/ReactToastify.css';
import '../node_modules/bootstrap/dist/css/bootstrap.min.css';
import {MenuItem, Nav, Navbar, NavDropdown, NavItem} from "react-bootstrap";

class App extends Component {
    render() {
        const {match} = this.props;

        return (
            <div className="App">
                <header className="App-header">
                    <img src={logo} className="App-logo" alt="logo"/>
                    <h1 className="App-title">Welcome to Restaurant parser manager</h1>
                </header>

                <Navbar inverse collapseOnSelect>
                    <Navbar.Header>
                        <Navbar.Brand>
                            <a href={`${match.url}`}>Parser-Manager</a>
                        </Navbar.Brand>
                        <Navbar.Toggle />
                    </Navbar.Header>
                    <Navbar.Collapse>
                        <Nav>
                            <NavItem eventKey={1} href={`${match.url.replace(/\/$/, "")}/restaurants`}>
                                Restaurants
                            </NavItem>
                        </Nav>
                        <Nav pullRight>
                            <NavItem eventKey={1} href="#">
                                Account
                            </NavItem>
                            <NavItem eventKey={2} href="#">
                                Logout
                            </NavItem>
                        </Nav>
                    </Navbar.Collapse>
                </Navbar>

                <div>
                    <ToastContainer/>
                    <Switch>
                        <Route exact path={`${match.url.replace(/\/$/, "")}/restaurants`} component={AllRestaurantsComponent}/>
                        <Route path={`${match.url.replace(/\/$/, "")}/restaurants/:googlePlaceID`} component={RestaurantComponent}/>
                    </Switch>
                </div>
            </div>
        );
    }
}

export default App;

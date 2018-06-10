import React, {Component} from 'react';
import logo from './logo.svg';
import './App.css';
import RestaurantComponent from './components/RestaurantComponent';
import AllRestaurantsComponent from "./components/AllRestaurantsComponent";
import {Route, Switch} from "react-router-dom";
import {ToastContainer} from "react-toastify";

import 'react-toastify/dist/ReactToastify.css';
import '../node_modules/bootstrap/dist/css/bootstrap.min.css';
import {Nav, Navbar, NavItem} from "react-bootstrap";
import UnconfirmedParsersComponent from "./components/UnconfirmedParsersComponent";
import {NotificationContainer} from "react-notifications";
import 'react-notifications/lib/notifications.css';

class App extends Component {

    render() {
        const {match} = this.props;
        const kc = this.props.kc;

        return (
            <div className="App">
                <header className="App-header">
                    <img src={logo} className="App-logo" alt="logo"/>
                    <h1 className="App-title">Welcome to Restaurant parser manager</h1>
                </header>

                <Navbar inverse collapseOnSelect>
                    <Navbar.Header>
                        <Navbar.Brand>
                            <a href={`#`}>Parser-Manager</a>
                        </Navbar.Brand>
                        <Navbar.Toggle />
                    </Navbar.Header>
                    <Navbar.Collapse>
                        <Nav>
                            <NavItem eventKey={1} href={`#/restaurants`}>
                                Restaurants
                            </NavItem>
                            <NavItem eventKey={1} href={`#/parsers`}>
                                Parsers
                            </NavItem>
                        </Nav>
                        <Nav pullRight>
                            <NavItem eventKey={1} onClick={kc.accountManagement}>
                                Account
                            </NavItem>
                            {kc.authenticated &&
                                <NavItem eventKey={2} onClick={() => {
                                    localStorage.clear();
                                    kc.logout();
                                }}>
                                    Logout ({kc.tokenParsed.preferred_username})
                                </NavItem>
                            }

                            {!kc.authenticated &&
                                <NavItem eventKey={3} onClick={() => {
                                    kc.login();
                                }}>
                                    Sign in
                                </NavItem>
                            }
                        </Nav>
                    </Navbar.Collapse>
                </Navbar>

                <div>
                    <ToastContainer/>
                    <NotificationContainer/>
                    <Switch>
                        <Route exact path={`/restaurants`} render={(props) => {return <AllRestaurantsComponent
                            kc={kc}
                            {...props}
                        />}}/>
                        <Route path={`/restaurants/:googlePlaceID`} component={RestaurantComponent}/>
                        <Route path={`/parsers`} render={(props) => {
                            return <UnconfirmedParsersComponent
                                kc={kc}
                                {...props}
                            />
                        }}/>
                    </Switch>
                </div>
            </div>
        );
    }
}

export default App;

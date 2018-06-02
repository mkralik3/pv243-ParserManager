import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import App from './App';
import registerServiceWorker from './registerServiceWorker';
import {BrowserRouter, Switch, Route} from "react-router-dom";

ReactDOM.render(
    <BrowserRouter>
        <Switch>
            <Route path="/ParserManager-react" component={App}/>
            <Route render={() => "Path not found"}/>
        </Switch>
    </BrowserRouter>,
    document.getElementById('root'));
registerServiceWorker();

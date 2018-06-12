import React, {Component} from 'react';
import {NotificationManager} from "react-notifications"
import {BootstrapTable, TableHeaderColumn} from "react-bootstrap-table"
import EditableText from './EditableText';
import axios from 'axios';

export default class UnconfirmedParsersComponent extends Component {

    websocket;

    constructor(props) {
        super(props);

        this.state = {
            parsers: [],
            error: null
        };
    }

    componentWillUnmount() {
        this.websocket.close();
    }

    componentDidMount() {
        const kc = this.props.kc;
        this.websocket = new WebSocket("ws://mitko:admin@localhost:8080/ParserManager-rest/ws");

        var msg = {
            type: 'authenticate',
            payload: { token:  kc.token}
        };

        this.websocket.onopen = function () {
            NotificationManager.success("SUCCESS", "Connected to websocket");
        };

        this.websocket.onmessage = function (event) {
            let parsers = JSON.parse(event.data);
            this.setState({parsers});
            NotificationManager.success("SUCCESS", "Successfully processed message from server", 3000);
        }.bind(this);

        this.websocket.onerror = function (event) {
            NotificationManager.warning('Warning', 'Something went wrong', 3000);
        };
        this.websocket.onclose = function () {
            NotificationManager.info("INFO", "Connection closed", 3000);
        };
    }

    static xpathFormater(cell, row) {
        return <EditableText value={cell} callback={(newValue) => {
            row.xpath = newValue;

            axios.put('http://localhost:8080/ParserManager-rest/rest/parsers/', row, {
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                }
            }).then(response => {
                NotificationManager.success("INFO", "Parser was added", 3000);
            });
        }}/>
    }

    render() {
        const {match} = this.props;

        return <BootstrapTable data={this.state.parsers}
                               options={{
                                   noDataText: 'No unconfirmed parsers',
                                   onRowClick: (row, columnIndex) => {
                                       switch (columnIndex) {
                                           case 3:
                                               this.props.history.push("/ParserManager-react/restaurants/" + row.restaurant.googlePlaceID);
                                               break;
                                           case 4:
                                               axios.post('http://localhost:8080/ParserManager-rest/rest/parsers/accept/' + row.id, {
                                                   headers: {
                                                       'Accept': 'application/json',
                                                       'Content-Type': 'application/json',
                                                   }
                                               });

                                               break;
                                       }
                                   },
                                   paginationSize: 1
                               }} pagination>
            <TableHeaderColumn width={150} dataField='id' isKey>Parser ID</TableHeaderColumn>
            <TableHeaderColumn width={150} dataField='xpath'
                               dataFormat={UnconfirmedParsersComponent.xpathFormater}>XPath</TableHeaderColumn>
            <TableHeaderColumn width={150} dataField='day'>Day</TableHeaderColumn>
            <TableHeaderColumn width={150} dataField='restaurant'
                               dataFormat={(cell) => {if(cell!==null){ return <div style={{pointerEvents: "none"}}>{cell.name}</div>}
                               else{ return "Restaurant is null" }
                               }}>Restaurant</TableHeaderColumn>
            <TableHeaderColumn width={150} dataField='confirmParser' dataFormat={(cell) => <div style={{pointerEvents: "none"}}>Approve parser</div>}
                               tdStyle={{cursor: 'pointer', width: 'inherit'}} tdAttr={{'className': 'btn-default'}}>Confirm
                Parser</TableHeaderColumn>
        </BootstrapTable>;
    }
}
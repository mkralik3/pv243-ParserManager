import React, {Component} from 'react';
import {NotificationManager} from "react-notifications"
import {BootstrapTable, TableHeaderColumn} from "react-bootstrap-table"
import EditableText from './EditableText';

export default class AllRestaurantsComponent extends Component {

    websocket;

    constructor(props) {
        super(props);

        this.state = {
            parsers: [],
            error: null
        };
    }

    componentDidMount() {
        this.websocket = new WebSocket("ws://localhost:8080/ParserManager-rest/ws");

        this.websocket.onopen = function () {
            NotificationManager.success("SUCCESS", "Connected to websocket");
        };

        this.websocket.onmessage = function (event) {
            let parsers = JSON.parse(event.data);
            console.log("Parser:" + parsers);
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

            fetch('http://localhost:8080/ParserManager-rest/rest/parsers/', {
                method: 'POST',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(row)
            })
        }}/>
    }

    render() {
        console.log(this.state.parsers);
        const {match} = this.props;

        return <BootstrapTable data={this.state.parsers}
                               options={{
                                   noDataText: 'No unconfirmed parsers',
                                   onRowClick: (row, columnIndex) => {
                                       console.log(row);
                                       switch (columnIndex) {
                                           case 3:
                                               this.props.history.push("/ParserManager-react/restaurants/" + row.restaurant.googlePlaceID);
                                               break;
                                           case 4:
                                               let msg = JSON.stringify({id: row.id, action: 'CONFIRM'});
                                               this.websocket.send(msg);
                                               NotificationManager.info("INFO", "Parser was marked for confirmation", 3000);
                                               break;
                                       }
                                   },
                                   paginationSize: 1
                               }} pagination>
            <TableHeaderColumn width={150} dataField='id' isKey>Parser ID</TableHeaderColumn>
            <TableHeaderColumn width={150} dataField='xpath'
                               dataFormat={AllRestaurantsComponent.xpathFormater}>XPath</TableHeaderColumn>
            <TableHeaderColumn width={150} dataField='day'>Day</TableHeaderColumn>
            <TableHeaderColumn width={150} dataField='restaurant'
                               dataFormat={(cell) => {if(cell!==null){ return <div style={{pointerEvents: "none"}}>{cell.name}</div>}
                               else{ return "Restaurant is null" }
                               }}>Restaurant</TableHeaderColumn>
            <TableHeaderColumn width={150} dataField='confirmParser' dataFormat={(cell) => <div style={{pointerEvents: "none"}}>Approve parser</div>}
                               tdStyle={{cursor: 'pointer', width: 'inherit'}} tdAttr={{'class': 'btn-default'}}>Confirm
                Parser</TableHeaderColumn>
        </BootstrapTable>;
    }
}
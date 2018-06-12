import React, {Component} from 'react';
import {BootstrapTable, TableHeaderColumn} from "react-bootstrap-table"
import axios from 'axios'
import {NotificationManager} from "react-notifications"

export default class AllRestaurantsComponent extends Component {

    constructor(props) {
        super(props);

        this.state = {
            restaurants: [],
            isLoading: false,
            error: null
        };
        this.fetchRestaurantData();
    }

    fetchRestaurantData(){
        this.setState({isLoading: true});
        axios.get("http://localhost:8080/ParserManager-rest/rest/restaurants")
            .then(response => this.setState({restaurants: response.data, isLoading: false}))
            .catch(error => this.setState({error, isLoading: false}));
    }

    addRestaurant(row){
        console.log(row);
        axios.post('http://localhost:8080/ParserManager-rest/rest/restaurants/', row, {
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
            }
        }).then(response => {
            NotificationManager.success("INFO", "Restaurant was added", 3000);
            this.fetchRestaurantData();
        }).catch(error => {
            NotificationManager.error("ERROR", "Restaurant was not added due to " + error.toString(), 3000);
            this.setState({error, isLoading: false})
        });
    }

    googlePlaceIDvalidator(value){
        const response = { isValid: true, notification: { type: 'success', msg: '', title: '' } };
        if(!value){
            response.isValid = false;
            response.notification.type = 'error';
            response.notification.msg = 'Value must be inserted';
            response.notification.title = 'Requested Value';
        }else{
            for (let i = 0; i < this.state.restaurants.length; i++) {
                if(this.state.restaurants[i].googlePlaceID===value){
                    response.isValid = false;
                    response.notification.type = 'error';
                    response.notification.msg = 'Google ID is already in the DB';
                    response.notification.title = 'Requested Value';
                    return response;
                }
            }
        }
        return response;
    }

    notNullValidator(value) {
        const response = {isValid: true, notification: {type: 'success', msg: '', title: ''}};
        if (!value) {
            response.isValid = false;
            response.notification.type = 'error';
            response.notification.msg = 'Value must be inserted';
            response.notification.title = 'Requested Value';
        }
        return response
    }

    render(){
        const {match} = this.props;

        if (this.state.isLoading) {
            return <p>Loading ...</p>;
        }

        if (this.state.error) {
            return <p>{this.state.error.message}</p>;
        }
        return <BootstrapTable data={this.state.restaurants}
                               options={{
                                   noDataText: 'No restaurant in DB',
                                   onRowClick: (row, columnIndex) => {
                                       this.props.history.push("/restaurants/" + row.googlePlaceID);
                                   },
                                   onAddRow: this.addRestaurant.bind(this),
                                   paginationSize: 1
                               }} pagination insertRow={true}>
            <TableHeaderColumn width={250} dataField='googlePlaceID' editable={{validator: this.googlePlaceIDvalidator.bind(this)}} isKey>Google place ID</TableHeaderColumn>
            <TableHeaderColumn width={250} dataField='name' editable={{ validator: this.notNullValidator }} >Name</TableHeaderColumn>
            <TableHeaderColumn width={250} dataField='description'>Description</TableHeaderColumn>
        </BootstrapTable>;
    }
}
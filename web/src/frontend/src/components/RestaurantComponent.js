import React, {Component} from 'react';
import axios from 'axios'
import {NotificationManager} from "react-notifications"
import {BootstrapTable, TableHeaderColumn} from "react-bootstrap-table"
import UnconfirmedParsersComponent from './UnconfirmedParsersComponent';

export default class RestaurantComponent extends Component {

    constructor(props) {
        super(props);

        this.state = {
            restaurant: {googlePlaceID: this.props.match.params['googlePlaceID']},
            parsers: [],
            isLoading: false,
            error: null
        };

        this.fetchRestaurantData();
        this.fetchParserForRestaurant();
    }

    days = [ {
        value: 'MONDAY',
        text: 'MONDAY'
    }, {
        value: 'TUESDAY',
        text: 'TUESDAY'
    }, {
        value: 'WEDNESDAY',
        text: 'WEDNESDAY'
    }, {
        value: 'THURSDAY',
        text: 'THURSDAY'
    }, {
        value: 'FRIDAY',
        text: 'FRIDAY'
    } ];

    fetchRestaurantData() {
        this.setState({isLoading: true});

        axios.get("http://localhost:8080/ParserManager-rest/rest/restaurants/" + this.state.restaurant.googlePlaceID)
            .then(response => this.setState({restaurant: response.data, isLoading: false}))
            .catch(error => this.setState({error, isLoading: false}));
    }

    fetchParserForRestaurant(){
        this.setState({isLoading: true});

        axios.get("http://localhost:8080/ParserManager-rest/rest/restaurants/" + this.state.restaurant.googlePlaceID + "/parsers")
            .then(response => this.setState({parsers: response.data, isLoading: false}))
            .catch(error => this.setState({error, isLoading: false}));
    }

    insertRestaurant() {
        this.setState({isLoading: true});

        let restaurant = this.state.restaurant;

        const data = {"name": restaurant.name, "description": restaurant.description, "googlePlaceID": restaurant.googlePlaceID};

        axios.put("http://localhost:8080/ParserManager-rest/rest/restaurants", data)
            .then(response => {
                this.setState({restaurant: response.data, isLoading: false});
                NotificationManager.success("INFO", "Restaurant was updated", 3000);
            })
            .catch(error => this.setState({error, isLoading: false}));
    }

    addParser(row){
        row.restaurant = this.state.restaurant;
        row.id = null;
        console.log(row);
        axios.post('http://localhost:8080/ParserManager-rest/rest/parsers/', row, {
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
            }
        }).then(response => {
            this.fetchParserForRestaurant();
            NotificationManager.success("INFO", "Parser was added", 3000);
        });
    }

    notNullValidator(value) {
        const response = {isValid: true, notification: {type: 'success', msg: '', title: ''}};
        if (!value) {
            response.isValid = false;
            response.notification.type = 'error';
            response.notification.msg = 'Xpath must be inserted';
            response.notification.title = 'Requested Value';
        }
        return response
    }

    render() {
        if (this.state.isLoading) {

            return <p>Loading ...</p>;
        }
        if (this.state.error) {

            return <p>{this.state.error.message}</p>;
        }
        let restaurant = this.state.restaurant;

        return <div>
            <form>
            <div>{this.googlePlaceID}</div>
            <label htmlFor={"name"}>Name</label>

            <input type={"text"} id={"name"}
                   value={this.state.restaurant.name}
                   onChange={(e) => {
                       restaurant.name = e.target.value;
                       this.setState({restaurant})
                   }}
            />

            <label htmlFor={"description"}>Description</label>

            <input type={"text"} id={"description"}
                   value={this.state.restaurant.description}
                   onChange={(e) => {
                       restaurant.description = e.target.value;
                       this.setState({restaurant});
                   }}/>

            <input type={"submit"}
                   value={"Send"}
                   onClick={(e) => {
                       e.preventDefault();
                       NotificationManager.info("INFO", "Updating restaurant", 3000);
                       this.insertRestaurant();
                   }}
            />
        </form>
            <BootstrapTable data={this.state.parsers}
                            options={{
                                noDataText: 'No unconfirmed parsers',
                                paginationSize: 1,
                                onAddRow: this.addParser.bind(this)
                            }} pagination insertRow={true}>
                <TableHeaderColumn width={150} dataField='id' editable={false} isKey>Parser ID</TableHeaderColumn>
                <TableHeaderColumn width={150} dataField='xpath'
                                   dataFormat={UnconfirmedParsersComponent.xpathFormater}
                                   editable={{validator: this.notNullValidator}}>XPath</TableHeaderColumn>
                <TableHeaderColumn width={150} dataField='day'
                                   editable={{type:'select', options:{values:this.days}}}>Day</TableHeaderColumn>
            </BootstrapTable>
        </div>

    }

}
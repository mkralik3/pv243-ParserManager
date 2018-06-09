import React, {Component} from 'react';
import {Link} from "react-router-dom";
import axios from 'axios'


export default class AllRestaurantsComponent extends Component {

    constructor(props) {
        super(props);

        this.state = {
            restaurants: [],
            isLoading: false,
            error: null
        };
    }

    componentDidMount() {
        this.setState({isLoading: true});

        axios.get("http://localhost:8080/ParserManager-rest/rest/restaurants")
            .then(function (response) {
                this.setState({restaurants: response.data, isLoading: false})
            }.bind(this))
            .catch(function (error) {
                this.setState({error, isLoading: false})
            }.bind(this));


    }

    render(){
        const {match} = this.props;

        if (this.state.isLoading) {
            return <p>Loading ...</p>;
        }

        if (this.state.error) {
            return <p>{this.state.error.message}</p>;
        }

        let restaurants;

        if (this.state.restaurants.length) {
            restaurants = this.state.restaurants.map(
                (obj) => (<Link className="list-group-item list-group-item-action" key={obj.googlePlaceID}
                                to={match.url.replace(/\/$/, "") + "/" + obj.googlePlaceID}>{obj.googlePlaceID}</Link>)
            );
        } else {
            restaurants = <h1>No Restaurant in DB</h1>;
        }


        return <div className="list-group">
            {restaurants}
        </div>
    }
}
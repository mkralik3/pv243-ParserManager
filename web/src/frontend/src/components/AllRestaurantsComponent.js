import React, {Component} from 'react';
import {Link} from "react-router-dom";

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

        fetch("http://localhost:8080/ParserManager-rest/rest/restaurants")
            .then(response => {
                if (response.ok) {
                    return response.json();
                } else {
                    console.log("Not able to download data");
                }
            })
            .then(data => {
                this.setState({restaurants: data, isLoading: false})
            })
            .catch(error => this.setState({error, isLoading: false}));
    }

    render() {
        if (this.state.isLoading) {
            return <p>Loading ...</p>;
        }

        let restaurants;

        if (this.state.restaurants.length) {
            restaurants = this.state.restaurants.map(
                (obj) => (<Link to={`/ParserManager-react/restaurant/${obj.googlePlaceID}`}>{obj.googlePlaceID}</Link>)
            );
        } else {
            restaurants = <h1>Working</h1>;
        }


        console.log(restaurants);
        return <div>
            {restaurants}
        </div>
    }
}
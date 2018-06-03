import React, {Component} from 'react';
import { toast } from 'react-toastify';

export default class RestaurantComponent extends Component {
    toastId = null;

    notify = () => this.toastId = toast("Updating restaurant", { autoClose: false });
    update = () => toast.update(this.toastId, {render: "Restauraunt was updated", type: toast.TYPE.INFO, autoClose: 5000 });

    constructor(props) {
        super(props);

        this.state = {
            restaurant: {googlePlaceID: this.props.match.params['googlePlaceID']},
            isLoading: false,
            error: null
        };

        this.fetchRestaurantData();
    }

    fetchRestaurantData() {
        this.setState({isLoading: true});

        fetch("http://localhost:8080/ParserManager-rest/rest/restaurants/" + this.state.restaurant.googlePlaceID)
            .then(response => {
                if (response.ok) {
                    return response.json();
                } else {
                    throw new Error('Something went wrong ...');
                }
            })
            .then(restaurant => this.setState({restaurant, isLoading: false}))
            .catch(error => this.setState({error, isLoading: false}));
    }

    insertRestaurant() {
        this.setState({isLoading: true});

        let restaurant = this.state.restaurant;

        const settings = {
            crossDomain: true,
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Cache-Control": "no-cache"
            },
            body: `{"name":"${restaurant.name}", "description":"${restaurant.description}", "googlePlaceID": "${restaurant.googlePlaceID}"}`
        };

        fetch("http://localhost:8080/ParserManager-rest/rest/restaurants", settings)
            .then(response => {
                if (response.ok) {
                    return response.json();
                } else {
                    throw new Error('Something went wrong ...');
                }
            })
            .then(restaurant => {
                this.setState({restaurant, isLoading: false});
                this.update();
            })
            .catch(error => this.setState({error, isLoading: false}));
    }

    render() {
        if (this.state.isLoading) {

            return <p>Loading ...</p>;
        }
        if (this.state.error) {

            return <p>{this.state.error.message}</p>;
        }
        let restaurant = this.state.restaurant;

        return <form>
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
                       this.notify();
                       this.insertRestaurant();
                   }}
            />
        </form>
    }
}
import React, { Component } from 'react';

export default class RestaurantComponent extends Component {

    name;
    goodleId;
    text;

    constructor(props) {
        super(props);

        this.state = {
            text: "ahoj"
        };

        this.name = "";
        this.goodleId = 5;
        this.text = "";
    }

    insertRestaurant() {
        const settings = {
            crossDomain: true,
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Cache-Control": "no-cache"
            },
            body: `{"name":"${this.name}", "description":"${this.text}", "googlePlaceID":${parseInt(this.goodleId)}}`
        };

        fetch("http://localhost:8080/ParserManager/rest/restaurants", settings)
            .then(response => console.log(response)).catch(reason => console.log(reason));
    }

    render() {
        return <form>
            <label htmlFor={"name"}>Name</label>
            <input type={"text"} id={"name"} onChange={(e) => this.name = e.target.value} />
            <label htmlFor={"description"}>Name</label>
            <input type={"text"} id={"description"} onChange={(e) => this.text = e.target.value}/>
            <input type={"submit"} value={"Send"} onClick={(e) => { e.preventDefault(); this.insertRestaurant();}} />
        </form>
    }
}
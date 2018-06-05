package cz.fi.muni.pv243.entity;


import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import java.io.Serializable;

@Entity
@Table(name = "PARSER"/*, uniqueConstraints = {
        @UniqueConstraint(columnNames = {"RESTAURANT_ID", "DAY", "CONFIRMED"})}*/)
@NamedQueries({
    @NamedQuery(name= "findConfirmedParserForRestaurantAndDay", 
            query="SELECT p FROM Parser p WHERE p.restaurant.googlePlaceID = :restaurantId AND p.day = :day AND p.confirmed = true"),
    @NamedQuery(name= "findConfirmedParsers", 
    query="SELECT p FROM Parser p WHERE p.confirmed = true"),
    @NamedQuery(name= "findUnconfirmedParsers", 
    query="SELECT p FROM Parser p WHERE p.confirmed = false"),
})
public class Parser implements Serializable {

    private static final long serialVersionUID = 188164481825309731L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name="RESTAURANT_ID")
    private Restaurant restaurant;

    private String xpath;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getXpath() {
        return xpath;
    }

    public void setXpath(String xpath) {
        this.xpath = xpath;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    @Override
    public String toString() {
        return "Parser{" +
                "id='" + id + '\'' +
                "xpath='" + xpath + '\'' +
                '}';
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "DAY")
    private Day day;

    public Day getDay() {
        return day;
    }

    public void setDay(Day day) {
        this.day = day;
    }

    @Column(name = "CONFIRMED", nullable = false)
    private boolean confirmed = false;

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }
}

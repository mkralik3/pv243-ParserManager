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
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

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

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name="RESTAURANT_ID")
    @NotNull
    @Valid //valid object in the object in the rest layer
    private Restaurant restaurant;

    @NotNull
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Parser)) return false;

        Parser parser = (Parser) o;

        if (getId() != null ? !getId().equals(parser.getId()) : parser.getId() != null) return false;
        if (getRestaurant() != null ? !getRestaurant().equals(parser.getRestaurant()) : parser.getRestaurant() != null)
            return false;
        return getXpath() != null ? getXpath().equals(parser.getXpath()) : parser.getXpath() == null;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getRestaurant() != null ? getRestaurant().hashCode() : 0);
        result = 31 * result + (getXpath() != null ? getXpath().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Parser{" +
                "id=" + id +
                ", restaurant=" + ((restaurant==null) ? null : restaurant.getName()) +
                ", xpath='" + xpath + '\'' +
                ", day=" + day +
                ", confirmed=" + confirmed +
                '}';
    }
}

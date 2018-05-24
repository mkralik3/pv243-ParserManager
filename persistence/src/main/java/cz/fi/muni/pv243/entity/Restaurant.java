package cz.fi.muni.pv243.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import cz.fi.muni.pv243.entity.food.RestaurantWeekData;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "RESTAURANT")
public class Restaurant {

    @Id
    private String googlePlaceID;

    private String name;

    private String description;

    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "restaurant", orphanRemoval=true)
    private List<RestaurantWeekData> menuForWeeks;

    public String getGooglePlaceID() {
        return googlePlaceID;
    }

    public void setGooglePlaceID(String googlePlaceID) {
        this.googlePlaceID = googlePlaceID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<RestaurantWeekData> getMenuForWeeks() {
        return menuForWeeks;
    }

    public void setMenuForWeeks(List<RestaurantWeekData> menuForWeeks) {
        this.menuForWeeks = menuForWeeks;
    }

    public void addMenuForWeek(RestaurantWeekData weekData) {
        this.menuForWeeks.add(weekData);
    }
}

package cz.fi.muni.pv243.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import cz.fi.muni.pv243.entity.food.RestaurantWeekData;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "RESTAURANT")
public class Restaurant {

    @Id
    @NotNull
    private String googlePlaceID;

    @NotNull
    @Size(min = 1, max = 50)
    private String name;

    @Size(max=500)
    private String description;

    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "restaurant", orphanRemoval=true)
    private Set<RestaurantWeekData> menuForWeeks = new HashSet<>();

    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "restaurant", orphanRemoval=true)
    private Set<Parser> parsers = new HashSet<>();

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

    public Set<RestaurantWeekData> getMenuForWeeks() {
        return menuForWeeks;
    }

    public void setMenuForWeeks(Set<RestaurantWeekData> menuForWeeks) {
        this.menuForWeeks = menuForWeeks;
    }

    public void addMenuForWeek(RestaurantWeekData weekData) {
        this.menuForWeeks.add(weekData);
    }

	public Set<Parser> getParsers() {
		return parsers;
	}

	public void setParsers(Set<Parser> parsers) {
		this.parsers = parsers;
	}

    public void addParser(Parser parser) {
        this.parsers.add(parser);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Restaurant)) return false;

        Restaurant that = (Restaurant) o;

        if (getGooglePlaceID() != null ? !getGooglePlaceID().equals(that.getGooglePlaceID()) : that.getGooglePlaceID() != null)
            return false;
        return getName() != null ? getName().equals(that.getName()) : that.getName() == null;
    }

    @Override
    public int hashCode() {
        int result = getGooglePlaceID() != null ? getGooglePlaceID().hashCode() : 0;
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "googlePlaceID='" + googlePlaceID + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", menuForWeeks=" + menuForWeeks +
                ", parsers=" + parsers +
                '}';
    }
}

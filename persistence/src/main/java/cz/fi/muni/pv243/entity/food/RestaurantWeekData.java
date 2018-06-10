package cz.fi.muni.pv243.entity.food;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import cz.fi.muni.pv243.entity.Restaurant;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "RESTAURANT_WEEK_DATA", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"WEEK_NUMBER", "RESTAURANT_ID"})
})
@NamedQueries({
        @NamedQuery(name= "findWeekDataByRestaurandIDAndWeekNumber",
                query="SELECT r FROM RestaurantWeekData r WHERE r.restaurant.googlePlaceID = :googleID AND weekNumber = :weekNumber"),
})
public class RestaurantWeekData {

    @Id
    @Column(name = "RESTAURANT_WEEK_DATA_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private long id;

    private boolean soupIncludedInPrice;

    @Column(name = "WEEK_NUMBER")
    private int weekNumber;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name="RESTAURANT_ID")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Restaurant restaurant;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "FK_WEEK_DATA_ID")
    private Set<RestaurantDailyData> menuForDays = new HashSet<>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isSoupIncludedInPrice() {
        return soupIncludedInPrice;
    }

    public void setSoupIncludedInPrice(boolean soupIncludedInPrice) {
        this.soupIncludedInPrice = soupIncludedInPrice;
    }

    public int getWeekNumber() {
        return weekNumber;
    }

    public void setWeekNumber(int weekNumber) {
        this.weekNumber = weekNumber;
    }

    public Set<RestaurantDailyData> getMenuForDays() {
        return menuForDays;
    }

    public void setMenuForDays(Set<RestaurantDailyData> menuForDays) {
        this.menuForDays = menuForDays;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public void addMenuForDay(RestaurantDailyData dailyData) {
        this.menuForDays.add(dailyData);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RestaurantWeekData)) return false;

        RestaurantWeekData that = (RestaurantWeekData) o;

        if (isSoupIncludedInPrice() != that.isSoupIncludedInPrice()) return false;
        if (getWeekNumber() != that.getWeekNumber()) return false;
        return getRestaurant() != null ? getRestaurant().equals(that.getRestaurant()) : that.getRestaurant() == null;
    }

    @Override
    public int hashCode() {
        int result = (isSoupIncludedInPrice() ? 1 : 0);
        result = 31 * result + getWeekNumber();
        result = 31 * result + (getRestaurant() != null ? getRestaurant().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "RestaurantWeekData{" +
                "id=" + id +
                ", soupIncludedInPrice=" + soupIncludedInPrice +
                ", weekNumber=" + weekNumber +
                ", restaurant=" + ((restaurant==null) ? null : restaurant.getName()) +
                ", menuForDays=" + menuForDays +
                '}';
    }
}

package cz.fi.muni.pv243.entity.food;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "RESTAURANT_DAILY_DATA")
public class RestaurantDailyData {

    @Id
    @Column(name = "RESTAURANT_DAILY_DATA_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private long id;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "FK_DAILY_DATA_ID_MENU")
    private Set<FoodEntity> menu = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "FK_DAILY_DATA_ID_SOUP")
    private Set<FoodEntity> soup = new HashSet<>();


    public Set<FoodEntity> getMenu() {
        return menu;
    }

    public void setMenu(Set<FoodEntity> menu) {
        this.menu = menu;
    }

    public Set<FoodEntity> getSoup() {
        return soup;
    }

    public void setSoup(Set<FoodEntity> soup) {
        this.soup = soup;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void addMenuToDay(FoodEntity menu) {
        this.menu.add(menu);
    }

    public void addSoupToDay(FoodEntity soup) {
        this.soup.add(soup);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RestaurantDailyData)) return false;

        RestaurantDailyData that = (RestaurantDailyData) o;

        if (getMenu() != null ? !getMenu().equals(that.getMenu()) : that.getMenu() != null) return false;
        return getSoup() != null ? getSoup().equals(that.getSoup()) : that.getSoup() == null;
    }

    @Override
    public int hashCode() {
        int result = getMenu() != null ? getMenu().hashCode() : 0;
        result = 31 * result + (getSoup() != null ? getSoup().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "RestaurantDailyData{" +
                "id=" + id +
                ", menu=" + menu +
                ", soup=" + soup +
                '}';
    }
}

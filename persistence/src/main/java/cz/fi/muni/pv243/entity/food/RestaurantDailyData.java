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
    private Set<FoodEntity> menu;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "FK_DAILY_DATA_ID_SOUP")
    private Set<FoodEntity> soup;


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


}

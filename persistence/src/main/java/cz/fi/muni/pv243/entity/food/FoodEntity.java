package cz.fi.muni.pv243.entity.food;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Table(name = "FOOD_ENTITY")
public class FoodEntity {

    @Id
    @Column(name = "FOOD_ENTITY_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private long id;

    private String name;

    private Integer price;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "INGREDIENTS", joinColumns = @JoinColumn(name = "FK_FOOD_ENTITY_ID"))
    private Set<Ingredient> tags;

    public FoodEntity() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Set<Ingredient> getTags() {
        return tags;
    }

    public void setTags(Set<Ingredient> tags) {
        this.tags = tags;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}

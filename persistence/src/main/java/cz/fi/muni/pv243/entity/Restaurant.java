package cz.fi.muni.pv243.entity;

import javax.persistence.*;

@Entity
@Table(name = "RESTAURANT")
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String googlePlaceID;

    private String name;

    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
}

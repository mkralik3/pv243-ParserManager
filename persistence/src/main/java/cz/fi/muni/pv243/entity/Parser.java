package cz.fi.muni.pv243.entity;


import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import java.io.Serializable;

@Entity
@Table(name = "PARSER", uniqueConstraints = {
		@UniqueConstraint(columnNames = {"CONFIRMED"})})
public class Parser implements Serializable {

    private static final long serialVersionUID = 188164481825309731L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name="RESTAURANT_ID")
    @Column(nullable = false)
    Restaurant restaurant;

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
                "xpath='" + xpath + '\'' +
                '}';
    }
    
	@Column(name = "CONFIRMED", nullable = true)
    private String confirmed;

	public boolean isConfirmed() {
		return confirmed == null ? false : true;
	}

	public void setConfirmed(boolean confirmed) {
		if (confirmed)
			this.confirmed = String.valueOf(restaurant.getGooglePlaceID());
		else
			this.confirmed = null;
	}
    
}

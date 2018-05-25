package cz.fi.muni.pv243.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * @author Michaela Bocanova
 *
 */
@Entity
@Table(name = "PARSERCONFIGURATION")
public class ParserConfiguration implements Serializable {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	private Long restaurantId;
    private String xpath;
    private boolean confirmed = false;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getRestaurantId() {
		return restaurantId;
	}

	public void setRestaurantId(Long parserId) {
		this.restaurantId = parserId;
	}

	public boolean isConfirmed() {
		return confirmed;
	}

	public void setConfirmed(boolean confirmed) {
		this.confirmed = confirmed;
	}

	public String getXpath() {
		return xpath;
	}

	public void setXpath(String xpath) {
		this.xpath = xpath;
	}

	@Override
	public String toString() {
		return "ParserConfiguration [id=" + id + ", restaurantId=" + restaurantId + ", xpath=" + xpath + ", confirmed="
				+ confirmed + "]";
	}
	
}

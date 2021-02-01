package api.entity;

import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name="PrayagRaj")
public class LocationInPrayagRaj{
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="LocationId")
	private int LocationId;
	@Column(name="LocationName")
	private String LocationName;
	@Column(name="longitude")
    private double longitude;
	@Column(name="latitude")
    private double latitude; 

	@Override
	public int hashCode() {
		return Objects.hash(LocationId, LocationName, NumberOfCrimes, latitude, longitude);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof LocationInPrayagRaj))
			return false;
		LocationInPrayagRaj other = (LocationInPrayagRaj) obj;
		return LocationId == other.LocationId && Objects.equals(LocationName, other.LocationName)
				&& NumberOfCrimes == other.NumberOfCrimes
				&& Double.doubleToLongBits(latitude) == Double.doubleToLongBits(other.latitude)
				&& Double.doubleToLongBits(longitude) == Double.doubleToLongBits(other.longitude);
	}
	@Column(name="NumberOfCrimes")
	private int NumberOfCrimes;
	public int getLocationId() {
		return LocationId;
	}
	public void setLocationId(int locationId) {
		LocationId = locationId;
	}
	public String getLocationName() {
		return LocationName;
	}
	public void setLocationName(String locationName) {
		LocationName = locationName;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public int getNumberOfCrimes() {
		return NumberOfCrimes;
	}
	@Override
	public String toString() {
		return "LocationInPrayagRaj [LocationId=" + LocationId + ", LocationName=" + LocationName + ", longitude="
				+ longitude + ", latitude=" + latitude + ", NumberOfCrimes=" + NumberOfCrimes + "]";
	}
	public void setNumberOfCrimes(int numberOfCrimes) {
		NumberOfCrimes = numberOfCrimes;
	}

}

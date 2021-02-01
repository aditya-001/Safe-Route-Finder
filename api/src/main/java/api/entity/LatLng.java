package api.entity;

import java.util.Objects;

public class LatLng {

	public double Latitude;
	public double Longitude;
	
	public LatLng(double Latitude,double Longitude)
	{
		this.Latitude=Latitude;
		this.Longitude=Longitude;
	}

	@Override
	public int hashCode() {
		return Objects.hash(Latitude, Longitude);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof LatLng))
			return false;
		LatLng other = (LatLng) obj;
		return Double.doubleToLongBits(Latitude) == Double.doubleToLongBits(other.Latitude)
				&& Double.doubleToLongBits(Longitude) == Double.doubleToLongBits(other.Longitude);
	}
	
}

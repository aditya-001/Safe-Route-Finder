package api.rest;

import java.util.ArrayList;
import java.util.Set;

import api.entity.LocationInPrayagRaj;

public class ObjectToSend {
	Set<LocationInPrayagRaj> points;
	ArrayList<Integer> numofcrimes;
	public Set<LocationInPrayagRaj> getPoints() {
		return points;
	}
	public void setPoints(Set<LocationInPrayagRaj> points) {
		this.points = points;
	}
	public ArrayList<Integer> getNumofcrimes() {
		return numofcrimes;
	}
	public void setNumofcrimes(ArrayList<Integer> numofcrimes) {
		this.numofcrimes = numofcrimes;
	}

}

package api.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="UttarPradesh")
public class DistrictInUttarPradesh {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="DistrictID")
	private int DistrictID;
	@Column(name="DistrictName")
	private String DistrictName;
	public int getDistrictID() {
		return DistrictID;
	}
	public void setDistrictID(int districtID) {
		DistrictID = districtID;
	}
	public String getDistrictName() {
		return DistrictName;
	}
	public void setDistrictName(String districtName) {
		DistrictName = districtName;
	}

}

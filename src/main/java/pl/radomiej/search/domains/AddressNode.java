package pl.radomiej.search.domains;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

//@Entity
@Document(indexName = "address", type = "AddressNode", shards = 1, replicas = 0)
public class AddressNode {
	private static long sid;
	@Id
	private long id;
	private GeoPoint position;
	private String country, voivodeship, counties, gmine, city, postcode, street, houseNumber;

	public AddressNode() {
		id = sid++;
	}

	public static long getSid() {
		return sid;
	}

	public static void setSid(long sid) {
		AddressNode.sid = sid;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public GeoPoint getPosition() {
		return position;
	}

	public void setPosition(GeoPoint position) {
		this.position = position;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getVoivodeship() {
		return voivodeship;
	}

	public void setVoivodeship(String voivodeship) {
		this.voivodeship = voivodeship;
	}

	public String getCounties() {
		return counties;
	}

	public void setCounties(String counties) {
		this.counties = counties;
	}

	public String getGmine() {
		return gmine;
	}

	public void setGmine(String gmine) {
		this.gmine = gmine;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getHouseNumber() {
		return houseNumber;
	}

	public void setHouseNumber(String houseNumber) {
		this.houseNumber = houseNumber;
	}

	@Override
	public String toString() {
		return "AddressNode [id=" + id + ", position=[" + position.getLat() + "," + position.getLon() + "], country=" + country + ", voivodeship="
				+ voivodeship + ", counties=" + counties + ", gmine=" + gmine + ", city=" + city + ", postcode="
				+ postcode + ", street=" + street + ", houseNumber=" + houseNumber + "]";
	}

}

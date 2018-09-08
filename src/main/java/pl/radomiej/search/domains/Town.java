package pl.radomiej.search.domains;

/**
 * Centralny zbiór miejscowości w bazie CODIK nie związany bezpośrednio z TERYC
 * @author Radomiej
 *
 */
//@Entity
//@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "terytId"}) )
public class Town {
//	@Id
//	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private Long terytId;
	private double latitude, longitude;
//	@Column(columnDefinition = "TEXT")
	private String name, codikId;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Long getTerytId() {
		return terytId;
	}

	public void setTerytId(Long terytId) {
		this.terytId = terytId;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCodikId() {
		return codikId;
	}

	public void setCodikId(String codikId) {
		this.codikId = codikId;
	}
}

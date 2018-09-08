package pl.radomiej.search.domains;


//@Entity
//@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "sym" }) )
public class Simc {
//	@Id
//	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private String wojewodztwo, powiat, gmina, gmina_rodzaj, rm, mz;
//	@Column(columnDefinition = "TEXT")
	private String sym, sympod;
//	@Column(columnDefinition = "TEXT")
	private String date, nazwa;
	
	public String getMz() {
		return mz;
	}

	public void setMz(String mz) {
		this.mz = mz;
	}

	public String getWojewodztwo() {
		return wojewodztwo;
	}

	public void setWojewodztwo(String wojewodztwo) {
		this.wojewodztwo = wojewodztwo;
	}

	public String getPowiat() {
		return powiat;
	}

	public void setPowiat(String powiat) {
		this.powiat = powiat;
	}

	public String getGmina() {
		return gmina;
	}

	public void setGmina(String gmina) {
		this.gmina = gmina;
	}

	public String getGmina_rodzaj() {
		return gmina_rodzaj;
	}

	public void setGmina_rodzaj(String gmina_rodzaj) {
		this.gmina_rodzaj = gmina_rodzaj;
	}



	@Override
	public String toString() {
		return "Simc [wojewodztwo=" + wojewodztwo + ", powiat=" + powiat + ", gmina=" + gmina + ", gmina_rodzaj="
				+ gmina_rodzaj + ", sym=" + sym + ", sympod=" + sympod + ", date=" + date + ", nazwa=" + nazwa + "]";
	}

	public String getNazwa() {
		return nazwa;
	}

	public void setNazwa(String nazwa) {
		this.nazwa = nazwa;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getSym() {
		return sym;
	}

	public void setSym(String sym) {
		this.sym = sym;
	}

	public String getSympod() {
		return sympod;
	}

	public void setSympod(String sympod) {
		this.sympod = sympod;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getRm() {
		return rm;
	}

	public void setRm(String rm) {
		this.rm = rm;
	}
}

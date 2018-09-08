package pl.radomiej.search.domains;


//@Entity
public class Teryt {
//	@Id
//	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

//	@Column(columnDefinition = "TEXT")
	private String wojewodztwo, powiat, gmina, rodzaj, nazwa, nazwaDodatkowa, stanNa;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public String getRodzaj() {
		return rodzaj;
	}

	public void setRodzaj(String rodzaj) {
		this.rodzaj = rodzaj;
	}

	public String getNazwa() {
		return nazwa;
	}

	public void setNazwa(String nazwa) {
		this.nazwa = nazwa;
	}

	public String getNazwaDodatkowa() {
		return nazwaDodatkowa;
	}

	public void setNazwaDodatkowa(String nazwaDodatkowa) {
		this.nazwaDodatkowa = nazwaDodatkowa;
	}

	public String getStanNa() {
		return stanNa;
	}

	public void setStanNa(String stanNa) {
		this.stanNa = stanNa;
	}

}

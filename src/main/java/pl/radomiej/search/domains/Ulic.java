package pl.radomiej.search.domains;


//@Entity
//@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "sym" }) )
public class Ulic {
//	@Id
//	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

//	@Column(columnDefinition = "TEXT")
	private String sym, sym_ul;
//	@Column(columnDefinition = "TEXT")
	private String cecha, nazwa1, nazwa2, woj, pow, gmina, rodzajGminy, stanNa;

	public String getWoj() {
		return woj;
	}

	public void setWoj(String woj) {
		this.woj = woj;
	}

	public String getPow() {
		return pow;
	}

	public void setPow(String pow) {
		this.pow = pow;
	}


	public String getRodzajGminy() {
		return rodzajGminy;
	}

	public void setRodzajGminy(String rodzajGminy) {
		this.rodzajGminy = rodzajGminy;
	}

	public String getFullName() {
		return cecha + nazwa2 + nazwa1;
	}

	@Override
	public String toString() {
		return "Ulic [sym=" + sym + ", sym_ul=" + sym_ul + ", cecha=" + cecha + ", nazwa1=" + nazwa1 + ", nazwa2="
				+ nazwa2 + "]";
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

	public String getSym_ul() {
		return sym_ul;
	}

	public void setSym_ul(String sym_ul) {
		this.sym_ul = sym_ul;
	}

	public String getCecha() {
		return cecha;
	}

	public void setCecha(String cecha) {
		this.cecha = cecha;
	}

	public String getNazwa1() {
		return nazwa1;
	}

	public void setNazwa1(String nazwa1) {
		this.nazwa1 = nazwa1;
	}

	public String getNazwa2() {
		return nazwa2;
	}

	public void setNazwa2(String nazwa2) {
		this.nazwa2 = nazwa2;
	}

	public String getStanNa() {
		return stanNa;
	}

	public void setStanNa(String stanNa) {
		this.stanNa = stanNa;
	}

	public String getGmina() {
		return gmina;
	}

	public void setGmina(String gmina) {
		this.gmina = gmina;
	}
}

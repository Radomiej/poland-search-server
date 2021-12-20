package pl.radomiej.search.services.imports.handlers;

import org.springframework.stereotype.Service;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import pl.radomiej.search.domains.CodgikHouseNumberNode;
import pl.radomiej.search.services.imports.states.StateActionStreet;


@Service
public class CodikStreetHandler extends DefaultHandler{

	private String normalize = "";
	private StateActionStreet currentState = StateActionStreet.NONE;
	private CodgikHouseNumberNode node;
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
//		if (qName.equals("prg-ad:PRG_UlicaNazwa")) {
//			node = new CodgikHouseNumberNode();
//		} else if (qName.equals("prg-ad:jednostkaAdmnistracyjna")) {
//			currentState = StateActionStreet.ADMINISTRACJA;
//		} else if (qName.equals("prg-ad:miejscowosc")) {
//			currentState = StateActionStreet.MIEJSCOWOSC;
//		} else if (qName.equals("prg-ad:czescMiejscowosci")) {
//			currentState = StateActionStreet.CZESC_MIEJSCOWOSC;
//		} else if (qName.equals("prg-ad:ulica")) {
//			currentState = StateActionStreet.ULICA;
//		} else if (qName.equals("prg-ad:numerPorzadkowy")) {
//			currentState = StateActionStreet.NUMER_PORZADKOWY;
//		} else if (qName.equals("prg-ad:kodPocztowy")) {
//			currentState = StateActionStreet.KOD_POCZTOWY;
//		}else if (qName.equals("gml:pos")) {
//			currentState = StateActionStreet.POZYCJA;
//		}
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		String value = new String(ch, start, length);
		normalize += value;
//		System.out.println(this.qName + " Value : " + new String(ch, start, length));
		
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		normalize = normalize.trim();
//		currentState.procces(node, normalize);
		
		normalize = "";
		currentState = StateActionStreet.NONE;
		if (qName.equals("prg-ad:PRG_UlicaNazwa")) {
			persistAddressNode();
		}	
	}

	private void persistAddressNode() {
	}
	
	public void reset(){
		normalize = "";
		currentState = StateActionStreet.NONE;
		node = null;
	}
}

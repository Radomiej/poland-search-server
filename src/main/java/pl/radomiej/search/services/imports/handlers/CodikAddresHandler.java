package pl.radomiej.search.services.imports.handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import pl.radomiej.search.domains.AddressNode;
import pl.radomiej.search.domains.ImportResult;
import pl.radomiej.search.elasticsearch.repositories.HouseElsticsearchRepository;
import pl.radomiej.search.services.imports.states.StateActionAddress;


@Service
public class CodikAddresHandler extends DefaultHandler {
    @Autowired
    private HouseElsticsearchRepository codikRepository;

    private String normalize = "";
    private StateActionAddress currentState = StateActionAddress.NONE;
    private AddressNode node;
    private ImportResult result = new ImportResult();
    private List<AddressNode> resultNodes = new ArrayList<>();

    private ExecutorService executor = Executors.newFixedThreadPool(20);


    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equals("prg-ad:PRG_PunktAdresowy")) {
            node = new AddressNode();
        } else if (qName.equals("prg-ad:jednostkaAdmnistracyjna")) {
            currentState = StateActionAddress.ADMINISTRACJA;
        } else if (qName.equals("prg-ad:miejscowosc")) {
            currentState = StateActionAddress.MIEJSCOWOSC;
        } else if (qName.equals("prg-ad:czescMiejscowosci")) {
            currentState = StateActionAddress.CZESC_MIEJSCOWOSC;
        } else if (qName.equals("prg-ad:ulica")) {
            currentState = StateActionAddress.ULICA;
        } else if (qName.equals("prg-ad:numerPorzadkowy")) {
            currentState = StateActionAddress.NUMER_PORZADKOWY;
        } else if (qName.equals("prg-ad:kodPocztowy")) {
            currentState = StateActionAddress.KOD_POCZTOWY;
        } else if (qName.equals("gml:pos")) {
            currentState = StateActionAddress.POZYCJA;
        }
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
        currentState.procces(node, normalize);

        normalize = "";
        currentState = StateActionAddress.NONE;
        if (qName.equals("prg-ad:PRG_PunktAdresowy")) {
            result.incrasePersistResultNode();
            persistAddressNode();
        }
    }

    @Override
    public void endDocument() throws SAXException {
        storeNodesInDatabase();
    }

    private void persistAddressNode() {
        final AddressNode persistNode = node;
        resultNodes.add(persistNode);
        if (resultNodes.size() > 1000) storeNodesInDatabase();

    }

    private void storeNodesInDatabase() {
        final List snapshotNodes = resultNodes;
        resultNodes = new ArrayList<>();
        executor.submit(() -> save(snapshotNodes));
    }

    public void reset() {
        normalize = "";
        currentState = StateActionAddress.NONE;
        node = null;
        result = new ImportResult();
        resultNodes.clear();
    }

    public ImportResult getResult() {
        return result;
    }

    public void save(final List<AddressNode> persistNode) {
        long startIndex = persistNode.get(0).getId();
        long endIndex = persistNode.get(0).getId();
        System.out.println("Zapisuje: " + startIndex + " - " + endIndex);
        codikRepository.saveAll(persistNode);
    }

    public void save() {
        //codikRepository.saveAll(resultNodes);
    }
}

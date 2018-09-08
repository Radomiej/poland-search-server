package pl.radomiej.search.services;


import java.io.File;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.radomiej.search.domains.ImportResult;
import pl.radomiej.search.services.handlers.CodikAddresHandler;

@Service
public class ImporterService {
	@Autowired
	private CodikAddresHandler codikHandler;

	public ImportResult importAddresses(String path) {
		File inFile = new File(path);
		try {

			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
			saxParser.parse(inFile, codikHandler);
		} catch (Exception e) {
			e.printStackTrace();
		}
		codikHandler.save();
		ImportResult result = codikHandler.getResult();
		codikHandler.reset();
		return result;
	}
}

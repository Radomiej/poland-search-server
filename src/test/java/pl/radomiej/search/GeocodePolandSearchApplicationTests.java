package pl.radomiej.search;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.common.io.Files;

import pl.radomiej.search.controllers.ReverseGeocodingController;
import pl.radomiej.search.controllers.SearchController;
import pl.radomiej.search.domains.AddressNode;
import pl.radomiej.search.domains.SearchResult;
import pl.radomiej.search.elasticsearch.repositories.HouseElsticsearchRepository;
import pl.radomiej.search.services.imports.ImporterService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GeocodePolandSearchApplicationTests {

	@Autowired
	private ImporterService importService;
	
	@Autowired
	private SearchController searchController;

	@Autowired
	private ReverseGeocodingController reverseGeocodingController;

	@Autowired
	private HouseElsticsearchRepository houseRepo;
	
	@Autowired
	private ElasticsearchOperations elasticsearchTemplate;
	
//	@Test
	public void contextLoads() {
		List<String> files = new ArrayList<>();
		File root = new File("H:\\mapy\\punkty_adresowe");
		for (File file : root.listFiles()) {
			if (file.isDirectory())
				continue;
			if (Files.getFileExtension(file.getName()).equalsIgnoreCase("xml")) {
				files.add(file.getAbsolutePath());
			}
		}

		for (String path : files) {
			System.out.println("Wczytuje: " + path);
			importService.importAddresses(path);	
			System.out.println("Wczytano: " + path);
		}
		
//		importService.importAddresses("G:\\PunktyAdresowePolska\\2016_05_13_10_12_13__08_lubuskie.xml");
//		for(CodikHouseNumberNode2 house : houseRepo.findAll()){
//			System.out.println(house);
//		}
		
		contextLoads2();
	}
	
	@Test
	public void contextLoads2() {
		System.out.println("Szukam");
		Collection<SearchResult> results = searchController.search("lubuskie", "", "", "Gorzów", "", "Korcza", "33");
		System.out.println("Wyniki wyszukiwania: " + results.size());
		for(SearchResult sr : results){
			System.out.println(sr);
		}
	}
	
	@Test
	public void reverseGeocodeTest() {
		System.out.println("Szukam");
		SearchResult result = reverseGeocodingController.reverseSearch(52.87637051101798, 15.530304158160467);
		System.out.println("Wynik wyszukiwania: " + result);
		
	}
	
}

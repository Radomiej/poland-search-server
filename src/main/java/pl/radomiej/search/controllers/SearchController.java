package pl.radomiej.search.controllers;

import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.simpleQueryStringQuery;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pl.radomiej.search.domains.AddressNode;
import pl.radomiej.search.domains.SearchResult;
import pl.radomiej.search.elasticsearch.repositories.HouseElsticsearchRepository;

@RestController
@RequestMapping("/search")
public class SearchController {

	@Autowired
	private HouseElsticsearchRepository houseRepository;

	@Autowired
	private ElasticsearchOperations elasticsearchTemplate;

	@RequestMapping("")
	public Collection<SearchResult> search(@DefaultValue("") @QueryParam("voivodeship") String voivodeship,
			@QueryParam("counties") @DefaultValue("") String counties, @DefaultValue("") @QueryParam("gmine") String gmine,
			@DefaultValue("") @QueryParam("city") String city,
			@DefaultValue("") @QueryParam("postcode") String postcode, @QueryParam("street") String street,
			@QueryParam("houseNumber") String houseNumber) {

		System.out.println(voivodeship + counties + gmine + city + postcode + street + houseNumber);
		NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
		BoolQueryBuilder boolQueryBulider = boolQuery();
		builder.withQuery(boolQueryBulider);

		if (voivodeship != null && !voivodeship.equals("")) {
			QueryBuilder queryWojewodztwo = simpleQueryStringQuery(voivodeship).field("voivodeship");
			boolQueryBulider.must(queryWojewodztwo);
		}
		if (counties != null && !counties.equals("")) {
			QueryBuilder queryPowiat = simpleQueryStringQuery(counties).field("counties");
			boolQueryBulider.must(queryPowiat);
		}
		if (gmine != null && !gmine.equals("")) {
			QueryBuilder queryGmine = simpleQueryStringQuery(gmine).field("gmine");
			boolQueryBulider.must(queryGmine);
		}
		if (city != null && !city.equals("")) {
			QueryBuilder queryCity = simpleQueryStringQuery(city).field("city");
			boolQueryBulider.must(queryCity);
		}
		if (postcode != null && !postcode.equals("")) {
			QueryBuilder queryPostcode = simpleQueryStringQuery(postcode + "| 00-000").field("postcode");
			boolQueryBulider.must(queryPostcode);
		}
		if (street != null && !street.equals("")) {
			QueryBuilder queryStreet = simpleQueryStringQuery(street).field("street");
			boolQueryBulider.must(queryStreet);
		}
		
		

		QueryBuilder queryHouseNumber = simpleQueryStringQuery(houseNumber).field("houseNumber");
		boolQueryBulider.must(queryHouseNumber);

		SearchQuery searchQuery = builder.build();
		System.out.println("query: " + searchQuery.getQuery().toString());
		Page<AddressNode> housesResult = houseRepository.search(searchQuery);

		// Collection<CodikHouseNumberNode2> housesResult =
		// houseRepository.findByStreet(ulica);
		// Collection<CodgikHouseNumberNode> housesResult =
		// houseRepository.search(wojewodztwo, powiat, gmina, miasto,
		// kodPocztowy, ulica, numerDomu);

		Collection<SearchResult> results = new ArrayList<>();
		for (AddressNode house : housesResult) {
			SearchResult searchResult = new SearchResult();
			searchResult.setCity(house.getCity());
			searchResult.setVoivodeship(house.getVoivodeship());
			searchResult.setCountry("Polska");
			searchResult.setDivision(house.getCounties());
			searchResult.setSubdivision(house.getGmine());
			searchResult.setPostal(house.getPostcode());
			searchResult.setStreet(house.getStreet());
			searchResult.setHouseNumber(house.getHouseNumber());
			searchResult.setLatitude(house.getPosition().getLat());
			searchResult.setLongitude(house.getPosition().getLon());
			results.add(searchResult);
		}
		return results;
	}

	@RequestMapping("reverseGeocode/{latitude}/{longitude}/get")
	public SearchResult reverseSearch(@PathVariable("latitude") double latitude,
			@PathVariable("longitude") double longitude) {

		//TODO move to constructor and check that it work
		elasticsearchTemplate.createIndex(AddressNode.class);
		elasticsearchTemplate.refresh(AddressNode.class);
		elasticsearchTemplate.putMapping(AddressNode.class);

		CriteriaQuery geoLocationCriteriaQuery = new CriteriaQuery(
				new Criteria("position").within(new GeoPoint(latitude, longitude), "50m"));
		List<AddressNode> geoAuthorsForGeoCriteria = elasticsearchTemplate.queryForList(geoLocationCriteriaQuery,
				AddressNode.class);

		geoAuthorsForGeoCriteria = new ArrayList<>(geoAuthorsForGeoCriteria);
		geoAuthorsForGeoCriteria.sort(new Comparator<AddressNode>() {
			@Override
			public int compare(AddressNode o1, AddressNode o2) {
				double x0 = latitude;
				double y0 = longitude;
				double x1 = o1.getPosition().getLat();
				double y1 = o1.getPosition().getLon();
				double x2 = o2.getPosition().getLat();
				double y2 = o2.getPosition().getLon();

				double dx1 = x0 - x1;
				double dy1 = y0 - y1;
				double distance1 = Math.sqrt(Math.pow(dx1, 2) + Math.pow(dy1, 2));

				double dx2 = x0 - x2;
				double dy2 = y0 - y2;
				double distance2 = Math.sqrt(Math.pow(dx2, 2) + Math.pow(dy2, 2));
				return (int) ((distance1 - distance2) * 100000);
			}
		});

		System.out.println("Znalezione punkty: " + geoAuthorsForGeoCriteria.size());
		int i = 0;
		for (AddressNode house : geoAuthorsForGeoCriteria) {
			System.out.println(i++ + ": " + house);
		}

		AddressNode house = geoAuthorsForGeoCriteria.get(0);
		SearchResult searchResult = new SearchResult();
		searchResult.setCity(house.getCity());
		searchResult.setVoivodeship(house.getVoivodeship());
		searchResult.setCountry("Polska");
		searchResult.setDivision(house.getCounties());
		searchResult.setSubdivision(house.getGmine());
		searchResult.setPostal(house.getPostcode());
		searchResult.setStreet(house.getStreet());
		searchResult.setHouseNumber(house.getHouseNumber());
		searchResult.setLatitude(house.getPosition().getLat());
		searchResult.setLongitude(house.getPosition().getLon());

		return searchResult;
	}
}

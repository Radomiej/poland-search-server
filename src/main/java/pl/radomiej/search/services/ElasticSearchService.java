package pl.radomiej.search.services;

import lombok.extern.slf4j.Slf4j;
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
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import pl.radomiej.search.domains.AddressNode;
import pl.radomiej.search.domains.SearchResult;
import pl.radomiej.search.dto.SearchRequestV1;
import pl.radomiej.search.elasticsearch.repositories.HouseElsticsearchRepository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.simpleQueryStringQuery;

@Service
@Slf4j
public class ElasticSearchService {
    @Autowired
    private HouseElsticsearchRepository houseRepository;

    @Autowired
    private ElasticsearchOperations elasticsearchTemplate;

    @PostConstruct
    private void init() {
        elasticsearchTemplate.createIndex(AddressNode.class);
        elasticsearchTemplate.refresh(AddressNode.class);
        elasticsearchTemplate.putMapping(AddressNode.class);
    }

    public Collection<SearchResult> findGeopositions(SearchRequestV1 searchRequestV1) {
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        BoolQueryBuilder boolQueryBulider = boolQuery();
        builder.withQuery(boolQueryBulider);

        if (searchRequestV1.getVoivodeship() != null && !searchRequestV1.getVoivodeship().equals("")) {
            QueryBuilder queryWojewodztwo = simpleQueryStringQuery(searchRequestV1.getVoivodeship()).field("voivodeship");
            boolQueryBulider.must(queryWojewodztwo);
        }
        if (searchRequestV1.getCounties() != null && !searchRequestV1.getCounties().equals("")) {
            QueryBuilder queryPowiat = simpleQueryStringQuery(searchRequestV1.getCounties()).field("counties");
            boolQueryBulider.must(queryPowiat);
        }
        if (searchRequestV1.getGmine() != null && !searchRequestV1.getGmine().equals("")) {
            QueryBuilder queryGmine = simpleQueryStringQuery(searchRequestV1.getGmine()).field("gmine");
            boolQueryBulider.must(queryGmine);
        }
        if (searchRequestV1.getCity() != null && !searchRequestV1.getCity().equals("")) {
            QueryBuilder queryCity = simpleQueryStringQuery(searchRequestV1.getCity()).field("city");
            boolQueryBulider.must(queryCity);
        }
        if (searchRequestV1.getPostcode() != null && !searchRequestV1.getPostcode().equals("")) {
            QueryBuilder queryPostcode = simpleQueryStringQuery(searchRequestV1.getPostcode() + "| 00-000").field("postcode");
            boolQueryBulider.must(queryPostcode);
        }
        if (searchRequestV1.getStreet() != null && !searchRequestV1.getStreet().equals("")) {
            QueryBuilder queryStreet = simpleQueryStringQuery(searchRequestV1.getStreet()).field("street");
            boolQueryBulider.must(queryStreet);
        }


        QueryBuilder queryHouseNumber = simpleQueryStringQuery(searchRequestV1.getHouseNumber()).field("houseNumber");
        boolQueryBulider.must(queryHouseNumber);

        SearchQuery searchQuery = builder.build();
        log.debug("search query: " + searchQuery.getQuery().toString());
        Page<AddressNode> housesResult = houseRepository.search(searchQuery);

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

    public List<AddressNode> reverseSearch(@PathVariable("latitude") double latitude,
                                           @PathVariable("longitude") double longitude) {
        CriteriaQuery geoLocationCriteriaQuery = new CriteriaQuery(
                new Criteria("position").within(new GeoPoint(latitude, longitude), "50m"));
        List<AddressNode> sortedByDistance = elasticsearchTemplate.queryForList(geoLocationCriteriaQuery,
                AddressNode.class);

        sortedByDistance = new ArrayList<>(sortedByDistance);
        sortedByDistance.sort(new Comparator<AddressNode>() {
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

        log.debug("Founded nodes: " + sortedByDistance.size() + " for lat: " + latitude + " lon: " + longitude);
        return sortedByDistance;
    }
}

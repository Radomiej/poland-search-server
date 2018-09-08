package pl.radomiej.search.services;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import pl.radomiej.search.domains.AddressNode;
import pl.radomiej.search.elasticsearch.repositories.HouseElsticsearchRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.simpleQueryStringQuery;

@Service
public class PostalcodesFixService {
    @Autowired
    private HouseElsticsearchRepository houseRepository;

    @Autowired
    private ElasticsearchOperations elasticsearchTemplate;


    public void init() {
        //TODO move to constructor and check that it work
        elasticsearchTemplate.createIndex(AddressNode.class);
        elasticsearchTemplate.refresh(AddressNode.class);
        elasticsearchTemplate.putMapping(AddressNode.class);
    }

    public void fixPostalcodes() {
        Page<AddressNode> badPostalNodes = findBadPostalNodes();


        //Fast fix
        Pageable pageable = badPostalNodes.getPageable();
        while (true){
            badPostalNodes = houseRepository.findAll(pageable);
            if(badPostalNodes.getSize() == 0) break;
            for (AddressNode badNode : badPostalNodes) {
//                System.out.println("Iterate over badNode");
                List<AddressNode> nearestNodes = findNearest(badNode.getPosition(), 50);
                for (AddressNode nearest : nearestNodes) {
                    if (nearest.getPostcode() != null && !nearest.getPostcode().isEmpty() && !nearest.getPostcode().equalsIgnoreCase("00-000")) {
                        badNode.setPostcode(nearest.getPostcode());
                        houseRepository.save(badNode);
//                        System.out.println("Fix postcode from: " + badNode + " to: " + nearest);
                        break;
                    }
                }
            }
            pageable = pageable.next();
            if(pageable == null) break;
        }


        //Full fix
    }

    private List<AddressNode> findNearest(GeoPoint position, int searchDistanceInMeters) {
        CriteriaQuery geoLocationCriteriaQuery = new CriteriaQuery(
                new Criteria("position").within(position, searchDistanceInMeters + "m"));
        List<AddressNode> searchNearesCriteria = elasticsearchTemplate.queryForList(geoLocationCriteriaQuery,
                AddressNode.class);

        searchNearesCriteria = new ArrayList<>(searchNearesCriteria);
        searchNearesCriteria.sort(new Comparator<AddressNode>() {
            @Override
            public int compare(AddressNode o1, AddressNode o2) {
                double x0 = position.getLat();
                double y0 = position.getLon();
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
        return searchNearesCriteria;
    }


    private Page<AddressNode> findBadPostalNodes() {
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();

        BoolQueryBuilder boolQueryBulider = boolQuery();
        builder.withQuery(boolQueryBulider);

        QueryBuilder queryPostcode = simpleQueryStringQuery("00-000").field("postcode");
        boolQueryBulider.must(queryPostcode);

        SearchQuery searchQuery = builder.build();
        System.out.println("query: " + searchQuery.getQuery().toString());
//        List<AddressNode> housesResult = houseRepository.findAllByPostcode("00-000");

//        SearchHitIterator hitIterator = new SearchHitIterator(searchQuery);
        Page<AddressNode> housesResult = houseRepository.search(searchQuery);

        return housesResult;
    }
}

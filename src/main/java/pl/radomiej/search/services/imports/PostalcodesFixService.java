package pl.radomiej.search.services.imports;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.simpleQueryStringQuery;

@Service
public class PostalcodesFixService {
    @Autowired
    private HouseElsticsearchRepository houseRepository;

    @Autowired
    private ElasticsearchOperations elasticsearchTemplate;

    private ExecutorService executorFixer = Executors.newFixedThreadPool(10);
    private ExecutorService executor = Executors.newFixedThreadPool(20);

    public void init() {
        //TODO move to constructor and check that it work
        elasticsearchTemplate.createIndex(AddressNode.class);
        elasticsearchTemplate.refresh(AddressNode.class);
        elasticsearchTemplate.putMapping(AddressNode.class);
    }

    public void fixPostalcodes() {

       int concurrentFix = 10;
        while (true) {
            CountDownLatch doneSignal = new CountDownLatch(concurrentFix);
            for (int page = 0; page < concurrentFix; page++) {
                final List<AddressNode> badPostalNodes = findBadPostalNodes(page);
                if(badPostalNodes.size() == 0) return;

                executor.submit(() -> fixPage(badPostalNodes, doneSignal));
                //fixPage(badPostalNodes);
            }
            try {
                doneSignal.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    private void fixPage(List<AddressNode> badPostalNodes, CountDownLatch doneSignal) {
        if (badPostalNodes.size() == 0) return;
        for (AddressNode badNode : badPostalNodes) {
//                System.out.println("Iterate over badNode: " +);
            if (fixPostalcode(badNode, 50)) ;
            else if (fixPostalcode(badNode, 150)) ;
            else if (fixPostalcode(badNode, 500)) ;
            else if (fixPostalcode(badNode, 2500)) ;
            else {
                System.out.println("Nie można naprawić kodu: " + badNode);
            }

        }

        //save rest
        doneSignal.countDown();
    }

    private void save(AddressNode nodeToSave) {
//        System.out.println("Wysyłam");
        houseRepository.save(nodeToSave);
    }

    private boolean fixPostalcode(AddressNode badNode, int distance) {
        List<AddressNode> nearestNodes = findNearest(badNode.getPosition(), distance);
        for (AddressNode nearest : nearestNodes) {
            if (nearest.getPostcode() != null && !nearest.getPostcode().isEmpty() && !nearest.getPostcode().equalsIgnoreCase("00-000")) {
                System.out.println("Fix postcode on distance: " + distance + " from: " + badNode + " to: " + nearest);
                badNode.setPostcode(nearest.getPostcode());
                executor.submit(() -> save(badNode));
                return true;
            }
        }

        return false;
    }

    private List<AddressNode> findNearest(GeoPoint position, int searchDistanceInMeters) {
        CriteriaQuery geoLocationCriteriaQuery = new CriteriaQuery(
                new Criteria("position").within(position, searchDistanceInMeters + "m"));
        geoLocationCriteriaQuery.setPageable(PageRequest.of(0, 10000));
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


    private List<AddressNode> findBadPostalNodes(int page) {

        List<AddressNode> housesResult = houseRepository.findAllByPostcode("00-000", PageRequest.of(page, 1000));

        return housesResult;
    }
}

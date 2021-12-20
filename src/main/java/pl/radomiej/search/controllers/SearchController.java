package pl.radomiej.search.controllers;

import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.simpleQueryStringQuery;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.web.bind.annotation.*;

import pl.radomiej.search.domains.AddressNode;
import pl.radomiej.search.domains.SearchResult;
import pl.radomiej.search.dto.SearchRequestV1;
import pl.radomiej.search.services.ElasticSearchService;

@RestController
@RequestMapping("/search")
@Slf4j
public class SearchController {


    @Autowired
    private ElasticSearchService searchService;

    @GetMapping("")
    public Collection<SearchResult> search(@RequestParam(value = "voivodeship", required = false, defaultValue = "") String voivodeship,
                                           @RequestParam(value = "counties", required = false, defaultValue = "") String counties,
                                           @RequestParam(value = "gmine", required = false, defaultValue = "") String gmine,
                                           @RequestParam(value = "city", required = false, defaultValue = "") String city,
                                           @RequestParam(value = "postcode", required = false, defaultValue = "") String postcode,
                                           @RequestParam(value = "street", required = false, defaultValue = "") String street,
                                           @RequestParam(value = "houseNumber", required = false, defaultValue = "") String houseNumber) {

        SearchRequestV1 searchRequestV1 = SearchRequestV1.builder()
                .voivodeship(voivodeship)
                .counties(counties)
                .gmine(gmine)
                .city(city)
                .postcode(postcode)
                .street(street)
                .houseNumber(houseNumber)
                .build();
        log.info("search request: " + searchRequestV1);
        return searchService.findGeopositions(searchRequestV1);
    }
}

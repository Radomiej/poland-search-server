package pl.radomiej.search.controllers;

import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.radomiej.search.domains.AddressNode;
import pl.radomiej.search.domains.SearchResult;
import pl.radomiej.search.dto.SearchRequestV1;
import pl.radomiej.search.services.ElasticSearchService;

import java.util.Collection;

@RestController
@RequestMapping("/reverse")
@Slf4j
public class ReverseGeocodingController {


    @Autowired
    private ElasticSearchService searchService;

    @GetMapping("/{locationText}")
    public SearchResult reverseSearchSmart(@PathVariable("locationText") String locationText) {
        String[] twoParts = locationText.split(",");
        double latitude = Double.parseDouble(twoParts[0].trim());
        double longitude = Double.parseDouble(twoParts[1].trim());

        return reverseSearch(latitude, longitude);
    }

    @GetMapping("/{latitude}/{longitude}")
    public SearchResult reverseSearchFromUrl(@PathVariable("latitude") double latitude,
                                             @PathVariable("longitude") double longitude) {
        return reverseSearch(latitude, longitude);
    }

    @GetMapping("")
    public SearchResult reverseSearch(@RequestParam("latitude") double latitude,
                                      @RequestParam("longitude") double longitude) {

        var results = searchService.reverseSearch(latitude, longitude);

        AddressNode house = results.get(0);
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

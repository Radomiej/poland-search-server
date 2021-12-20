package pl.radomiej.search.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SearchRequestV1 {
    private String voivodeship;
    private String counties;
    private String gmine;
    private String city;
    private String postcode;
    private String street;
    private String houseNumber;
}

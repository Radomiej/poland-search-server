package pl.radomiej.search.elastic.repo;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import pl.radomiej.search.domains.AddressNode;

public interface HouseElsticsearchRepository extends ElasticsearchRepository<AddressNode, Long>{

	Collection<AddressNode> findByStreet(String street);

    List<AddressNode> findByPostcode(String postcode);

    List<AddressNode> findAllByPostcode(String postcode);

    Page<AddressNode> findAll(Pageable pageRequest);
}

package pl.radomiej.search.elastic.repo;

import java.util.Collection;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import pl.radomiej.search.domains.AddressNode;

public interface HouseElsticsearchRepository extends ElasticsearchRepository<AddressNode, Long>{

	Collection<AddressNode> findByStreet(String street);

}

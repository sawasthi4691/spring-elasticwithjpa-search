package com.group.search.elasticrepo;

import com.group.search.model.Customer;
import com.group.search.model.Employee;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ElasticCustomerRepository extends ElasticsearchRepository<Customer,Long> {

    List<Employee> findByFirstName(String value);

    List<Employee> findByLastName(String value);

}

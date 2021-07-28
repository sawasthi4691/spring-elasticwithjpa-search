package com.group.search.repository;

import com.group.search.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Long>, JpaSpecificationExecutor<Customer> {

    List<Customer> findByFirstName(String value);

    List<Customer> findByLastName(String value);

    List<Customer> findByemail(String value);
}

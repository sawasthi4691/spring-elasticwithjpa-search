package com.group.search.service;

import com.group.search.model.Customer;
import com.group.search.model.Employee;
import org.springframework.data.domain.Page;

import java.util.List;

public interface SearchService {

    List<Employee> findByFirstName(final String searchString);

    List<Employee> findEmployeeList();

    List<Customer> findCustomerList();

    Page<Customer> findCustomerBySpecification(String text);
}

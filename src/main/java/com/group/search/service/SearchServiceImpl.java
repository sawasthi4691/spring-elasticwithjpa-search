package com.group.search.service;

import com.group.search.elasticrepo.ElasticCustomerRepository;
import com.group.search.model.Customer;
import com.group.search.model.Employee;
import com.group.search.elasticrepo.ElasticEmployeeRepository;
import com.group.search.repository.EmployeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class SearchServiceImpl implements SearchService{

    private EmployeeRepository employeeRepository;

    private ElasticEmployeeRepository elasticEmployeeRepository;

    private ElasticCustomerRepository elasticCustomerRepository;

    @Autowired
    public SearchServiceImpl(EmployeeRepository employeeRepository,
                             ElasticEmployeeRepository elasticEmployeeRepository,
                             ElasticCustomerRepository elasticCustomerRepository){
        this.employeeRepository = employeeRepository;
        this.elasticEmployeeRepository = elasticEmployeeRepository;
        this.elasticCustomerRepository = elasticCustomerRepository;
    }

    @Override
    public List<Employee> findByFirstName(String searchString) {
        log.info("findByFirstName : starts");
        return employeeRepository.findByFirstName(searchString);
    }

    @Override
    public List<Employee> findEmployeeList() {
        List<Employee> employees = new ArrayList<>();
        for (Employee employee : elasticEmployeeRepository.findAll()) {
            employees.add(employee);
        }
        return employees;
    }


    @Override
    public List<Customer> findCustomerList() {
        List<Customer> customers = new ArrayList<>();
        for (Customer customer : elasticCustomerRepository.findAll()) {
            customers.add(customer);
        }
        return customers;
    }

}
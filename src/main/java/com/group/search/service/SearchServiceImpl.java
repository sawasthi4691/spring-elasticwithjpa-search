package com.group.search.service;

import com.group.search.elasticrepo.ElasticCustomerRepository;
import com.group.search.elasticrepo.ElasticEmployeeRepository;
import com.group.search.model.Customer;
import com.group.search.model.Employee;
import com.group.search.repository.CustomerRepository;
import com.group.search.repository.EmployeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class SearchServiceImpl implements SearchService{

    private EmployeeRepository employeeRepository;

    private CustomerRepository customerRepository;

    private ElasticEmployeeRepository elasticEmployeeRepository;

    private ElasticCustomerRepository elasticCustomerRepository;

    @Autowired
    public SearchServiceImpl(EmployeeRepository employeeRepository,
                             ElasticEmployeeRepository elasticEmployeeRepository,
                             ElasticCustomerRepository elasticCustomerRepository,
                             CustomerRepository customerRepository){
        this.employeeRepository = employeeRepository;
        this.elasticEmployeeRepository = elasticEmployeeRepository;
        this.elasticCustomerRepository = elasticCustomerRepository;
        this.customerRepository = customerRepository;
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

    /**
     * Ensure that your PageRequest object is requesting 0 for small sets, not 1.
     *
     * The pagination begins from 0.
     *
     * This is a common mistake for beginners and is a common redherring when using @Query in conjunction with
     * Spring pagination. If your @Query works without pagination and then returns nothing when using it,
     * check the page number.
     * @param text : text
     * @return :Page<Customer>
     */
    @Override
    public Page<Customer> findCustomerBySpecification(String text) {
        return customerRepository.findAll(getSpecification(text),
                PageRequest.of(0,100 , Sort.by(Sort.Direction.ASC, "firstName")));


    }

    /**
     * Builds and return specification object that filters data based on search string
     * @param filterText : filterText
     * @return : Specification<Customer>
     */
    private Specification<Customer> getSpecification(String filterText){
        //Field[] fields = FieldUtils.getAllFields(Customer.class);
        return (root, criteriaQuery, criteriaBuilder) -> {
            criteriaQuery.distinct(true);
            Predicate predicateForCustomer = null;
            if(StringUtils.isNoneBlank(filterText)){
                 predicateForCustomer = criteriaBuilder.or(
                        criteriaBuilder.like(root.get("firstName"), "%" + filterText+ "%"),
                        criteriaBuilder.like(root.get("lastName"), "%" + filterText + "%"),
                        criteriaBuilder.like(root.get("email").as(String.class), "%" + filterText + "%"),
                        criteriaBuilder.like(root.get("ipaddress"), "%" + filterText + "%"),
                        criteriaBuilder.like(root.get("dob").as(String.class), "%" + filterText + "%"));
            }
            return criteriaBuilder.and(predicateForCustomer);
        };
    }


    @Override
    public Page<Customer> findEmployeeProjectsExampleMatcher(String text)
    {
        /* Build Search object */
        Customer customer = new Customer();
        customer.setIpaddress(text);
        customer.setDob(text);
        customer.setEmail(text);
        customer.setFirstName(text);
        customer.setLastName(text);

        /* Build Example and ExampleMatcher object */
        ExampleMatcher customExampleMatcher = ExampleMatcher.matchingAny()
                .withMatcher("firstName", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("lastName", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("email", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("ipaddress", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("dob", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());

        Example<Customer> employeeExample= Example.of(customer, customExampleMatcher);

        /* Get employees based on search criteria*/
        return customerRepository.findAll(employeeExample, PageRequest.of(0,
                100, Sort.by(Sort.Direction.DESC,"firstName")));
    }


}
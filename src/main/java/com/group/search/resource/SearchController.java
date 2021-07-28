package com.group.search.resource;


import com.group.search.model.Customer;
import com.group.search.model.Employee;
import com.group.search.service.SearchService;
import com.group.search.utils.SearchQueryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/rest/")
public class SearchController {

    private SearchService searchService;

    private SearchQueryBuilder searchQueryBuilder;

    @Autowired
    public SearchController(SearchService searchService, SearchQueryBuilder searchQueryBuilder) {
        this.searchService = searchService;
        this.searchQueryBuilder = searchQueryBuilder;
    }

    /**
     * Give from H2 database.
     * @param value : value
     * @return ResponseEntity<List<Employee>>
     */
    @GetMapping(value = "search/{value}")
    public ResponseEntity<List<Employee>> getEmployeeByFirstName(@PathVariable String value){
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(searchService.findByFirstName(value));
    }

    /**
     * Give from elastic search
     * @return ResponseEntity<List<Employee>>
     */
    @GetMapping(value = "search/all")
    public ResponseEntity<List<Employee>> getAllEmployee(){
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(searchService.findEmployeeList());
    }

    /**
     * Dynamic search [wild card search via elastic search]
     * @param text : text
     * @return : ResponseEntity<List<Employee>>
     */
    @GetMapping(value = "search/all/{text}")
    public ResponseEntity<List<Employee>> getAllEmployeeWithsearchString(@PathVariable String text){
        List<Employee> employees = searchQueryBuilder.getEmployeeText(text);
        log.info("Employee Size : " + employees.size());
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(employees);
    }



    /**
     * Give from elastic search
     * @return ResponseEntity<List<Customer>>
     */
    @GetMapping(value = "search/customer/all")
    public ResponseEntity<List<Customer>> getAllCustomer(){
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(searchService.findCustomerList());
    }

    /**
     * Dynamic search [wild card search via elastic search]
     * @param text : text
     * @return : ResponseEntity<List<customers>>
     */
    @GetMapping(value = "search/customer/all/{text}")
    public ResponseEntity<List<Customer>> getAllCustomerWithsearchString(@PathVariable String text){
        List<Customer> customers = searchQueryBuilder.getCustomerText(text);
        log.info("Customer Size : " + customers.size());
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(customers);
    }


    /**
     * Get Data from DB using criteria builder/specification.
     * @param text : text
     * @return : ResponseEntity<Page<Customer>>
     */
    @GetMapping(value = "search/customer/{text}")
    public ResponseEntity<Page<Customer>> getAllCustomerBySpecification(@PathVariable String text){
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(searchService.findCustomerBySpecification(text));
    }
}

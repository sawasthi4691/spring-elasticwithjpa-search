package com.group.search.utils;

import com.group.search.model.Customer;
import com.group.search.model.Employee;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SearchQueryBuilder {

    private static final String EMPLOYEE_INDEX = "employee";
    private static final String CUSTOMER_INDEX = "customer";

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    public List<Employee> getEmployeeText(String text) {

        QueryBuilder query = QueryBuilders.boolQuery()
                .should(QueryBuilders.queryStringQuery(text)
                                .lenient(true)
                                .field("firstName")
                                .field("email"))
                .should(QueryBuilders.queryStringQuery("*" + text + "*")
                        .lenient(true)
                        .field("firstName")
                        .field("email")
                        .field("description"));

        NativeSearchQuery build = new NativeSearchQueryBuilder()
                .withQuery(query)
                .build();

        SearchHits<Employee> employeeList = elasticsearchOperations.search(build,Employee.class, IndexCoordinates.of(EMPLOYEE_INDEX));
        return employeeList.get().map(SearchHit::getContent).collect(Collectors.toList());
    }


    public List<Customer> getCustomerText(String text) {

        QueryBuilder query = QueryBuilders.boolQuery()
                .should(QueryBuilders.queryStringQuery(text)
                        .lenient(true)
                        .field("firstName")
                        .field("email"))
                .should(QueryBuilders.queryStringQuery("*" + text + "*")
                        .lenient(true)
                        .field("firstName")
                        .field("email")
                        .field("ipaddress")
                        .field("dob"));

        NativeSearchQuery build = new NativeSearchQueryBuilder()
                .withQuery(query)
                .build();

        SearchHits<Customer> customerList = elasticsearchOperations.search(build, Customer.class, IndexCoordinates.of(CUSTOMER_INDEX));

        return customerList.get().map(SearchHit::getContent).collect(Collectors.toList());
    }
}

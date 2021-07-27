package com.group.search.config;

import com.group.search.elasticrepo.ElasticCustomerRepository;
import com.group.search.elasticrepo.ElasticEmployeeRepository;
import com.group.search.model.Customer;
import com.group.search.model.Employee;
import com.group.search.utils.CSVReader;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.scheduling.annotation.Scheduled;
import javax.annotation.PostConstruct;
import java.util.concurrent.atomic.AtomicInteger;

@Configuration
@EnableElasticsearchRepositories(basePackages = "com.group.search.elasticrepo")
@Slf4j
public class ElasticsearchClientConfig extends AbstractElasticsearchConfiguration {

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    @Autowired
    private ElasticEmployeeRepository elasticEmployeeRepository;

    @Autowired
    private ElasticCustomerRepository elasticCustomerRepository;


    @Override
    @Bean
    public RestHighLevelClient elasticsearchClient() {

        final ClientConfiguration clientConfiguration = ClientConfiguration
                                                        .builder()
                                                        .connectedTo("localhost:9200")
                                                        .build();
        return RestClients.create(clientConfiguration).rest();
    }

    @PostConstruct
    @Scheduled(cron = "0 0/5 * * * ?")
    public void buildIndex() {
        getEmployeeDataIntoElastic();
        getCustomerDataIntoElastic();
    }

    private void getEmployeeDataIntoElastic() {
        elasticsearchOperations.indexOps(Employee.class).refresh();
        log.info("insert data to  employee elastic search");
        elasticEmployeeRepository.saveAll(CSVReader.processEmployeeFile("D:\\workspace\\search\\src\\main\\resources\\MOCK_DATA.csv"));
        log.info("data insertion Success full");
        AtomicInteger count = new AtomicInteger();
        elasticEmployeeRepository.findAll().forEach(employee -> {
           //log.info(employee.getFirstName() + "  " + employee.getLastName());
            count.getAndIncrement();
        });
        log.info("count of employee  :  " + count);
    }


    private void getCustomerDataIntoElastic() {
        elasticsearchOperations.indexOps(Customer.class).refresh();
        log.info("insert data to customer elastic search");
        elasticCustomerRepository.saveAll(CSVReader.processCustomerFile("D:\\workspace\\search\\src\\main\\resources\\Customer.csv"));
        log.info("data insertion Success full");
        AtomicInteger count = new AtomicInteger();
        elasticCustomerRepository.findAll().forEach(customer -> {
            //log.info(customer.getFirstName() + "  " + customer.getLastName());
            count.getAndIncrement();
        });
        log.info("count of customer  :  " + count);
    }
}

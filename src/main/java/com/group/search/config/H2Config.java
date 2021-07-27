package com.group.search.config;

import com.group.search.repository.CustomerRepository;
import com.group.search.repository.EmployeeRepository;
import com.group.search.utils.CSVReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class H2Config {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Bean
    public void  initDatabase() {
        log.info("init Databse call");
        employeeRepository.saveAll(CSVReader.processEmployeeFile("D:\\workspace\\search\\src\\main\\resources\\MOCK_DATA.csv"));
        customerRepository.saveAll(CSVReader.processCustomerFile("D:\\workspace\\search\\src\\main\\resources\\Customer.csv"));
        log.info("Data saved in Database");
    }
}

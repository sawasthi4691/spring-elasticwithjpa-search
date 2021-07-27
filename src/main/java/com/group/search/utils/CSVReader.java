package com.group.search.utils;

import com.group.search.model.Customer;
import com.group.search.model.Employee;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface CSVReader {

    static final String COMMA = ",";


    Function<String, Employee> mapToEmployee = (line) -> {
        String[] p = line.split(COMMA);
        Employee employee = new Employee();
        employee.setId(Long.valueOf(p[0]));
        employee.setFirstName(p[1]);
        employee.setLastName(p[2]);
        employee.setEmail(p[3]);
        employee.setDescription(p[4]);
        return employee;
    };

    Function<String, Customer> mapToCustomer = (line) -> {
        String[] p = line.split(COMMA);
        Customer customer = new Customer();
        customer.setId(Long.valueOf(p[0]));
        customer.setFirstName(p[1]);
        customer.setLastName(p[2]);
        customer.setEmail(p[3]);
        customer.setDob(p[4]);
        customer.setIpaddress(p[5]);
        return customer;
    };

    static List<Employee> processEmployeeFile(String inputFilePath) {
        // skip the header of the csv
        try (Stream<String> stream = Files.lines(Paths.get(inputFilePath))) {
            return stream.skip(1).map(mapToEmployee).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();

        }
        return null;
    }


    static List<Customer> processCustomerFile(String inputFilePath) {
        // skip the header of the csv
        try (Stream<String> stream = Files.lines(Paths.get(inputFilePath))) {
            return stream.skip(1).map(mapToCustomer).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();

        }
        return null;
    }


}

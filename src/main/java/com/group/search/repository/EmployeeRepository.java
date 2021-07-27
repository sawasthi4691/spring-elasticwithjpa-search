package com.group.search.repository;

import com.group.search.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee,Long> {

    List<Employee> findByFirstName(String value);

    List<Employee> findByLastName(String value);

    List<Employee> findByDescription(String value);
}

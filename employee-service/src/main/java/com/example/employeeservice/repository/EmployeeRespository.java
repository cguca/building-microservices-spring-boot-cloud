package com.example.employeeservice.repository;

import com.example.employeeservice.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeRespository extends JpaRepository<Employee, Long>
{
    //
    Optional<Employee> findById(Long id);
}

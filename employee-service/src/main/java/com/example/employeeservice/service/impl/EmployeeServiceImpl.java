package com.example.employeeservice.service.impl;

import com.example.employeeservice.dto.EmployeeDto;
import com.example.employeeservice.entity.Employee;
import com.example.employeeservice.repository.EmployeeRespository;
import com.example.employeeservice.service.EmployeeService;
import com.example.employeeservice.service.mappers.EmployeeMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService
{
    private EmployeeRespository employeeRespository;
    @Override
    public EmployeeDto saveEmployee(EmployeeDto employeeDto) {
        Employee employee = EmployeeMapper.MAPPER.mapToEmployee(employeeDto);
        Employee savedEmploye = employeeRespository.save(employee);
        return EmployeeMapper.MAPPER.mapToEmployeeDto(savedEmploye);
    }

    @Override
    public EmployeeDto findEmployeeById(Long id) {
        Employee employee = employeeRespository.findById(id).get();
        return EmployeeMapper.MAPPER.mapToEmployeeDto(employee);
    }
}

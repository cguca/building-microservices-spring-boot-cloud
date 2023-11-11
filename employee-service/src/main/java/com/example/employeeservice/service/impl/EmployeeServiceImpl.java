package com.example.employeeservice.service.impl;

import com.example.employeeservice.dto.APIResponseDto;
import com.example.employeeservice.dto.DepartmentDto;
import com.example.employeeservice.dto.EmployeeDto;
import com.example.employeeservice.entity.Employee;
import com.example.employeeservice.exception.ResourceNotFoundException;
import com.example.employeeservice.repository.EmployeeRespository;
import com.example.employeeservice.service.EmployeeService;
import com.example.employeeservice.service.mappers.EmployeeMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService
{
    private EmployeeRespository employeeRespository;
//    private RestTemplate restTemplate;
    private WebClient webClient;
    @Override
    public EmployeeDto saveEmployee(EmployeeDto employeeDto) {
        Employee employee = EmployeeMapper.MAPPER.mapToEmployee(employeeDto);
        Employee savedEmploye = employeeRespository.save(employee);
        return EmployeeMapper.MAPPER.mapToEmployeeDto(savedEmploye);
    }

    @Override
    public APIResponseDto findEmployeeById(Long id) {
        Optional<Employee> optionalEmployee = employeeRespository.findById(id);

        EmployeeDto employeeDto =  EmployeeMapper.MAPPER
                .mapToEmployeeDto(optionalEmployee
                        .orElseThrow(() -> new ResourceNotFoundException("Employee", "Id", id.toString())));

//        ResponseEntity<DepartmentDto> response =
//                restTemplate.getForEntity("http://localhost:8080/api/departments/" + employeeDto.getDepartmentCode(),
//                        DepartmentDto.class);
//        DepartmentDto departmentDto = response.getBody();

        DepartmentDto departmentDto = webClient.get()
                .uri("http://localhost:8080/api/departments/" + employeeDto.getDepartmentCode())
                .retrieve()
                .bodyToMono(DepartmentDto.class)
                .block();

        APIResponseDto apiResponseDto = new APIResponseDto(employeeDto, departmentDto);

        return apiResponseDto;
    }
}

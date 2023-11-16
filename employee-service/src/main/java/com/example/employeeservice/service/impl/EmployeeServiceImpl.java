package com.example.employeeservice.service.impl;

import com.example.employeeservice.dto.APIResponseDto;
import com.example.employeeservice.dto.DepartmentDto;
import com.example.employeeservice.dto.EmployeeDto;
import com.example.employeeservice.dto.OrganizationDto;
import com.example.employeeservice.entity.Employee;
import com.example.employeeservice.exception.ResourceNotFoundException;
import com.example.employeeservice.repository.EmployeeRespository;
import com.example.employeeservice.service.APIClient;
import com.example.employeeservice.service.EmployeeService;
import com.example.employeeservice.service.mappers.EmployeeMapper;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    private EmployeeRespository employeeRespository;
//    private RestTemplate restTemplate;
    private WebClient webClient;
//    private APIClient apiClient;
    @Override
    public EmployeeDto saveEmployee(EmployeeDto employeeDto) {
        Employee employee = EmployeeMapper.MAPPER.mapToEmployee(employeeDto);
        Employee savedEmploye = employeeRespository.save(employee);
        return EmployeeMapper.MAPPER.mapToEmployeeDto(savedEmploye);
    }

//    @CircuitBreaker(name ="${spring.application.name}", fallbackMethod = "getDefaultDepartment")
    @Retry(name = "${spring.application.name}", fallbackMethod = "getDefaultDepartment")
    @Override
    public APIResponseDto findEmployeeById(Long id) {

        LOGGER.info("inside getEmployeeById method");
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

//        DepartmentDto departmentDto = apiClient.getDepartment(employeeDto.getDepartmentCode());

        OrganizationDto organizationDto = webClient.get()
                .uri("http://localhost:8083/api/organizations/" + employeeDto.getOrganizationCode())
                .retrieve()
                .bodyToMono(OrganizationDto.class)
                .block();


        APIResponseDto apiResponseDto = new APIResponseDto(employeeDto, departmentDto, organizationDto);

        return apiResponseDto;
    }

    public APIResponseDto getDefaultDepartment(Long id, Exception e) {
        LOGGER.info("inside getDefaultDepartment  method");
        Optional<Employee> optionalEmployee = employeeRespository.findById(id);

        EmployeeDto employeeDto =  EmployeeMapper.MAPPER
                .mapToEmployeeDto(optionalEmployee
                        .orElseThrow(() -> new ResourceNotFoundException("Employee", "Id", id.toString())));


        DepartmentDto departmentDto = new DepartmentDto(0L, "R&D", "RD001", "Research and Development");
        OrganizationDto organizationDto = new OrganizationDto(null, "ORG", "Default Organization", "0000", null);
        APIResponseDto apiResponseDto = new APIResponseDto(employeeDto, departmentDto, organizationDto);

        return apiResponseDto;
    }
}

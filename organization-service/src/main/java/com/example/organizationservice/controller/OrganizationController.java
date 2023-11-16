package com.example.organizationservice.controller;

import com.example.organizationservice.dto.OrganizationDto;
import lombok.AllArgsConstructor;
import com.example.organizationservice.service.OrganizationService;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("api/organizations")
public class OrganizationController
{
    private OrganizationService organizationService;

    @PostMapping
    public ResponseEntity<OrganizationDto> saveOrganization(@RequestBody OrganizationDto organizationDto)
    {
        OrganizationDto saveOrganizationDto = organizationService.saveOrganization(organizationDto);
        return new ResponseEntity<>(saveOrganizationDto, HttpStatus.CREATED);
    }

    @GetMapping("{code}")
    public ResponseEntity<OrganizationDto> getOrganizationByCode(@PathVariable("code") String organizationCode)
    {
        OrganizationDto organizationDto = organizationService.getOrganizationByCode(organizationCode);
        return new ResponseEntity<>(organizationDto, HttpStatus.OK);
    }
}

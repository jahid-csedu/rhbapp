package com.example.rhbapp.service;

import com.example.rhbapp.dto.CustomerDto;
import com.example.rhbapp.dto.CustomerSearchDto;
import com.example.rhbapp.entity.Customer;
import com.example.rhbapp.exception.ResourceNotFoundException;
import com.example.rhbapp.repository.CustomerRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    public Page<CustomerDto> getAllCustomers(CustomerSearchDto searchDto, Pageable pageable) {
        return customerRepository.findAll(getCustomerSpecification(searchDto), pageable)
                .map(this::convertToDto);
    }

    public CustomerDto getCustomerById(Long id) {
        return customerRepository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not Found"));
    }

    public CustomerDto saveCustomer(CustomerDto customer) {
        Customer savedCustomer = customerRepository.save(convertToEntity(customer));
        return convertToDto(savedCustomer);
    }

    public CustomerDto updateCustomer(Long id, CustomerDto customerDto) {
        Optional<Customer> existingCustomerOptional = customerRepository.findById(id);
        if (existingCustomerOptional.isPresent()) {
            Customer customer = existingCustomerOptional.get();
            convertToUpdateEntity(customerDto, customer);
            Customer updatedCustomer = customerRepository.save(customer);
            return convertToDto(updatedCustomer);
        }
        throw new ResourceNotFoundException("Customer not Found");
    }

    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }

    private CustomerDto convertToDto(Customer customer) {
        CustomerDto customerDto = new CustomerDto();
        customerDto.setId(customer.getId());
        customerDto.setName(customer.getName());
        customerDto.setEmail(customer.getEmail());
        customerDto.setCreateDate(customer.getCreateDate());
        customerDto.setLastModifyDate(customer.getLastModifyDate());
        return customerDto;
    }

    private Customer convertToEntity(CustomerDto customerDto) {
        Customer customer = new Customer();
        customer.setId(customerDto.getId());
        customer.setName(customerDto.getName());
        customer.setEmail(customerDto.getEmail());
        return customer;
    }

    private void convertToUpdateEntity(CustomerDto customerDto, Customer customer) {
        customer.setName(customerDto.getName());
        customer.setEmail(customerDto.getEmail());
    }

    private Specification<Customer> getCustomerSpecification(CustomerSearchDto searchDto) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (Objects.nonNull(searchDto.getName())) {
                predicates.add(criteriaBuilder.like(root.get("name"), "%" + searchDto.getName() + "%"));
            }

            if (Objects.nonNull(searchDto.getEmail())) {
                predicates.add(criteriaBuilder.like(root.get("email"), "%" + searchDto.getEmail() + "%"));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}

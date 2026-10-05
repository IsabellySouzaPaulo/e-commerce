package com.tads.ecommerce.service;

import com.tads.ecommerce.dto.CategoryDTO;
import com.tads.ecommerce.entity.Category;
import com.tads.ecommerce.repository.CategoryRepository;
import com.tads.ecommerce.service.exception.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class CategoryService {
    @Autowired
    private CategoryRepository repository;

    public List<CategoryDTO> findAll(){
        List<Category> list = repository.findAll();
        // com expressão Lamba - (map reduce filter)
        List<CategoryDTO> listDTO = list.stream().map(x -> new CategoryDTO(x)).collect(Collectors.toList());
        return listDTO;
    }
    @Transactional(readOnly = true)
    public CategoryDTO findById(Long id) {
        Optional<Category> obj = repository.findById(id);
        Category entity = obj.orElseThrow(()->new ResourceNotFoundException("Entity not Found!"));

        return new CategoryDTO(entity);
    }

    @Transactional
    public CategoryDTO insert(CategoryDTO dto) {
        Category entity = new Category();
        entity.setName(dto.getName());

        entity = repository.save(entity);
        return new CategoryDTO(entity);
    }

    public CategoryDTO update(long id, CategoryDTO dto) {
        try{
            Category entity = repository.getReferenceById(id);
            entity.setName(dto.getName());
            entity = repository.save(entity);
            return new CategoryDTO(entity);
        }catch(EntityNotFoundException e){
            throw new ResourceNotFoundException("id not Found!" + id);

        }
    }
}
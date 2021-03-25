package com.example.license.repos;


import com.example.license.entities.Project;
import org.springframework.data.repository.CrudRepository;

public interface ProjectRepository extends CrudRepository<Project, String> {

    @Override
    void delete(Project deleted);

    //other method implementations are already covered by Crud repo with mongoDB
}
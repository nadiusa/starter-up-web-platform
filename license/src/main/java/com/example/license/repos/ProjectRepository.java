package com.example.license.repos;


import com.example.license.entities.Project;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends CrudRepository<Project, String> {

    @Override
    void delete(Project deleted);

    Optional<Project> findByFullAuthorName(String authorName);

    List<Project> findAllByFullAuthorName(String authorName);

    List<Project> findAllByCategory(String category);


    //other method implementations are already covered by Crud repo with mongoDB
}
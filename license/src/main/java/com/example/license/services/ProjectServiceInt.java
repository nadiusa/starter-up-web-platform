package com.example.license.services;

import com.example.license.entities.Project;

import java.util.Optional;

public interface ProjectServiceInt {
    Project update(Optional<Project> proj, Project project);

    Project create();


}

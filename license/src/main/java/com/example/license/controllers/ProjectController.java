package com.example.license.controllers;

import com.example.license.entities.Project;
import com.example.license.repos.ProjectRepository;
import com.example.license.services.serviceImplimentation.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
public class ProjectController {

    @Autowired
    ProjectRepository projectRepository;

    private ProjectService projectService;

    @Autowired
    public void setProjectService(ProjectService projectService) {
        this.projectService = projectService;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/api/projects")
    public Iterable<Project> project() {
        return projectRepository.findAll();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/api/projects/create")
    public String save(@RequestBody Project project) {
        projectRepository.save(project);
        return project.getId();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/api/projects/project/{id}")
    public Optional<Project> show(@PathVariable String id) {
        return projectRepository.findById(id);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/api/projects/update/{id}")
    public Project update(@PathVariable String id, @RequestBody Project project) {
        Optional<Project> proj = projectRepository.findById(id);
        projectRepository.save(projectService.update(proj, project));
        return proj.get();
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/api/projects/delete/{id}")
    public String delete(@PathVariable String id) {
        Optional<Project> project = projectRepository.findById(id);
        if (project.isPresent()) {
            projectRepository.delete(project.get());
            return "The project with name '" + project.get().getName() + "' was deleted";
        } else
            return "Such project does not exist!";
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/api/projects/deleteAll")
    public String deleteAll() {
        projectService.deleteAll();
        return "Deleted all projects.";
    }
}
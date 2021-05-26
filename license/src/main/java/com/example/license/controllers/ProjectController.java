package com.example.license.controllers;

import com.example.license.entities.Project;
import com.example.license.repos.ProjectRepository;
import com.example.license.repos.UserRepository;
import com.example.license.services.serviceImplimentation.ProjectService;
import com.example.license.services.serviceImplimentation.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping("/api")
public class ProjectController {

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    UserRepository userRepository;

    private ProjectService projectService;
    private UserService userService;

    @Autowired
    public void setProjectService(ProjectService projectService) {
        this.projectService = projectService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/projects")
    public Iterable<Project> project() {
        return projectRepository.findAll();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/projects/create")
    public String save(@RequestBody Project project) {
        String id = userService.getCurrentUserId();
        String name = userRepository.findById(id).get().getFullName();
        project.setFullAuthorName(name);
        projectRepository.save(project);
        return project.getId();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/projects/project/{id}")
    public Optional<Project> show(@PathVariable String id) {
        return projectRepository.findById(id);
    }


    @RequestMapping(method = RequestMethod.PUT, value = "/projects/update/{id}")
    public Project update(@PathVariable String id, @RequestBody Project project) {
        Optional<Project> proj = projectRepository.findById(id);
        projectRepository.save(projectService.update(proj, project));
        return proj.get();
    }


    @RequestMapping(method = RequestMethod.DELETE, value = "/projects/delete/{id}")
    public String delete(@PathVariable String id) {
        Optional<Project> project = projectRepository.findById(id);
        if (project.isPresent()) {
            projectRepository.delete(project.get());
            return "The project with name '" + project.get().getName() + "' was deleted";
        } else
            return "Such project does not exist!";
    }


    @RequestMapping(method = RequestMethod.DELETE, value = "/projects/deleteAll")
    public String deleteAll() {
        projectService.deleteAll();
        return "Deleted all projects.";
    }
}
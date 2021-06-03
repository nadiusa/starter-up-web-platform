package com.example.license.controllers;

import com.example.license.entities.Project;
import com.example.license.exception.APIRequstException;
import com.example.license.repos.ProjectRepository;
import com.example.license.repos.UserRepository;
import com.example.license.services.serviceImplimentation.ProjectService;
import com.example.license.services.serviceImplimentation.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;


@RestController
@RequestMapping("/api")
public class ProjectController {
    private final static Logger logger = Logger.getLogger(UserController.class.getName());

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    UserRepository userRepository;

    private ProjectService projectService;
    private UserService userService;

    @Autowired
    private StorageController storageController;


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

    @RequestMapping(method = RequestMethod.GET, value = "/projects/show/{name}")
    public List<Project> getProjectsByAuthorName(@PathVariable String name) {
        List<Project> projects = projectRepository.findAllByFullAuthorName(name);
        if (projects.size() > 0) {
            return projects;
        } else
            throw new APIRequstException("The author name was not correctly provided. Retry");
    }

    @RequestMapping(method = RequestMethod.GET, value = "/projects/showAll/{category}")
    public List<Project> getProjectsByCategory(@PathVariable String category) {
        List<Project> projects = projectRepository.findAllByCategory(category);
        if (projects.size() > 0) {
            return projects;
        } else
            throw new APIRequstException("Such category does not exist, or there are no projects for this category. Retry");
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/uploadBusinessPlan/{id}")
    public ResponseEntity addBusinessPlanFile(@RequestParam(value = "file") MultipartFile file, @PathVariable String id) {
        Optional<Project> projectOptional = projectRepository.findById(id);
        Project project = projectOptional.get();
        project.setBusinessPlanDocUrl(storageController.uploadFile(file).getBody());
        projectRepository.save(project);
        logger.log(Level.INFO, "The business plan document for project: " + project.getName() + " was uploaded successfully");
        return ResponseEntity.ok("The business plan document for project: " + project.getName() + " was uploaded successfully");
    }


    @RequestMapping(method = RequestMethod.DELETE, value = "/projects/deleteAll")
    public String deleteAll() {
        projectService.deleteAll();
        return "Deleted all projects.";
    }
}
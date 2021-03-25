package com.example.license.services.serviceImplimentation;

import com.example.license.entities.Project;
import com.example.license.services.ProjectServiceInt;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ProjectService implements ProjectServiceInt {
    @Override
    public Project update(Optional<Project> proj, Project project) {
        if (project.getDescription() != null)
            proj.get().setDescription(project.getDescription());
        if (project.getStatus() != null)
            proj.get().setStatus(project.getStatus());
        if (project.getName() != null)
            proj.get().setName(project.getName());
        if (project.getCategory() != null)
            proj.get().setCategory(project.getCategory());
        if (project.getTeamMembers() != null)
            proj.get().setTeamMembers(project.getTeamMembers());
        if (project.getFundingAmountRequired() != null)
            proj.get().setFundingAmountRequired(project.getFundingAmountRequired());
        if (project.getCurrentAmountFounded() != null)
            proj.get().setCurrentAmountFounded(project.getCurrentAmountFounded());
        if (project.getFullAuthorName() != null)
            proj.get().setFullAuthorName(project.getFullAuthorName());
        if (project.getStartDate() != null)
            proj.get().setStartDate(project.getStartDate());
        if (project.getEndDate() != null)
            proj.get().setEndDate(project.getEndDate());
        return proj.get();
    }

    @Override
    public Project create() {
        return null;
    }
}

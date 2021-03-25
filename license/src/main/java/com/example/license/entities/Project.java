package com.example.license.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.List;

@Document(collection = "projects")
public class Project {

    @Id
    private String id;
    @Indexed(unique = true, direction = IndexDirection.DESCENDING, dropDups = true)

    private String description;
    private String status; // existing or new
    private String name;
    private String category;
    private List<String> teamMembers;
    private BigDecimal fundingAmountRequired; // the amount of money will be shown in €
    private BigDecimal currentAmountFounded;
    private String fullAuthorName;

    private String startDate;
    private String endDate;

    public Project(String description, String status, String name, String category, List<String> teamMembers, BigDecimal fundingAmountRequired,
                   BigDecimal currentAmountFounded, String fullAuthorName, String startDate, String endDate) {
        this.description = description;
        this.status = status;
        this.name = name;
        this.category = category;
        this.teamMembers = teamMembers;
        this.fundingAmountRequired = fundingAmountRequired;
        this.currentAmountFounded = currentAmountFounded;
        this.fullAuthorName = fullAuthorName;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<String> getTeamMembers() {
        return teamMembers;
    }

    public void setTeamMembers(List<String> teamMembers) {
        this.teamMembers = teamMembers;
    }

    public BigDecimal getFundingAmountRequired() {
        return fundingAmountRequired;
    }

    public void setFundingAmountRequired(BigDecimal fundingAmountRequired) {
        this.fundingAmountRequired = fundingAmountRequired;
    }

    public BigDecimal getCurrentAmountFounded() {
        return currentAmountFounded;
    }

    public void setCurrentAmountFounded(BigDecimal currentAmountFounded) {
        this.currentAmountFounded = currentAmountFounded;
    }

    public String getFullAuthorName() {
        return fullAuthorName;
    }

    public void setFullAuthorName(String fullAuthorName) {
        this.fullAuthorName = fullAuthorName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}

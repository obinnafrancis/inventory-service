package com.vlad.tech.inventoryservice.services;

import com.vlad.tech.inventoryservice.daos.ProjectRepository;
import com.vlad.tech.inventoryservice.exceptions.DuplicateException;
import com.vlad.tech.inventoryservice.exceptions.NotFoundException;
import com.vlad.tech.inventoryservice.models.dtos.Project;
import com.vlad.tech.inventoryservice.models.dtos.Storage;
import com.vlad.tech.inventoryservice.models.responses.BaseResponse;
import com.vlad.tech.inventoryservice.utils.ResponseMapper;
import com.vlad.tech.inventoryservice.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final StorageService storageService;

    @Autowired
    public ProjectService(ProjectRepository projectRepository, StorageService storageService){
        this.projectRepository = projectRepository;
        this.storageService = storageService;
    }

    public Project find(long id) {
        Optional<Project> projectOptional = projectRepository.findById(id);
        if(projectOptional.isPresent()){
            return projectOptional.get();
        }
        return null;
    }

    public BaseResponse createProject(Project project) {
        Project exists = projectRepository.findByName(project.getName());
        if(Objects.nonNull(exists)){
            throw new DuplicateException(ResponseMapper.PROJECT_ALREADY_EXISTS);
        }else {
            if(!project.getStorages().isEmpty()){
                for(Storage storage: project.getStorages()){
                    Storage find = storageService.find(storage.getId());
                    if(!Objects.nonNull(find)) {
                        throw new NotFoundException(ResponseMapper.NO_RECORD_FOUND);
                    }
                }
            }
            return new BaseResponse(ResponseMapper.SUCCESS_RESPONSE,projectRepository.save(project));
        }
    }

    public BaseResponse findProject(long id) {
       Project exists = find(id);
        if(Objects.nonNull(exists)){
            return new BaseResponse(ResponseMapper.SUCCESS_RESPONSE, exists);
        }else {
            throw new NotFoundException(ResponseMapper.NO_RECORD_FOUND);
        }
    }

    public void updateProject(Project project) {
        Optional<Project> optionalExists = projectRepository.findById(project.getId());
        if(optionalExists.isPresent()){
            Project exists = optionalExists.get();
            if(!project.getStorages().isEmpty()){
                for(Storage storage: project.getStorages()){
                    Storage find = storageService.find(storage.getId());
                    if(!Objects.nonNull(find)) {
                        throw new NotFoundException(ResponseMapper.NO_RECORD_FOUND);
                    }
                }
            }
            Utils.copyNonNullProperties(project, exists);
            projectRepository.save(exists);
        }else {
            throw new NotFoundException(ResponseMapper.NO_RECORD_FOUND);
        }
    }

    public void deleteProjectById(long id) {
        Optional<Project> exists = projectRepository.findById(id);
        if(exists.isPresent()){
            projectRepository.delete(exists.get());
        }else{
            throw new NotFoundException(ResponseMapper.NO_RECORD_FOUND);
        }
    }

    public BaseResponse findAllProjcts() {
        List<Project> projectList =  this.projectRepository.findAll();
        if(projectList.isEmpty()){
            throw new NotFoundException(ResponseMapper.NO_RECORD_FOUND);
        }
        return new BaseResponse(ResponseMapper.SUCCESS_RESPONSE, projectList);
    }
}

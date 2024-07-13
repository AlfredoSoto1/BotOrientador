/*
 * Copyright 2024 Alfredo Soto
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package assistant.rest.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import assistant.database.SubTransactionResult;
import assistant.rest.dao.ProjectsDAO;
import assistant.rest.dto.ProjectDTO;

/**
 * @author Alfredo
 */
@Service
public class ProjectsService {

	private final ProjectsDAO projectDAO;
	
	@Autowired
	public ProjectsService(ProjectsDAO projectDAO) {
		this.projectDAO = projectDAO;
	}
	
	/**
	 * @param page
	 * @param size
	 * @return Names of all the projects
	 */
	public List<String> getProjectNames(int page, int size) {
        SubTransactionResult result = projectDAO.queryProjectNames(page, size);
        
        List<String> names = new ArrayList<>();
        for (int i = 0; i < result.rowCount(); i++) {
			names.add(result.getValue("name", i));
		}
		return names;
	}
	
	/**
	 * @param page
	 * @param size
	 * @return List of projects
	 */
	public List<ProjectDTO> getAllProjects(int page, int size) {
        SubTransactionResult result = projectDAO.queryAllProjects(page, size);
        
        Map<Integer, ProjectDTO> projects = new HashMap<>();
        for (int i = 0; i < result.rowCount(); i++) {
        	if (projects.containsKey(result.getValue("projecid", i))) {
        		ProjectDTO project = projects.get(result.getValue("projecid", i));
        		project.addPlatforms(result.getValue("platform",  i));
    			project.addUrlhandle(result.getValue("urlhandle", i));
        		continue;
        	}
			ProjectDTO project = new ProjectDTO();
			project.setId(result.getValue("projecid", i));
			project.setName(result.getValue("name",   i));
			project.setEmail(result.getValue("email", i));
			project.setDescription(result.getValue("description", i));
			
			project.setWebsite(result.getValue("url", i));
			project.addPlatforms(result.getValue("platform",  i));
			project.addUrlhandle(result.getValue("urlhandle", i));
			
			projects.put(project.getId(), project);
		}
		return new ArrayList<>(projects.values());
	}
	
	/**
	 * @param name
	 * @return Single project by name
	 */
	public Optional<ProjectDTO> getProject(String name) {
		SubTransactionResult result = projectDAO.queryProject(name);
		
		if (result.isEmpty())
			return Optional.empty();
		
        Map<Integer, ProjectDTO> projects = new HashMap<>();
        for (int i = 0; i < result.rowCount(); i++) {
        	if (projects.containsKey(result.getValue("projecid", i))) {
        		ProjectDTO project = projects.get(result.getValue("projecid", i));
        		project.addPlatforms(result.getValue("platform", i));
    			project.addUrlhandle(result.getValue("urlhandle", i));
        		continue;
        	}
			ProjectDTO project = new ProjectDTO();
			project.setId(result.getValue("projecid", i));
			project.setName(result.getValue("name",   i));
			project.setEmail(result.getValue("email", i));
			project.setDescription(result.getValue("description", i));
			
			project.setWebsite(result.getValue("url", i));
			project.addPlatforms(result.getValue("platform",  i));
			project.addUrlhandle(result.getValue("urlhandle", i));
			
			projects.put(project.getId(), project);
		}
        
        if (projects.size() != 1)
        	return Optional.empty();
        
        Map.Entry<Integer, ProjectDTO> entry = projects.entrySet().iterator().next();
		return Optional.of(entry.getValue());
	}
}

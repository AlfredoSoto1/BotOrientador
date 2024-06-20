/**
 * 
 */
package assistant.rest.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.stereotype.Repository;

import assistant.app.core.Application;
import assistant.database.DatabaseConnection.RunnableSQL;
import assistant.rest.dto.ContactDTO;
import assistant.rest.dto.ExtensionDTO;
import assistant.rest.dto.FacultyDTO;
import assistant.rest.dto.OrganizationDTO;
import assistant.rest.dto.ProjectDTO;
import assistant.rest.dto.SocialMediaDTO;
import assistant.rest.dto.WebpageDTO;

/**
 * @author Alfredo
 */
@Repository
public class FacultyDAO {
	
	private final String SQL_SELECT_PROJECT =
		"""
		select projecid, name, description
			from project
			where fcontid = ?
		""";
	private final String SQL_SELECT_ORGANIZATION =
		"""
		select orgid, name, description
			from organization
			where fcontid = ?
		""";
	private final String SQL_SELECT_EXTENSION =
		"""
		select extid, ext
			from extension
			where fcontid = ?
		""";
	private final String SQL_SELECT_WEB =
		"""
		select webid, url, description
			from webpage
			where fcontid = ?
		""";
	private final String SQL_SELECT_SOCIAL =
		"""
		select socialid, platform, urlhandle
			from socialmedia
			where fcontid = ?
		""";
	
	public FacultyDAO() {
		 
	}
	
	public List<FacultyDTO> getAll(int offset, int limit) {
		final String SQL =
			"""
			select  facid, fcontid, fdepid, 
			        name, jobentitlement, description, office, email
				from faculty
			        inner join contact on fcontid = contid
			offset ?
			limit  ?
			""";
		List<FacultyDTO> faculty = new ArrayList<>();
		
		RunnableSQL rq = connection -> {
			PreparedStatement stmt = connection.prepareStatement(SQL);
			stmt.setInt(1, offset);
			stmt.setInt(2, limit);
			
			ResultSet result = stmt.executeQuery();
			while(result.next()) {
				FacultyDTO professor = new FacultyDTO();
				ContactDTO contact = new ContactDTO();
				
				professor.setId(result.getInt("facid"));
				professor.setDepid(result.getInt("fdepid"));
				
				professor.setName(result.getString("name"));
				professor.setJobentitlement(result.getString("jobentitlement"));
				professor.setDescription(result.getString("description"));
				professor.setOffice(result.getString("office"));
				
				int contid = result.getInt("fcontid");
				
				contact.setId(contid);
				contact.setEmail(result.getString("email"));
				professor.setContact(contact);
				
				faculty.add(professor);
				
				PreparedStatement stmt_project = connection.prepareStatement(SQL_SELECT_PROJECT);
				PreparedStatement stmt_organiz = connection.prepareStatement(SQL_SELECT_ORGANIZATION);
				PreparedStatement stmt_extensi = connection.prepareStatement(SQL_SELECT_EXTENSION);
				PreparedStatement stmt_webpage = connection.prepareStatement(SQL_SELECT_WEB);
				PreparedStatement stmt_socialm = connection.prepareStatement(SQL_SELECT_SOCIAL);
				
				stmt_project.setInt(1, contid);
				stmt_organiz.setInt(1, contid);
				stmt_extensi.setInt(1, contid);
				stmt_webpage.setInt(1, contid);
				stmt_socialm.setInt(1, contid);
				
				ResultSet result_project = stmt_project.executeQuery();
				ResultSet result_organiz = stmt_organiz.executeQuery();
				ResultSet result_extensi = stmt_extensi.executeQuery();
				ResultSet result_webpage = stmt_webpage.executeQuery();
				ResultSet result_socialm = stmt_socialm.executeQuery();
				
				while(result_project.next())
					contact.addProjects(new ProjectDTO(result_project.getInt("projecid"), result_project.getString("name"), result_project.getString("description")));
				while(result_organiz.next())
					contact.addOrganizations(new OrganizationDTO(result_organiz.getInt("orgid"), result_organiz.getString("name"), result_organiz.getString("description")));
				while(result_extensi.next())
					contact.addExtensions(new ExtensionDTO(result_extensi.getInt("extid"), result_extensi.getString("ext")));
				while(result_webpage.next())
					contact.addWebpages(new WebpageDTO(result_webpage.getInt("webid"), result_webpage.getString("url"), result_webpage.getString("description")));
				while(result_socialm.next())
					contact.addSocialmedias(new SocialMediaDTO(result_socialm.getInt("socialid"), result_socialm.getString("platform"), result_socialm.getString("urlhandle")));
				
				result_project.close();
				result_organiz.close();
				result_extensi.close();
				result_webpage.close();
				result_socialm.close();
				
				stmt_project.close();
				stmt_organiz.close();
				stmt_extensi.close();
				stmt_webpage.close();
				stmt_socialm.close();
			}
			
			result.close();
			stmt.close();
		};
		
		Application.instance().getDatabaseConnection().establishConnection(rq);
		return faculty;
	}
	
	public Optional<FacultyDTO> getProfessor(int id) {
		final String SQL_SELECT_FACULTY =
			"""
			select  facid, fcontid, fdepid, 
			        name, jobentitlement, description, office, email
				from faculty
			        inner join contact on fcontid = contid
				where 
					facid = ?
			""";
		AtomicBoolean found = new AtomicBoolean(false);
		FacultyDTO professor = new FacultyDTO();
		ContactDTO contact = new ContactDTO();
		
		RunnableSQL rq = connection -> {
			PreparedStatement stmt_faculty = connection.prepareStatement(SQL_SELECT_FACULTY);
			stmt_faculty.setInt(1, id);

			ResultSet result = stmt_faculty.executeQuery();
			int contid = -1;
			while(result.next()) {
				professor.setId(result.getInt("facid"));
				professor.setDepid(result.getInt("fdepid"));
				
				professor.setName(result.getString("name"));
				professor.setJobentitlement(result.getString("jobentitlement"));
				professor.setDescription(result.getString("description"));
				professor.setOffice(result.getString("office"));
				
				contid = result.getInt("fcontid");
				
				contact.setId(contid);
				contact.setEmail(result.getString("email"));
				professor.setContact(contact);
				
				found.set(true);
			}
			result.close();
			stmt_faculty.close();
			
			if(contid < 0)
				return;
			
			PreparedStatement stmt_project = connection.prepareStatement(SQL_SELECT_PROJECT);
			PreparedStatement stmt_organiz = connection.prepareStatement(SQL_SELECT_ORGANIZATION);
			PreparedStatement stmt_extensi = connection.prepareStatement(SQL_SELECT_EXTENSION);
			PreparedStatement stmt_webpage = connection.prepareStatement(SQL_SELECT_WEB);
			PreparedStatement stmt_socialm = connection.prepareStatement(SQL_SELECT_SOCIAL);
			
			stmt_project.setInt(1, contid);
			stmt_organiz.setInt(1, contid);
			stmt_extensi.setInt(1, contid);
			stmt_webpage.setInt(1, contid);
			stmt_socialm.setInt(1, contid);
			
			ResultSet result_project = stmt_project.executeQuery();
			ResultSet result_organiz = stmt_organiz.executeQuery();
			ResultSet result_extensi = stmt_extensi.executeQuery();
			ResultSet result_webpage = stmt_webpage.executeQuery();
			ResultSet result_socialm = stmt_socialm.executeQuery();
			
			while(result_project.next())
				contact.addProjects(new ProjectDTO(result_project.getInt("projecid"), result_project.getString("name"), result_project.getString("description")));
			while(result_organiz.next())
				contact.addOrganizations(new OrganizationDTO(result_organiz.getInt("orgid"), result_organiz.getString("name"), result_organiz.getString("description")));
			while(result_extensi.next())
				contact.addExtensions(new ExtensionDTO(result_extensi.getInt("extid"), result_extensi.getString("ext")));
			while(result_webpage.next())
				contact.addWebpages(new WebpageDTO(result_webpage.getInt("webid"), result_webpage.getString("url"), result_webpage.getString("description")));
			while(result_socialm.next())
				contact.addSocialmedias(new SocialMediaDTO(result_socialm.getInt("socialid"), result_socialm.getString("platform"), result_socialm.getString("urlhandle")));
			
			result_project.close();
			result_organiz.close();
			result_extensi.close();
			result_webpage.close();
			result_socialm.close();
			
			stmt_project.close();
			stmt_organiz.close();
			stmt_extensi.close();
			stmt_webpage.close();
			stmt_socialm.close();
		};
		
		Application.instance().getDatabaseConnection().establishConnection(rq);
		return found.get() ? Optional.of(professor) : Optional.empty();
	}
}

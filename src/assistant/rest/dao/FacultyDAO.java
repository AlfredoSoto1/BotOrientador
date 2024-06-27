/**
 * 
 */
package assistant.rest.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Repository;

import assistant.app.core.Application;
import assistant.database.DatabaseConnection.RunnableSQL;
import assistant.rest.dto.ContactDTO;
import assistant.rest.dto.EmailDTO;
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
	
	public List<EmailDTO> getFacultyEmails() {
		final String SQL =
			"""
			SELECT  facid, email
                FROM faculty
			        INNER JOIN contact ON fcontid = contid
			""";
		List<EmailDTO> emails = new ArrayList<>();
		
		RunnableSQL rq = connection -> {
			PreparedStatement stmt = connection.prepareStatement(SQL);
			
			ResultSet result = stmt.executeQuery();
			while(result.next()) {
				EmailDTO email = new EmailDTO();
				email.setId(result.getInt("facid"));
				email.setEmail(result.getString("email"));
				emails.add(email);
			}
			result.close();
			stmt.close();
		};
		
		Application.instance().getDatabaseConnection().establishConnection(rq);
		return emails;
	}
	
	public List<FacultyDTO> getAllFaculty(int offset, int limit, String department) {
		final String SQL =
			"""
			SELECT  facid, fcontid, abreviation, 
			        faculty.name,
			        faculty.description, 
			        jobentitlement, 
			        office, 
			        email
				
                FROM faculty
			        INNER JOIN contact    ON fcontid = contid
			        INNER JOIN department ON fdepid  = depid
				WHERE
					abreviation = ?
				
			OFFSET ?
			LIMIT  ?
			""";
		List<FacultyDTO> faculty = new ArrayList<>();
		
		RunnableSQL rq = connection -> {
			PreparedStatement stmt = connection.prepareStatement(SQL);
			stmt.setString(1, department);
			stmt.setInt(2, offset);
			stmt.setInt(3, limit);
			
			ResultSet result = stmt.executeQuery();
			while(result.next()) {
				FacultyDTO professor = new FacultyDTO();
				ContactDTO contact = new ContactDTO();
				
				professor.setId(result.getInt("facid"));
				professor.setDepartment(result.getString("abreviation"));
				
				professor.setName(result.getString("name"));
				professor.setJobentitlement(result.getString("jobentitlement"));
				professor.setDescription(result.getString("description"));
				professor.setOffice(result.getString("office"));
				
				int contid = result.getInt("fcontid");
				
				contact.setId(contid);
				contact.setEmail(result.getString("email"));
				professor.setContact(contact);
				
				faculty.add(professor);
				
				contactInfoSelect(connection, contact);
			}
			
			result.close();
			stmt.close();
		};
		
		Application.instance().getDatabaseConnection().establishConnection(rq);
		return faculty;
	}
	
	public Optional<FacultyDTO> getProfessor(EmailDTO email) {
		final String SQL_SELECT_FACULTY =
			"""
			SELECT  facid, 
                    fcontid, 
                    abreviation, 
			        faculty.name,
			        faculty.description, 
			        jobentitlement, 
			        office, 
			        email
				
                FROM faculty
			        INNER JOIN contact    ON fcontid = contid
			        INNER JOIN department ON fdepid  = depid
				WHERE 
					email = ?
			""";
		AtomicBoolean found = new AtomicBoolean(false);
		FacultyDTO professor = new FacultyDTO();
		ContactDTO contact = new ContactDTO();
		
		RunnableSQL rq = connection -> {
			PreparedStatement stmt_faculty = connection.prepareStatement(SQL_SELECT_FACULTY);
			stmt_faculty.setString(1, email.getEmail());

			ResultSet result = stmt_faculty.executeQuery();
			int contid = -1;
			while(result.next()) {
				professor.setId(result.getInt("facid"));
				professor.setDepartment(result.getString("abreviation"));
				
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
			
			contactInfoSelect(connection, contact);
		};
		
		Application.instance().getDatabaseConnection().establishConnection(rq);
		return found.get() ? Optional.of(professor) : Optional.empty();
	}
	
	public int insertProfessor(FacultyDTO professor) {
		final String SQL_INSERT_PROJECT =
			"""
			insert into project(name, description, fcontid) values (?, ?, ?)
			""";
		final String SQL_INSERT_ORGANIZATION =
			"""
			insert into organization (name, description, fcontid) values (?, ?, ?)
			""";
		final String SQL_INSERT_EXTENSION =
			"""
			insert into extension (ext, fcontid) values (?, ?)
			""";
		final String SQL_INSERT_WEBPAGE =
			"""
			insert into webpage (url, description, fcontid) values (?, ?, ?)
			""";
		final String SQL_INSERT_SOCIALMEDIA =
			"""
			insert into webpage (url, platform, fcontid) values (?, ?, ?)
			""";
		final String SQL_INSERT_PROFESSOR =
			"""
			insert into faculty (name, jobentitlement, office, description, fcontid, fdepid)
			select 
			        ?,               -- placeholder for name
			        ?,               -- placeholder for jobentitlement
			        ?,               -- placeholder for office
			        ?,               -- placeholder for description
			        get_or_insert_contact(?),
			        (select depid from department where abreviation = ? limit 1)  
			returning facid, fcontid;
			""";
		AtomicInteger facid = new AtomicInteger(-1);
		
		RunnableSQL rq = connection -> {
			connection.setAutoCommit(false);
			
			try {
				PreparedStatement stmt_professor = connection.prepareStatement(SQL_INSERT_PROFESSOR);
				stmt_professor.setString(1, professor.getName());
				stmt_professor.setString(2, professor.getJobentitlement());
				stmt_professor.setString(3, professor.getOffice());
				stmt_professor.setString(4, professor.getDescription());
				stmt_professor.setString(5, professor.getContact().getEmail());
				stmt_professor.setString(6, professor.getDepartment());
				
				ResultSet result = stmt_professor.executeQuery();
				int contid = -1;
				if(result.next()) {
					facid.set(result.getInt("facid"));
					contid = result.getInt("fcontid");
				}
				
				PreparedStatement stmt_project = connection.prepareStatement(SQL_INSERT_PROJECT);
				PreparedStatement stmt_organiz = connection.prepareStatement(SQL_INSERT_ORGANIZATION);
				PreparedStatement stmt_extensi = connection.prepareStatement(SQL_INSERT_EXTENSION);
				PreparedStatement stmt_webpage = connection.prepareStatement(SQL_INSERT_WEBPAGE);
				PreparedStatement stmt_socialm = connection.prepareStatement(SQL_INSERT_SOCIALMEDIA);
				
				if (!professor.getContact().getProjects().isEmpty()) {
					for(ProjectDTO p : professor.getContact().getProjects()) {
						stmt_project.setString(1, p.getName());
						stmt_project.setString(2, p.getDescription());
						stmt_project.setInt(3, contid);
						stmt_project.addBatch();
					}
					stmt_project.executeBatch();
				}
				
				if (!professor.getContact().getOrganizations().isEmpty()) {
					for(OrganizationDTO o : professor.getContact().getOrganizations()) {
						stmt_organiz.setString(1, o.getName());
						stmt_organiz.setString(2, o.getDescription());
						stmt_organiz.setInt(3, contid);
						stmt_organiz.addBatch();
					}
					stmt_organiz.executeBatch();
				}
				
				if (!professor.getContact().getExtensions().isEmpty()) {
					for(ExtensionDTO e : professor.getContact().getExtensions()) {
						stmt_extensi.setString(1, e.getExt());
						stmt_extensi.setInt(2, contid);
						stmt_extensi.addBatch();
					}
					stmt_extensi.executeBatch();
				}
				
				if (!professor.getContact().getWebpages().isEmpty()) {
					for(WebpageDTO w : professor.getContact().getWebpages()) {
						stmt_webpage.setString(1, w.getUrl());
						stmt_webpage.setString(2, w.getDescription());
						stmt_webpage.setInt(3, contid);
						stmt_webpage.addBatch();
					}
					stmt_webpage.executeBatch();
				}
				
				if (!professor.getContact().getSocialmedias().isEmpty()) {
					for(SocialMediaDTO s : professor.getContact().getSocialmedias()) {
						stmt_socialm.setString(1, s.getUrl());
						stmt_socialm.setString(2, s.getPlatform());
						stmt_socialm.setInt(3, contid);
						stmt_socialm.addBatch();
					}
					stmt_socialm.executeBatch();
				}
				connection.commit();
			} catch(SQLException sqle) {
				sqle.printStackTrace();
				connection.rollback();
			} finally {
				connection.setAutoCommit(true);
			}
		};
		
		Application.instance().getDatabaseConnection().establishConnection(rq);
		return facid.get();
	}
	
	private void contactInfoSelect(Connection connection,  ContactDTO contact) throws SQLException {
		PreparedStatement stmt_project = connection.prepareStatement(SQL_SELECT_PROJECT);
		PreparedStatement stmt_organiz = connection.prepareStatement(SQL_SELECT_ORGANIZATION);
		PreparedStatement stmt_extensi = connection.prepareStatement(SQL_SELECT_EXTENSION);
		PreparedStatement stmt_webpage = connection.prepareStatement(SQL_SELECT_WEB);
		PreparedStatement stmt_socialm = connection.prepareStatement(SQL_SELECT_SOCIAL);
		
		stmt_project.setInt(1, contact.getId());
		stmt_organiz.setInt(1, contact.getId());
		stmt_extensi.setInt(1, contact.getId());
		stmt_webpage.setInt(1, contact.getId());
		stmt_socialm.setInt(1, contact.getId());
		
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
}

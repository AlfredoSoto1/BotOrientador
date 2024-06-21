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

import org.springframework.stereotype.Repository;

import assistant.app.core.Application;
import assistant.database.DatabaseConnection.RunnableSQL;
import assistant.rest.dto.ContactDTO;
import assistant.rest.dto.ExtensionDTO;
import assistant.rest.dto.OrganizationDTO;
import assistant.rest.dto.ProjectDTO;
import assistant.rest.dto.ServiceDTO;
import assistant.rest.dto.SocialMediaDTO;
import assistant.rest.dto.WebpageDTO;

/**
 * @author Alfredo
 */
@Repository
public class ServiceDAO {

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
	
	public ServiceDAO() {
		
	}
	
	public List<ServiceDTO> getAll(int offset, int limit) {
		final String SQL = 
			"""
			select  servid,
					fdepid,
					fcontid,
					name,
					office,
					availability,
					description,
					offering,
					additional,
					email
				from service
					inner join contact on contid = fcontid
			
			order by servid asc
			offset ?
			limit  ?;
			""";
		List<ServiceDTO> services = new ArrayList<>();
		
		RunnableSQL rq = connection -> {
			PreparedStatement stmt = connection.prepareStatement(SQL);
			stmt.setInt(1, offset);
			stmt.setInt(2, limit);
			
			ResultSet result = stmt.executeQuery();
			while(result.next()) {
				ServiceDTO service = new ServiceDTO();
				service.setId(result.getInt("servid"));
				service.setDepid(result.getInt("fdepid"));
				service.setName(result.getString("name"));
				service.setDescription(result.getString("description"));
				service.setAvailability(result.getString("availability"));
				
				service.setOffering(result.getString("offering"));
				service.setAdditional(result.getString("additional"));

				ContactDTO contact = new ContactDTO();
				contact.setId(result.getInt("fcontid"));
				contact.setEmail(result.getString("email"));
				service.setContact(contact);
				
				contactInfoSelect(connection, contact);
				services.add(service);
			}
			result.close();
			stmt.close();
		};
		
		Application.instance().getDatabaseConnection().establishConnection(rq);
		return services;
	}
	
	public Optional<ServiceDTO> getService(int id) {
		final String SQL = 
			"""
			select  servid,
					fdepid,
					fcontid,
					name,
					office,
					availability,
					description,
					offering,
					additional,
					email
				from service
					inner join contact on contid = fcontid
				where
					servid = ?
			""";
		AtomicBoolean found = new AtomicBoolean(false);
		ServiceDTO service = new ServiceDTO();
		
		RunnableSQL rq = connection -> {
			PreparedStatement stmt = connection.prepareStatement(SQL);
			stmt.setInt(1, id);
			
			ResultSet result = stmt.executeQuery();
			while(result.next()) {
				service.setId(result.getInt("servid"));
				service.setDepid(result.getInt("fdepid"));
				service.setName(result.getString("name"));
				service.setDescription(result.getString("description"));
				service.setAvailability(result.getString("availability"));
				
				service.setOffering(result.getString("offering"));
				service.setAdditional(result.getString("additional"));

				ContactDTO contact = new ContactDTO();
				contact.setId(result.getInt("fcontid"));
				contact.setEmail(result.getString("email"));
				service.setContact(contact);
				
				contactInfoSelect(connection, contact);
				found.set(true);
			}
			result.close();
			stmt.close();
		};
		
		Application.instance().getDatabaseConnection().establishConnection(rq);
		return found.get() ? Optional.of(service) : Optional.empty();
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

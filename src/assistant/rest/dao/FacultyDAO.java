/**
 * 
 */
package assistant.rest.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Repository;

import assistant.app.core.Application;
import assistant.database.DatabaseConnection.RunnableSQL;
import assistant.database.SubTransactionResult;
import assistant.database.Transaction;
import assistant.database.TransactionError;
import assistant.database.TransactionStatementType;
import assistant.rest.dto.EmailDTO;
import assistant.rest.dto.ExtensionDTO;
import assistant.rest.dto.FacultyDTO;
import assistant.rest.dto.SocialMediaDTO;
import assistant.rest.dto.WebpageDTO;

/**
 * @author Alfredo
 */
@Repository
public class FacultyDAO {
	
	public FacultyDAO() {
		 
	}
	
	public SubTransactionResult getFacultyCount(String departmentAbbreviation) {
		@SuppressWarnings("resource")
		Transaction transaction = new Transaction();
		
		transaction.submitSQL(
			"""
			SELECT COUNT(*) 
			    FROM faculty
			        INNER JOIN department ON fdepid = depid
			    WHERE
			        abreviation = ?
			""", List.of(departmentAbbreviation));
		
		transaction.prepare()
			.executeThen(TransactionStatementType.SELECT_QUERY)
			.commit();
		
		// Close transaction
		transaction.forceClose();
		
		// Display errors
		for (TransactionError error : transaction.catchErrors()) {
			System.err.println(error);
			System.err.println("==============================");
		}
		
		return transaction.getLatestResult();
	}
	
	public SubTransactionResult getFacultyEmails() {
		@SuppressWarnings("resource")
		Transaction transaction = new Transaction();
		
		transaction.submitSQL(
			"""
			SELECT  facid, email
                FROM faculty
			        INNER JOIN contact ON fcontid = contid
			""", List.of());
		
		transaction.prepare()
			.executeThen(TransactionStatementType.SELECT_QUERY)
			.commit();
		
		// Close transaction
		transaction.forceClose();
		
		// Display errors
		for (TransactionError error : transaction.catchErrors()) {
			System.err.println(error);
			System.err.println("==============================");
		}
		
		return transaction.getLatestResult();
	}
	
	public SubTransactionResult getAllFaculty(int offset, int limit, String department) {
		@SuppressWarnings("resource")
		Transaction transaction = new Transaction();
		
		transaction.submitSQL(
			"""
			WITH faculty_selected AS (
			    SELECT  facid,
			            contid, 
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
			            abreviation = ?
			), 
			extension_selected AS (
			    SELECT fcontid, extid, ext, true AS has_ext
			        FROM extension
			            INNER JOIN faculty_selected ON fcontid = contid
			        WHERE fcontid = contid
			),
			web_selected AS (
			    SELECT fcontid, webid, url, webpage.description, true AS has_web 
			        FROM webpage
			            INNER JOIN faculty_selected ON fcontid = contid
					WHERE fcontid = contid
			)
			SELECT  *,
			        COALESCE(web_selected.has_web, false) AS has_webpage,
			        COALESCE(extension_selected.has_ext, false) AS has_extension
			    FROM faculty_selected
			        LEFT  JOIN web_selected       ON fcontid = contid
			        INNER JOIN extension_selected ON extension_selected.fcontid = contid
				
			OFFSET ?
			LIMIT  ?
			""", List.of(department, offset, limit));
		
		transaction.prepare()
			.executeThen(TransactionStatementType.SELECT_QUERY)
			.commit();
		
		// Close transaction
		transaction.forceClose();
		
		// Display errors
		for (TransactionError error : transaction.catchErrors()) {
			System.err.println(error);
			System.err.println("==============================");
		}
		
		return transaction.getLatestResult();
	}
	
	public SubTransactionResult getProfessor(EmailDTO email) {
		@SuppressWarnings("resource")
		Transaction transaction = new Transaction();
		
		transaction.submitSQL(
			"""
			WITH faculty_selected AS (
			    SELECT  facid,
			            contid, 
			            abreviation, 
			            faculty.name,
			            faculty.description, 
			            jobentitlement, 
			            office, 
			            email 
			        FROM faculty
			            INNER JOIN contact    ON fcontid = contid
			            INNER JOIN department ON fdepid  = depid
			), 
			extension_selected AS (
			    SELECT fcontid, extid, ext, true AS has_ext
			        FROM extension
			            INNER JOIN faculty_selected ON fcontid = contid
			        WHERE fcontid = contid
			),
			web_selected AS (
			    SELECT fcontid, webid, url, webpage.description, true AS has_web 
			        FROM webpage
			            INNER JOIN faculty_selected ON fcontid = contid
					WHERE fcontid = contid
			)
			SELECT  *,
			        COALESCE(web_selected.has_web, false) AS has_webpage,
			        COALESCE(extension_selected.has_ext, false) AS has_extension
			    FROM faculty_selected
			        LEFT  JOIN web_selected       ON fcontid = contid
			        INNER JOIN extension_selected ON extension_selected.fcontid = contid
			    WHERE
			        email = ?
			""", List.of(email.getEmail()));
		
		transaction.prepare()
			.executeThen(TransactionStatementType.SELECT_QUERY)
			.commit();
		
		// Close transaction
		transaction.forceClose();
		
		// Display errors
		for (TransactionError error : transaction.catchErrors()) {
			System.err.println(error);
			System.err.println("==============================");
		}
		
		return transaction.getLatestResult();
	}
	
	public int insertProfessor(FacultyDTO professor) {
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
				
				PreparedStatement stmt_extensi = connection.prepareStatement(SQL_INSERT_EXTENSION);
				PreparedStatement stmt_webpage = connection.prepareStatement(SQL_INSERT_WEBPAGE);
				PreparedStatement stmt_socialm = connection.prepareStatement(SQL_INSERT_SOCIALMEDIA);
				
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
}

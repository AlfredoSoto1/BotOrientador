/**
 * 
 */
package assistant.rest.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import assistant.database.SubTransactionResult;
import assistant.rest.dao.GameDAO;
import assistant.rest.dto.UserRankDTO;

/**
 * @author Alfredo
 */
@Service
public class GameService {
	
	private final GameDAO gameDAO;
	
	@Autowired
	public GameService(GameDAO gameDAO) {
		this.gameDAO = gameDAO;
	}
	
	public void updateCommandUserCount(String commandName, String user, long server) {
		gameDAO.queryUpdateCommandUserCount(commandName, user, server);
	}
	
	public Optional<UserRankDTO> giveXP(String user, int quantity, long server) {
		SubTransactionResult result = gameDAO.queryUpdateXP(user, quantity, server);
		
		if (result.isEmpty())
			return Optional.empty();
		
		UserRankDTO ranked = new UserRankDTO();
		ranked.setHasLevelup(result.getValue("level_increased"));
		ranked.setLevel(result.getValue("level"));
		ranked.setUserXP(result.getValue("message_xp"));
		ranked.setUsername(result.getValue("username"));
		ranked.setMilestone(result.getValue("xp_milestone"));
		ranked.setCommandsUsed(result.getValue("commands_used"));
		
		return Optional.of(ranked);
	}
	
	public List<UserRankDTO> getLeaderboard(long server) {
		SubTransactionResult result = gameDAO.queryLeaderboard(server);
		
		List<UserRankDTO> leaderboard = new ArrayList<>();
		for (int i = 0;i < result.rowCount(); i++) {
			UserRankDTO ranked = new UserRankDTO();
			ranked.setRank(result.getValue("rank"));
			ranked.setLevel(result.getValue("level"));
			ranked.setUserXP(result.getValue("message_xp"));
			ranked.setUsername(result.getValue("username"));
			ranked.setMilestone(result.getValue("xp_milestone"));
			ranked.setCommandsUsed(result.getValue("commands_used"));
			leaderboard.add(ranked);
		}
		return leaderboard;
	}
	
	public Optional<UserRankDTO> getUserLeaderboardPosition(String user, long server) {
		SubTransactionResult result = gameDAO.queryLeaderboardUserPosition(user, server);
		
		if (result.isEmpty())
			return Optional.empty();
		
		UserRankDTO ranked = new UserRankDTO();
		ranked.setRank(result.getValue("rank"));
		ranked.setLevel(result.getValue("level"));
		ranked.setUserXP(result.getValue("message_xp"));
		ranked.setUsername(result.getValue("username"));
		ranked.setMilestone(result.getValue("xp_milestone"));
		ranked.setCommandsUsed(result.getValue("commands_used"));
		
		return Optional.of(ranked);
	}
}

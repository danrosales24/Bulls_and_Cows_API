package bulls_cows.todoapi.data;

import bulls_cows.todoapi.models.game;
import bulls_cows.todoapi.models.rounds;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Profile("database")
public class gameDatabaseDao implements gameDao {

	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public gameDatabaseDao(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;

	}

	@Override
	@Transactional
	public String add(game game) {

		game.setanswer(game.random(game.getanswer()));

		final String INSERT_GAME = "INSERT INTO game(answer) VALUES(?)";

		int newId = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
		game.setgameId(newId);

		jdbcTemplate.update(INSERT_GAME, game.getanswer());

		return game.begin(game);
	}

	@Override
	public List<game> getAll() {
		final String sql = "SELECT gameID,answer,isFinished FROM game WHERE isFinished IS NOT NULL ;";
		return jdbcTemplate.query(sql, new gameMapper());
	}

	private static final class gameMapper implements RowMapper<game> {

		@Override
		public game mapRow(ResultSet rs, int index) throws SQLException {
			game td = new game();
			td.setgameId(rs.getInt("gameID"));
			td.setanswer(rs.getInt("answer"));
			td.setFinished(rs.getBoolean("isFinished"));
			return td;
		}
	}

	@Override
	public boolean update(game game) {

		final String sql = "UPDATE todo SET " + "finished = ? " + "WHERE id = ?;";

		return jdbcTemplate.update(sql, game.isFinished(), game.getgameId()) > 0;
	}

	@Transactional
	@Override
	public rounds roundadd(rounds rounds, game game) {

		final String INSERT_ROUND = "INSERT INTO round(gameID,guess) VALUES(?,?)";

		int newId = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
		game.setgameId(newId);

		jdbcTemplate.update(INSERT_ROUND, game.getgameId(), rounds.getguess());

		return rounds;

	}

	@Override
	public List<rounds> getALL() {
		// TODO Auto-generated method stub
		return null;
	}
	
	private static final class roundMapper implements RowMapper<rounds> {

		@Override
		public rounds mapRow(ResultSet rs, int index) throws SQLException {
			rounds td = new rounds();
			td.setguess(rs.getInt("guess"));
			td.setroundId(rs.getInt("roundID"));
			td.setFinished(rs.getBoolean("isFinished"));
			return td;
		}
	}
	

}

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
		final String sql = "SELECT gameID,isFinished,answer FROM game;";
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
	public rounds roundadd(rounds rounds) {

	    final String sql = "INSERT INTO round(gameID, guess) VALUES(?,?);";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update((Connection conn) -> {

            PreparedStatement statement = conn.prepareStatement(
                sql, 
                Statement.RETURN_GENERATED_KEYS);

            statement.setInt(1, rounds.getgameID());
            statement.setInt(2, rounds.getguess());
            return statement;

        }, keyHolder);

        rounds.setroundId(keyHolder.getKey().intValue());

        correct(rounds);
        
        return rounds;
	}

	@Override
	public List<rounds> getALLrounds(game game) {
		final String sql = "SELECT game.gameID,round.roundID,round.guess,round.ts FROM game RIGHT JOIN round ON game.gameID = round.gameID ORDER BY game.gameID;";
		return jdbcTemplate.query(sql, new roundMapper());
	}

	private static final class roundMapper implements RowMapper<rounds> {

		@Override
		public rounds mapRow(ResultSet rs, int index) throws SQLException {
			rounds td = new rounds();
			td.setguess(rs.getInt("guess"));
			td.setroundId(rs.getInt("roundID"));
			td.setgameID(rs.getInt("gameID"));
			td.setts(rs.getString("ts"));
			return td;
		}
	}

	@Override
	public game findById(int id) {

		final String sql = "SELECT gameID, answer, isFinished " + "FROM game WHERE gameID = ?;";

		return jdbcTemplate.queryForObject(sql, new gameMapper(), id);
	}

	@Override
	public String correct(rounds rounds) {
		
		int x =rounds.getgameID();
		
		game game = findById(x);
		if(game.getanswer()==rounds.getguess()) {
			return ("WINNNERRRRRRRRRRRRRRRRRRRRR");
		}else {
			return ("No good try again");
		}
		
		
	
	}


	

}

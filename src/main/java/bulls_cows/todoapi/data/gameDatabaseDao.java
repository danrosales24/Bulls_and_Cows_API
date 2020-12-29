package bulls_cows.todoapi.data;

import bulls_cows.todoapi.models.game;
import bulls_cows.todoapi.models.rounds;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
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
		final String sql = "SELECT gameID,answer,isFinished FROM game;";

		return jdbcTemplate.query(sql, new gameMapperA());

	}

	private static final class gameMapperA implements RowMapper<game> {

		@Override
		public game mapRow(ResultSet rs, int index) throws SQLException {
			game td = new game();
			td.setgameId(rs.getInt("gameID"));
			td.setanswer(rs.getInt("answer"));
			td.setFinished(rs.getBoolean("isFinished"));
				//if(td.isFinished()==false) {
					//td.setanswer(0);
				//}
			return td;
		}
	}

	@Override
	public boolean update(game game) {

		final String sql = "UPDATE game SET " + "isFinished = ? " + "WHERE gameID = ?;";
		return jdbcTemplate.update(sql, game.isFinished(), game.getgameId()) > 0;
	}

	@Transactional
	@Override
	public rounds roundadd(rounds rounds) {
		String guess = String.valueOf(rounds.getguess());
		int x = rounds.getgameID();
		game game = findById(x);
		String Answer = String.valueOf(game.getanswer());
		int bulls = 0;
		int cows = 0;
		int[] arr1 = new int[10];
		int[] arr2 = new int[10];

		for (int i = 0; i < Answer.length(); i++) {
			char c1 = Answer.charAt(i);
			char c2 = guess.charAt(i);

			if (c1 == c2)
				bulls++;
			else {
				arr1[c1 - '0']++;
				arr2[c2 - '0']++;
			}
		}
		for (int i = 0; i < 10; i++) {
			cows += Math.min(arr1[i], arr2[i]);
		}

		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		rounds.setBullsandCows("Amount of Bulls " + bulls + " Amount of Cows " + cows);
		String ts = (String.valueOf(timestamp));
		rounds.setts(ts);

		final String sql = "INSERT INTO round(gameID, guess,BullsandCows,ts) VALUES(?,?,?,?);";
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update((Connection conn) -> {

			PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

			statement.setInt(1, rounds.getgameID());
			statement.setInt(2, rounds.getguess());
			statement.setString(3, rounds.getBullsandCows());
			statement.setString(4, rounds.getts());

			return statement;

		}, keyHolder);

		rounds.setroundId(keyHolder.getKey().intValue());
		if (bulls == 4) {
			game.setFinished(true);
			update(game);
		}

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
		return jdbcTemplate.queryForObject(sql, new gameMapperA(), id);
	}

}

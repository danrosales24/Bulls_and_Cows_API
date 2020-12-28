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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

@Repository
@Profile("database")
public class gameDatabaseDao implements gameDao {

	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public gameDatabaseDao(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;

	}

	@Override
	public game add(game game) {
		String sql = "INSERT INTO actor(first_name,last_name) " + "VALUES(?,?)";

		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

		jdbcTemplate.update((Connection conn) -> {

			PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

			statement.setInt(1, game.getgameId());
			statement.setInt(2, game.getAnswer());
			return statement;

		}, keyHolder);

		game.setgameId(keyHolder.getKey().intValue());

		return game;

	}

	@Override
	public List<game> getAll() {
		final String sql = "SELECT gameID, answer,finished FROM game;";
		return jdbcTemplate.query(sql, new gameMapper());
	}

	 private static final class gameMapper implements RowMapper<game> {

	        @Override
	        public game mapRow(ResultSet rs, int index) throws SQLException {
	            game td = new game();
	            td.setgameId(rs.getInt("id"));
	            td.setanswer(rs.getInt("todo"));
	            td.setFinished(rs.getBoolean("finished"));
	            return td;
	        }
	    }
	
}
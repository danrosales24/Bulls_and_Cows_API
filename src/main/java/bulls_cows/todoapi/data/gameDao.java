package bulls_cows.todoapi.data;

import bulls_cows.todoapi.models.game;
import bulls_cows.todoapi.models.rounds;
import java.util.List;

public interface gameDao {

    game add(game game);

    List<game> getAll();

   // game findBygameId(int id);
    
    //rounds findByroundId(int id);

    // true if item exists and is updated
   // boolean update(game game);

    // true if item exists and is deleted
   // boolean deleteById(int id);
}
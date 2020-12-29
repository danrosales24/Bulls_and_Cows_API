package bulls_cows.todoapi.data;

import bulls_cows.todoapi.models.game;
import bulls_cows.todoapi.models.rounds;
import java.util.List;

public interface gameDao {

    String add(game game);

    List<game> getAll();
    
   rounds roundadd(rounds rounds);
    
    List<rounds> getALLrounds(game game);

    boolean update (game game);
    
    game findById(int id);

    // true if item exists and is updated
   String correct(rounds rounds);

    // true if item exists and is deleted
   // boolean deleteById(int id);
}
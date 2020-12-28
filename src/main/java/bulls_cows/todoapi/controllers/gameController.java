package bulls_cows.todoapi.controllers;

import bulls_cows.todoapi.data.gameDao;
import bulls_cows.todoapi.models.game;
import bulls_cows.todoapi.models.rounds;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/game")
public class gameController {

	private final gameDao dao;
	private rounds rounds;

	public gameController(gameDao dao) {
		this.dao = dao;
	}

	@GetMapping
	public List<game> all() {
		return dao.getAll();
	}

	@PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String create(game game) {
    	return dao.add(game);
   

    }

	@PostMapping("/{id}/{guess}")
	public ResponseEntity update(@PathVariable int id, @RequestBody game game) {
	    ResponseEntity response = new ResponseEntity(HttpStatus.NOT_FOUND);
		if (id != game.getgameId()) {
	        response = new ResponseEntity(HttpStatus.UNPROCESSABLE_ENTITY);
	    } else {
	    	dao.roundadd(rounds, game);
	    	response = new ResponseEntity(HttpStatus.ACCEPTED);
	    }
	    return response;
	}



}

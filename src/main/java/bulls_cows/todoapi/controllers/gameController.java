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
	

	public gameController(gameDao dao) {
		this.dao = dao;
	}

	@GetMapping
	public List<game> all() {
	return dao.getAll();
	}

	@PostMapping("/begin")
	@ResponseStatus(HttpStatus.CREATED)
	public String create(game game) {
		return dao.add(game);

	}

	@PostMapping("/guess")
	public rounds create(@RequestBody rounds rounds) {
		return dao.roundadd(rounds);
	}

	@GetMapping( "/{id}" )
	public ResponseEntity<game> findById(@PathVariable int id) {
		game result = dao.findById(id);
		if (result == null) {
			return new ResponseEntity(null, HttpStatus.NOT_FOUND);
		}
		return ResponseEntity.ok(result);
	}
}

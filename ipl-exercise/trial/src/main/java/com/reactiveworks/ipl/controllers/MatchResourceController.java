package com.reactiveworks.ipl.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reactiveworks.ipl.model.Match;
import com.reactiveworks.ipl.repository.MatchesRepository;

/**
 * controller class for the match resource.
 */
@RestController
@RequestMapping("/matches")
public class MatchResourceController {

	@Autowired
	private MatchesRepository repo;

	@GetMapping
	public List<Match> getMatches() {
		List<Match> matches=new ArrayList<>();
		repo.findAll().forEach(matches::add);
		return  matches;
	}

	@PostMapping
	public void insertMatch(@RequestBody Match match)  {
       repo.save(match);
	}

	

}

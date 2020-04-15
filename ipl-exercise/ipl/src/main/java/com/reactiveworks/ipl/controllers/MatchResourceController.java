package com.reactiveworks.ipl.controllers;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.reactiveworks.ipl.model.Match;
import com.reactiveworks.ipl.service.IPLService;
import com.reactiveworks.ipl.service.exceptions.InsufficientInformationException;
import com.reactiveworks.ipl.service.exceptions.MatchIdNotFoundException;
import com.reactiveworks.ipl.service.exceptions.MatchNotFoundException;

/**
 * controller class for the match resource.
 */
@RestController
@RequestMapping("/matches")
public class MatchResourceController {

	private static final Logger LOGGER_OBJ = LoggerFactory.getLogger(MatchResourceController.class);

	@Autowired
	private IPLService iplService;

	/**
	 * gets either all the matches from the database or all the matches in the
	 * season.
	 * 
	 * @param season   the season from which matches are required.
	 * @param pageNo   number of the page to be displayed.
	 * @param pageSize number of matches to be displayed in one page.
	 * @return the list of matches from the database.
	 * @throws MatchNotFoundException when match record is not available.
	 */
	@GetMapping(produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public CollectionModel<Match> getMatches(@RequestParam(defaultValue = "0") int season,
			@RequestParam(defaultValue = "0") int pageNo, @RequestParam(defaultValue = "5") int pageSize)
			throws MatchNotFoundException {
		LOGGER_OBJ.debug("execution of getMatches() started");
		List<Match> matches;

		if (season != 0) {
			matches = iplService.getMatchDetails(season);
		} else {
			matches = iplService.getAllMatchDetails(pageNo, pageSize);
		}

		for (Match match : matches) {
			List<Link> links = new ArrayList<>();
			Link link = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder.methodOn(MatchResourceController.class).getMatch(match.getMatchId()))
					.withSelfRel();
			links.add(link);
			link = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MatchResourceController.class)
					.updateMatchResource(match.getMatchId(), match)).withRel("update");
			links.add(link);
			link = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder.methodOn(MatchResourceController.class).deleteMatch(match.getMatchId()))
					.withRel("delete");
			links.add(link);
			match.add(links);

		}
		Link link = WebMvcLinkBuilder.linkTo(MatchResourceController.class).withSelfRel();
		CollectionModel<Match> result = new CollectionModel<Match>(matches, link);
		LOGGER_OBJ.debug("execution of getMatches() completed");
		return result;
	}

	/**
	 * gets match with the given id from the database.
	 * 
	 * @param matchId id of the match to be fetched from the database.
	 * @return the match with the given id.
	 * @throws MatchNotFoundException when match record is not available.
	 */
	@GetMapping(value = "/{matchId}", produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Match> getMatch(@PathVariable int matchId) throws MatchNotFoundException {
		LOGGER_OBJ.debug("execution of getMatch() started");

		Match match = iplService.getMatchById(matchId);
		List<Link> links = new ArrayList<>();
		Link link = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(MatchResourceController.class).getMatch(match.getMatchId()))
				.withSelfRel();
		links.add(link);
		link = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MatchResourceController.class)
				.updateMatchResource(match.getMatchId(), match)).withRel("update");
		links.add(link);
		link = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(MatchResourceController.class).deleteMatch(match.getMatchId()))
				.withRel("delete");
		links.add(link);
		link = WebMvcLinkBuilder.linkTo(MatchResourceController.class).withRel("matches");
		match.add(links);
		LOGGER_OBJ.debug("execution of getMatch() completed");
		return new ResponseEntity<Match>(match, HttpStatus.OK);

	}

	/**
	 * inserts match details into the database.
	 * 
	 * @param match the match object to be inserted into the database.
	 * @return the link to the record created and links for update and deletion of
	 *         the record.
	 * @throws MatchNotFoundException           when match record is not available.
	 * @throws MatchIdNotFoundException         when the id of the match is not
	 *                                          available.F
	 * @throws InsufficientInformationException when match object doesn't have the
	 *                                          required fields.
	 */
	@PostMapping(consumes = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<List<Link>> insertMatch(@RequestBody Match match)
			throws MatchNotFoundException, MatchIdNotFoundException, InsufficientInformationException {
		LOGGER_OBJ.debug("execution of insertMatch() started");
		List<Link> links = new ArrayList<>();
		iplService.insertMatchDetails(match);
		Link link = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(MatchResourceController.class).getMatch(match.getMatchId()))
				.withSelfRel();
		LOGGER_OBJ.info("link to the created resource is " + link);
		links.add(link);
		link = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MatchResourceController.class)
				.updateMatchResource(match.getMatchId(), match)).withRel("update");
		links.add(link);
		link = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(MatchResourceController.class).deleteMatch(match.getMatchId()))
				.withRel("delete");
		links.add(link);

		LOGGER_OBJ.debug("execution of insertMatch() started");
		return new ResponseEntity<List<Link>>(links, HttpStatus.CREATED);
	}

	/**
	 * updates the match record in the database.
	 * 
	 * @param matchId id of the match to be deleted.
	 * @param match   match object with the values to be updated.
	 * @return the status of the operation.
	 */
	@PutMapping(value = "/{matchId}", consumes = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<List<Link>> updateMatchResource(@PathVariable int matchId, @RequestBody Match match) {
		LOGGER_OBJ.debug("execution of updateMatchResource() started");
		iplService.updateMatch(matchId, match);
		List<Link> links = new ArrayList<>();
		Link link = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MatchResourceController.class)
				.updateMatchResource(matchId, match)).withSelfRel();
		links.add(link);
		link = WebMvcLinkBuilder.linkTo(MatchResourceController.class).slash(matchId).withRel("get");
		links.add(link);
		link = WebMvcLinkBuilder.linkTo(MatchResourceController.class).slash(matchId).withRel("delete");
		links.add(link);
		LOGGER_OBJ.debug("execution of updateMatchResource() completed");
		return new ResponseEntity<List<Link>>(links, HttpStatus.OK);
	}

	/**
	 * updates the match record in the database.
	 * 
	 * @param matchId id of the match to be deleted.
	 * @param match   match object with the values to be updated.
	 * @return the status of the operation.
	 * @throws MatchNotFoundException when the match with the given id is not
	 *                                available.
	 */
	@PatchMapping(value = "/{matchId}", consumes = { MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<List<Link>> updateMatchDetails(@PathVariable int matchId, @RequestBody Match match)
			throws MatchNotFoundException {
		LOGGER_OBJ.debug("execution of updateMatchDetails() started");
		match=iplService.updateMatchDetails(matchId, match);
		List<Link> links = new ArrayList<>();
		Link link = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MatchResourceController.class)
				.updateMatchResource(matchId, match)).withSelfRel();
		links.add(link);
		// for getting match according to the season
		link = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(MatchResourceController.class).getMatches(match.getSeason(), 0, 10))
				.withRel("get");
		links.add(link);
		link = WebMvcLinkBuilder.linkTo(MatchResourceController.class).slash(matchId).withRel("get");
		links.add(link);
		link = WebMvcLinkBuilder.linkTo(MatchResourceController.class).slash(matchId).withRel("delete");
		links.add(link);
		LOGGER_OBJ.debug("execution of updateMatchDetails() completed");
		return new ResponseEntity<List<Link>>(links, HttpStatus.OK);
	}

	/**
	 * deletes the record from the database.
	 * 
	 * @param matchId id of the match to be deleted.
	 * @return the status of the operation.
	 * @throws MatchNotFoundExceptions when match record is not available.
	 */
	@DeleteMapping("/{matchId}")
	public ResponseEntity<String> deleteMatch(@PathVariable int matchId) throws MatchNotFoundException {
		LOGGER_OBJ.debug("execution of deleteMatch() started");
		iplService.deleteMatch(matchId);
		LOGGER_OBJ.info("match with id is " + matchId + " is deleted");
		LOGGER_OBJ.debug("execution of deleteMatch() completed");

		return new ResponseEntity<String>("match with id " + matchId + " is deleted", HttpStatus.OK);
	}

}

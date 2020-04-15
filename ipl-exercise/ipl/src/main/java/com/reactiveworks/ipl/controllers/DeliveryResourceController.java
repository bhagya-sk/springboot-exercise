package com.reactiveworks.ipl.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
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

import com.reactiveworks.ipl.model.Delivery;
import com.reactiveworks.ipl.service.IPLService;
import com.reactiveworks.ipl.service.exceptions.DeliveryNotFoundException;
import com.reactiveworks.ipl.service.exceptions.InsufficientInformationException;
import com.reactiveworks.ipl.service.exceptions.MatchNotFoundException;

/**
 * controller class for the match resource.
 */
@RestController
@RequestMapping("/deliveries")
public class DeliveryResourceController {

	private static Logger LOGGER_OBJ = LoggerFactory.getLogger(DeliveryResourceController.class);
	@Autowired
	private IPLService iplService;

	/**
	 * finds the winning team with the given information.
	 * 
	 * @param matchId     id of the match.
	 * @param inning1     inning in the match.
	 * @param battingTeam name of the team which is batting.
	 * @return the name of the winning team .
	 * @throws DeliveryNotFoundException        when the delivery is not available.
	 * @throws MatchNotFoundException           when the match is not available.
	 * @throws InsufficientInformationException when the sufficient information is
	 *                                          not provided to find the winning
	 *                                          team name.
	 */
	@GetMapping
	public ResponseEntity<String> getWinningTeamName(@RequestParam(defaultValue = "0") int matchId,
			@RequestParam(defaultValue = "0") int inning1, @RequestParam(defaultValue = "null") String battingTeam)
			throws DeliveryNotFoundException, MatchNotFoundException, InsufficientInformationException {
		LOGGER_OBJ.debug("execution of the getDeliveries() started");
		String winningTeamName = iplService.getWinningTeamName(matchId, inning1, battingTeam);
		if (winningTeamName == null) {
			return new ResponseEntity<String>("result of a match is a tie", HttpStatus.OK);
		}
		LOGGER_OBJ.debug("execution of the getDeliveries() completed");
		return new ResponseEntity<String>(winningTeamName, HttpStatus.OK);
	}

	/**
	 * finds the delivery with the given id.
	 * 
	 * @param deliveryId id of the delivery to be fetched from the database.
	 * @return the delivery with the given id.
	 * @throws DeliveryNotFoundException when the delivery with the given id is not
	 *                                   found.
	 */
	@GetMapping("/{deliveryId}")
	public ResponseEntity<Delivery> getDelivery(@PathVariable int deliveryId) throws DeliveryNotFoundException {
		LOGGER_OBJ.debug("execution of the getDelivery() started");
		Delivery delivery = iplService.getDeliveryById(deliveryId);
		List<Link> links = new ArrayList<>();
		Link link = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(DeliveryResourceController.class).getDelivery(deliveryId))
				.withSelfRel();
		links.add(link);
		link = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(DeliveryResourceController.class).updateDelivery(deliveryId, null))
				.withRel("update");
		links.add(link);
		link = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(DeliveryResourceController.class).deleteDelivery(deliveryId))
				.withRel("delete");
		links.add(link);
		delivery.add(links);
		LOGGER_OBJ.debug("execution of the getDelivery() completed");
		return new ResponseEntity<Delivery>(delivery, HttpStatus.OK);
	}

	/**
	 * inserts deliveries into the database.
	 * 
	 * @param deliveries list of deliveries.
	 * @return the links to the created resources.
	 * @throws InsufficientInformationException when sufficient information to find
	 *                                          the result of match is not
	 *                                          available.
	 * @throws MatchNotFoundException           when match is not found .
	 */
	@PostMapping
	@Transactional
	public ResponseEntity<List<Link>> insertDeliveries(@RequestBody List<Delivery> deliveries)
			throws MatchNotFoundException, InsufficientInformationException {
		LOGGER_OBJ.debug("execution of the insertDeliveries() started");
		List<Link> links = new ArrayList<Link>();
		int num;
		try {
			num = iplService.getDeliveries().size();
		} catch (DeliveryNotFoundException e) {
			num = 0;
		}
		if (deliveries.size() == 1) {
			iplService.addDelivery(deliveries.get(0));
			Link link = WebMvcLinkBuilder.linkTo(DeliveryResourceController.class).slash(++num).withSelfRel();
			links.add(link);
			link = WebMvcLinkBuilder.linkTo(DeliveryResourceController.class).slash(num).withRel("delete");
			links.add(link);
			link = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder.methodOn(DeliveryResourceController.class).updateDelivery(num, null))
					.withRel("update");
			links.add(link);
			LOGGER_OBJ.info("link to the added delivery resource is " + link);
		} else {
			iplService.addDeliveries(deliveries);
			int count = deliveries.size();
			while (count > 0) {
				Link link = WebMvcLinkBuilder.linkTo(DeliveryResourceController.class).slash(++num).withSelfRel();
				links.add(link);
				link = WebMvcLinkBuilder.linkTo(DeliveryResourceController.class).slash(num).withRel("delete");
				links.add(link);
				link = WebMvcLinkBuilder
						.linkTo(WebMvcLinkBuilder.methodOn(DeliveryResourceController.class).updateDelivery(num, null))
						.withRel("update");
				links.add(link);
				count--;
			}
			LOGGER_OBJ.info(deliveries.size() + " records are added into the database.");
		}

		LOGGER_OBJ.debug("execution of the insertDeliveries() completed");
		return new ResponseEntity<List<Link>>(links, HttpStatus.CREATED);
	}

	/**
	 * updates the delivery.
	 * 
	 * @param deliveryId id of the delivery to be updated.
	 * @param delivery   the updated delivery.
	 * @return the status of the operation.
	 */
	@PutMapping("/{deliveryId}")
	public ResponseEntity<List<Link>> updateDelivery(@PathVariable int deliveryId, @RequestBody Delivery delivery) {
		LOGGER_OBJ.debug("execution of the updateDelivery() started");
		List<Link> links = new ArrayList<>();
		iplService.updateDelivery(deliveryId, delivery);
		Link link = WebMvcLinkBuilder.linkTo(
				WebMvcLinkBuilder.methodOn(DeliveryResourceController.class).updateDelivery(deliveryId, delivery))
				.withSelfRel();
		links.add(link);
		link = WebMvcLinkBuilder.linkTo(
				WebMvcLinkBuilder.methodOn(DeliveryResourceController.class).updateDelivery(deliveryId, delivery))
				.withRel("delete");
		links.add(link);
		link = WebMvcLinkBuilder.linkTo(
				WebMvcLinkBuilder.methodOn(DeliveryResourceController.class).updateDelivery(deliveryId, delivery))
				.withRel("get");
		links.add(link);

		LOGGER_OBJ.debug("execution of the updateDelivery() completed");
		return new ResponseEntity<List<Link>>(links, HttpStatus.OK);
	}

	/**
	 * updates the delivery.
	 * 
	 * @param deliveryId id of the delivery to be updated.
	 * @param delivery   the updated delivery.
	 * @return the status of the operation.
	 * @throws DeliveryNotFoundException if the delivery is not available.
	 */
	@PatchMapping("/{deliveryId}")
	public ResponseEntity<List<Link>> updateDeliveryDetails(@PathVariable int deliveryId,
			@RequestBody Delivery delivery) throws DeliveryNotFoundException {
		LOGGER_OBJ.debug("execution of the updateDeliveryDetails() started");
		List<Link> links = new ArrayList<Link>();
		iplService.updateDeliveryDetails(deliveryId, delivery);
		Link link = WebMvcLinkBuilder.linkTo(
				WebMvcLinkBuilder.methodOn(DeliveryResourceController.class).updateDelivery(deliveryId, delivery))
				.withSelfRel();
		links.add(link);
		link = WebMvcLinkBuilder.linkTo(
				WebMvcLinkBuilder.methodOn(DeliveryResourceController.class).updateDelivery(deliveryId, delivery))
				.withRel("delete");
		links.add(link);
		link = WebMvcLinkBuilder.linkTo(
				WebMvcLinkBuilder.methodOn(DeliveryResourceController.class).updateDelivery(deliveryId, delivery))
				.withRel("get");
		links.add(link);
		LOGGER_OBJ.debug("execution of the updateDeliveryDetails() completed");
		return new ResponseEntity<List<Link>>(links, HttpStatus.OK);
	}

	/**
	 * deletes the delivery with the given id from the database.
	 * 
	 * @param deliveryId id of the delivery to be deleted from the database.
	 * @return the status of the operation.
	 * @throws DeliveryNotFoundException when delivery with the given id is not
	 *                                   available.
	 */
	@DeleteMapping("/{deliveryId}")
	public ResponseEntity<String> deleteDelivery(@PathVariable int deliveryId) throws DeliveryNotFoundException {
		LOGGER_OBJ.debug("execution of the deleteDelivery() started");
		iplService.deleteDelivery(deliveryId);
		LOGGER_OBJ.debug("execution of the deleteDelivery() completed");
		return new ResponseEntity<String>("delivery with the id " + deliveryId + " is deleted", HttpStatus.OK);
	}

}

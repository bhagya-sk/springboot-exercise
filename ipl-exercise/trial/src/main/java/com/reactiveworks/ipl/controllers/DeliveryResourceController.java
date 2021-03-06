package com.reactiveworks.ipl.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reactiveworks.ipl.model.Delivery;
import com.reactiveworks.ipl.repository.DeliveriesRepository;

/**
 * controller class for the match resource.
 */
@RestController
@RequestMapping("/deliveries")
public class DeliveryResourceController {

	@Autowired
	private DeliveriesRepository repo;

	@GetMapping
	public List<Delivery> getDeliveries() {
		List<Delivery> deliveries = new ArrayList<Delivery>();
		repo.findAll().forEach(deliveries::add);
		return deliveries;
	}

	@PostMapping
	public void insertDelivery(@RequestBody Delivery delivery) {
		repo.save(delivery);
	}

	@GetMapping("/{matchId}")
	public List<Delivery> getDeliveriesById(@PathVariable int matchId) {
		List<Delivery> deliveries = new ArrayList<Delivery>();
		List<Delivery> result = new ArrayList<Delivery>();
		repo.findAll().forEach(deliveries::add);
		
		deliveries.stream().filter(d->d.getMatchId()==matchId).forEach(result::add);
		return deliveries;
	}

}

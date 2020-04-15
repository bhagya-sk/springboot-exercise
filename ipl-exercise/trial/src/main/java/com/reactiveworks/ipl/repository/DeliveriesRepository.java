package com.reactiveworks.ipl.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.reactiveworks.ipl.model.Delivery;

public interface DeliveriesRepository extends PagingAndSortingRepository<Delivery, Integer> {

	//public Delivery findByMatchIdAndInning(int matchId,int inning);
	//@Query(value = "select * from deliveries where matchId=?1")
}

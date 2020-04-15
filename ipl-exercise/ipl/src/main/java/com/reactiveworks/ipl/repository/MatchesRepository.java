package com.reactiveworks.ipl.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.reactiveworks.ipl.model.Match;

public interface MatchesRepository extends PagingAndSortingRepository<Match, Integer>{

	public List<Match> findBySeason(int season);
}

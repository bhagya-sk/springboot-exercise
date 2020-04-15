package com.reactiveworks.ipl.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.reactiveworks.ipl.model.Delivery;
import com.reactiveworks.ipl.model.Match;
import com.reactiveworks.ipl.repository.DeliveriesRepository;
import com.reactiveworks.ipl.repository.MatchesRepository;
import com.reactiveworks.ipl.service.exceptions.DeliveryNotFoundException;
import com.reactiveworks.ipl.service.exceptions.InsufficientInformationException;
import com.reactiveworks.ipl.service.exceptions.MatchIdNotFoundException;
import com.reactiveworks.ipl.service.exceptions.MatchNotFoundException;

/**
 * Provides ipl information service.
 */
@Service
public class IPLService {
	private Logger LOGGER_OBJ = LoggerFactory.getLogger(IPLService.class);

	@Autowired
	private MatchesRepository matchesRepository;

	@Autowired
	private DeliveriesRepository deliveriesRepository;

	/**
	 * gets the matches details which were played in the given season.
	 * 
	 * @param season season for which matches are required.
	 * @return the list of matches played in the season
	 * @throws MatchNotFoundException when match is not found.
	 */
	public List<Match> getMatchDetails(int season) throws MatchNotFoundException {
		LOGGER_OBJ.debug("execution of getMatchDetails() started");
		List<Match> matches = matchesRepository.findBySeason(season);
		if (matches.size() == 0) {
			LOGGER_OBJ.error("matches are not available for the given season");
			throw new MatchNotFoundException("matches are not available for the season " + season);
		}
		LOGGER_OBJ.debug("execution of getMatchDetails() completed");
		return matches;
	}

	/**
	 * gets all the matches from the database.
	 * 
	 * @param pageNo   number of the page to be displayed.
	 * @param pageSize number of matches to be displayed in the page.
	 * @return the list of matches.
	 * @throws MatchNotFoundException when the matches are not available.
	 */
	public List<Match> getAllMatchDetails(int pageNo, int pageSize) throws MatchNotFoundException {
		LOGGER_OBJ.debug("execution of getAllMatchDetails() started");
		if (pageNo > 0) {
			pageNo -= 1;
		}
		List<Match> matches = null;
		Pageable paging = PageRequest.of(pageNo, pageSize);
		Page<Match> pagedResult = matchesRepository.findAll(paging);
		if (pagedResult.hasContent() == false) {
			LOGGER_OBJ.error("matches are not available ");
			throw new MatchNotFoundException("matches are not available ");
		}
		matches = pagedResult.getContent();
		LOGGER_OBJ.debug("execution of getAllMatchDetails() completed");
		return matches;
	}

	/**
	 * gets match with the given id from the database.
	 * 
	 * @param matchId id of the match to be fetched from the database.
	 * @return the match with the given id from the database.
	 * @throws MatchNotFoundException when match with the given id is not available.
	 */
	public Match getMatchById(int matchId) throws MatchNotFoundException {
		LOGGER_OBJ.debug("execution of getAllMatchDetails() started");
		Match match = null;
		match = matchesRepository.findById(matchId).orElse(null);
		if (match == null) {
			throw new MatchNotFoundException("match with id " + matchId + " is not available ");
		}

		LOGGER_OBJ.debug("execution of getAllMatchDetails() completed");
		return match;
	}

	/**
	 * inserts match into the database.
	 * 
	 * @param match match object to be inserted into the database.
	 * @throws MatchIdNotFoundException         when the id of the match is not
	 *                                          available.
	 * @throws InsufficientInformationException when match object doesn't have the
	 *                                          required fields.
	 */
	public void insertMatchDetails(Match match) throws MatchIdNotFoundException, InsufficientInformationException {
		LOGGER_OBJ.debug("execution of insertMatchDetails() started");
		if (match.getMatchId() == 0) {
			throw new MatchIdNotFoundException("match id is required to store the match record");
		}
		if (match.getMatchId() != 0 && match.getSeason() != 0 && match.getCity() != null && match.getDate() != null
				&& match.getTeam1() != null && match.getTeam2() != null) {
			matchesRepository.save(match);
		} else {
			throw new InsufficientInformationException(
					"matchId, season, city, date, team1 and team2 are required for match object insertion");
		}

		LOGGER_OBJ.debug("execution of insertMatchDetails() completed");

	}

	/**
	 * updates the match details.
	 * 
	 * @param matchId id of the match to be updated.
	 * @param match   to be updated.
	 */
	public void updateMatch(int matchId, Match match) {
		LOGGER_OBJ.debug("execution of updateMatch() started");
		Match matchObj = matchesRepository.findById(matchId).orElse(null);
		if (matchObj == null) {
			match.setMatchId(matchId);
			matchesRepository.save(match);
		} else {
			if (match.getDate() != null) {
				matchObj.setDate(match.getDate());
			}
			if (match.getCity() != null) {
				matchObj.setCity(match.getCity());
			}
			if (match.getSeason() != 0) {
				matchObj.setSeason(match.getSeason());
			}
			if (match.getTeam1() != null) {
				matchObj.setTeam1(match.getCity());
			}
			if (match.getTeam2() != null) {
				matchObj.setTeam2(match.getTeam2());
			}
			if (match.getTossDecision() != null) {
				matchObj.setTossDecision(match.getTossDecision());
			}
			if (match.getWinner() != null) {
				matchObj.setWinner(match.getWinner());
			}
			if (match.getResult() != null) {
				matchObj.setResult(match.getResult());
			}
			matchesRepository.save(matchObj);
		}
		LOGGER_OBJ.debug("execution of updateMatch() completed");

	}

	/**
	 * updates the match details.
	 * 
	 * @param matchId id of the match to be updated.
	 * @param match   to be updated.
	 * @throws MatchNotFoundException when match with the given id not available.
	 */
	public Match updateMatchDetails(int matchId, Match match) throws MatchNotFoundException {
		LOGGER_OBJ.debug("execution of updateMatchDetails() started");
		Match matchObj = matchesRepository.findById(matchId).orElse(null);
		if (matchObj == null) {
			throw new MatchNotFoundException("match with id " + matchId + " is not available for updation.");
		} else {
			if (match.getDate() != null) {
				matchObj.setDate(match.getDate());
			}
			if (match.getCity() != null) {
				matchObj.setCity(match.getCity());
			}
			if (match.getSeason() != 0) {
				matchObj.setSeason(match.getSeason());
			}
			if (match.getTeam1() != null) {
				matchObj.setTeam1(match.getCity());
			}
			if (match.getTeam2() != null) {
				matchObj.setTeam2(match.getTeam2());
			}
			if (match.getTossDecision() != null) {
				matchObj.setTossDecision(match.getTossDecision());
			}
			if (match.getWinner() != null) {
				matchObj.setWinner(match.getWinner());
			}
			if (match.getResult() != null) {
				matchObj.setResult(match.getResult());
			}
			matchesRepository.save(matchObj);
		}
		LOGGER_OBJ.debug("execution of updateMatchDetails() completed");
		return matchObj;

	}

	/**
	 * deletes the match from the database.
	 * 
	 * @param matchId id of the to be deleted.
	 * @throws MatchNotFoundException match with the id is not available.
	 */
	public void deleteMatch(int matchId) throws MatchNotFoundException {
		LOGGER_OBJ.debug("execution of deleteMatch() started");
		getMatchById(matchId);
		matchesRepository.deleteById(matchId);
		LOGGER_OBJ.debug("execution of deleteMatch() completed");
	}

	/**
	 * gets all the deliveries from the database.
	 * 
	 * @param pageNo   number of the page for display.
	 * @param pageSize the number of deliveries that should be displayed in one
	 *                 page.
	 * @return the list of deliveries.
	 * @throws DeliveryNotFoundException when the delivery is not available.
	 */
	public List<Delivery> getDeliveries() throws DeliveryNotFoundException {
		LOGGER_OBJ.debug("execution of getDelivery() started");

		List<Delivery> deliveries = new ArrayList<Delivery>();
		deliveriesRepository.findAll().forEach(deliveries::add);
		if (deliveries.size() != 0) {
			return deliveries;
		} else {
			LOGGER_OBJ.error("deliveries are not available.");
			throw new DeliveryNotFoundException("deliveries are not available.");
		}
	}

	/**
	 * gets the winner of the match.
	 * 
	 * @param matchId     id of the match.
	 * @param inning1     inning of the match.
	 * @param battingTeam name playing team.
	 * @return the name of the team which won the match.
	 * @throws MatchNotFoundException           if the match is not available.
	 * @throws InsufficientInformationException when the sufficient information is
	 *                                          not provided to find the winning
	 *                                          team name.
	 */
	public String getWinningTeamName(int matchId, int inning1, String battingTeam)
			throws MatchNotFoundException, InsufficientInformationException {
		LOGGER_OBJ.debug("execution of getDelivery() started");
		if (matchId == 0 || inning1 == 0 || battingTeam == null) {
			throw new InsufficientInformationException(
					"matchId, inning1 and battingTeam are required fields to find the winning team name");
		}
		Match match = matchesRepository.findById(matchId).orElse(null);
		String winningTeamName;
		if (match == null) {
			throw new MatchNotFoundException("no match available for the given id");
		}
		int team1Score = getDeliveriesById(matchId).stream().filter(d -> d.getInning() == inning1)
				.mapToInt(d -> d.getTotalRuns()).sum();
		int team2Score = getDeliveriesById(matchId).stream().filter(d -> d.getInning() == (3 - inning1))
				.mapToInt(d -> d.getTotalRuns()).sum();

		if (team1Score > team2Score) {

			winningTeamName = battingTeam;
		} else if (team1Score < team2Score) {
			winningTeamName = getDeliveriesById(matchId).stream().filter(d -> d.getInning() == inning1).findAny().get()
					.getBowlingTeam();
		} else {
			winningTeamName = null;
			LOGGER_OBJ.info("result of the match is a tie.");
		}

		LOGGER_OBJ.debug("execution of getDelivery() completed");
		return winningTeamName;

	}

	/**
	 * gets the deliveries by the match id.
	 * 
	 * @param matchId id of the match whose deliveries has to be fetched.
	 * @return the list of deliveries of the given matchId.
	 */
	private List<Delivery> getDeliveriesById(int matchId) {
		LOGGER_OBJ.debug("execution of getDeliveriesById() started");
		List<Delivery> deliveries = new ArrayList<Delivery>();
		List<Delivery> result = new ArrayList<Delivery>();
		deliveriesRepository.findAll().forEach(deliveries::add);
		deliveries.stream().filter(d -> d.getMatchId() == matchId).forEach(result::add);
		LOGGER_OBJ.debug("execution of getDeliveriesById() completed");
		return result;
	}

	/**
	 * gets the delivery by the given id.
	 * 
	 * @param deliveryId id of the delivery to be fetched.
	 * @return the delivery with the given id.
	 * @throws DeliveryNotFoundException when the delivery is not available.
	 */
	public Delivery getDeliveryById(int deliveryId) throws DeliveryNotFoundException {
		LOGGER_OBJ.debug("execution of getDeliveryById() started");
		Delivery delivery = deliveriesRepository.findById(deliveryId).orElse(null);
		if (delivery == null) {
			throw new DeliveryNotFoundException("delivery with id " + deliveryId + " is not available");
		}
		LOGGER_OBJ.debug("execution of getDeliveryById() completed");
		return delivery;
	}

	/**
	 * inserts delivery into the database.
	 * 
	 * @param delivery delivery to be inserted into the database.
	 * @throws InsufficientInformationException when sufficient information to find
	 *                                          the result of match is not
	 *                                          available.
	 * @throws MatchNotFoundException           when match is not found .
	 */
	public void addDelivery(Delivery delivery) throws MatchNotFoundException, InsufficientInformationException {

		LOGGER_OBJ.debug("execution of addDelivery() started");
		System.out.println(delivery);
		if (delivery.getMatchId() != 0 && delivery.getBall() != 0 && delivery.getBatsman() != null
				&& delivery.getBattingTeam() != null && delivery.getBowler() != null
				&& delivery.getBowlingTeam() != null && delivery.getInning() != 0 && delivery.getOver() != 0) {

			deliveriesRepository.save(delivery);

			String winner = getWinningTeamName(delivery.getMatchId(), delivery.getInning(), delivery.getBattingTeam());
			Match match = new Match();
			if (winner == null) {
				match.setResult("tie");
			} else {
				match.setWinner(winner);
				match.setResult("normal");
			}
			updateMatch(delivery.getMatchId(), match);

		} else {
			throw new InsufficientInformationException(
					"matchId, ball, batsman, battingTeam, bowler, bowlingTeam, inning, over are mandatory fields for the insertion of delivery");
		}

		LOGGER_OBJ.debug("execution of addDelivery() completed");

	}

	/**
	 * inserts deliveries into the database
	 * 
	 * @param deliveries list of deliveries.
	 * @throws InsufficientInformationException when sufficient information to find
	 *                                          the result of match is not
	 *                                          available.
	 * @throws MatchNotFoundException           when match is not found .
	 */
	@Transactional(value = TxType.REQUIRED) // test this method
	public void addDeliveries(List<Delivery> deliveries)
			throws MatchNotFoundException, InsufficientInformationException {
		LOGGER_OBJ.debug("execution of addDeliveries() started");

		for (Delivery delivery : deliveries) {

			if (!(delivery.getMatchId() != 0 && delivery.getBall() != 0 && delivery.getBatsman() != null
					&& delivery.getBattingTeam() != null && delivery.getBowler() != null
					&& delivery.getBowlingTeam() != null && delivery.getInning() != 0 && delivery.getOver() != 0)) {

				throw new InsufficientInformationException(
						"matchId, ball, batsman, battingTeam, bowler, bowlingTeam, inning, over are mandatory fields for the insertion of delivery information: delivery-"
								+ (deliveries.indexOf(delivery) + 1) + " the doesn't have all the mandatory fields");
			}

		}
		deliveriesRepository.saveAll(deliveries);
		for (Delivery delivery : deliveries) {
			String winner = getWinningTeamName(delivery.getMatchId(), delivery.getInning(), delivery.getBattingTeam());
			Match match = new Match();
			if (winner == null) {
				match.setResult("tie");
			} else {
				match.setWinner(winner);
				match.setResult("normal");
			}
			updateMatch(delivery.getMatchId(), match);
		}

		LOGGER_OBJ.debug("execution of addDeliveries() completed");
	}

	/**
	 * updates the delivery details if the delivery exists otherwise adds delivery
	 * into the database.
	 * 
	 * @param deliveryId id of the delivery to be updated.
	 * @param delivery   delivery object with the fields to be updated.
	 */
	public void updateDelivery(int deliveryId, Delivery delivery) {
		LOGGER_OBJ.debug("execution of updateDelivery() started");
		Delivery deliveryObj = deliveriesRepository.findById(deliveryId).orElse(null);

		if (deliveryObj == null) {

			delivery.setDeliveryId(deliveryId);
			deliveriesRepository.save(delivery);
			LOGGER_OBJ.info("delivery object with id " + deliveryId
					+ " is not available. delivery object is added into the database");
		} else {
			deliveryObj.setDeliveryId(deliveryId);
			if (delivery.getMatchId() != 0) {
				deliveryObj.setMatchId(delivery.getMatchId());
			}
			if (delivery.getInning() != 0) {
				deliveryObj.setInning(delivery.getInning());
			}
			if (delivery.getBattingTeam() != null) {
				deliveryObj.setBattingTeam(delivery.getBattingTeam());
			}
			if (delivery.getBowlingTeam() != null) {
				deliveryObj.setBowlingTeam(delivery.getBowlingTeam());
			}
			if (delivery.getOver() != 0) {
				deliveryObj.setOver(delivery.getOver());
			}
			if (delivery.getBall() != 0) {
				deliveryObj.setBall(delivery.getBall());
			}
			if (delivery.getBatsman() != null) {
				deliveryObj.setBatsman(delivery.getBatsman());
			}
			if (delivery.getBowler() != null) {
				deliveryObj.setBowler(delivery.getBowler());
			}
			if (delivery.getWideRuns() != 0) {
				deliveryObj.setWideRuns(delivery.getWideRuns());
			}
			if (delivery.getByeRuns() != 0) {
				deliveryObj.setByeRuns(delivery.getByeRuns());
			}
			if (delivery.getLegbyeRuns() != 0) {
				deliveryObj.setLegbyeRuns(delivery.getLegbyeRuns());
			}
			if (delivery.getNoballRuns() != 0) {
				deliveryObj.setNoballRuns(delivery.getNoballRuns());
			}
			if (delivery.getPenaltyRuns() != 0) {
				deliveryObj.setPenaltyRuns(delivery.getPenaltyRuns());
			}
			if (delivery.getBatsmanRuns() != 0) {
				deliveryObj.setBatsmanRuns(delivery.getBatsmanRuns());
			}
			if (delivery.getExtraRuns() != 0) {
				deliveryObj.setExtraRuns(delivery.getExtraRuns());
			}
			if (delivery.getTotalRuns() != 0) {
				deliveryObj.setTotalRuns(delivery.getTotalRuns());
			}
			deliveriesRepository.save(deliveryObj);
			LOGGER_OBJ.info("delivery object with id " + deliveryId + " is updated");
		}

		LOGGER_OBJ.debug("execution of updateDelivery() completed");

	}

	/**
	 * deletes the delivery from the database.
	 * 
	 * @param deliveryId id of the delivery to be deleted.
	 * @throws DeliveryNotFoundException delivery with the given id is not
	 *                                   available.
	 */
	public void deleteDelivery(int deliveryId) throws DeliveryNotFoundException {

		LOGGER_OBJ.debug("execution of updateDelivery() started");
		Delivery delivery = deliveriesRepository.findById(deliveryId).orElse(null);
		if (delivery == null) {
			throw new DeliveryNotFoundException("delivery with id " + deliveryId + " is not available");
		}
		deliveriesRepository.deleteById(deliveryId);
		LOGGER_OBJ.info("delivery object with id " + deliveryId + " is deleted.");
		LOGGER_OBJ.debug("execution of updateDelivery() completed");
	}

	/**
	 * updates the delivery details of the delivery with the given id from the
	 * database.
	 * 
	 * @param deliveryId id of the delivery to be updated.
	 * @param delivery   delivery object with the fields to be updated.
	 */
	public void updateDeliveryDetails(int deliveryId, Delivery delivery) throws DeliveryNotFoundException {

		LOGGER_OBJ.debug("execution of updateDeliveryDetails() started");
		Delivery deliveryObj = deliveriesRepository.findById(deliveryId).orElse(null);

		if (deliveryObj == null) {
			throw new DeliveryNotFoundException(
					"delivery with the given id " + deliveryId + " is not available for updation");
		} else {
			deliveryObj.setDeliveryId(deliveryId);
			if (delivery.getMatchId() != 0) {
				deliveryObj.setMatchId(delivery.getMatchId());
			}
			if (delivery.getInning() != 0) {
				deliveryObj.setInning(delivery.getInning());
			}
			if (delivery.getBattingTeam() != null) {
				deliveryObj.setBattingTeam(delivery.getBattingTeam());
			}
			if (delivery.getBowlingTeam() != null) {
				deliveryObj.setBowlingTeam(delivery.getBowlingTeam());
			}
			if (delivery.getOver() != 0) {
				deliveryObj.setOver(delivery.getOver());
			}
			if (delivery.getBall() != 0) {
				deliveryObj.setBall(delivery.getBall());
			}
			if (delivery.getBatsman() != null) {
				deliveryObj.setBatsman(delivery.getBatsman());
			}
			if (delivery.getBowler() != null) {
				deliveryObj.setBowler(delivery.getBowler());
			}
			if (delivery.getWideRuns() != 0) {
				deliveryObj.setWideRuns(delivery.getWideRuns());
			}
			if (delivery.getByeRuns() != 0) {
				deliveryObj.setByeRuns(delivery.getByeRuns());
			}
			if (delivery.getLegbyeRuns() != 0) {
				deliveryObj.setLegbyeRuns(delivery.getLegbyeRuns());
			}
			if (delivery.getNoballRuns() != 0) {
				deliveryObj.setNoballRuns(delivery.getNoballRuns());
			}
			if (delivery.getPenaltyRuns() != 0) {
				deliveryObj.setPenaltyRuns(delivery.getPenaltyRuns());
			}
			if (delivery.getBatsmanRuns() != 0) {
				deliveryObj.setBatsmanRuns(delivery.getBatsmanRuns());
			}
			if (delivery.getExtraRuns() != 0) {
				deliveryObj.setExtraRuns(delivery.getExtraRuns());
			}
			if (delivery.getTotalRuns() != 0) {
				deliveryObj.setTotalRuns(delivery.getTotalRuns());
			}
			deliveriesRepository.save(deliveryObj);
			LOGGER_OBJ.info("delivery object with id " + deliveryId + " is updated");
		}

		LOGGER_OBJ.debug("execution of updateDeliveryDetails() completed");

	}

}

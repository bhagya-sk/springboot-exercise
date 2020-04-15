package com.reactiveworks.ipl.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.hateoas.RepresentationModel;

/**
 * Represents the each Delivery in the IPL match.
 */
@XmlRootElement	
@Entity
@Table(name = "deliveries")
public class Delivery extends RepresentationModel<Delivery> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "deliveryId")
	private int deliveryId;
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Match.class)
	@JoinColumn(name = "matchId")
	private Match matchId = new Match();
	private int inning;
	private String battingTeam;
	private String bowlingTeam;
	@Column(name = "overs")
	private double over;
	private int ball;
	private String batsman;
	private String bowler;
	private int wideRuns;
	private int byeRuns;
	private int legbyeRuns;
	private int noballRuns;
	private int penaltyRuns;
	private int batsmanRuns;
	private int extraRuns;
	private int totalRuns;

	public int getDeliveryId() {
		return deliveryId;
	}

	public void setDeliveryId(int deliveryId) {
		this.deliveryId = deliveryId;
	}

	/**
	 * Gets the inning of the delivery.
	 * 
	 * @return the current value of the inning.
	 */
	public int getInning() {
		return inning;
	}

	/**
	 * Sets the value of the inning of the delivery.
	 * 
	 * @param inning new value of the inning.
	 */
	public void setInning(int inning) {
		this.inning = inning;
	}

	public int getMatchId() {
		return matchId.getMatchId();
	}

	public void setMatchId(int matchId) {
		this.matchId.setMatchId(matchId);
		;
	}

	/**
	 * Gets the battingTeam of the delivery.
	 * 
	 * @return the current value of the battingTeam.
	 */
	public String getBattingTeam() {
		return battingTeam;
	}

	/**
	 * Sets the value of the battingTeam of the delivery.
	 * 
	 * @param battingTeam new value of the battingTeam.
	 */
	public void setBattingTeam(String battingTeam) {
		this.battingTeam = battingTeam;
	}

	/**
	 * Gets the bowlingTeam of the delivery.
	 * 
	 * @return the current value of the bowlingTeam.
	 */
	public String getBowlingTeam() {
		return bowlingTeam;
	}

	/**
	 * Sets the value of the bowlingTeam of the delivery.
	 * 
	 * @param bowlingTeam new value of the bowlingTeam.
	 */
	public void setBowlingTeam(String bowlingTeam) {
		this.bowlingTeam = bowlingTeam;
	}

	/**
	 * Gets the over of the delivery.
	 * 
	 * @return the current value of the over.
	 */
	public double getOver() {
		return over;
	}

	/**
	 * Sets the value of the over of the delivery.
	 * 
	 * @param over new value of the over.
	 */
	public void setOver(double over) {
		this.over = over;
	}

	/**
	 * Gets the ball of the delivery.
	 * 
	 * @return the current value of the ball.
	 */
	public int getBall() {
		return ball;
	}

	/**
	 * Sets the value of the ball of the delivery.
	 * 
	 * @param ball new value of the ball.
	 */
	public void setBall(int ball) {
		this.ball = ball;
	}

	/**
	 * Gets the batsman of the delivery.
	 * 
	 * @return the current value of the batsman.
	 */
	public String getBatsman() {
		return batsman;
	}

	/**
	 * Sets the value of the batsman of the delivery.
	 * 
	 * @param batsman new value of the batsman.
	 */
	public void setBatsman(String batsman) {
		this.batsman = batsman;
	}

	/**
	 * Gets the bowler of the delivery.
	 * 
	 * @return the current value of the bowler.
	 */
	public String getBowler() {
		return bowler;
	}

	/**
	 * Sets the value of the bowler of the delivery.
	 * 
	 * @param bowler new value of the bowler.
	 */
	public void setBowler(String bowler) {
		this.bowler = bowler;
	}

	/**
	 * Gets the wideRuns of the delivery.
	 * 
	 * @return the current value of the wideRuns.
	 */
	public int getWideRuns() {
		return wideRuns;
	}

	/**
	 * Sets the value of the wideRuns of the delivery.
	 * 
	 * @param wideRuns new value of the wideRuns.
	 */
	public void setWideRuns(int wideRuns) {
		this.wideRuns = wideRuns;
	}

	/**
	 * Gets the byeRuns of the delivery.
	 * 
	 * @return the current value of the byeRuns.
	 */
	public int getByeRuns() {
		return byeRuns;
	}

	/**
	 * Sets the value of the byeRuns of the delivery.
	 * 
	 * @param byeRuns new value of the byeRuns.
	 */
	public void setByeRuns(int byeRuns) {
		this.byeRuns = byeRuns;
	}

	/**
	 * Gets the legbyeRuns of the delivery.
	 * 
	 * @return the current value of the legbyeRuns.
	 */
	public int getLegbyeRuns() {
		return legbyeRuns;
	}

	/**
	 * Sets the value of the legbyeRuns of the delivery.
	 * 
	 * @param legbyeRuns new value of the legbyeRuns.
	 */
	public void setLegbyeRuns(int legbyeRuns) {
		this.legbyeRuns = legbyeRuns;
	}

	/**
	 * Gets the noballRuns of the delivery.
	 * 
	 * @return the current value of the noballRuns.
	 */
	public int getNoballRuns() {
		return noballRuns;
	}

	/**
	 * Sets the value of the noballRuns of the delivery.
	 * 
	 * @param noballRuns new value of the noballRuns.
	 */
	public void setNoballRuns(int noballRuns) {
		this.noballRuns = noballRuns;
	}

	/**
	 * Gets the penaltyRuns of the delivery.
	 * 
	 * @return the current value of the penaltyRuns.
	 */
	public int getPenaltyRuns() {
		return penaltyRuns;
	}

	/**
	 * Sets the value of the penaltyRuns of the delivery.
	 * 
	 * @param penaltyRuns new value of the penaltyRuns.
	 */
	public void setPenaltyRuns(int penaltyRuns) {
		this.penaltyRuns = penaltyRuns;
	}

	/**
	 * Gets the batsmanRuns of the delivery.
	 * 
	 * @return the current value of the batsmanRuns.
	 */
	public int getBatsmanRuns() {
		return batsmanRuns;
	}

	/**
	 * Sets the value of the batsmanRuns of the delivery.
	 * 
	 * @param batsmanRuns new value of the batsmanRuns.
	 */
	public void setBatsmanRuns(int batsmanRuns) {
		this.batsmanRuns = batsmanRuns;
	}

	/**
	 * Gets the extraRuns of the delivery.
	 * 
	 * @return the current value of the extraRuns.
	 */
	public int getExtraRuns() {
		return extraRuns;
	}

	/**
	 * Sets the value of the extraRuns of the delivery.
	 * 
	 * @param extraRuns new value of the extraRuns.
	 */
	public void setExtraRuns(int extraRuns) {
		this.extraRuns = extraRuns;
	}

	/**
	 * Gets the totalRuns of the delivery.
	 * 
	 * @return the current value of the totalRuns.
	 */
	public int getTotalRuns() {
		return totalRuns;
	}

	/**
	 * Sets the value of the totalRuns of the delivery.
	 * 
	 * @param totalRuns new value of the totalRuns.
	 */
	public void setTotalRuns(int totalRuns) {
		this.totalRuns = totalRuns;
	}

	/**
	 * Creates the String representation of the object.
	 */
	@Override
	public String toString() {
		return "Delivery [deliveryId=" + deliveryId + ", inning=" + inning + ", battingTeam=" + battingTeam
				+ ", bowlingTeam=" + bowlingTeam + ", over=" + over + ", ball=" + ball + ", batsman=" + batsman
				+ ", bowler=" + bowler + ", wideRuns=" + wideRuns + ", byeRuns=" + byeRuns + ", legbyeRuns="
				+ legbyeRuns + ", noballRuns=" + noballRuns + ", penaltyRuns=" + penaltyRuns + ", batsmanRuns="
				+ batsmanRuns + ", extraRuns=" + extraRuns + ", totalRuns=" + totalRuns + "]";
	}

}

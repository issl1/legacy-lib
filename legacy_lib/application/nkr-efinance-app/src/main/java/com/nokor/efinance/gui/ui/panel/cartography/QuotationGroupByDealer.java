package com.nokor.efinance.gui.ui.panel.cartography;

import com.nokor.efinance.core.dealer.model.Dealer;

public class QuotationGroupByDealer {
	
	private Dealer dealer;
	private int numApproved;
	private int numRejected;
	private int numDeclined;
	private int numProposal;
	private int numTotal;

	/**
	 * @return the dealer
	 */
	public Dealer getDealer() {
		return dealer;
	}

	/**
	 * @param dealer
	 *            the dealer to set
	 */
	public void setDealer(Dealer dealer) {
		this.dealer = dealer;
	}

	/**
	 * @return the numApproved
	 */
	public int getNumApproved() {
		return numApproved;
	}

	/**
	 * @param numApproved
	 *            the numApproved to set
	 */
	public void setNumApproved(int numApproved) {
		this.numApproved = numApproved;
	}

	/**
	 * @return the numRejected
	 */
	public int getNumRejected() {
		return numRejected;
	}

	/**
	 * @param numRejected
	 *            the numRejected to set
	 */
	public void setNumRejected(int numRejected) {
		this.numRejected = numRejected;
	}

	/**
	 * @return the numDeclined
	 */
	public int getNumDeclined() {
		return numDeclined;
	}

	/**
	 * @param numDeclined
	 *            the numDeclined to set
	 */
	public void setNumDeclined(int numDeclined) {
		this.numDeclined = numDeclined;
	}

	/**
	 * @return the numProposal
	 */
	public int getNumProposal() {
		return numProposal;
	}

	/**
	 * @param numProposal
	 *            the numProposal to set
	 */
	public void setNumProposal(int numProposal) {
		this.numProposal = numProposal;
	}

	/**
	 * @return the numTotal
	 */
	public int getNumTotal() {
		return numTotal;
	}

	/**
	 * @param numTotal the numTotal to set
	 */
	public void setNumTotal(int numTotal) {
		this.numTotal = numTotal;
	}
}

package com.nokor.finance.services.shared;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nokor.finance.services.exception.InvalidCashFlowLayoutException;

/**
 * 
 * @author youhort.ly
 *
 */
public class CashFlowLayout implements Serializable {
	/** */
	private static final long serialVersionUID = 5973801537466015432L;

	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * Selected Payments position.
	 */
	private PaymentsPosition paymentsPosition = PaymentsPosition.END_OF_PERIODS;
	/**
	 * Selected First Payment position.
	 */
	private FirstPaymentPosition firstPaymentPosition = FirstPaymentPosition.AFTER_HOLIDAY;
	/**
	 * Selected First Payment Kind.
	 */
	private FirstPaymentKind firstPaymentKind = FirstPaymentKind.PERIODIC_PAYMENT_DEPENDANT;
	/**
	 * Selected Final Value presence.
	 */
	private FinalValuePresence finalValuePresence = FinalValuePresence.NOT_PRESENT;

	private boolean firstPaymentAsDeposit = false;

	private boolean hAddedToN = false;

	private boolean fvAddedToN = false;

	/**
	 * Selected Interests configuration during the Holiday Period.
	 */
	private InterestDuringHolidayPeriod interestInHolidayPeriod = InterestDuringHolidayPeriod.INTEREST_CAPITALIZED;

	/**
	 * Base Constructor. <br/>
	 * Please note that the base configuration is :
	 * <ul>
	 * <li>Payments are at the end of periods</li>
	 * <li>First Payment is after holiday period</li>
	 * <li>First Payment is Periodic Payment dependent</li>
	 * <li>Final Value is not present</li>
	 * <li>Holiday periods are included in the number of periods</li>
	 * <li>Final value period is included in the number of periods</li>
	 * <li>Interests Capitalized during the Holiday Period</li>
	 * </ul>
	 */
	public CashFlowLayout() {
		super();
	}

	/**
	 * Set the selected payment position.
	 * 
	 * @param paymentsPosition
	 *            new payments position
	 */
	public void setPaymentsPostion(PaymentsPosition paymentsPosition) {
		this.paymentsPosition = paymentsPosition;
	}

	/**
	 * Get the selected payments position. <br/>
	 * Please note that the default value is {@link CashFlow#PaymentsPosition
	 * #END_OF_PERIODS}
	 * 
	 * @return selected payments position
	 */
	public PaymentsPosition getPaymentsPosition() {
		return this.paymentsPosition;
	}

	/**
	 * Set the selected first payment position.
	 * 
	 * @param firstPaymentPosition
	 *            new first payment position
	 */
	public void setFirstPaymentPosition(
			FirstPaymentPosition firstPaymentPosition) {
		this.firstPaymentPosition = firstPaymentPosition;
	}

	/**
	 * Get the selected first payment position. <br/>
	 * Please note that the default value is
	 * {@link CashFlowLayout#FirstPaymentPosition#AFTER_HOLIDAY}
	 * 
	 * @return selected fist payment position
	 */
	public FirstPaymentPosition getFirstPaymentPosition() {
		return this.firstPaymentPosition;
	}

	/**
	 * Set the selected first payment kind.
	 * 
	 * @param firstPaymentKind
	 *            new first payment kind
	 */
	public void setFirstPaymentKind(FirstPaymentKind firstPaymentKind) {
		this.firstPaymentKind = firstPaymentKind;
	}

	/**
	 * Get the selected first payment kind. <br/>
	 * Please note that the default value is
	 * {@link CashFlowLayout#FirstPaymentKind#SINGLE_VALUE}
	 * 
	 * @return selected first payment kind
	 */
	public FirstPaymentKind getFirstPaymentKind() {
		return this.firstPaymentKind;
	}

	/**
	 * Set the selected final value presence.
	 * 
	 * @param finalValuePresence
	 *            new final value presence
	 */
	public void setFinalValuePresence(FinalValuePresence finalValuePresence) {
		this.finalValuePresence = finalValuePresence;
	}

	/**
	 * Get the selected final value presence. <br/>
	 * Please note that the default value is
	 * {@link CashFlowLayout#FinalValuePresence#NOT_PRESENT}
	 * 
	 * @return selected final value presence
	 */
	public FinalValuePresence getFinalPaymentPresence() {
		return this.finalValuePresence;
	}

	/**
	 * Builds an integer value corresponding to the cash flow layout
	 * configuration. <br/>
	 * This method considers each configuration switch as a bit. Here is the
	 * order that is considered (from LSB / MSB) to build this code :
	 * <ul>
	 * <li>Payments position</li>
	 * <li>First Payment position</li>
	 * <li>First Payment kind</li>
	 * </ul>
	 * 
	 * @return the corresponding configuration code
	 * @throws InvalidCashFlowLayoutException
	 *             if the given cash flow is not recognized
	 */
	protected int getConfigurationCode() throws InvalidCashFlowLayoutException {
		logger.debug("getConfigurationCode");
		String configurationCode = null;

		if (CashFlowLayout.PaymentsPosition.END_OF_PERIODS.equals(this
				.getPaymentsPosition())) {
			logger.debug("PaymentsPosition.END_OF_PERIODS");
		} else if (CashFlowLayout.PaymentsPosition.BEGINNING_OF_PERIODS
				.equals(this.getPaymentsPosition())) {
			logger.debug("PaymentsPosition.BEGINNING_OF_PERIODS");
			if (!firstPaymentAsDeposit)
				throw new InvalidCashFlowLayoutException(
						"BEGINNING_OF_PERIODS => the first payment must be as a deposit");
		}

		if (CashFlowLayout.FirstPaymentPosition.AFTER_HOLIDAY.equals(this
				.getFirstPaymentPosition())) {
			logger.debug("FirstPaymentPosition.AFTER_HOLIDAY");
			configurationCode = "0";
		} else if (CashFlowLayout.FirstPaymentPosition.BEFORE_HOLIDAY.equals(this.getFirstPaymentPosition())) {
			logger.debug("FirstPaymentPosition.BEFORE_HOLIDAY");

			configurationCode = "1";
		} else {
			throw new InvalidCashFlowLayoutException("null or not recognized FirstPaymentPosition");
		}

		if (CashFlowLayout.FirstPaymentKind.SINGLE_VALUE.equals(this.getFirstPaymentKind())) {
			logger.debug("FirstPaymentKind.SINGLE_VALUE");
			configurationCode += "0";
		} else if (CashFlowLayout.FirstPaymentKind.PERIODIC_PAYMENT_DEPENDANT.equals(this.getFirstPaymentKind())) {
			logger.debug("FirstPaymentKind.PERIODIC_PAYMENT_DEPENDANT");
			configurationCode += "1";
		} else {
			throw new InvalidCashFlowLayoutException("null or not recognized FirstPaymentKind");
		}

		configurationCode += (firstPaymentAsDeposit ? "1" : "0");

		logger.debug("result code is : " + configurationCode);
		logger.debug("getConfigurationCode");
		return Integer.parseInt(configurationCode, 2);
	}

	/**
	 * Gives a human readable representation of this object. <br/>
	 * 
	 * @return a string representation of this object
	 */
	public String toString() {
		return new StringBuffer(CashFlowLayout.class.getName()).append("{")
				.append("finalValuePresence=").append(this.finalValuePresence)
				.append(", ").append("firstPaymentKind=")
				.append(this.firstPaymentKind).append(", ")
				.append("firstPaymentPosition=")
				.append(this.firstPaymentPosition).append(", ")
				.append("paymentsPosition=").append(this.paymentsPosition)
				.append("}").toString();
	}

	/**
	 * Defines the presence or not of a final value.
	 */
	public static class FinalValuePresence implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 7516597732164054159L;
		/**
		 * Internal id of 'FV is present'
		 */
		private static final int ID_PRESENT = 0;
		/**
		 * Internal id of 'FV is not present'
		 */
		private static final int ID_NOT_PRESENT = 1;
		/**
		 * Selected internal id
		 */
		private final int internalId;

		/**
		 * Private Constructor.
		 * 
		 * @param internalId
		 *            internal id
		 */
		private FinalValuePresence(int internalId) {
			this.internalId = internalId;
		}

		/**
		 * Tells if the current final value configuration equals another one.
		 * 
		 * @param other
		 *            final value configuration to compare to
		 */
		public boolean equals(FinalValuePresence other) {
			return this.internalId == other.internalId;
		}

		/**
		 * Final Value is present.
		 */
		public static final FinalValuePresence PRESENT = new FinalValuePresence(
				ID_PRESENT);
		/**
		 * Final value is not present.
		 */
		public static final FinalValuePresence NOT_PRESENT = new FinalValuePresence(
				ID_NOT_PRESENT);

		/**
		 * Gives a human readable representation of this object. <br/>
		 * 
		 * @return a string representation of this object
		 */
		public String toString() {
			StringBuffer res = new StringBuffer("FinalValuePresence{");
			switch (this.internalId) {
			case 0:
				res.append("PRESENT");
				break;
			case 1:
				res.append("NOT PRESENT");
				break;
			default:
				res.append("*** UNKNOWN FINAL VALUE PRESENCE <")
						.append(internalId).append("> ***");
				break;
			}
			return res.append("}").toString();
		}
	}

	/**
	 * Defines the first payment value kind.
	 */
	public static class FirstPaymentKind implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 5837726516592056414L;
		/**
		 * Internal id of 'FP is a single amount'.
		 */
		private static final int ID_SINGLE = 0;
		/**
		 * Internal id of 'FP is PP dependent'.
		 */
		private static final int ID_PP_DEP = 1;
		/**
		 * Selected internal id.
		 */
		private final int internalId;

		/**
		 * Private constructor.
		 * 
		 * @param internalId
		 *            internal id
		 */
		private FirstPaymentKind(int internalId) {
			this.internalId = internalId;
		}

		/**
		 * Tells if the current first payment kind configuration equals another
		 * one.
		 * 
		 * @param other
		 *            first payment kind to compare to
		 */
		public boolean equals(FirstPaymentKind other) {
			return this.internalId == other.internalId;
		}

		/**
		 * First Payment is a single value.
		 */
		public static final FirstPaymentKind SINGLE_VALUE = new FirstPaymentKind(
				ID_SINGLE);
		/**
		 * First Payment is Periodic Payment dependent.
		 */
		public static final FirstPaymentKind PERIODIC_PAYMENT_DEPENDANT = new FirstPaymentKind(
				ID_PP_DEP);

		/**
		 * Gives a human readable representation of this object. <br/>
		 * 
		 * @return a string representation of this object
		 */
		public String toString() {
			StringBuffer res = new StringBuffer("FirstPaymentKind{");
			switch (this.internalId) {
			case 0:
				res.append("SINGLE");
				break;
			case 1:
				res.append("PP DEPENDANT");
				break;
			default:
				res.append("*** UNKNOWN FIRST PAYMENT KIND <")
						.append(internalId).append("> ***");
				break;
			}
			return res.append("}").toString();
		}
	}

	/**
	 * Defines the first payment position.
	 */
	public static class FirstPaymentPosition implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = -4192307513372451928L;
		/**
		 * Internal id of 'FP before Holidays'.
		 */
		private static final int ID_BEFORE_H = 0;
		/**
		 * Internal id of 'FP after position'.
		 */
		private static final int ID_AFTER_H = 1;
		/**
		 * Selected internal id.
		 */
		private final int internalId;

		/**
		 * Private Constructor.
		 * 
		 * @param internalId
		 *            internal id
		 */
		private FirstPaymentPosition(int internalId) {
			this.internalId = internalId;
		}

		/**
		 * Tells if the current first payment position configuration equals
		 * another one.
		 * 
		 * @param other
		 *            first payment position to compare to
		 */
		public boolean equals(FirstPaymentPosition other) {
			return this.internalId == other.internalId;
		}

		/**
		 * First payment is before holiday period.
		 */
		public static final FirstPaymentPosition BEFORE_HOLIDAY = new FirstPaymentPosition(
				ID_BEFORE_H);
		/**
		 * First payment is after holiday period.
		 */
		public static final FirstPaymentPosition AFTER_HOLIDAY = new FirstPaymentPosition(
				ID_AFTER_H);

		/**
		 * Gives a human readable representation of this object. <br/>
		 * 
		 * @return a string representation of this object
		 */
		public String toString() {
			StringBuffer res = new StringBuffer("FirstPaymentPosition{");
			switch (this.internalId) {
			case 0:
				res.append("BEFORE HOLIDAY");
				break;
			case 1:
				res.append("AFTER HOLIDAY");
				break;
			default:
				res.append("*** UNKNOWN FIRST PAYMENT POSITION <")
						.append(internalId).append("> ***");
				break;
			}
			return res.append("}").toString();
		}
	}

	/**
	 * Defines the available Payment Positions.
	 */
	public static class PaymentsPosition implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = -8149373727425125275L;
		/**
		 * Internal id of 'begin of periods payments'.
		 */
		private static final int ID_BEGIN_PERIODS = 0;
		/**
		 * Internal id of 'end of periods payments'.
		 */
		private static final int ID_END_PERIODS = 1;
		/**
		 * Selected internal id.
		 */
		private final int internalId;

		/**
		 * Private constructor.
		 * 
		 * @param internalId
		 *            internal id
		 */
		private PaymentsPosition(int internalId) {
			this.internalId = internalId;
		}

		/**
		 * Tells if the current payment position configuration equals another
		 * one.
		 * 
		 * @param other
		 *            payment position to compare to
		 */
		public boolean equals(PaymentsPosition other) {
			return this.internalId == other.internalId;
		}

		/**
		 * Payments are at the beginning of periods.
		 */
		public static final PaymentsPosition BEGINNING_OF_PERIODS = new PaymentsPosition(
				ID_BEGIN_PERIODS);
		/**
		 * Payments are at the end of periods.
		 */
		public static final PaymentsPosition END_OF_PERIODS = new PaymentsPosition(
				ID_END_PERIODS);

		/**
		 * Gives a human readable representation of this object. <br/>
		 * 
		 * @return a string representation of this object
		 */
		public String toString() {
			StringBuffer res = new StringBuffer("PaymentsPosition{");
			switch (this.internalId) {
			case 0:
				res.append("BEGINNING OF PERIODS");
				break;
			case 1:
				res.append("END OF PERIODS");
				break;
			default:
				res.append("*** UNKNOWN PAYMENTS POSITION <")
						.append(internalId).append("> ***");
				break;
			}
			return res.append("}").toString();
		}
	}

	/**
	 * Defines the interests configuration (paid or capitalized) during the
	 * holiday period.
	 */
	public static class InterestDuringHolidayPeriod implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = -3982867111583016128L;
		/**
		 * Internal id of 'Interests Capitalized during Holiday Period'.
		 */
		private static final int ID_INTEREST_CAPITALIZED = 0;
		/**
		 * Internal id of 'Interests Paid during Holiday Period'.
		 */
		private static final int ID_INTEREST_PAID = 1;
		/**
		 * Selected internal id.
		 */
		private final int internalId;

		/**
		 * Private constructor.
		 * 
		 * @param internalId
		 *            internal id
		 */
		private InterestDuringHolidayPeriod(int internalId) {
			this.internalId = internalId;
		}

		/**
		 * Tells whether the current Interest during Holiday Period
		 * configuration is equal to another one.
		 * 
		 * @param other
		 *            Interest during Holiday Period configuration to compare to
		 */
		public boolean equals(InterestDuringHolidayPeriod other) {
			return this.internalId == other.internalId;
		}

		/**
		 * Interests are capitalized during Holiday Period.
		 */
		public static final InterestDuringHolidayPeriod INTEREST_CAPITALIZED = new InterestDuringHolidayPeriod(
				ID_INTEREST_CAPITALIZED);
		/**
		 * Interests are paid during Holiday Period.
		 */
		public static final InterestDuringHolidayPeriod INTEREST_PAID = new InterestDuringHolidayPeriod(
				ID_INTEREST_PAID);

		/**
		 * Gives a human readable representation of this object. <br/>
		 * 
		 * @return a string representation of this object
		 */
		public String toString() {
			StringBuffer res = new StringBuffer("InterestDuringHolidayPeriod{");
			switch (this.internalId) {
			case 0:
				res.append("INTEREST_CAPITALIZED");
				break;
			case 1:
				res.append("INTEREST_PAID");
				break;
			default:
				res.append("*** UNKNOWN CONFIG <").append(internalId)
						.append("> ***");
				break;
			}
			return res.append("}").toString();
		}
	}

	/**
	 * @return Returns the firstPaymentAsDeposit.
	 */
	public boolean isFirstPaymentAsDeposit() {
		return firstPaymentAsDeposit;
	}

	/**
	 * @param firstPaymentAsDeposit
	 *            The firstPaymentAsDeposit to set.
	 */
	public void setFirstPaymentAsDeposit(boolean firstPaymentAsDeposit) {
		this.firstPaymentAsDeposit = firstPaymentAsDeposit;
	}

	/**
	 * @return Returns the fvAddedToN.
	 */
	public boolean isFvAddedToN() {
		return fvAddedToN;
	}

	/**
	 * @param fvAddedToN
	 *            The fvAddedToN to set.
	 */
	public void setFvAddedToN(boolean fvAddedToN) {
		this.fvAddedToN = fvAddedToN;
	}

	/**
	 * @return Returns the hAddedToN.
	 */
	public boolean isHAddedToN() {
		return hAddedToN;
	}

	/**
	 * @param addedToN
	 *            The hAddedToN to set.
	 */
	public void setHAddedToN(boolean addedToN) {
		hAddedToN = addedToN;
	}

	/**
	 * Get the selected Interests Configuration during the Holiday Period. <br/>
	 * Please note that the default value is
	 * {@link CashFlowLayout.InterestDuringHolidayPeriod#ID_INTEREST_CAPITALIZED}
	 * 
	 * @return the interestInHolidayPeriod.
	 */
	public InterestDuringHolidayPeriod getInterestInHolidayPeriod() {
		return interestInHolidayPeriod;
	}

	/**
	 * @param interestInHolidayPeriod
	 *            the interestInHolidayPeriod to set.
	 * 
	 */
	public void setInterestInHolidayPeriod(
			InterestDuringHolidayPeriod interestInHolidayPeriod) {
		this.interestInHolidayPeriod = interestInHolidayPeriod;
	}

}

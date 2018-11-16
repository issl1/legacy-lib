package com.nokor.efinance.share.locksplit;


/**
 * 
 * @author uhout.cheng
 */
public enum LockSplitStatus {
	NEW("LNEW"), // New
	PEN("LPEN"), // Pending
	PAR("LPAR"), // Partial Paid
	PAI("LPAI"), // Paid
	EXP("LEXP"); // Expired

	private final String code;

	/**
	 * 
	 * @param code
	 */
	private LockSplitStatus(final String code) {
		this.code = code;
	}

	/**
	 * return code
	 */
	public String getCode() {
		return code;
	}
	
	/**
	 * @param code
	 * @return
	 */
	public static LockSplitStatus getByCode(String code) {
		for (LockSplitStatus lockSplitStatus : LockSplitStatus.values()) {
			if (lockSplitStatus.getCode().equals(code)) {
				return lockSplitStatus;
			}
		}
		return null;
	}
}
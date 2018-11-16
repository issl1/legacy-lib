package com.nokor.efinance.core.collection.service;

import com.nokor.efinance.core.collection.model.EColType;
import com.nokor.efinance.core.common.IProfileCode;

public class DebtLevelUtils {
	
	public static final int DEBT_LEVEL_0 = 0;
	public static final int DEBT_LEVEL_1 = 1;
	public static final int DEBT_LEVEL_2 = 2;
	public static final int DEBT_LEVEL_3 = 3;
	public static final int DEBT_LEVEL_4 = 4;
	public static final int DEBT_LEVEL_5 = 5;
	public static final int DEBT_LEVEL_6 = 6;
	public static final int DEBT_LEVEL_7 = 7;
	
	public static final Integer[] DAY_END = new Integer[] {DEBT_LEVEL_0};
	public static final Integer[] PHONE = new Integer[] {DEBT_LEVEL_1, DEBT_LEVEL_2, DEBT_LEVEL_3};
	public static final Integer[] DAY_END_PHONE = new Integer[] {DEBT_LEVEL_0, DEBT_LEVEL_1, DEBT_LEVEL_2, DEBT_LEVEL_3};
	public static final Integer[] FIELD = new Integer[] {DEBT_LEVEL_1, DEBT_LEVEL_2, DEBT_LEVEL_3, DEBT_LEVEL_4, DEBT_LEVEL_5};
	public static final Integer[] INSIDE_REPO = new Integer[] {DEBT_LEVEL_1, DEBT_LEVEL_2, DEBT_LEVEL_3, DEBT_LEVEL_4, DEBT_LEVEL_5, DEBT_LEVEL_6};
	public static final Integer[] OA = new Integer[] {DEBT_LEVEL_1, DEBT_LEVEL_2, DEBT_LEVEL_3, DEBT_LEVEL_4, DEBT_LEVEL_5, DEBT_LEVEL_6, DEBT_LEVEL_7};
	
	/**
	 * @param colType
	 * @return
	 */
	public static Integer[] getDebtLevels(String profileCode) {
		EColType colType = null;
		
		if (IProfileCode.COL_PHO_STAFF.equals(profileCode)) {
			colType = EColType.PHONE;
		} else if (IProfileCode.COL_FIE_STAFF.equals(profileCode)) {
			colType = EColType.FIELD;
		} else if (IProfileCode.COL_INS_STAFF.equals(profileCode)) {
			colType = EColType.INSIDE_REPO;
		} else if (IProfileCode.COL_OA_STAFF.equals(profileCode)) {
			colType = EColType.OA;
		}
		return getDebtLevels(colType);
	}
	
	/**
	 * @param colType
	 * @return
	 */
	public static Integer[] getDebtLevels(EColType colType) {
		if (EColType.PHONE.equals(colType)) {
			return DAY_END_PHONE;
		} else if (EColType.FIELD.equals(colType)) {
			return FIELD;
		} else if (EColType.INSIDE_REPO.equals(colType)) {
			return INSIDE_REPO;
		} else if (EColType.OA.equals(colType)) {
			return OA;
		}
		return null;
	}
	
	/**
	 * @param levels
	 * @param debtLevel
	 * @return
	 */
	private static boolean isLevel(Integer[] levels, int debtLevel) {
		for (int i : levels) {
			if (i == debtLevel) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * @param debtLevel
	 * @return
	 */
	public static boolean isPhoneLevel(int debtLevel) {
		return isLevel(DAY_END_PHONE, debtLevel);
	}
	
	/**
	 * @param debtLevel
	 * @return
	 */
	public static boolean isFieldLevel(int debtLevel) {
		return isLevel(FIELD, debtLevel);
	}
	
	/**
	 * @param debtLevel
	 * @return
	 */
	public static EColType getColType(int debtLevel) {
		switch (debtLevel) {
		case DEBT_LEVEL_0:
		case DEBT_LEVEL_1:
		case DEBT_LEVEL_2:
		case DEBT_LEVEL_3:
			return EColType.PHONE;
		case DEBT_LEVEL_4:
		case DEBT_LEVEL_5:
			return EColType.FIELD;
		case DEBT_LEVEL_6:
			return EColType.INSIDE_REPO;
		case DEBT_LEVEL_7:
			return EColType.OA;
		default:
			return EColType.PHONE;
		}
	}
}

package com.nokor.efinance.third.creditbureau.cbc.model;

import java.util.ArrayList;
import java.util.List;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.RefDataId;

/**
 * @author ly.youhort
 */
public enum AccountType implements RefDataId {
	
	J("Joint"),
	S("Single"),
	G("Group");

	private final String code;

	/**
     * 
     */
	private AccountType(final String code) {
		this.code = code;
	}

	/**
	 * return code
	 */
	@Override
	public String getCode() {
		return code;
	}

	/**
	 * return desc
	 */
	@Override
	public String getDesc() {
		return I18N.value(code);
	}

	@Override
	public String getDescEn() {
		return I18N.value(code);
	}
	
	/**
	 * List of service type
	 * 
	 * @return
	 */
	public static List<AccountType> list() {
		List<AccountType> accountTypes = new ArrayList<AccountType>();
		accountTypes.add(G);
		accountTypes.add(J);
		accountTypes.add(S);
		return accountTypes;
	}

	@Override
	public Long getId() {
		// TODO Auto-generated method stub
		return 0L;
	}

}
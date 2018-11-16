package com.nokor.efinance.core.shared.system;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.MyDataId;

/**
 * Represents the domain type
 * @author ly.youhort
 */
public enum DomainType implements MyDataId {
	
	ASS("asset"),
	CUS("customer"),
	DEA("dealer"),
	DEF("default"),
	DOC("document"),
	FIN("financial.product"),
	GUA("guarantor"),
	QUO("quotation"),
	CON("contract");

	private final String code;

	private DomainType(final String code) {
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
	public Long getId() {
		// TODO Auto-generated method stub
		return 0L;
	}

}
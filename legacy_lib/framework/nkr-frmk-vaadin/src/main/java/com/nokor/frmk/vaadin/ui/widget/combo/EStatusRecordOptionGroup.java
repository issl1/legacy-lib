package com.nokor.frmk.vaadin.ui.widget.combo;

import java.util.Arrays;

import org.seuksa.frmk.model.entity.EStatusRecord;

/**
 * 
 * @author prasnar
 *
 */
public class EStatusRecordOptionGroup extends ERefDataOptionGroup<EStatusRecord> {
	/** */
	private static final long serialVersionUID = -5130681694925732064L;

	/**
	 * 
	 */
	public EStatusRecordOptionGroup() {
		super(EStatusRecord.class, Arrays.asList(EStatusRecord.ACTIV , EStatusRecord.INACT), Arrays.asList(EStatusRecord.ACTIV));
	}

	/**
	 * 
	 */
	public void setDefaultValue() {
		setValue(Arrays.asList(EStatusRecord.ACTIV));
	}

}


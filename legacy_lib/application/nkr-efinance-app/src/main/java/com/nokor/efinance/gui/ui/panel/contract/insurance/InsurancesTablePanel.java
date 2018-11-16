package com.nokor.efinance.gui.ui.panel.contract.insurance;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;

import com.gl.finwiz.share.domain.insurance.InsuranceDTO;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.efinance.third.finwiz.client.ins.ClientInsurance;
import com.nokor.ersys.core.hr.model.organization.Organization;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimpleTable;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.themes.Reindeer;

/**
 * Insurance table panel
 * @author uhout.cheng
 */
public class InsurancesTablePanel extends Panel implements FinServicesHelper {

	/** */
	private static final long serialVersionUID = 1813011103485191660L;

	private final static String ISRTYPE = "isr.type";
	private final static String COMPANY = "company";
	private final static String POLICYNO = "plicy.no";
	private final static String EFFECTIVEDATE = "effective.date";
	private final static String EXPIRYDATE = "expiry.date";
	private final static String ISRSTATUS = "status";
	
	private SimpleTable<Entity> simpleTable;
	
	/**
	 * 
	 * @param caption
	 */
	public InsurancesTablePanel(String caption) {
		simpleTable = new SimpleTable<Entity>(getColumnDefinitions());
		simpleTable.setSizeFull();
		simpleTable.setPageLength(3);
		setCaption(I18N.message(caption));
		setStyleName(Reindeer.PANEL_LIGHT);
		setContent(simpleTable);
	}
	
	/**
	 * 
	 * @return
	 */
	private List<ColumnDefinition> getColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(FMEntityField.ID, I18N.message("id"), Long.class, Align.LEFT, 60));
		columnDefinitions.add(new ColumnDefinition(ISRTYPE, I18N.message("isr.type"), String.class, Align.LEFT, 130));
		columnDefinitions.add(new ColumnDefinition(COMPANY, I18N.message("company"), String.class, Align.LEFT, 180));
		columnDefinitions.add(new ColumnDefinition(POLICYNO, I18N.message("policy.no"), String.class, Align.LEFT, 130));
		columnDefinitions.add(new ColumnDefinition(EFFECTIVEDATE, I18N.message("effective.date"), Date.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(EXPIRYDATE, I18N.message("expiry.date"), Date.class, Align.LEFT, 80));
		columnDefinitions.add(new ColumnDefinition(ISRSTATUS, I18N.message("status"), String.class, Align.LEFT, 130));
		return columnDefinitions;
	}
	
	/**
	 * 
	 * @param contract
	 */
	public void assignValues(Contract contract) {
		setIndexedContainer(ClientInsurance.getInsuranceByContractReference(contract.getReference()));
	}
	
	/**
	 * 
	 * @param insuranceDTOs
	 */
	@SuppressWarnings("unchecked")
	private void setIndexedContainer(List<InsuranceDTO> insuranceDTOs) {
		simpleTable.removeAllItems();
		Container container = simpleTable.getContainerDataSource();
		if (insuranceDTOs != null && !insuranceDTOs.isEmpty()) {
			int index = 0;
			for (InsuranceDTO insuranceDTO : insuranceDTOs) {
				Item item = container.addItem(index);
				item.getItemProperty(FMEntityField.ID).setValue(insuranceDTO.getInsuranceId());
				item.getItemProperty(ISRTYPE).setValue(insuranceDTO.getInsuranceType() == null ? StringUtils.EMPTY : insuranceDTO.getInsuranceType().getValue());
				if (insuranceDTO.getInsuranceCompanyId() != null) {
					Organization isrCompany = ORG_SRV.getById(Organization.class, insuranceDTO.getInsuranceCompanyId());
					if (isrCompany != null) {
						item.getItemProperty(COMPANY).setValue(isrCompany.getNameLocale());
					}
				}
				item.getItemProperty(POLICYNO).setValue(insuranceDTO.getPolicyNo());
				if (insuranceDTO.getEffectiveDate() != null) {
					item.getItemProperty(EFFECTIVEDATE).setValue(new Date(insuranceDTO.getEffectiveDate().getTime()));
				}
				if (insuranceDTO.getExpiryDate() != null) {
					item.getItemProperty(EXPIRYDATE).setValue(new Date(insuranceDTO.getExpiryDate().getTime()));
				}
				item.getItemProperty(ISRSTATUS).setValue(insuranceDTO.getStatusTask());
				index++;
			}
		}
	}
	
}

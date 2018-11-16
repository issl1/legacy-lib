package com.nokor.efinance.gui.ui.panel.contract.insurance;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;

import com.gl.finwiz.share.domain.insurance.InsuranceClaimDTO;
import com.gl.finwiz.share.domain.insurance.InsuranceDTO;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.helper.FinServicesHelper;
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
 * claim table panel
 * @author uhout.cheng
 */
public class ClaimsTablePanel extends Panel implements FinServicesHelper {

	/***/
	private static final long serialVersionUID = -304594817225560438L;

	private final static String ISRTYPE = "isr.type";
	private final static String CLAIMTYPE = "claim.type";
	private final static String COMPANY = "company";
	private final static String POLICYNO = "plicy.no";
	private final static String USERID = "user.id";
	private final static String DATE = "date";
	private final static String STATUS = "status";
	
	private SimpleTable<Entity> simpleTable;
	
	/**
	 * 
	 */
	public ClaimsTablePanel() {
		init();
	}
	
	/**
	 * 
	 */
	private void init() {
		simpleTable = new SimpleTable<Entity>(getColumnDefinitions());
		simpleTable.setSizeFull();
		simpleTable.setPageLength(3);
		setCaption(I18N.message("claims"));
		setStyleName(Reindeer.PANEL_LIGHT);
		setContent(simpleTable);
	}
	
	/**
	 * 
	 * @return
	 */
	private List<ColumnDefinition> getColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(ISRTYPE, I18N.message("isr.type"), String.class, Align.LEFT, 130));
		columnDefinitions.add(new ColumnDefinition(CLAIMTYPE, I18N.message("claim.type"), String.class, Align.LEFT, 130));
		columnDefinitions.add(new ColumnDefinition(COMPANY, I18N.message("company"), String.class, Align.LEFT, 130));
		columnDefinitions.add(new ColumnDefinition(POLICYNO, I18N.message("policy.no"), String.class, Align.LEFT, 130));
		columnDefinitions.add(new ColumnDefinition(USERID, I18N.message("user.id"), String.class, Align.LEFT, 130));
		columnDefinitions.add(new ColumnDefinition(DATE, I18N.message("date"), Date.class, Align.LEFT, 80));
		columnDefinitions.add(new ColumnDefinition(STATUS, I18N.message("status"), String.class, Align.LEFT, 110));
		return columnDefinitions;
	}
	
	/**
	 * 
	 * @param contract
	 */
	public void assignValues(Contract contract) {
		setIndexedContainer(ClientInsurance.getInsClaimByContractReference(contract.getReference()));
	}
	
	/**
	 * 
	 * @param insClaimDTOs
	 */
	@SuppressWarnings("unchecked")
	private void setIndexedContainer(List<InsuranceClaimDTO> insClaimDTOs) {
		simpleTable.removeAllItems();
		Container container = simpleTable.getContainerDataSource();
		if (insClaimDTOs != null && !insClaimDTOs.isEmpty()) {
			int index = 0;
			for (InsuranceClaimDTO insClaimDTO : insClaimDTOs) {
				Item item = container.addItem(index);
				
				item.getItemProperty(CLAIMTYPE).setValue(insClaimDTO.getClaimType());
				item.getItemProperty(USERID).setValue(insClaimDTO.getCreatedBy());
				item.getItemProperty(DATE).setValue(new Date(insClaimDTO.getCreatedDate().getTime()));
				
				InsuranceDTO insDTO = insClaimDTO.getInsurance();
				if (insDTO != null) {
					item.getItemProperty(ISRTYPE).setValue(insDTO.getInsuranceType() == null ? StringUtils.EMPTY : 
						insDTO.getInsuranceType().getValue());
					if (insDTO.getInsuranceCompanyId() != null) {
						Organization isrCompany = ENTITY_SRV.getById(Organization.class, insDTO.getInsuranceCompanyId());
						if (isrCompany != null) {
							item.getItemProperty(COMPANY).setValue(isrCompany.getNameLocale());
						}
					}
					item.getItemProperty(POLICYNO).setValue(insDTO.getPolicyNo());
					item.getItemProperty(STATUS).setValue(insDTO.getStatusTask());
				}
				index++;
			}
		}
	}
	
}

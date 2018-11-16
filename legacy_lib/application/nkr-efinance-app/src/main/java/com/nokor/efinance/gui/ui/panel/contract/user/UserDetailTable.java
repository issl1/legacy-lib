package com.nokor.efinance.gui.ui.panel.contract.user;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;

import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.applicant.model.Company;
import com.nokor.efinance.core.applicant.model.EApplicantType;
import com.nokor.efinance.core.applicant.model.Individual;
import com.nokor.efinance.core.applicant.model.MApplicant;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.ContractApplicant;
import com.nokor.efinance.core.contract.model.ContractSimulation;
import com.nokor.efinance.core.contract.model.ContractSimulationApplicant;
import com.nokor.efinance.core.contract.service.ContractUtils;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.ersys.core.hr.model.address.MBaseAddress;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimpleTable;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.themes.Reindeer;

/**
 * User detail table 
 * @author uhout.cheng
 */
public class UserDetailTable extends Panel implements ItemClickListener, MApplicant, FinServicesHelper {

	/** */
	private static final long serialVersionUID = 5392858306176958175L;
	
	private SimpleTable<Entity> simpleTable;
	private Item selectedItem;
	private UsersPanel usersPanel;
	
	/**
	 * @return the simpleTable
	 */
	public SimpleTable<Entity> getSimpleTable() {
		return simpleTable;
	}
	
	/**
	 * @param usersPanel
	 * @param userContactAddressTable
	 * @param userContactPhoneTable
	 * @param userReferenceTable
	 * @param userGenderalPanel
	 */
	public UserDetailTable(UsersPanel usersPanel) {
		this.usersPanel = usersPanel;
		init();
	}
	
	/**
	 * 
	 */
	private void init() {
		simpleTable = new SimpleTable<Entity>(getColumnDefinitions());
		simpleTable.setSizeUndefined();
		simpleTable.setPageLength(4);
		simpleTable.addItemClickListener(this);
		setStyleName(Reindeer.PANEL_LIGHT);
		setContent(simpleTable);
	}
	
	/**
	 * 
	 * @return
	 */
	private List<ColumnDefinition> getColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 50, false));
		columnDefinitions.add(new ColumnDefinition(REFERENCE, I18N.message("customer.id"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(CREATEDATE, I18N.message("creation.date"), Date.class, Align.LEFT, 70));
		columnDefinitions.add(new ColumnDefinition(WKFSTATUS, I18N.message("status"), String.class, Align.LEFT, 80));
		columnDefinitions.add(new ColumnDefinition(NAME, I18N.message("name"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(NICKNAME, I18N.message("nickname"), String.class, Align.LEFT, 80));
		columnDefinitions.add(new ColumnDefinition(IDNUMBER, I18N.message("id.number"), String.class, Align.LEFT, 110));
		columnDefinitions.add(new ColumnDefinition(MBaseAddress.TYPE, I18N.message("type"), String.class, Align.LEFT, 130));
		return columnDefinitions;
	}
	
	/**
	 * 
	 * @param individuals
	 */
	public void assignValues(Contract contract) {
		List<ApplicantWithType> applicants = new ArrayList<>();		
		if (ContractUtils.isPendingTransfer(contract)) {
			ContractSimulation contractSimulation = ContractUtils.getLastContractSimulation(contract.getId());
			if (contractSimulation != null) {
				applicants.add(new ApplicantWithType(EApplicantType.C, contractSimulation.getApplicant()));
				List<ContractSimulationApplicant> contractSimulationApplicants = contractSimulation.getContractSimulationApplicants();
				if (contractSimulationApplicants != null && !contractSimulationApplicants.isEmpty()) {
					for (ContractSimulationApplicant contractSimulationApplicant : contractSimulationApplicants) {
						if (EApplicantType.G.equals(contractSimulationApplicant.getApplicantType())) {
							applicants.add(new ApplicantWithType(EApplicantType.G, contractSimulationApplicant.getApplicant()));
						}
					}
				}
				
				applicants.add(new ApplicantWithType(EApplicantType.OLD_CUS, contract.getApplicant()));
				List<ContractApplicant> contractApplicants = contract.getContractApplicants();
				if (contractApplicants != null && !contractApplicants.isEmpty()) {
					for (ContractApplicant contractApplicant : contractApplicants) {
						if (EApplicantType.G.equals(contractApplicant.getApplicantType())) {
							applicants.add(new ApplicantWithType(EApplicantType.O, contractApplicant.getApplicant()));
						}
					}
				}
			}
		} else {
			applicants.add(new ApplicantWithType(EApplicantType.C, contract.getApplicant()));
			List<ContractApplicant> contractApplicants = contract.getContractApplicants();
			if (contractApplicants != null && !contractApplicants.isEmpty()) {
				for (ContractApplicant contractApplicant : contractApplicants) {
					if (EApplicantType.G.equals(contractApplicant.getApplicantType())) {
						applicants.add(new ApplicantWithType(EApplicantType.G, contractApplicant.getApplicant()));
					} else if (EApplicantType.O.equals(contractApplicant.getApplicantType())) {
						applicants.add(new ApplicantWithType(EApplicantType.O, contractApplicant.getApplicant()));
					} else if (EApplicantType.OLD_CUS.equals(contractApplicant.getApplicantType())) {
						applicants.add(new ApplicantWithType(EApplicantType.OLD_CUS, contractApplicant.getApplicant()));
					} 
				}
			}
		}
		
		setUserDetailIndexedContainer(applicants);
	}
	
	/**
	 * 
	 * @param individuals
	 */
	@SuppressWarnings("unchecked")
	private void setUserDetailIndexedContainer(List<ApplicantWithType> applicants) {
		simpleTable.removeAllItems();
		selectedItem = null;
		Container indexedContainer = simpleTable.getContainerDataSource();
		if (!applicants.isEmpty()) {
			long index = 0l;
			for (ApplicantWithType applicantWithType : applicants) {
				Applicant applicant = applicantWithType.applicant;
				Individual individual = applicant.getIndividual();
				Company company = applicant.getCompany();
				Item item = indexedContainer.addItem(index);
				item.getItemProperty(ID).setValue(applicant.getId());
				item.getItemProperty(REFERENCE).setValue(individual != null ? individual.getReference() : company.getLicenceNo());
				item.getItemProperty(CREATEDATE).setValue(applicant.getCreateDate());
				item.getItemProperty(WKFSTATUS).setValue(individual != null ? individual.getWkfStatus().getDescLocale() : company.getWkfStatus().getDescLocale());
				item.getItemProperty(NAME).setValue(applicant.getNameLocale());
				item.getItemProperty(NICKNAME).setValue(individual != null ? individual.getNickName() : StringUtils.EMPTY);
				item.getItemProperty(IDNUMBER).setValue(individual != null ? individual.getIdNumber() : StringUtils.EMPTY);
				item.getItemProperty(MBaseAddress.TYPE).setValue(applicantWithType.type.getDescEn());
				index++;
			}
		}
	}

	/**
	 * @return
	 */
	public Long getItemSelectedId() {
		if (this.selectedItem != null) {
			return ((Long) this.selectedItem.getItemProperty(ID).getValue());
		}
		return null;
	}
	
	/**
	 * @see com.vaadin.event.ItemClickEvent.ItemClickListener#itemClick(com.vaadin.event.ItemClickEvent)
	 */
	@Override
	public void itemClick(ItemClickEvent event) {
		this.selectedItem = event.getItem();
		Long id = getItemSelectedId();
		if (id != null) {
			Applicant applicant = ENTITY_SRV.getById(Applicant.class, id);
			usersPanel.displayApplicant(applicant);
		}
	}	
	
	private class ApplicantWithType implements Serializable {
		/**
		 */
		private static final long serialVersionUID = 2230244365588435480L;
		public EApplicantType type;
		public Applicant applicant;
		public ApplicantWithType(EApplicantType type, Applicant applicant) {
			this.type = type;
			this.applicant = applicant;
		}
	}
}

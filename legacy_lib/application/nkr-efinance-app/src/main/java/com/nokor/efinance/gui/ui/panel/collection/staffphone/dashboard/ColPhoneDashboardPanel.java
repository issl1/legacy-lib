package com.nokor.efinance.gui.ui.panel.collection.staffphone.dashboard;

import java.io.Serializable;
import java.util.List;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.amount.AmountUtils;

import com.nokor.efinance.core.collection.model.CollectionHistory;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author buntha.chea
 *
 */
public class ColPhoneDashboardPanel extends AbstractControlPanel implements FinServicesHelper {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9114952094102144823L;
	
	private List<Contract> contractsAssigned;
	
	private int nbContractAssign;
	private int nbContractCalled;
	private double totalAmountOverdue;
	private int nbContractFollowup;
	private int nbCollectionAssistPendding;
	private int nbCollectionFlagPendding;
	private int nbCollectionAssistReject;
	private int nbCollectionFlagReject;
	private int nbContractUnProcessed;
	private int nbContractReturnRepoPendding;
	private int nbContractReturnRepoAlready;
	private int nbContractWithPromise;
	private int nbContractNoOverdue;
	private int nbCollectionFlagValidated;
	private int nbCollectionAssistValidated;

	/**
	 * 
	 * @param contract
	 */
	public void assignValue() {
		contractsAssigned = COL_SRV.getContractAssigned();
		
		nbContractAssign = contractsAssigned.size();
		nbContractCalled = getContractCalled(contractsAssigned);
		getTotalAmountOverdue(contractsAssigned);
		nbContractFollowup = COL_SRV.getCollectionActionFollowup(contractsAssigned).size();
		nbCollectionAssistPendding = COL_SRV.getContractAssignPendding(contractsAssigned).size();
		nbCollectionFlagPendding = COL_SRV.getContractFlagPendding(contractsAssigned).size();
		nbCollectionAssistReject = COL_SRV.getContractAssistReject(contractsAssigned).size();
		nbCollectionFlagReject = COL_SRV.getContractFlagReject(contractsAssigned).size();
		nbContractUnProcessed = COL_SRV.getCollectionContractsUnProcessed().size();
		nbContractReturnRepoPendding = COL_SRV.getContractReturnRepoPendding(contractsAssigned).size();
		nbContractReturnRepoAlready = COL_SRV.getContractReturnRepoAlready(contractsAssigned).size();
		nbContractWithPromise = COL_SRV.getContractWithPromise().size();
		nbContractNoOverdue = COL_SRV.getContractNoOverdue().size();
		nbCollectionAssistValidated = COL_SRV.getCollectionAssistValidated(contractsAssigned).size();
		nbCollectionFlagValidated = COL_SRV.getCollectionFlagValidated(contractsAssigned).size();
		
		
		ContractItems[] items = {
				new ContractItems(I18N.message("number.of.contract.assigned"), getDefaultString(nbContractAssign), I18N.message("nb.called.contracts"), getDefaultString(nbContractCalled)),
				new ContractItems(I18N.message("total.amount.overdue"), AmountUtils.format(totalAmountOverdue), I18N.message("total.amount.collected"), AmountUtils.format(0d)),
				new ContractItems(I18N.message("percentag.amount.collected"), getDefaultString(0), I18N.message("nb.contracts.follow.up"), getDefaultString(nbContractFollowup)),
				new ContractItems(I18N.message("nb.contract.assist.pedding"), getDefaultString(nbCollectionAssistPendding), I18N.message("nb.contract.flag.pedding"), getDefaultString(nbCollectionFlagPendding)),
				new ContractItems(I18N.message("nb.validated.assist"), getDefaultString(nbCollectionAssistValidated), I18N.message("nb.validated.flag"), getDefaultString(nbCollectionFlagValidated)),
				new ContractItems(I18N.message("nb.rejected.assist"), getDefaultString(nbCollectionAssistReject), I18N.message("nb.rejected.flag"), getDefaultString(nbCollectionFlagReject)),
				new ContractItems(I18N.message("nb.contracts.promise"), getDefaultString(nbContractWithPromise), I18N.message("nb.contracts.no.move.overdue"), getDefaultString(nbContractNoOverdue)),
				new ContractItems(I18N.message("nb.contracts.repo.return.pedding"), getDefaultString(nbContractReturnRepoPendding), I18N.message("nb.contracts.repo.return"), getDefaultString(nbContractReturnRepoAlready)),
				new ContractItems(I18N.message("nb.contracts.unprocessed"), getDefaultString(nbContractUnProcessed), "", "")
		};
		
		String template = "<table cellspacing=\"0\" cellpadding=\"5\" border=\"solid 1px black\" style=\"border-collapse:collapse;width:100%;\">"
				+ "<tr style=\"background-color:lightgray;\">"
				+ "<th width=\"" + 100 + "\"><b>" + I18N.message("desc") + "</b></th>"
				+ "<th width=\"" + 100 + "\"><b>" + I18N.message("value") + "</b></th>"
				+ "<th width=\"" + 100 + "\"><b>" + I18N.message("desc") + "</b></th>"
				+ "<th width=\"" + 100 + "\"><b>" + I18N.message("value") + "</b></th>"
				+ "</tr>";
		
		for (int i = 0; i < items.length; i++) {
			String location0 = " location=\"lblDescFirst" + i;
			String location1 = " location=\"lblValueFirst" + i;
			String location2 = " location=\"lblDescSecond" + i;
			String location3 = " location=\"lblValueSecond" + i;
			template += "<tr><td" + location0 + "\"></td><td " + location1 + "\"></td><td " + location2 + "\"></td><td " + location3 + "\"></td></tr>";
		}
		template += "</table>";
		
		CustomLayout tableLayout = new CustomLayout();
		tableLayout.setTemplateContents(template);

		for (int i = 0; i < items.length; i++) {
			ContractItems item = items[i];
			tableLayout.addComponent(new Label(item.descFirst), "lblDescFirst" + i);
			tableLayout.addComponent(new Label(item.valueFirst), "lblValueFirst" + i);
			tableLayout.addComponent(new Label(item.descSecond), "lblDescSecond"+ i);
			tableLayout.addComponent(new Label(item.valueSecond), "lblValueSecond" + i);
		}
		
		VerticalLayout content = new VerticalLayout();
		content.setMargin(true);
		content.addComponent(tableLayout);
		
		removeAllComponents();
		addComponent(new Panel(content));
	}
	
	/**
	 * getContractCalled
	 */
	private int getContractCalled(List<Contract> contracts) {
		List<CollectionHistory> collectionHistories = COL_SRV.getCalledContract(contracts);
		return collectionHistories.size();
	}
	
	/**
	 * 
	 * @return
	 */
	private void getTotalAmountOverdue(List<Contract> contracts) {
		totalAmountOverdue = 0d;
		for (Contract contract : contracts) {
			if (contract.getCollection() != null) {
				totalAmountOverdue += contract.getCollection().getTeTotalAmountInOverdue();
			}
		}
	}
	
	private class ContractItems implements Serializable {
		/**
		 */
		private static final long serialVersionUID = -4791427988900490680L;
		public String descFirst;
		public String descSecond;
		public String valueFirst;
		public String valueSecond;
		
		/**
		 * @param label
		 * @param teAmount
		 * @param vatAmount
		 * @param tiAmount
		 */
		public ContractItems(String descFirst, String valueFirst, String descSecond, String valueSecond) {
			this.descFirst = descFirst;
			this.descSecond = descSecond;
			this.valueFirst = valueFirst;
			this.valueSecond = valueSecond;
		}
	}
}

package com.nokor.efinance.gui.ui.panel.collection.staffphone;

import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.tools.DateUtils;

import com.nokor.efinance.core.collection.model.ContractFlag;
import com.nokor.efinance.core.collection.model.EFlag;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

/**
 * @author buntha.chea
 */
public class ReturnMotobikeRequestPanel extends AbstractControlPanel implements FinServicesHelper {

	/** */
	private static final long serialVersionUID = 7383958395123969142L;

	private Label lblRequestedBy;
	private Label lblRequestedOn;
	private Label lblResult;
	
	/**
	 * 
	 * @return
	 */
	private Label getLabelValue() {
		Label label = ComponentFactory.getHtmlLabel(null);
		label.setSizeUndefined();
		return label;
	}
	
	/**
	 * 
	 */
	public ReturnMotobikeRequestPanel() {
		setWidth(525, Unit.PIXELS);
		Panel mainPanel = new Panel(getContentLayout());
		mainPanel.setCaption(I18N.message("return.motobike.request"));
		addComponent(mainPanel);
	}
	
	/**
	 * 
	 * @return
	 */
	private Component getContentLayout() {
		lblRequestedBy = getLabelValue();
		lblRequestedOn = getLabelValue();
		lblResult = getLabelValue();
				
		GridLayout gridLayout = new GridLayout(10, 1);
		gridLayout.setMargin(true);
		gridLayout.setSpacing(true);
				
		int iCol = 0;
		gridLayout.addComponent(getLabelCaption(I18N.message("requested.by") + " : "), iCol++, 0);
		gridLayout.addComponent(lblRequestedBy, iCol++, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(5, Unit.PIXELS), iCol++, 0);
		gridLayout.addComponent(getLabelCaption(I18N.message("requested.on") + " : "), iCol++, 0);
		gridLayout.addComponent(lblRequestedOn, iCol++, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(5, Unit.PIXELS), iCol++, 0);
		gridLayout.addComponent(getLabelCaption(I18N.message("result") + " : "), iCol++, 0);
		gridLayout.addComponent(lblResult, iCol++, 0);
		
		VerticalLayout contentLayout = new VerticalLayout();
		contentLayout.addComponent(gridLayout);			
		return contentLayout;
	}

	/**
	 * 
	 * @param value
	 * @return
	 */
	private String getDescription(String value) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("<b>");
		stringBuffer.append(value == null ? "" : value);
		stringBuffer.append("</b>");
		return stringBuffer.toString();
	}
	
	/**
	 * 
	 * @param contract
	 */
	public void assignValues(Contract contract) {
		resetControls();
		List<ContractFlag> contractFlags = getContractFlags(contract);
		manageVisible(contractFlags);
		for (ContractFlag contractFlag : contractFlags) {
			lblRequestedBy.setValue(getDescription(contractFlag == null ? "" : contractFlag.getUpdateUser()));
			lblRequestedOn.setValue(getDescription(contractFlag.getUpdateDate() == null ? "" : DateUtils.getDateLabel(contractFlag.getUpdateDate(), "dd/MM/yyyy")));
			lblResult.setValue(getDescription(contractFlag == null ? "" : contractFlag.getWkfStatus().getDescEn()));
		}
	}
	
	/**
	 * 
	 * @param contract
	 * @return
	 */
	private List<ContractFlag> getContractFlags(Contract contract) {
		BaseRestrictions<ContractFlag> restrictions = new BaseRestrictions<>(ContractFlag.class);
		restrictions.addCriterion(Restrictions.eq("contract", contract));
		restrictions.addCriterion(Restrictions.eq("flag", EFlag.RETURN));
		return ENTITY_SRV.list(restrictions);
	}
	
	/**
	 * 
	 */
	private void resetControls() {
		lblRequestedBy.setValue("");
		lblRequestedOn.setValue("");
		lblResult.setValue("");
	}
	
	/**
	 * 
	 * @param caption
	 * @return
	 */
	private Label getLabelCaption(String caption) {
		Label label = ComponentFactory.getLabel(caption);
		label.setSizeUndefined();
		return label;
	}
	
	/**
	 * 
	 * @param contractFlags
	 */
	private void manageVisible(List<ContractFlag> contractFlags) {
		if (contractFlags != null && contractFlags.isEmpty()) {
			setVisible(false);
		} else {
			setVisible(true);
		}
	}
}

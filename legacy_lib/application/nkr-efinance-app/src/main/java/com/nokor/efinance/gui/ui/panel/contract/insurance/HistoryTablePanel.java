package com.nokor.efinance.gui.ui.panel.contract.insurance;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;

import com.gl.finwiz.share.domain.insurance.InsuranceDTO;
import com.gl.finwiz.share.domain.insurance.InsuranceTaskDTO;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.third.finwiz.client.ins.ClientInsurance;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimpleTable;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.themes.Reindeer;

/**
 * History table panel
 * @author uhout.cheng
 */
public class HistoryTablePanel extends Panel implements FinServicesHelper {

	/** */
	private static final long serialVersionUID = 2396367516019145719L;

	private final static String ISRTYPE = "isr.type";
	private final static String USERID = "user.id";
	private final static String DATE = "date";
	
	private SimpleTable<Entity> simpleTable;
	
	/**
	 * 
	 */
	public HistoryTablePanel() {
		init();
	}
	
	/**
	 * 
	 */
	private void init() {
		simpleTable = new SimpleTable<Entity>(getColumnDefinitions());
		simpleTable.setSizeFull();
		simpleTable.setPageLength(3);
		setCaption(I18N.message("history"));
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
		columnDefinitions.add(new ColumnDefinition(DATE, I18N.message("date"), Date.class, Align.LEFT, 80));
		columnDefinitions.add(new ColumnDefinition(USERID, I18N.message("user.id"), String.class, Align.LEFT, 130));
		return columnDefinitions;
	}
	
	/**
	 * 
	 * @param contract
	 */
	public void assignValues(Contract contract) {
		setIndexedContainer(ClientInsurance.getInsTaskByContractReference(contract.getReference()));
	}
	
	/**
	 * 
	 * @param insTaskDTOs
	 */
	@SuppressWarnings("unchecked")
	private void setIndexedContainer(List<InsuranceTaskDTO> insTaskDTOs) {
		simpleTable.removeAllItems();
		Container container = simpleTable.getContainerDataSource();
		if (insTaskDTOs != null && !insTaskDTOs.isEmpty()) {
			int index = 0;
			for (InsuranceTaskDTO insTaskDTO : insTaskDTOs) {
				Item item = container.addItem(index);

				item.getItemProperty(DATE).setValue(new Date(insTaskDTO.getCreatedDate().getTime()));
				item.getItemProperty(USERID).setValue(insTaskDTO.getCreatedBy());
				
				InsuranceDTO insDTO = insTaskDTO.getInsurance();
				if (insDTO != null) {
					item.getItemProperty(ISRTYPE).setValue(insDTO.getInsuranceType() == null ? StringUtils.EMPTY : 
						insDTO.getInsuranceType().getValue());
				}
				index++;
			}
		}
	}
	
}

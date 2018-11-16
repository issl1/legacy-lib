package com.nokor.efinance.gui.ui.panel.collection.staffphone;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;

import com.nokor.efinance.core.applicant.model.EApplicantType;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.ersys.core.hr.model.address.Address;
import com.nokor.ersys.core.hr.model.address.MBaseAddress;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimpleTable;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.themes.Reindeer;

/**
 * Contact addresses table 
 * @author uhout.cheng
 */
public class ContactAddressTable extends AbstractControlPanel implements FinServicesHelper, MBaseAddress {

	/** */
	private static final long serialVersionUID = 6836243481463548819L;

	private final static String ADDRESS = "address";
	
	private SimpleTable<Entity> simpleTable;
	private List<ColumnDefinition> columnDefinitions;
	
	/**
	 * 
	 * @param caption
	 */
	public ContactAddressTable(String caption) {
		init(caption);
	}
	
	/**
	 * 
	 * @param caption
	 */
	private void init(String caption) {
		simpleTable = new SimpleTable<Entity>(getColumnDefinitions());
		simpleTable.setSizeFull();
		simpleTable.setPageLength(3);
		
		Panel tablePanel = new Panel(simpleTable);
		tablePanel.setCaption(I18N.message(caption));
		tablePanel.setStyleName(Reindeer.PANEL_LIGHT);
		addComponent(tablePanel);
	}
	
	/**
	 * 
	 * @return
	 */
	private List<ColumnDefinition> getColumnDefinitions() {
		columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(TYPE, I18N.message("type"), String.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition(RESIDENCESTATUS, I18N.message("residence.status"), String.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition(RESIDENCETYPE, I18N.message("residence.type"), String.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition(ADDRESS, I18N.message("address"), String.class, Align.LEFT, 550));
		return columnDefinitions;
	}
	
	/**
	 * 
	 * @param addresses
	 * @param applicantType
	 */
	public void assignValues(Map<EApplicantType, List<Address>> addresses, EApplicantType applicantType) {
		simpleTable.setContainerDataSource(getAddressesIndexedContainer(addresses, applicantType));
	}
	
	/**
	 * 
	 * @param addresses
	 * @param applicantType
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public IndexedContainer getAddressesIndexedContainer(Map<EApplicantType, List<Address>> addresses, EApplicantType applicantType) {
		IndexedContainer indexedContainer = new IndexedContainer();
		for (ColumnDefinition column : this.columnDefinitions) {
			indexedContainer.addContainerProperty(column.getPropertyId(), column.getPropertyType(), null);
		}
		if (addresses != null && !addresses.isEmpty()) {
			List<Address> addrs = addresses.get(applicantType);
			if (addrs != null && !addrs.isEmpty()) {
				for (int i = 0; i < addrs.size(); i++) {
					Item item = indexedContainer.addItem(i);
					item.getItemProperty(TYPE).setValue(addrs.get(i).getType() != null ? addrs.get(i).getType().getDescLocale() : "");
					item.getItemProperty(RESIDENCESTATUS).setValue(addrs.get(i).getResidenceStatus() != null ? 
							addrs.get(i).getResidenceStatus().getDescLocale() : "");
					item.getItemProperty(RESIDENCETYPE).setValue(addrs.get(i).getResidenceType() != null ? 
							addrs.get(i).getResidenceType().getDescLocale() : "");
					item.getItemProperty(ADDRESS).setValue(getAddress(addrs.get(i)));
				}
			}
			
		}
		return indexedContainer;
	}
	
	/**
	 * 
	 * @param address
	 * @return
	 */
	private String getAddress(Address address) {
		StringBuffer stringBuffer = new StringBuffer(); 
		List<String> descriptions = new ArrayList<>();
		descriptions.add(getDefaultString(address.getHouseNo()));
		descriptions.add(getDefaultString(address.getLine1()));
		descriptions.add(getDefaultString(address.getLine2()));
		descriptions.add(getDefaultString(address.getStreet()));
		descriptions.add(address.getCommune() != null ? address.getCommune().getDescEn() : "");
		descriptions.add(address.getDistrict() != null ? address.getDistrict().getDescEn() : "");
		descriptions.add(address.getProvince() != null ? address.getProvince().getDescEn() : "");
		descriptions.add(getDefaultString(address.getPostalCode()));
		for (String string : descriptions) {
			stringBuffer.append(string);
			if (StringUtils.isNotEmpty(string)) {
				stringBuffer.append(",");
			}
		}
		int lastIndex = stringBuffer.lastIndexOf(",");
		stringBuffer.replace(lastIndex, lastIndex + 1, "");
		return stringBuffer.toString();
	}

}

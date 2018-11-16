package com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.contact;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;

import com.nokor.efinance.core.collection.model.CollectionHistory;
import com.nokor.efinance.core.collection.model.ECallType;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.ersys.core.hr.model.eref.ETypeContactInfo;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimpleTable;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.Reindeer;

/**
 * 
 * @author uhout.cheng
 */
public class ColContactFollowUpHistoryTable extends Panel implements FinServicesHelper {

	/** */
	private static final long serialVersionUID = 5300755663451455970L;

	private SimpleTable<Entity> simpleTable;
	
	/**
	 * 
	 */
	public ColContactFollowUpHistoryTable() {
		init();
	}
	
	/**
	 * 
	 */
	private void init() {
		simpleTable = new SimpleTable<Entity>(getColumnDefinitions());
		simpleTable.setPageLength(5);
		setStyleName(Reindeer.PANEL_LIGHT);
		setContent(simpleTable);
	}
	
	/**
	 * 
	 * @return
	 */
	private List<ColumnDefinition> getColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(CollectionHistory.ID, I18N.message("id"), Long.class, Align.LEFT, 50, false));
		columnDefinitions.add(new ColumnDefinition(CollectionHistory.CREATEDATE, I18N.message("date"), Date.class, Align.LEFT, 70));
		columnDefinitions.add(new ColumnDefinition(CollectionHistory.CREATEUSER, I18N.message("user"), String.class, Align.LEFT, 100));
//		columnDefinitions.add(new ColumnDefinition(CollectionHistory.CHANNEL, I18N.message("channel"), String.class, Align.LEFT, 80));
		columnDefinitions.add(new ColumnDefinition(CollectionHistory.CALLTYPE, I18N.message("type"), String.class, Align.LEFT, 70));
		columnDefinitions.add(new ColumnDefinition(CollectionHistory.NOADDMAIL, I18N.message("no.add.mail"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(CollectionHistory.CONTACTWITH, I18N.message("contact.with.by"), String.class, Align.LEFT, 100));
//		columnDefinitions.add(new ColumnDefinition(CollectionHistory.SUBJECT, I18N.message("subject"), String.class, Align.LEFT, 100));
//		columnDefinitions.add(new ColumnDefinition(CollectionHistory.ANSWER, I18N.message("answer"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(CollectionHistory.CATEGORY, I18N.message("category"), Button.class, Align.CENTER, 55));
		return columnDefinitions;
	}
	
	/**
	 * 
	 * @param contract
	 */
	public void assignValues(Contract contract) {	
		setUserDetailIndexedContainer(COL_SRV.getCollectionHistoriesByContractId(contract.getId()));
	}
	
	/**
	 * 
	 * @param colHistories
	 */
	@SuppressWarnings("unchecked")
	private void setUserDetailIndexedContainer(List<CollectionHistory> colHistories) {
		simpleTable.removeAllItems();
		Container indexedContainer = simpleTable.getContainerDataSource();
		if (colHistories != null && !colHistories.isEmpty()) {
			for (CollectionHistory history : colHistories) {
				Item item = indexedContainer.addItem(history.getId());
				item.getItemProperty(CollectionHistory.ID).setValue(history.getId());
				item.getItemProperty(CollectionHistory.CREATEDATE).setValue(history.getCreateDate());
				item.getItemProperty(CollectionHistory.CREATEUSER).setValue(history.getCreateUser());
//				item.getItemProperty(CollectionHistory.CHANNEL).setValue(history.getCallType() != null ? 
//						history.getCallType().getDescLocale() : StringUtils.EMPTY);
//				item.getItemProperty(CollectionHistory.CALLTYPE).setValue(history.isCallIn() ? "IN" : "OUT");
				item.getItemProperty(CollectionHistory.CALLTYPE).setValue(history.getCallType() != null ? 
						history.getCallType().getDescLocale() : StringUtils.EMPTY);
				String detailInfo = StringUtils.EMPTY;
				if (ECallType.FIELD.equals(history.getCallType()) && history.getAddress() != null) {
					detailInfo = ADDRESS_SRV.getDetailAddress(history.getAddress());
				} else {
					String cntType = StringUtils.EMPTY;
					if (ETypeContactInfo.MOBILE.equals(history.getContactedTypeInfo())) {
						cntType = "(M)";
					} else if (ETypeContactInfo.LANDLINE.equals(history.getContactedTypeInfo())) {
						cntType = "(L)";
					} else if (ETypeContactInfo.EMAIL.equals(history.getContactedTypeInfo())) {
						cntType = "(E)";
					}
					detailInfo = StringUtils.isNotEmpty(cntType) ? cntType + StringUtils.SPACE + history.getContactedInfoValue() :
						history.getOtherContact();
				}
				item.getItemProperty(CollectionHistory.NOADDMAIL).setValue(detailInfo);
				item.getItemProperty(CollectionHistory.CONTACTWITH).setValue(history.getReachedPerson() != null ? 
						history.getReachedPerson().getDescLocale() : StringUtils.EMPTY);
//				item.getItemProperty(CollectionHistory.SUBJECT).setValue(history.getSubject() != null ?
//						history.getSubject().getDescLocale() : StringUtils.EMPTY);
//				item.getItemProperty(CollectionHistory.ANSWER).setValue(history.getComment());
				Button btnInfo = ComponentLayoutFactory.getButtonIcon(FontAwesome.INFO);
				btnInfo.setData(history.getCallType());
				item.getItemProperty(CollectionHistory.CATEGORY).setValue(btnInfo);
				
				btnInfo.addClickListener(new ClickListener() {
					
					/** */
					private static final long serialVersionUID = 143989164036314811L;

					@Override
					public void buttonClick(ClickEvent event) {
						ECallType callType = (ECallType) btnInfo.getData();
						if (ECallType.MAIL.equals(callType)) {
							EmailContactDetailPopupPanel emailContactDetailPopupPanel = new EmailContactDetailPopupPanel();
							emailContactDetailPopupPanel.assignValues(history);
							UI.getCurrent().addWindow(emailContactDetailPopupPanel);
						} else if (ECallType.CALL.equals(callType)) {
							CallContactDetailPopupPanel callContactDetailPopupPanel = new CallContactDetailPopupPanel();
							callContactDetailPopupPanel.assignValues(history);
							UI.getCurrent().addWindow(callContactDetailPopupPanel);
						} 	
					}
				});
			}
		}
	}

}

package com.nokor.efinance.gui.ui.panel.inbox.collection.ar;

import java.util.ArrayList;
import java.util.List;

import org.seuksa.frmk.model.entity.Entity;

import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimpleTable;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * 
 * @author buntha.chea
 *
 */
public class RecordPaymentPopupPanel extends Window implements ClickListener {

	private static final long serialVersionUID = -4545472466911317146L;
	
	private static final String ID = "id";
	private static final String ITEM = "item";
	private static final String TOTAL = "total";
	private static final String PAYMENT = "payment";
	private static final String BALANCE = "balance";
	
	private static final String CHEQUE = I18N.message("cheque");
	private static final String TRANFER = I18N.message("tranfer");
	private static final String CASH = I18N.message("cash");
	
	private SimpleTable<Entity> simpleTable;
	private ComboBox cbxPaymentBy;
	
	private Button btnSubmit;
	private Button btnCancel;
	
	public RecordPaymentPopupPanel(String caption) {
		setModal(true);
		setCaption(I18N.message(caption));
		
		simpleTable = new SimpleTable<>(createColumnDifinition());
		simpleTable.setPageLength(5);
		btnSubmit = ComponentLayoutFactory.getButtonStyle("submin", FontAwesome.CHECK, 70, "btn btn-success");
		btnSubmit.addClickListener(this);
		
		btnCancel = ComponentLayoutFactory.getButtonStyle("cancel", FontAwesome.TIMES, 70, "btn btn-danger");
		btnCancel.addClickListener(this);
		
		cbxPaymentBy = new ComboBox();
		cbxPaymentBy.addItem(CHEQUE);
		cbxPaymentBy.addItem(CASH);
		cbxPaymentBy.addItem(TRANFER);
		cbxPaymentBy.select(CHEQUE);
		
		HorizontalLayout paymentByLayout = ComponentLayoutFactory.getHorizontalLayout(false, true);
		paymentByLayout.addComponent(ComponentLayoutFactory.getLabelCaption("payment.by"));
		paymentByLayout.addComponent(cbxPaymentBy);
		
		HorizontalLayout buttonaLayout = ComponentLayoutFactory.getHorizontalLayout(true, true);
		buttonaLayout.addComponent(btnSubmit);
		buttonaLayout.addComponent(btnCancel);
		
		VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.setSpacing(true);
		mainLayout.setMargin(true);
		
		mainLayout.addComponent(simpleTable);
		mainLayout.addComponent(paymentByLayout);
		mainLayout.addComponent(buttonaLayout);
		mainLayout.setComponentAlignment(buttonaLayout, Alignment.BOTTOM_RIGHT);
		setContent(mainLayout);
	}
	
	/**
	 * 
	 * @return
	 */
	private List<ColumnDefinition> createColumnDifinition() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 50, false));
		columnDefinitions.add(new ColumnDefinition(ITEM, I18N.message("item"), String.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition(TOTAL, I18N.message("total"), String.class, Align.LEFT, 80));
		columnDefinitions.add(new ColumnDefinition(PAYMENT, I18N.message("payment"), HorizontalLayout.class, Align.LEFT, 140));
		columnDefinitions.add(new ColumnDefinition(BALANCE, I18N.message("balance"), String.class, Align.LEFT, 80));
		return columnDefinitions;
	}
	
	/**
	 * 
	 * @param event
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnSubmit) {
			close();
		} else if (event.getButton() == btnCancel) {
			close();
		}
	}

}

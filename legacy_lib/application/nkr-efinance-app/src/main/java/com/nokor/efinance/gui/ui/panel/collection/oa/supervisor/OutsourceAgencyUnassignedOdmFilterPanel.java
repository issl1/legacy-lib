package com.nokor.efinance.gui.ui.panel.collection.oa.supervisor;

import java.util.List;

import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.gui.ui.panel.collection.supervisor.AreaSelectPopupPanel;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author uhout.cheng
 */
public class OutsourceAgencyUnassignedOdmFilterPanel extends AbstractControlPanel implements ClickListener {

	/** */
	private static final long serialVersionUID = -3157048495566293229L;

	private ComboBox cbxDueDateFrom;
	private ComboBox cbxDueDateTo;
	private CheckBox cbFlag;
	private CheckBox cbAssist;
	private CheckBox cbExpired;
	
	private Button btnSearch;
	private Button btnReset;
	private Button btnSelect;
	
	private boolean isSearchAction = false;
	
	private OutsourceAgencyUnassignedOdmPanel unassignedOdmPanel;
	
	private AreaSelectPopupPanel selectPopup;
	
	private List<Long> selectArea = null ;
	
	/**
	 * 
	 * @param unassignedOdmPanel
	 */
	public OutsourceAgencyUnassignedOdmFilterPanel(OutsourceAgencyUnassignedOdmPanel unassignedOdmPanel) {
		
		selectPopup = new AreaSelectPopupPanel();
		
		this.unassignedOdmPanel = unassignedOdmPanel;
		cbxDueDateFrom = getComboBoxDueDate();
		cbxDueDateTo = getComboBoxDueDate();
		cbFlag = new CheckBox(I18N.message("flag"));
		cbAssist = new CheckBox(I18N.message("assist"));
		cbExpired = new CheckBox(I18N.message("expired"));
		
		Label lblArea = ComponentFactory.getLabel(I18N.message("area"));
		btnSelect = new NativeButton(I18N.message("select"));
		btnSelect.addStyleName("btn btn-success button-small");
		btnSelect.addClickListener(this);
		
		btnSearch = ComponentLayoutFactory.getButtonSearch();
		btnReset = ComponentLayoutFactory.getButtonReset();
		
		btnSearch.addClickListener(this);
		btnReset.addClickListener(this);
		
		HorizontalLayout layout = ComponentLayoutFactory.getHorizontalLayout(false, true);
		layout.addComponent(ComponentLayoutFactory.getLabelCaption(I18N.message("due.date") + " : "));
		layout.addComponent(ComponentLayoutFactory.getLabelCaption("from"));
		layout.addComponent(cbxDueDateFrom);
		layout.addComponent(ComponentLayoutFactory.getLabelCaption("to"));
		layout.addComponent(cbxDueDateTo);
		layout.addComponent(cbFlag);
		layout.addComponent(cbAssist);
		layout.addComponent(cbExpired);
		layout.addComponent(lblArea);
		layout.addComponent(btnSelect);
		
		layout.setComponentAlignment(cbFlag, Alignment.MIDDLE_CENTER);
		layout.setComponentAlignment(cbAssist, Alignment.MIDDLE_CENTER);
		layout.setComponentAlignment(cbExpired, Alignment.MIDDLE_CENTER);
		layout.setComponentAlignment(lblArea, Alignment.MIDDLE_CENTER);
		
		HorizontalLayout btnLayout = ComponentLayoutFactory.getHorizontalLayout(false, true);
		btnLayout.addComponent(btnSearch);
		btnLayout.addComponent(btnReset);
		
		VerticalLayout verLayout = ComponentLayoutFactory.getVerticalLayout(true, true);
		verLayout.addComponent(layout);
		verLayout.addComponent(btnLayout);
		verLayout.setComponentAlignment(btnLayout, Alignment.BOTTOM_CENTER);
		
		Panel searchPanel = new Panel(verLayout);
		
		addComponent(searchPanel);
	}
	
	/**
	 * Validate Before Filter
	 * @return
	 */
	private boolean validate() {
		super.reset();
		
		if (cbxDueDateFrom.getValue() == null && cbxDueDateTo.getValue() != null) {
			errors.add(I18N.message("field.required.1",new String[] { I18N.message("from") }));
		} else if (cbxDueDateFrom.getValue() != null && cbxDueDateTo.getValue() == null) {
			errors.add(I18N.message("field.required.1",new String[] { I18N.message("to") }));
		} else if (cbxDueDateFrom.getValue() != null && cbxDueDateTo.getValue() != null) {
			if (converterObj(cbxDueDateFrom.getValue()) > converterObj(cbxDueDateTo.getValue())) {
				errors.add(I18N.message("field.due.date.from.should.less.than.field.due.date.to"));
			}
		}
		return errors.isEmpty();
	}
	
	/**
	 * convert object from combo box due date form and to
	 * @param value
	 * @return
	 */
	private Integer converterObj(Object value) {
		if (value != null) {
			return Integer.valueOf(String.valueOf(value));
		}
		return null;
	}
	
	/**
	 * Get Combo box due date with data from 1 to 20
	 * @param caption
	 * @return
	 */
	private ComboBox getComboBoxDueDate() {
		ComboBox comboBox = ComponentFactory.getComboBox();
		comboBox.setWidth("50px");
		for (int items = 1; items <= 20; items++) {
			comboBox.addItem(String.valueOf(items));
		}
		return comboBox;
	}
	
	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		this.isSearchAction = false;
		if (event.getButton().equals(btnSearch)) {
			if (validate()) {
				this.isSearchAction = true;
				unassignedOdmPanel.setStaffTableContainerDataSource(true);
				this.isSearchAction = false;
			} else {
				ComponentLayoutFactory.displayErrorMsg(errors.get(0).toString());
			}
		} else if (event.getButton().equals(btnReset)) {
			this.reset();
		} else if (event.getButton().equals(btnSelect)) {
			showAreaSelectPanel();
		}
	}
	
	/**
	 * show area
	 */
	private void showAreaSelectPanel() {
		AreaSelectPopupPanel areaSelectPopupPanel = new AreaSelectPopupPanel();
		areaSelectPopupPanel.show(new AreaSelectPopupPanel.Listener() {				
			private static final long serialVersionUID = 7757863260440994161L;
			@Override
			public void onClose(AreaSelectPopupPanel dialog) {
				selectArea = dialog.getSelectedIds();
				unassignedOdmPanel.setAreaIds(selectArea);
				selectPopup.reset();
			}
		});
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel#reset()
	 */
	@Override
	protected void reset() {
		cbxDueDateFrom.setValue(null);
		cbxDueDateTo.setValue(null);
		cbFlag.setValue(false);
		cbAssist.setValue(false);
		cbExpired.setValue(false);
	}

	/**
	 * @return the isSearchAction
	 */
	public boolean isSearchAction() {
		return isSearchAction;
	}

	/**
	 * @return the cbxDueDateFrom
	 */
	public ComboBox getCbxDueDateFrom() {
		return cbxDueDateFrom;
	}

	/**
	 * @return the cbxDueDateTo
	 */
	public ComboBox getCbxDueDateTo() {
		return cbxDueDateTo;
	}

	/**
	 * @return the cbFlag
	 */
	public CheckBox getCbFlag() {
		return cbFlag;
	}

	/**
	 * @return the cbAssist
	 */
	public CheckBox getCbAssist() {
		return cbAssist;
	}
	
}

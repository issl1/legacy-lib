/**
 * 
 */
package com.nokor.ersys.vaadin.ui.referential.address.province.list;

import java.util.ArrayList;
import java.util.Collection;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;

import com.nokor.ersys.core.hr.model.address.Province;
import com.nokor.ersys.core.hr.service.ProvinceRestriction;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.EStatusRecordOptionGroup;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.TextField;

/**
 * 
 * @author phirun.kong
 *
 */
public class ProvinceSearchPanel extends AbstractSearchPanel<Province> {
	/**	 */
	private static final long serialVersionUID = 5489871740478671343L;

	private TextField txtCode;
	private TextField txtDesc;
	private EStatusRecordOptionGroup cbStatuses;
	
	public ProvinceSearchPanel(ProvinceTablePanel provinceTablePanel) {
		super(I18N.message("search"), provinceTablePanel);
	}
	
	@Override
	protected void reset() {
		txtCode.setValue("");
		txtDesc.setValue("");
		cbStatuses.setDefaultValue();
	}

	@Override
	protected Component createForm() {		
		final GridLayout gridLayout = new GridLayout(3, 1);	
		gridLayout.setWidth(100, Unit.PERCENTAGE);
		txtCode = ComponentFactory.getTextField("code", false, 60, 180);        
		txtDesc = ComponentFactory.getTextField("desc.search", false, 60, 180);	
		cbStatuses = new EStatusRecordOptionGroup();
	    
	    gridLayout.setSpacing(true);
        gridLayout.addComponent(new FormLayout(txtCode));
        gridLayout.addComponent(new FormLayout(txtDesc));
        gridLayout.addComponent(new FormLayout(cbStatuses));
        
		return gridLayout;
	}
	
	@Override
	public ProvinceRestriction getRestrictions() {
		ProvinceRestriction restriction = new ProvinceRestriction();
		restriction.setCode(txtCode.getValue());
		restriction.setDesc(txtDesc.getValue());
		Collection<EStatusRecord> colSta = (Collection<EStatusRecord>) cbStatuses.getValue();
		restriction.setStatusRecordList(new ArrayList(colSta));
		return restriction;
	}
}

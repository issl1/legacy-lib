/**
 * 
 */
package com.nokor.ersys.vaadin.ui.referential.address.zone.list;

import java.util.ArrayList;
import java.util.Collection;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;

import com.nokor.ersys.core.hr.model.address.Province;
import com.nokor.ersys.core.hr.model.address.Zone;
import com.nokor.ersys.core.hr.service.ProvinceRestriction;
import com.nokor.ersys.core.hr.service.ZoneRestriction;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.EStatusRecordOptionGroup;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityRefComboBox;
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
public class ZoneSearchPanel extends AbstractSearchPanel<Zone> {

	/**	 */
	private static final long serialVersionUID = -4182359941280340720L;
	
	private TextField txtCode;
	private TextField txtDesc;
	private EntityRefComboBox<Province> cbxProvince;
	private EStatusRecordOptionGroup cbStatuses;
	
	public ZoneSearchPanel(ZoneTablePanel zoneTablePanel) {
		super(I18N.message("search"), zoneTablePanel);
	}
	
	@Override
	protected void reset() {
		txtCode.setValue("");
		txtDesc.setValue("");
		cbxProvince.setSelectedEntity(null);
		cbStatuses.setDefaultValue();
	}


	@Override
	protected Component createForm() {	
		
		txtCode = ComponentFactory.getTextField("code", false, 50, 180);        
		txtDesc = ComponentFactory.getTextField("desc.search", false, 50, 180);	
		cbxProvince = new EntityRefComboBox<Province>(I18N.message("province"));
		cbxProvince.setRestrictions(new ProvinceRestriction());
		cbxProvince.setWidth(180, Unit.PIXELS);
		cbxProvince.renderer();
		
		cbStatuses = new EStatusRecordOptionGroup();
        
        final GridLayout gridLayout = new GridLayout(3, 1);
		gridLayout.setWidth(100, Unit.PERCENTAGE);
		
		FormLayout formLayoutLeft = new FormLayout();
		formLayoutLeft.setWidth(100, Unit.PERCENTAGE);
		formLayoutLeft.addComponent(txtCode);
		formLayoutLeft.addComponent(cbxProvince);
		
		FormLayout formLayoutMiddle = new FormLayout();
		formLayoutMiddle.setWidth(100, Unit.PERCENTAGE);
		formLayoutMiddle.addComponent(txtDesc);
		formLayoutMiddle.addComponent(cbStatuses);
		
		gridLayout.addComponent(formLayoutLeft);
		gridLayout.addComponent(formLayoutMiddle);	
		
		return gridLayout;
	}
	
	@Override
	public ZoneRestriction getRestrictions() {	
		ZoneRestriction restriction = new ZoneRestriction();
		restriction.setCode(txtCode.getValue());
		restriction.setDesc(txtDesc.getValue());
		restriction.setProvince(cbxProvince.getSelectedEntity());
		Collection<EStatusRecord> colSta = (Collection<EStatusRecord>) cbStatuses.getValue();
		restriction.setStatusRecordList(new ArrayList(colSta));
		return restriction;
	}
}

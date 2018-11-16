/**
 * 
 */
package com.nokor.ersys.vaadin.ui.referential.address.commune.list;

import java.util.ArrayList;
import java.util.Collection;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;

import com.nokor.ersys.core.hr.model.address.Commune;
import com.nokor.ersys.core.hr.model.address.District;
import com.nokor.ersys.core.hr.model.address.Province;
import com.nokor.ersys.core.hr.service.CommuneRestriction;
import com.nokor.ersys.core.hr.service.DistrictRestriction;
import com.nokor.ersys.core.hr.service.ProvinceRestriction;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.EStatusRecordOptionGroup;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityRefComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.TextField;


/**
 * 
 * @author phirun.kong
 *
 */
public class CommuneSearchPanel extends AbstractSearchPanel<Commune> {
	/**	 */
	private static final long serialVersionUID = -8845527509232330942L;

	private TextField txtCode;
	private TextField txtDesc;
	private EntityRefComboBox<District> cbxDistrict;
	private EntityRefComboBox<Province> cbxProvince;
	private EStatusRecordOptionGroup cbStatuses;
	
	public CommuneSearchPanel(CommuneTablePanel communeTablePanel) {
		super(I18N.message("search"), communeTablePanel);
	}
	
	@Override
	protected void reset() {
		txtCode.setValue("");
		txtDesc.setValue("");
		cbxDistrict.setSelectedEntity(null);
		cbxProvince.setSelectedEntity(null);
		cbStatuses.setDefaultValue();
	}

	@Override
	protected Component createForm() {		
		txtCode = ComponentFactory.getTextField("code",false, 50, 180);		        
		txtDesc = ComponentFactory.getTextField("desc.search",false, 50, 180);
		cbxProvince = new EntityRefComboBox<Province>(I18N.message("province"));
		cbxProvince.setRestrictions(new ProvinceRestriction());
		cbxProvince.setWidth(180, Unit.PIXELS);
		cbxProvince.setImmediate(true);
		cbxProvince.renderer();
		cbxProvince.addValueChangeListener(new ValueChangeListener() {

			private static final long serialVersionUID = 6613715128059756285L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if (cbxProvince.getSelectedEntity() != null) {
					DistrictRestriction restriction = new DistrictRestriction();
					restriction.setProvince(cbxProvince.getSelectedEntity());
					cbxDistrict.setRestrictions(restriction);
					cbxDistrict.renderer();
				} else {
					cbxDistrict.clear();
				}
			}
		});
		cbxDistrict = new EntityRefComboBox<District>(I18N.message("district"));
		cbxDistrict.setWidth(180, Unit.PIXELS);
		cbxDistrict.setImmediate(true);
        
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
		formLayoutMiddle.addComponent(cbxDistrict);

		FormLayout formLayoutRight = new FormLayout();
        formLayoutRight.setWidth(100, Unit.PERCENTAGE);
        formLayoutRight.setSpacing(true);
        formLayoutRight.addComponent(cbStatuses);
        
		gridLayout.addComponent(formLayoutLeft);
		gridLayout.addComponent(formLayoutMiddle);
		gridLayout.addComponent(formLayoutRight);
		
		return gridLayout;
	}
	
	@Override
	public CommuneRestriction getRestrictions() {	
		CommuneRestriction restriction = new CommuneRestriction();
		restriction.setCode(txtCode.getValue());
		restriction.setDesc(txtDesc.getValue());
		restriction.setDistrict(cbxDistrict.getSelectedEntity());
		restriction.setProvince(cbxProvince.getSelectedEntity());
		Collection<EStatusRecord> colSta = (Collection<EStatusRecord>) cbStatuses.getValue();
		restriction.setStatusRecordList(new ArrayList(colSta));
		return restriction;
	}
}

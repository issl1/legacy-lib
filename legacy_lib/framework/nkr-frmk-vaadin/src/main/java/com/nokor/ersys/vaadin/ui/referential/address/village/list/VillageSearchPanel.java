/**
 * 
 */
package com.nokor.ersys.vaadin.ui.referential.address.village.list;

import java.util.ArrayList;
import java.util.Collection;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;

import com.nokor.ersys.core.hr.model.address.Commune;
import com.nokor.ersys.core.hr.model.address.District;
import com.nokor.ersys.core.hr.model.address.Province;
import com.nokor.ersys.core.hr.model.address.Village;
import com.nokor.ersys.core.hr.service.CommuneRestriction;
import com.nokor.ersys.core.hr.service.DistrictRestriction;
import com.nokor.ersys.core.hr.service.ProvinceRestriction;
import com.nokor.ersys.core.hr.service.VillageRestriction;
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
public class VillageSearchPanel extends AbstractSearchPanel<Village> {
	/**	 */
	private static final long serialVersionUID = 5128511390005531648L;

	private TextField txtCode;
	private TextField txtDesc;
	private EntityRefComboBox<Commune> cbxCommune;
	private EntityRefComboBox<Province> cbxProvince;
    private EntityRefComboBox<District> cbxDistrict;
    private EStatusRecordOptionGroup cbStatuses;
	
	public VillageSearchPanel(VillageTablePanel villageTablePanel) {
		super(I18N.message("search"), villageTablePanel);
	}
	
	@Override
	protected void reset() {
		txtCode.setValue("");
		txtDesc.setValue("");
		cbxProvince.setSelectedEntity(null);
		cbxDistrict.setSelectedEntity(null);
		cbxCommune.setSelectedEntity(null);
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

			/** */
			private static final long serialVersionUID = -6193972388187498579L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if (cbxProvince.getSelectedEntity() != null) {
					DistrictRestriction restriction = new DistrictRestriction();
					restriction.setProvince(cbxProvince.getSelectedEntity());
					cbxDistrict.setRestrictions(restriction);
					cbxDistrict.renderer();
				} else {
					cbxDistrict.clear();
					cbxCommune.clear();
				}
			}
		});
		cbxDistrict = new EntityRefComboBox<District>(I18N.message("district"));
		cbxDistrict.setWidth(180, Unit.PIXELS);
		cbxDistrict.setImmediate(true);
		cbxDistrict.addValueChangeListener(new ValueChangeListener() {

			/** */
			private static final long serialVersionUID = 1254869953823247343L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if (cbxDistrict.getSelectedEntity() != null) {
					CommuneRestriction restriction = new CommuneRestriction();
					restriction.setDistrict(cbxDistrict.getSelectedEntity());
					cbxCommune.setRestrictions(restriction);
					cbxCommune.renderer();
				} else {
					cbxCommune.clear();
				}
			}
		});
		cbxCommune = new EntityRefComboBox<Commune>(I18N.message("commune"));
		cbxCommune.setWidth(180, Unit.PIXELS);
		cbxCommune.setImmediate(true);
		
		cbStatuses = new EStatusRecordOptionGroup();
	    
	    final GridLayout gridLayout = new GridLayout(3, 1);
		gridLayout.setWidth(100, Unit.PERCENTAGE);
		
		FormLayout formLayoutLeft = new FormLayout();
		formLayoutLeft.setWidth(100, Unit.PERCENTAGE);
		formLayoutLeft.addComponent(txtCode);
		formLayoutLeft.addComponent(cbxDistrict);
		
		FormLayout formLayoutMiddle = new FormLayout();
		formLayoutMiddle.setWidth(100, Unit.PERCENTAGE);
		formLayoutMiddle.addComponent(txtDesc);
		formLayoutMiddle.addComponent(cbxCommune);
		
		FormLayout formLayoutRight = new FormLayout();
        formLayoutRight.setWidth(100, Unit.PERCENTAGE);
        formLayoutRight.setSpacing(true);
        formLayoutRight.addComponent(cbxProvince);
        formLayoutRight.addComponent(cbStatuses);
        
		gridLayout.addComponent(formLayoutLeft);
		gridLayout.addComponent(formLayoutMiddle);
		gridLayout.addComponent(formLayoutRight);
		
		return gridLayout;	
	}
	
	@Override
	public VillageRestriction getRestrictions() {	
		VillageRestriction restriction = new VillageRestriction();
		restriction.setCode(txtCode.getValue());
		restriction.setDesc(txtDesc.getValue());
		restriction.setCommune(cbxCommune.getSelectedEntity());
		restriction.setDistrict(cbxDistrict.getSelectedEntity());
		restriction.setProvince(cbxProvince.getSelectedEntity());
		Collection<EStatusRecord> colSta = (Collection<EStatusRecord>) cbStatuses.getValue();
		restriction.setStatusRecordList(new ArrayList(colSta));
		return restriction;
	}

}

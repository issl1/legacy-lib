package com.nokor.efinance.ra.ui.panel.collections.parameter.status;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.collection.model.EColResult;
import com.nokor.efinance.core.collection.model.EColType;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * Status form panel in collection
 * @author uhout.cheng
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class StatusFormPanel extends AbstractFormPanel implements FMEntityField {

	/** */
	private static final long serialVersionUID = -9162707550224742667L;
	
	private EColResult colResult;
    private TextField txtCode;
    private TextField txtDescEn;
    private TextField txtDesc;
    private List<CheckBox> cbCollectionTypes;
    private Panel teamPanel;
	
    @PostConstruct
	public void PostConstruct() {
        super.init();
        NavigationPanel navigationPanel = addNavigationPanel();
		navigationPanel.addSaveClickListener(this);
	}
	
    /**
     * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#getEntity()
     */
	@Override
	protected Entity getEntity() {
		colResult.setCode(txtCode.getValue());
		colResult.setDescEn(txtDescEn.getValue());
		colResult.setDesc(txtDesc.getValue());
		List<EColType> lColTypes = new ArrayList<EColType>();
		if (cbCollectionTypes != null && !cbCollectionTypes.isEmpty()) {
			for (int i = 0; i < cbCollectionTypes.size(); i++) {
				CheckBox cbColType = cbCollectionTypes.get(i);
				if (cbColType.getValue()) {
					EColType colType = (EColType) cbColType.getData();
					lColTypes.add(colType);
				}
			}
		}
		// colResult.setColTypes(lColTypes);
		return colResult;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#createForm()
	 */
	@Override
	protected com.vaadin.ui.Component createForm() {
		teamPanel = ComponentFactory.getPanel();
		teamPanel.setWidth(200, Unit.PIXELS);
		txtCode = ComponentFactory.getTextField(60, 200);
		txtDescEn = ComponentFactory.getTextField(60, 200);
		txtDesc = ComponentFactory.getTextField(60, 200);
		VerticalLayout teamLayout = ComponentFactory.getVerticalLayout();
		teamLayout.setSpacing(true);
		
		cbCollectionTypes = new ArrayList<CheckBox>(); 
		List<EColType> colTypes = EColType.values();
		if (colTypes != null && !colTypes.isEmpty()) {
			for (EColType eColType : colTypes) {
				CheckBox cbColType = new CheckBox();
				if (eColType.equals(EColType.PHONE)) {
					cbColType.setCaption(I18N.message("phone"));
				} else if (eColType.equals(EColType.FIELD)) {
					cbColType.setCaption(I18N.message("field"));
				} else if (eColType.equals(EColType.INSIDE_REPO)) {
					cbColType.setCaption(I18N.message("repo"));
				} else if (eColType.equals(EColType.OA)) {
					cbColType.setCaption(I18N.message("oa"));
				}
				cbColType.setData(eColType);
        		cbCollectionTypes.add(cbColType);
        		teamLayout.addComponent(cbColType);
			}
			teamPanel.setContent(teamLayout);
		}
		Panel mainPanel = ComponentFactory.getPanel();
		mainPanel.setSizeFull();
		mainPanel.setContent(createLayout());
		return mainPanel;
	}
	
	/**
	 * 
	 * @return
	 */
	private CustomLayout createLayout() {
		String OPEN_TABLE = "<table border=\"0\" cellspacing=\"0\" cellpadding=\"5\" >";
		String CLOSE_TABLE = "</table>";
		String OPEN_TR = "<tr>";
		String CLOSE_TR = "</tr>";
		String OPEN_TD = "<td  align=\"left\">";
		String CLOSE_TD = "</td>";
		CustomLayout formCustomLayout = new CustomLayout("xxx");
		String formTemplate = OPEN_TABLE;
		formTemplate += OPEN_TR;
		formTemplate += OPEN_TD;
		formTemplate += "<div location=\"lblCode\" class=\"inline-block\"></div>";
		formTemplate += "<div class=\"inline-block requiredfield\">&nbsp;*</div>";
		formTemplate += CLOSE_TD;
		formTemplate += OPEN_TD;
		formTemplate += "<div location=\"txtCode\" />";
		formTemplate += CLOSE_TD;
		formTemplate += CLOSE_TR;
		formTemplate += OPEN_TR;
		formTemplate += OPEN_TD;
		formTemplate += "<div location=\"lblDescEn\" class=\"inline-block\"></div>";
		formTemplate += "<div class=\"inline-block requiredfield\">&nbsp;*</div>";
		formTemplate += CLOSE_TD;
		formTemplate += OPEN_TD;
		formTemplate += "<div location=\"txtDescEn\" />";
		formTemplate += CLOSE_TD;
		formTemplate += CLOSE_TR;
		formTemplate += OPEN_TR;
		formTemplate += OPEN_TD;
		formTemplate += "<div location=\"lblDesc\"></div>";
		formTemplate += CLOSE_TD;
		formTemplate += OPEN_TD;
		formTemplate += "<div location=\"txtDesc\" />";
		formTemplate += CLOSE_TD;
		formTemplate += CLOSE_TR;
		formTemplate += OPEN_TR;
		formTemplate += OPEN_TD;
		formTemplate += "<div location=\"lblTeam\"></div>";
		formTemplate += CLOSE_TD;
		formTemplate += OPEN_TD;
		formTemplate += "<div location=\"teamLayout\" />";
		formTemplate += CLOSE_TD;
		formTemplate += CLOSE_TR;
		formTemplate += CLOSE_TABLE;
		
		formCustomLayout.addComponent(new Label(I18N.message("code")), "lblCode");
		formCustomLayout.addComponent(txtCode, "txtCode");
		formCustomLayout.addComponent(new Label(I18N.message("desc.en")), "lblDescEn");
		formCustomLayout.addComponent(txtDescEn, "txtDescEn");
		formCustomLayout.addComponent(new Label(I18N.message("desc")), "lblDesc");
		formCustomLayout.addComponent(txtDesc, "txtDesc");
		formCustomLayout.addComponent(new Label(I18N.message("team")), "lblTeam");
		formCustomLayout.addComponent(teamPanel, "teamLayout");
		formCustomLayout.setTemplateContents(formTemplate);
		return formCustomLayout;
	}
	
	/**
	 * @param colResultID
	 */
	public void assignValues(Long colResultID) {
		super.reset();
		if (colResultID != null) {
			colResult = ENTITY_SRV.getById(EColResult.class, colResultID);
			txtCode.setValue(colResult.getCode());
			txtDescEn.setValue(colResult.getDescEn());
			txtDesc.setValue(colResult.getDesc());
			List<EColType> colTypes = null; // colResult.getColTypes();
			if (cbCollectionTypes != null && !cbCollectionTypes.isEmpty()) {
				for (int i = 0; i < cbCollectionTypes.size(); i++) {
					CheckBox cbCollection = cbCollectionTypes.get(i);
					EColType collectionType = (EColType) cbCollection.getData();
					boolean found = false;
					if (colTypes != null && !colTypes.isEmpty()) {
						for (EColType colType : colTypes) {
							if (colType != null) {
								if (collectionType.getId().equals(colType.getId())) {
									cbCollection.setValue(true);
									found = true;
									break;
								}
							}
						} 
					}
					if (!found) {
						cbCollection.setValue(false);	
					}
				}
			}
		}
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#reset()
	 */
	@Override
	public void reset() {
		super.reset();
		colResult = new EColResult();
		txtCode.setValue("");
		txtDescEn.setValue("");
		txtDesc.setValue("");
		if (cbCollectionTypes != null && !cbCollectionTypes.isEmpty()) {
			for (int i = 0; i < cbCollectionTypes.size(); i++) {
				cbCollectionTypes.get(i).setValue(false);
			}
		}
		markAsDirty();
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#validate()
	 */
	@Override
	protected boolean validate() {
		checkMandatoryField(txtCode, "code");
		checkMandatoryField(txtDescEn, "desc.en");
		boolean mandatory = true;
		for (int i = 0; i < cbCollectionTypes.size(); i++) {
			CheckBox cbColType = cbCollectionTypes.get(i);
			if (cbColType.getValue()) {
				mandatory = false;
				break;
			}
		}
		if (mandatory) {
			errors.add(I18N.message("please.check.at.least.one.team"));
		}
		return errors.isEmpty();
	}
}

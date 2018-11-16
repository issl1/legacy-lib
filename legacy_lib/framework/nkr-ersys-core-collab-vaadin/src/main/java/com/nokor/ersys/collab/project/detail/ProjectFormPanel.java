package com.nokor.ersys.collab.project.detail;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.ersys.collab.project.list.ProjectHolderPanel;
import com.nokor.ersys.collab.project.model.EProjectCategory;
import com.nokor.ersys.collab.project.model.EProjectType;
import com.nokor.ersys.collab.project.model.ETaskTemplateCategory;
import com.nokor.ersys.collab.project.model.Project;
import com.nokor.ersys.collab.tools.helper.ErsysCollabAppServicesHelper;
import com.nokor.ersys.core.hr.model.organization.OrgStructure;
import com.nokor.ersys.core.partner.model.Partner;
import com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * Assignment Form Panel
 * @author bunlong.taing
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ProjectFormPanel extends AbstractFormPanel implements ErsysCollabAppServicesHelper{

	/** */
	private static final long serialVersionUID = 8779886481417959471L;
	
	private TextField txtCode;
	private TextField txtDesc;
	private TextField txtDescEn;
	private TextField txtKeywords;
	private TextField txtKeywordsEn;
	private ERefDataComboBox<EProjectType> cbxType;
	private ERefDataComboBox<EProjectCategory> cbxCategory;
	private EntityComboBox<OrgStructure> cbxOwner;
	private ERefDataComboBox<ETaskTemplateCategory> cbxTaskTemplate;
	private EntityComboBox<Partner> cbxPartner;
	private AutoDateField dfStartDate;
	private AutoDateField dfEndDate;
	private TextArea txtComment;
	
	private Long entityId;
	private ProjectHolderPanel mainPanel;
	
	/**
	 * Assignment Form Panel post constructor
	 */
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
		Project project = null;
		boolean isUpdate = getEntityId() != null && getEntityId() > 0;
		if (isUpdate) {
			project = PROJECT_SRV.getById(Project.class, getEntityId());
		} else {
			project = Project.createInstance();
		}
		buildProjectDetailsFromControls(project);
		return project;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#saveEntity()
	 */
	@Override
	public void saveEntity() {
		Project project = (Project) getEntity();
		boolean isUpdate = project.getId() != null && project.getId() > 0;
		if (isUpdate) {
			PROJECT_SRV.updateProcess(project);
        } else {
        	PROJECT_SRV.createProcess(project);
        	setEntityId(project.getId());
        	mainPanel.addSubTab(project.getId());
        }
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#createForm()
	 */
	@Override
	protected com.vaadin.ui.Component createForm() {
		VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setSizeFull();
        verticalLayout.setSpacing(true);
        verticalLayout.addComponent(createProjectPanel());
        
        Panel mainPanel = ComponentFactory.getPanel();        
		mainPanel.setContent(verticalLayout);        
		return mainPanel;
	}
	
	/**
	 * Create Project Panel
	 * @return
	 */
	private com.vaadin.ui.Component createProjectPanel() {
		txtCode = ComponentFactory.getTextField("code", true, 50, 200);
		txtDescEn = ComponentFactory.getTextField("desc.en", true, 50, 200);
		txtDesc = ComponentFactory.getTextField35("desc", true, 50, 200);
		txtKeywordsEn = ComponentFactory.getTextField("keywords.en", false, 100, 200);
		txtKeywords = ComponentFactory.getTextField35("keywords", false, 100, 200);
		
		cbxType = new ERefDataComboBox<EProjectType>(I18N.message("project.type"), EProjectType.values());
		cbxType.setWidth(200, Unit.PIXELS);
		cbxType.setRequired(true);
		
		cbxCategory = new ERefDataComboBox<EProjectCategory>(I18N.message("category"), EProjectCategory.values());
		cbxCategory.setWidth(200, Unit.PIXELS);
		
		cbxOwner = new EntityComboBox<OrgStructure>(OrgStructure.class, OrgStructure.NAMEEN);
		cbxOwner.setCaption(I18N.message("owner"));
		cbxOwner.setWidth(200, Unit.PIXELS);
		cbxOwner.renderer();
		
		cbxTaskTemplate = new ERefDataComboBox<ETaskTemplateCategory>(I18N.message("task.template"), ETaskTemplateCategory.values());
		cbxTaskTemplate.setWidth(200, Unit.PIXELS);
		
		cbxPartner = new EntityComboBox<Partner>(Partner.class, Partner.NAMEEN);
		cbxPartner.setCaption(I18N.message("partner"));
		cbxPartner.setWidth(200, Unit.PIXELS);
		cbxPartner.renderer();

		dfStartDate = ComponentFactory.getAutoDateField("date.start", false);
		dfStartDate.setWidth(95, Unit.PIXELS);
		dfEndDate = ComponentFactory.getAutoDateField("date.end", false);
		dfEndDate.setWidth(95, Unit.PIXELS);
		
		txtComment = ComponentFactory.getTextArea(false, 400, 100);
		txtComment.setCaption(I18N.message("comment"));
		
		FormLayout formLayout = new FormLayout();
		formLayout.addComponent(txtCode);
		formLayout.addComponent(txtDescEn);
		formLayout.addComponent(txtDesc);
		formLayout.addComponent(txtKeywordsEn);
		formLayout.addComponent(txtKeywords);
		formLayout.addComponent(cbxType);
		formLayout.addComponent(cbxCategory);
		formLayout.addComponent(cbxOwner);
		formLayout.addComponent(cbxTaskTemplate);
		formLayout.addComponent(cbxPartner);
		formLayout.addComponent(dfStartDate);
		formLayout.addComponent(dfEndDate);
		formLayout.addComponent(txtComment);
		
		return formLayout;
	}
	
	/**
	 * Assign Values
	 * @param entityId
	 */
	public void assignValues(Long entityId) {
		if (entityId != null) {
			reset();
			setEntityId(entityId);
			Project project = PROJECT_SRV.getById(Project.class, entityId);
			txtCode.setValue(getDefaultString(project.getCode()));
			txtDesc.setValue(getDefaultString(project.getDesc()));
			txtDescEn.setValue(getDefaultString(project.getDescEn()));
			txtKeywords.setValue(getDefaultString(project.getKeywords()));
			txtKeywordsEn.setValue(getDefaultString(project.getKeywordsEn()));
			cbxType.setSelectedEntity(project.getType());
			cbxCategory.setSelectedEntity(project.getCategory());
			cbxOwner.setSelectedEntity(project.getOrgStructureOwner());
			cbxPartner.setSelectedEntity(project.getPartner());
			cbxTaskTemplate.setSelectedEntity(project.getTaskTemplateCategory());
			dfStartDate.setValue(project.getStartDate());
			dfEndDate.setValue(project.getEndDate());
			txtComment.setValue(project.getComment());
		}
	}
	
	/**
	 * Build Project Details From Controls
	 * @param project
	 */
	private void buildProjectDetailsFromControls(Project project) {
		project.setCode(txtCode.getValue());
		project.setDesc(txtDesc.getValue());
		project.setDescEn(txtDescEn.getValue());
		project.setKeywords(txtKeywords.getValue());
		project.setKeywordsEn(txtKeywordsEn.getValue());
		project.setType(cbxType.getSelectedEntity());
		project.setCategory(cbxCategory.getSelectedEntity());
		project.setOrgStructureOwner(cbxOwner.getSelectedEntity());
		project.setTaskTemplateCategory(cbxTaskTemplate.getSelectedEntity());
		project.setPartner(cbxPartner.getSelectedEntity());
		project.setStartDate(dfStartDate.getValue());
		project.setEndDate(dfEndDate.getValue());
		project.setComment(txtComment.getValue());
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#reset()
	 */
	@Override
	public void reset() {
		super.reset();
		setEntityId(null);
		txtCode.setValue("");
		txtDesc.setValue("");
		txtDesc.setValue("");
		txtDescEn.setValue("");
		txtKeywords.setValue("");
		txtKeywordsEn.setValue("");
		cbxType.setSelectedEntity(null);
		cbxCategory.setSelectedEntity(null);
		cbxOwner.setSelectedEntity(null);
		cbxTaskTemplate.setSelectedEntity(null);
		cbxPartner.setSelectedEntity(null);
		dfStartDate.setValue(null);
		dfEndDate.setValue(null);
		txtComment.setValue("");
	}
	
	/**
	 * Validate
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#validate()
	 */
	@Override
	protected boolean validate() {
		checkMandatoryField(txtCode, "code");
		checkMandatoryField(txtDescEn, "desc.en");
		checkMandatoryField(txtDesc, "desc");
		checkMandatorySelectField(cbxType, "project.type");
		checkRangeDateField(dfStartDate, dfEndDate);
		return errors.isEmpty();
	}
	
	/**
	 * Set Main Panel
	 * @param mainPanel
	 */
	public void setMainPanel(ProjectHolderPanel mainPanel) {
		this.mainPanel = mainPanel;
	}
	
	/**
	 * @return the entityId
	 */
	public Long getEntityId() {
		return entityId;
	}
	
	/**
	 * Set Entity Id
	 * @param entityId
	 */
	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}

}

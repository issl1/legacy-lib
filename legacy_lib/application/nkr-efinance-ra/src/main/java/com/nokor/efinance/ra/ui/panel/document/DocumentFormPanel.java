package com.nokor.efinance.ra.ui.panel.document;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.tools.exception.DaoException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.vaadin.dialogs.ConfirmDialog;

import com.nokor.efinance.core.applicant.model.EApplicantType;
import com.nokor.efinance.core.document.model.Document;
import com.nokor.efinance.core.document.model.DocumentGroup;
import com.nokor.efinance.core.document.model.DocumentScoring;
import com.nokor.efinance.core.document.model.DocumentUwGroup;
import com.nokor.efinance.core.document.model.EDocumentState;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityRefComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox.ButtonType;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimplePagedTable;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.nokor.frmk.vaadin.ui.widget.toolbar.event.AddClickListener;
import com.nokor.frmk.vaadin.ui.widget.toolbar.event.DeleteClickListener;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * Document form panel
 * @author youhort.ly
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class DocumentFormPanel extends AbstractFormPanel implements FMEntityField {
	
	private static final long serialVersionUID = 6440116626693434925L;

	private Document document;
	private List<ColumnDefinition> columnDefinitions;
	private TabSheet tabSheet;
	private FormLayout detailformPanel;
	private SimplePagedTable<DocumentScoring> pagedTable;
	private Item selectedItem = null;
	
	private CheckBox cbActive;
	private TextField txtCode;
	private TextField txtDesc;
    private TextField txtDescEn;
    private CheckBox cbMandatory;
    private TextField txtNumGroup;
    private CheckBox cbReferenceRequired;
    private CheckBox cbIssueDateRequired;
    private CheckBox cbExpireDateRequired;
    private CheckBox cbSubmitCreditBureau;
    private CheckBox cbFieldCheck;
    private CheckBox cbAllowChangeUpdateAsset;
    private TextField txtSortIndex;
    private ERefDataComboBox<EDocumentState> cbxDocumentState;
    private ERefDataComboBox<EApplicantType> cbxApplicantType;
    private EntityRefComboBox<DocumentGroup> cbxDocumentGroup;
    
    
    @PostConstruct
	public void PostConstruct() {
        super.init();
        NavigationPanel navigationPanel = addNavigationPanel();
		navigationPanel.addSaveClickListener(this);
	}
	
	@Override
	protected Entity getEntity() {
		document.setCode(txtCode.getValue());
		document.setDesc(txtDesc.getValue());
		document.setDescEn(txtDescEn.getValue());
		document.setMandatory(cbMandatory.getValue());
		if (StringUtils.isNotEmpty(txtNumGroup.getValue())) {
			document.setNumGroup(Integer.parseInt(txtNumGroup.getValue()));
		}
		document.setReferenceRequired(cbReferenceRequired.getValue());
		document.setIssueDateRequired(cbIssueDateRequired.getValue());
		document.setExpireDateRequired(cbExpireDateRequired.getValue());
		document.setSubmitCreditBureau(cbSubmitCreditBureau.getValue());
		document.setFieldCheck(cbFieldCheck.getValue());
		document.setAllowUpdateChangeAsset(cbAllowChangeUpdateAsset.getValue());
		document.setSortIndex(getInteger(txtSortIndex));
		document.setDocumentState(cbxDocumentState.getSelectedEntity());
		document.setApplicantType(cbxApplicantType.getSelectedEntity());
		document.setDocumentGroup(cbxDocumentGroup.getSelectedEntity());
		document.setStatusRecord(cbActive.getValue() ? EStatusRecord.ACTIV : EStatusRecord.INACT);
		return document;
	}

	@Override
	protected com.vaadin.ui.Component createForm() {
		detailformPanel = new FormLayout();		
		txtCode = ComponentFactory.getTextField("code", true, 60, 200);		
		txtDescEn = ComponentFactory.getTextField("desc.en", true, 60, 200);        
		txtDesc = ComponentFactory.getTextField35("desc", false, 60, 200);		
        cbActive = new CheckBox(I18N.message("active"));
        cbActive.setValue(true);
        cbMandatory = new CheckBox(I18N.message("mandatory"));
        cbMandatory.setValue(false);
        cbReferenceRequired = new CheckBox(I18N.message("reference.required"));
        cbReferenceRequired.setValue(false);
        cbIssueDateRequired = new CheckBox(I18N.message("issuedate.required"));
        cbIssueDateRequired.setValue(false);
        cbExpireDateRequired = new CheckBox(I18N.message("expiredate.required"));
        cbExpireDateRequired.setValue(false);
        cbSubmitCreditBureau = new CheckBox(I18N.message("submit.creditbureau"));
        cbSubmitCreditBureau.setValue(false);
        cbFieldCheck = new CheckBox(I18N.message("field.check"));
        cbFieldCheck.setValue(false);
        cbAllowChangeUpdateAsset = new CheckBox(I18N.message("allow.change.update.asset"));
        cbAllowChangeUpdateAsset.setValue(false);
        txtNumGroup = ComponentFactory.getTextField("num.group", false, 60, 100);
        txtSortIndex = ComponentFactory.getTextField("sort.index", false, 60, 100);
        cbxDocumentState = new ERefDataComboBox<EDocumentState>(I18N.message("document.state"), EDocumentState.class);
        cbxDocumentState.setRequired(true);
        cbxDocumentState.setSelectedEntity(EDocumentState.QUO);
        
        cbxApplicantType = new ERefDataComboBox<EApplicantType>(I18N.message("applicant.type"), EApplicantType.class);
        cbxApplicantType.setRequired(true);
                
        cbxDocumentGroup = new EntityRefComboBox<DocumentGroup>(I18N.message("document.group"));
        cbxDocumentGroup.setRestrictions(new BaseRestrictions<DocumentGroup>(DocumentGroup.class));
        cbxDocumentGroup.setRequired(true);
        cbxDocumentGroup.renderer();
        
        detailformPanel.addComponent(txtCode);
        detailformPanel.addComponent(txtDescEn);
        detailformPanel.addComponent(txtDesc);
        detailformPanel.addComponent(cbMandatory);
        detailformPanel.addComponent(txtNumGroup);
        detailformPanel.addComponent(cbReferenceRequired);
        detailformPanel.addComponent(cbIssueDateRequired);
        detailformPanel.addComponent(cbExpireDateRequired);
        detailformPanel.addComponent(cbSubmitCreditBureau);
        detailformPanel.addComponent(cbFieldCheck);
        detailformPanel.addComponent(cbAllowChangeUpdateAsset);
        detailformPanel.addComponent(txtSortIndex);
        detailformPanel.addComponent(cbxApplicantType);
        detailformPanel.addComponent(cbxDocumentState);
        detailformPanel.addComponent(cbxDocumentGroup);
        detailformPanel.addComponent(cbActive);
        
        this.columnDefinitions = createColumnDefinitions();
        pagedTable = new SimplePagedTable<DocumentScoring>(this.columnDefinitions);
        pagedTable.addItemClickListener(new ItemClickListener() {
			private static final long serialVersionUID = -6676228064499031341L;
			@Override
			public void itemClick(ItemClickEvent event) {
				selectedItem = event.getItem();
			}
		});
        
        VerticalLayout scoreLayout = new VerticalLayout();
        scoreLayout.setSpacing(true);
        scoreLayout.setMargin(true);
        NavigationPanel navigationPanel = new NavigationPanel();
        navigationPanel.addAddClickListener(new AddClickListener() {
			private static final long serialVersionUID = 6097382598671337038L;
			@Override
			public void addButtonClick(ClickEvent event) {
				final Window winAddScore = new Window(I18N.message("scoring"));
				winAddScore.setModal(true);
				
				VerticalLayout contentLayout = new VerticalLayout(); 
				contentLayout.setSpacing(true);
								
		        FormLayout formLayout = new FormLayout();
		        formLayout.setMargin(true);
		        formLayout.setSpacing(true);
		        
		        final EntityRefComboBox<DocumentUwGroup> cbxDocumentUwGroup = new EntityRefComboBox<DocumentUwGroup>(I18N.message("document.group")); 
		        cbxDocumentUwGroup.setRestrictions(new BaseRestrictions<DocumentUwGroup>(DocumentUwGroup.class));
		        cbxDocumentUwGroup.setRequired(true);
		        cbxDocumentUwGroup.renderer();
		        
		        final TextField txtScore = new TextField(I18N.message("score"));
		        txtScore.setRequired(true);
		        
		        formLayout.addComponent(cbxDocumentUwGroup);
		        formLayout.addComponent(txtScore);
		        		        
		        Button btnSave = new NativeButton(I18N.message("save"), new Button.ClickListener() {

					private static final long serialVersionUID = -4024064977917270885L;

					public void buttonClick(ClickEvent event) {
						DocumentScoring documentScoring = new DocumentScoring();
						documentScoring.setDocumentUwGroup(cbxDocumentUwGroup.getSelectedEntity());
						documentScoring.setDocument(document);
						documentScoring.setScore(getInteger(txtScore));
						ENTITY_SRV.saveOrUpdate(documentScoring);
						winAddScore.close();
						ENTITY_SRV.refresh(document);
						assignDocument(document);
		            }
		        });
				btnSave.setIcon(new ThemeResource("../nkr-default/icons/16/save.png"));
				
				Button btnCancel = new NativeButton(I18N.message("close"), new Button.ClickListener() {

					private static final long serialVersionUID = 3975121141565713259L;

					public void buttonClick(ClickEvent event) {
		            	winAddScore.close();
		            }
		        });
				btnCancel.setIcon(new ThemeResource("../nkr-default/icons/16/delete.png"));
				
				NavigationPanel navigationPanel = new NavigationPanel();
				navigationPanel.addButton(btnSave);
				navigationPanel.addButton(btnCancel);
				

				contentLayout.addComponent(navigationPanel);
		        contentLayout.addComponent(formLayout);
		        
		        winAddScore.setContent(contentLayout);
		        UI.getCurrent().addWindow(winAddScore);
			}
		});
        
        navigationPanel.addDeleteClickListener(new DeleteClickListener() {

			private static final long serialVersionUID = -564709310901875316L;

			@Override
			public void deleteButtonClick(ClickEvent event) {
				if (selectedItem == null) {
					MessageBox mb = new MessageBox(UI.getCurrent(), "300px", "160px", I18N.message("information"),
							MessageBox.Icon.INFO, I18N.message("delete.item.not.selected"), Alignment.MIDDLE_RIGHT, 
							new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
					mb.show();
				} else {
					final Long id = (Long) selectedItem.getItemProperty("id").getValue();
					ConfirmDialog.show(UI.getCurrent(), I18N.message("delete.mgs.single", String.valueOf(id)),
				        new ConfirmDialog.Listener() {
							private static final long serialVersionUID = -5719696946393183890L;
							
							public void onClose(ConfirmDialog dialog) {
				                if (dialog.isConfirmed()) {
				            		ENTITY_SRV.delete(DocumentScoring.class, id);
				            		assignValues(document.getId());
				                }
				            }
				        });
				}
			}
		});
        
        scoreLayout.addComponent(navigationPanel, 0);
        scoreLayout.addComponent(pagedTable);
        scoreLayout.addComponent(pagedTable.createControls());
        
        tabSheet = new TabSheet();
        
        tabSheet.addTab(detailformPanel, I18N.message("document"));
        tabSheet.addTab(scoreLayout, I18N.message("scoring"));
        
		return tabSheet;
	}
	
	/**
	 * @param asmakId
	 */
	public void assignValues(Long documId) {
		super.reset();
		if (documId != null) {
			document = ENTITY_SRV.getById(Document.class, documId);
			txtCode.setValue(document.getCode());
			txtDescEn.setValue(document.getDescEn());
			txtDesc.setValue(document.getDesc());
			cbMandatory.setValue(document.isMandatory());
			txtNumGroup.setValue(document.getNumGroup() == null ? "" : String.valueOf(document.getNumGroup()));
			cbReferenceRequired.setValue(document.isReferenceRequired());
			cbIssueDateRequired.setValue(document.isIssueDateRequired());
			cbExpireDateRequired.setValue(document.isExpireDateRequired());
			cbSubmitCreditBureau.setValue(document.isSubmitCreditBureau());
			cbFieldCheck.setValue(document.isFieldCheck());
			cbAllowChangeUpdateAsset.setValue(document.isAllowUpdateChangeAsset());
			txtSortIndex.setValue(getDefaultString(document.getSortIndex()));
			cbxApplicantType.setSelectedEntity(document.getApplicantType());
			cbxDocumentState.setSelectedEntity(document.getDocumentState());
			cbxDocumentGroup.setSelectedEntity(document.getDocumentGroup());
			cbActive.setValue(document.getStatusRecord().equals(EStatusRecord.ACTIV));		
			assignDocument(document);
		}
	}
	
	/**
	 * @param document
	 */
	private void assignDocument(Document document) {
		pagedTable.setContainerDataSource(getIndexedContainer(document));
	}
	
	/**
	 * Reset
	 */
	@Override
	public void reset() {
		super.reset();
		document = new Document();
		txtCode.setValue("");
		txtDescEn.setValue("");
		txtDesc.setValue("");
		cbActive.setValue(true);
		cbMandatory.setValue(false);
		txtNumGroup.setValue("");
		cbReferenceRequired.setValue(false);
		cbIssueDateRequired.setValue(false);
		cbExpireDateRequired.setValue(false);
		cbSubmitCreditBureau.setValue(false);
		cbFieldCheck.setValue(false);
		cbAllowChangeUpdateAsset.setValue(false);
		txtSortIndex.setValue("");
		cbxApplicantType.setSelectedEntity(null);
		cbxDocumentState.setSelectedEntity(EDocumentState.QUO);
		cbxDocumentGroup.setSelectedEntity(null);
		
		tabSheet.setSelectedTab(detailformPanel);
		pagedTable.removeAllItems();
		
		markAsDirty();
	}
	
	/**
	 * @return
	 */
	@Override
	protected boolean validate() {
		checkMandatoryField(txtCode, "code");		
		checkMandatoryField(txtDescEn, "desc.en");
		checkDoubleField(txtNumGroup, "num.group");
		checkMandatorySelectField(cbxApplicantType, "applicant.type");
		checkMandatorySelectField(cbxDocumentState, "document.state");
		checkMandatorySelectField(cbxDocumentGroup, "document.group");
		return errors.isEmpty();
	}
	
	/**
	 * Get indexed container
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private IndexedContainer getIndexedContainer(Document document) {
		IndexedContainer indexedContainer = new IndexedContainer();
		try {
					
			for (ColumnDefinition column : this.columnDefinitions) {
				indexedContainer.addContainerProperty(column.getPropertyId(), column.getPropertyType(), null);
			}			
			
			for (DocumentScoring documentScoring : document.getDocumentsScoring()) {
				Item item = indexedContainer.addItem(documentScoring.getId());
				item.getItemProperty(ID).setValue(documentScoring.getId());
				item.getItemProperty(DESC_EN).setValue(documentScoring.getDocumentUwGroup().getDescEn());
				item.getItemProperty("score").setValue(documentScoring.getScore());
			}
						
		} catch (DaoException e) {
			logger.error("DaoException", e);
		}
		return indexedContainer;
	}
	
	/**
	 * Get Paged definition
	 * @return
	 */
	protected List<ColumnDefinition> createColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(DESC_EN, I18N.message("desc"), String.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition("score", I18N.message("score"), Integer.class, Align.LEFT, 100));
		return columnDefinitions;
	}
}

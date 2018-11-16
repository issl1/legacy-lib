package com.nokor.efinance.ra.ui.panel.document;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.tools.exception.DaoException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.vaadin.dialogs.ConfirmDialog;

import com.nokor.efinance.core.applicant.model.EApplicantType;
import com.nokor.efinance.core.document.model.DocumentConfirmEvidence;
import com.nokor.efinance.core.document.model.DocumentScoring;
import com.nokor.efinance.core.document.model.DocumentUwGroup;
import com.nokor.efinance.core.document.model.EConfirmEvidence;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
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
 * Document Group form panel
 * @author youhort.ly
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class DocumentUwGroupFormPanel extends AbstractFormPanel implements FMEntityField {
	
	private static final long serialVersionUID = -9153896352540789513L;

	private DocumentUwGroup documentUwGroup;
	
	private List<ColumnDefinition> columnDefinitions;
	private TabSheet tabSheet;
	private SimplePagedTable<DocumentScoring> pagedTable;
	private Item selectedItem = null;
	private FormLayout detailformPanel;
	
	private CheckBox cbActive;
	private TextField txtCode;
	private TextField txtDesc;
    private TextField txtDescEn;
    private TextField txtSortIndex;
    private ERefDataComboBox<EApplicantType> cbxApplicantType;
    
    @PostConstruct
	public void PostConstruct() {
        super.init();
        NavigationPanel navigationPanel = addNavigationPanel();
		navigationPanel.addSaveClickListener(this);
	}
	
	@Override
	protected Entity getEntity() {
		documentUwGroup.setCode(txtCode.getValue());
		documentUwGroup.setDesc(txtDesc.getValue());
		documentUwGroup.setDescEn(txtDescEn.getValue());
		documentUwGroup.setSortIndex(getInteger(txtSortIndex));
		documentUwGroup.setStatusRecord(cbActive.getValue() ? EStatusRecord.ACTIV : EStatusRecord.INACT);
		documentUwGroup.setApplicantType(cbxApplicantType.getSelectedEntity());
		return documentUwGroup;
	}

	@Override
	protected com.vaadin.ui.Component createForm() {
		detailformPanel = new FormLayout();		
		txtCode = ComponentFactory.getTextField("code", true, 60, 200);		
		txtDescEn = ComponentFactory.getTextField("desc.en", true, 60, 200);        
		txtDesc = ComponentFactory.getTextField35("desc", false, 60, 200);	
		txtSortIndex = ComponentFactory.getTextField("sort.index", false, 60, 100);
        cbActive = new CheckBox(I18N.message("active"));
        cbActive.setValue(true);
        
        cbxApplicantType = new ERefDataComboBox<EApplicantType>(I18N.message("applicant.type"), EApplicantType.class);
        cbxApplicantType.setRequired(true);
        
        detailformPanel.addComponent(txtCode);
        detailformPanel.addComponent(txtDescEn);
        detailformPanel.addComponent(txtDesc);
        detailformPanel.addComponent(txtSortIndex);
        detailformPanel.addComponent(cbxApplicantType);
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
        
        VerticalLayout confirmEvidenceLayout = new VerticalLayout();
        confirmEvidenceLayout.setSpacing(true);
        confirmEvidenceLayout.setMargin(true);
        NavigationPanel navigationPanel = new NavigationPanel();
        navigationPanel.addAddClickListener(new AddClickListener() {

			private static final long serialVersionUID = -7211608726843257915L;

			@Override
			public void addButtonClick(ClickEvent event) {
				final Window winAddConfirmEvidence = new Window(I18N.message("confirm.evidence"));
				winAddConfirmEvidence.setModal(true);
				
				VerticalLayout contentLayout = new VerticalLayout(); 
				contentLayout.setSpacing(true);
								
		        FormLayout formLayout = new FormLayout();
		        formLayout.setMargin(true);
		        formLayout.setSpacing(true);
		        
		        final ERefDataComboBox<EConfirmEvidence> cbxConfirmEvidence = new ERefDataComboBox<EConfirmEvidence>(I18N.message("confirm.evidence"), EConfirmEvidence.class); 
		        cbxConfirmEvidence.setRequired(true);
		        
		        formLayout.addComponent(cbxConfirmEvidence);
		        		        
		        Button btnSave = new NativeButton(I18N.message("save"), new Button.ClickListener() {
					private static final long serialVersionUID = -5743551402153217555L;

					public void buttonClick(ClickEvent event) {
						DocumentConfirmEvidence documentConfirmEvidence = new DocumentConfirmEvidence();
						documentConfirmEvidence.setConfirmEvidence(cbxConfirmEvidence.getSelectedEntity());
						documentConfirmEvidence.setDocumentUwGroup(documentUwGroup);
						ENTITY_SRV.saveOrUpdate(documentConfirmEvidence);
						winAddConfirmEvidence.close();
						ENTITY_SRV.refresh(documentUwGroup);
						assignConfirmEvidence(documentUwGroup);
		            }
		        });
				btnSave.setIcon(new ThemeResource("../nkr-default/icons/16/save.png"));
				
				Button btnCancel = new NativeButton(I18N.message("close"), new Button.ClickListener() {
					private static final long serialVersionUID = 1429484682148403361L;

					public void buttonClick(ClickEvent event) {
		            	winAddConfirmEvidence.close();
		            }
		        });
				btnCancel.setIcon(new ThemeResource("../nkr-default/icons/16/delete.png"));
				
				NavigationPanel navigationPanel = new NavigationPanel();
				navigationPanel.addButton(btnSave);
				navigationPanel.addButton(btnCancel);
				

				contentLayout.addComponent(navigationPanel);
		        contentLayout.addComponent(formLayout);
		        
		        winAddConfirmEvidence.setContent(contentLayout);
		        UI.getCurrent().addWindow(winAddConfirmEvidence);
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
							private static final long serialVersionUID = -4631449106989961635L;

							public void onClose(ConfirmDialog dialog) {
				                if (dialog.isConfirmed()) {
				            		ENTITY_SRV.delete(DocumentConfirmEvidence.class, id);
				            		assignValues(documentUwGroup.getId());
				                }
				            }
				        });
				}
			}
		});
        
        confirmEvidenceLayout.addComponent(navigationPanel, 0);
        confirmEvidenceLayout.addComponent(pagedTable);
        confirmEvidenceLayout.addComponent(pagedTable.createControls());
        
        tabSheet = new TabSheet();
        
        tabSheet.addTab(detailformPanel, I18N.message("document.uw.group"));
        tabSheet.addTab(confirmEvidenceLayout, I18N.message("confirm.evidences"));
        
		return tabSheet;
	}
	
	/**
	 * @param asmakId
	 */
	public void assignValues(Long id) {
		super.reset();
		if (id != null) {
			documentUwGroup = ENTITY_SRV.getById(DocumentUwGroup.class, id);
			txtCode.setValue(documentUwGroup.getCode());
			txtDescEn.setValue(documentUwGroup.getDescEn());
			txtDesc.setValue(documentUwGroup.getDesc());
			txtSortIndex.setValue(getDefaultString(documentUwGroup.getSortIndex()));
			cbActive.setValue(documentUwGroup.getStatusRecord().equals(EStatusRecord.ACTIV));
			cbxApplicantType.setSelectedEntity(documentUwGroup.getApplicantType());
			assignConfirmEvidence(documentUwGroup);
		}
	}
	
	/**
	 * @param document
	 */
	private void assignConfirmEvidence(DocumentUwGroup documentUwGroup) {
		pagedTable.setContainerDataSource(getIndexedContainer(documentUwGroup));
	}
	
	/**
	 * Get indexed container
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private IndexedContainer getIndexedContainer(DocumentUwGroup documentUwGroup) {
		IndexedContainer indexedContainer = new IndexedContainer();
		try {
			for (ColumnDefinition column : this.columnDefinitions) {
				indexedContainer.addContainerProperty(column.getPropertyId(), column.getPropertyType(), null);
			}			
			for (DocumentConfirmEvidence documentConfirmEvidence : documentUwGroup.getDocumentsConfirmEvidence()) {
				Item item = indexedContainer.addItem(documentConfirmEvidence.getId());
				item.getItemProperty(ID).setValue(documentConfirmEvidence.getId());
				item.getItemProperty(DESC_EN).setValue(documentConfirmEvidence.getConfirmEvidence().getDescEn());
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
		return columnDefinitions;
	}
	
	/**
	 * Reset
	 */
	@Override
	public void reset() {
		super.reset();
		documentUwGroup = new DocumentUwGroup();
		txtCode.setValue("");
		txtDescEn.setValue("");
		txtDesc.setValue("");
		txtSortIndex.setValue("");
		cbActive.setValue(true);
		cbxApplicantType.setSelectedEntity(null);
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
		checkMandatorySelectField(cbxApplicantType, "applicant.type");
		return errors.isEmpty();
	}
}

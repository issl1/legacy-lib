package com.nokor.efinance.core.document.template;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.seuksa.frmk.model.entity.Entity;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.document.model.DocumentTemplate;
import com.nokor.efinance.core.document.panel.DocumentFileUploader;
import com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Upload;

/**
 * Document Template Form panel
 * @author bunlong.taing
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class DocumentTemplateFormPanel extends AbstractFormPanel {

	/** */
	private static final long serialVersionUID = 1383899324087835446L;
	
	private TextField txtCode;
	private TextField txtDesc;
	private TextField txtDescEn;
	private Upload upload;
	private DocumentFileUploader uploader;
	private DocumentTemplate document;
	
	/** */
    @PostConstruct
	public void PostConstruct() {
        super.init();
        NavigationPanel navigationPanel = addNavigationPanel();
		navigationPanel.addSaveClickListener(this);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#createForm()
	 */
	@Override
	protected com.vaadin.ui.Component createForm() {
		txtCode = ComponentFactory.getTextField("code", true, 10, 200);
		txtDescEn = ComponentFactory.getTextField("desc.en", true, 60, 200);
		txtDesc = ComponentFactory.getTextField("desc", false, 60, 200);
		
		DocumentFileUploader documentFileUploader = new DocumentFileUploader("DocumentTemplates");
		uploader = documentFileUploader;
		upload = new Upload();
		upload.setReceiver(uploader);
		upload.setCaption(I18N.message("document.template"));
		upload.addSucceededListener(uploader);
		
		FormLayout formLayout = new FormLayout();
		
		formLayout.addComponent(txtCode);
		formLayout.addComponent(txtDescEn);
		formLayout.addComponent(txtDesc);
		formLayout.addComponent(upload);
		
		return formLayout;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#getEntity()
	 */
	@Override
	protected Entity getEntity() {
		document.setCode(txtCode.getValue());
		document.setDesc(txtDesc.getValue());
		document.setDescEn(txtDescEn.getValue());
		document.setDocument(uploader.getFileNameDoc());
		return document;
	}
	
	/**
	 * 
	 * @param id
	 */
	public void assignValues(Long id) {
		reset();
		if (id != null) {
			document = ENTITY_SRV.getById(DocumentTemplate.class, id);
			txtCode.setValue(document.getCode());
			txtDescEn.setValue(document.getDescEn());
			txtDesc.setValue(document.getDesc());
			uploader.setFileNameDoc(document.getDocument());
		}
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#reset()
	 */
	@Override
	public void reset() {
		super.reset();
		document = new DocumentTemplate();
		txtCode.setValue("");
		txtDesc.setValue("");
		txtDescEn.setValue("");
		uploader.setFileNameDoc("");
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#validate()
	 */
	@Override
	protected boolean validate() {
		checkMandatoryField(txtCode, "code");		
		checkMandatoryField(txtDescEn, "desc.en");
		checkMandatoryUploadField(uploader, "document.template");
		return errors.isEmpty();
	}
	
	/**
	 * Validate whether file is uploaded
	 * @param uploader
	 * @param messageKey
	 * @return
	 */
	protected boolean checkMandatoryUploadField(DocumentFileUploader uploader, String messageKey) {
		boolean isValid = true;
		if (StringUtils.isEmpty(uploader.getFileNameDoc())) {
			this.errors.add(I18N.message("field.required.1",
					new String[] { I18N.message(messageKey) }));
			isValid = false;
		}
		return isValid;
	}

}

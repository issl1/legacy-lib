package com.nokor.efinance.core.quotation.panel.include;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.springframework.security.core.context.SecurityContextHolder;

import com.nokor.efinance.core.document.model.Document;
import com.nokor.efinance.core.document.model.DocumentScoring;
import com.nokor.efinance.core.document.model.DocumentUwGroup;
import com.nokor.efinance.core.quotation.model.Comment;
import com.nokor.efinance.core.quotation.model.Quotation;
import com.nokor.efinance.core.quotation.model.QuotationDocument;
import com.nokor.frmk.security.model.SecUser;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;

/**
 * @author ly.youhort
 *
 */
public class DocumentUwGroupPanel extends VerticalLayout {

	private static final long serialVersionUID = 1139246858105549959L;
		
	private DocumentUwGroup documentUwGroup;
	private int totalScore = 0;
	private List<CheckBox> cbDocuments;
	
	private Comment comment;
	private Quotation quotation;
	private TextArea txtComment;
	
	public DocumentUwGroupPanel(DocumentUwGroup documentUwGroup, Quotation quotation, boolean isPOS) {
		setMargin(true);
		setSpacing(true);
		this.documentUwGroup = documentUwGroup;
		this.quotation = quotation;
	
		if (!isPOS) {
			cbDocuments = new ArrayList<CheckBox>();
			List<DocumentScoring> documentsScoring = getSelectedDocumentsScoring(documentUwGroup.getDocumentsScoring(), quotation);
			if (documentsScoring != null && !documentsScoring.isEmpty()) {
				
				GridLayout gridLayout = new GridLayout(1, documentsScoring.size() + 1);
	        	gridLayout.setSpacing(true);
	        	gridLayout.addComponent(new Label(I18N.message("document")), 0, 0);
	        
	        	int i = 1;
	        	for (DocumentScoring documentScoring : documentsScoring) {
	        		Document document = documentScoring.getDocument();
	        		totalScore += documentScoring.getScore();        			
	        		CheckBox cbDocument = new CheckBox("(" + documentScoring.getScore() + ") " + document.getApplicantType() + " - " + document.getDescEn());
	        		cbDocument.setStyleName("checkbox_unchange_disabled_color");
	        		cbDocument.setData(document);
	        		cbDocument.setValue(true);
	        		//cbDocument.setEnabled(false);
	        		cbDocument.setWidth(300, Unit.PIXELS);
	        		cbDocuments.add(cbDocument);
	        		gridLayout.addComponent(cbDocument, 0, i);
	        		i++;
	        	}
	        	addComponent(gridLayout);
			}
		
			txtComment = new TextArea();
			txtComment.setWidth(900, Unit.PIXELS);
			txtComment.setHeight(120, Unit.PIXELS);
			txtComment.setMaxLength(1000);
			List<Comment> comments = quotation.getComments();
			if (comments != null && !comments.isEmpty()) {
				for (Comment comment : comments) {
					if (comment.getDocumentUwGroup() != null && comment.getDocumentUwGroup().getId().equals(documentUwGroup.getId())) {
						this.comment = comment;
						txtComment.setValue(comment.getDesc());
						break;
					}
				}
			}
			
			addComponent(txtComment);
		}
		
	}
	
	
	/**
	 * @return the comment
	 */
	public Comment getComment() {
		SecUser changeUser = (SecUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (StringUtils.isNotEmpty(txtComment.getValue())) {
			if (comment == null) {
				comment = new Comment();
				comment.setDocumentUwGroup(documentUwGroup);
				comment.setQuotation(quotation);
				comment.setOnlyForUW(true);
				List<Comment> comments = quotation.getComments();
				if (comments == null) {
					comments = new ArrayList<Comment>();
					quotation.setComments(comments);
				}
				comments.add(0, comment);
			} else if (txtComment.getValue().equals(comment.getDesc())) {
				return null;
			}
			comment.setDesc(txtComment.getValue());
			comment.setUser(changeUser);
		} else if (comment != null) {
			comment.setDesc(txtComment.getValue());
			comment.setUser(changeUser);
		} else {
			return null;
		}
		return comment;
	}



	/**
	 * @return the documentUwGroup
	 */
	public DocumentUwGroup getDocumentUwGroup() {
		return documentUwGroup;
	}

	/**
	 * @return the cbDocuments
	 */
	public List<CheckBox> getCbDocuments() {
		return cbDocuments;
	}

	/**
	 * @return the totalScore
	 */
	public int getTotalScore() {
		return totalScore;
	}

	/**
	 * @return the txtComment
	 */
	public TextArea getTxtComment() {
		return txtComment;
	}

	/**
	 * @param documentsScoring
	 * @param quotation
	 * @return
	 */
	private List<DocumentScoring> getSelectedDocumentsScoring(List<DocumentScoring> documentsScoring, Quotation quotation) {
		List<DocumentScoring> selectedDocumentsScoring = new ArrayList<DocumentScoring>();
		if (documentsScoring != null && !documentsScoring.isEmpty()) {			
			for (DocumentScoring documentScoring : documentsScoring) {
				Document document = documentScoring.getDocument();
				if (getQuotationDocumentSelected(document, quotation.getQuotationDocuments()) != null) {
					selectedDocumentsScoring.add(documentScoring);
				}
			}
		}
		return selectedDocumentsScoring;
	}
	
	/**
	 * @param document
	 * @param quotationDocuments
	 * @return
	 */
	public QuotationDocument getQuotationDocumentSelected(Document document, List<QuotationDocument> quotationDocuments) {
		if (quotationDocuments != null && !quotationDocuments.isEmpty()) {
			for (QuotationDocument quotationDocument : quotationDocuments) {
				if (document.getId().equals(quotationDocument.getDocument().getId())) {
					return quotationDocument;
				}
			}
		}
		return null;
	}
}

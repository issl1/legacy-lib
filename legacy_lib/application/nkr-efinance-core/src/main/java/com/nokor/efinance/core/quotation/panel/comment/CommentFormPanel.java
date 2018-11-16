package com.nokor.efinance.core.quotation.panel.comment;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.service.EntityService;
import org.springframework.security.core.context.SecurityContextHolder;

import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.efinance.core.quotation.QuotationService;
import com.nokor.efinance.core.quotation.model.Comment;
import com.nokor.efinance.core.quotation.model.Quotation;
import com.nokor.efinance.core.quotation.model.QuotationSupportDecision;
import com.nokor.efinance.core.quotation.model.SupportDecision;
import com.nokor.efinance.core.shared.referencial.DataReference;
import com.nokor.efinance.core.shared.util.ProfileUtil;
import com.nokor.efinance.core.workflow.QuotationWkfStatus;
import com.nokor.frmk.security.context.SecApplicationContextHolder;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox.ButtonType;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * Comment form panel
 * @author ly.youhort
 */
public class CommentFormPanel extends Window {
	
    /** */
	private static final long serialVersionUID = 7837382885463743029L;
	
	private EntityService entityService = (EntityService) SecApplicationContextHolder.getContext().getBean("entityService");
	protected QuotationService quotationService = (QuotationService) SecApplicationContextHolder.getContext().getBean("quotationService");
	
	private NavigationPanel navigationPanel;
	private Label lblDescreption;
	private TextArea txtComment;
	private CheckBox cbOnlyForUw;
	
	private VerticalLayout contentLayout;
	private VerticalLayout commentLayoutCommentDetail;
	private VerticalLayout commentLayout;
	private boolean isMandatory;
	
	private List<CheckBox> cbSupportDecisions;
	private List<QuotationSupportDecision> quotationSupportDecisionCheckeds;
	
	/**
	 * 
	 * @param quotation
	 * @param quotationStatus
	 * @param forManager
	 * @param onSaveListener
	 */
	public CommentFormPanel(final Quotation quotation, final EWkfStatus quotationStatus, final boolean forManager, 
							final ClickListener onSaveListener) {
		
		setModal(true);		
		cbSupportDecisions = new ArrayList<>();
		
		contentLayout = new VerticalLayout();
		contentLayout.setSpacing(true);

        Button btnSave = new NativeButton(I18N.message("save"), new Button.ClickListener() {
			private static final long serialVersionUID = -4024064977917270885L;
			public void buttonClick(ClickEvent event) {
				List<QuotationSupportDecision> quotationSupportDecisions = new ArrayList<>();
				boolean commentMandatory = false;
				
				if (quotationStatus != QuotationWkfStatus.AWT){
					if (cbSupportDecisions != null && !cbSupportDecisions.isEmpty()) {
						for (CheckBox cbSupportDecision : cbSupportDecisions) {
							if (cbSupportDecision.getValue()) {
								QuotationSupportDecision quotationSupportDecision = new QuotationSupportDecision();
								quotationSupportDecision.setSupportDecision((SupportDecision) cbSupportDecision.getData());
								quotationSupportDecision.setWkfStatus(quotationStatus);
								quotationSupportDecision.setQuotation(quotation);
								quotationSupportDecisions.add(quotationSupportDecision);
							}
						}
					}
				}
				
				int nbMaxSelectReason = 1000;
				boolean describeRequirementCannotMeet = false;
				if (quotationStatus == QuotationWkfStatus.DEC || 
					quotationStatus == QuotationWkfStatus.REJ || 
					quotationStatus == QuotationWkfStatus.REU ||
					quotationStatus == QuotationWkfStatus.RFC ||
					quotationStatus == QuotationWkfStatus.AWU ||
					quotationStatus == QuotationWkfStatus.AWS) {
					nbMaxSelectReason = 1;
					if (quotationStatus == QuotationWkfStatus.REJ || quotationStatus == QuotationWkfStatus.REU
							|| quotationStatus == QuotationWkfStatus.AWS || quotationStatus == QuotationWkfStatus.AWU) {
						commentMandatory = true;
					}
					
					if (!commentMandatory && quotationSupportDecisions != null) {
						for (QuotationSupportDecision quotationSupportDecision : quotationSupportDecisions) {
							if (quotationSupportDecision.getSupportDecision().isCommentRequired()) {
								commentMandatory = true;
								if ("06DR".equals(quotationSupportDecision.getSupportDecision().getCode())) {
									describeRequirementCannotMeet = true;
								}
							}
						}
					}
				}
				if (isMandatory && quotationSupportDecisions.isEmpty()) {
					MessageBox mb = new MessageBox(UI.getCurrent(), "320px", "160px", I18N.message("information"),
							MessageBox.Icon.ERROR, I18N.message("please.check.reason.in.checkbox.field"), Alignment.MIDDLE_RIGHT,
							new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
					mb.show();				
				} else if (nbMaxSelectReason < quotationSupportDecisions.size() && quotationStatus != QuotationWkfStatus.RFC && 
						   quotationStatus != QuotationWkfStatus.AWU && quotationStatus != QuotationWkfStatus.AWS) {
					MessageBox mb = new MessageBox(UI.getCurrent(), "320px", "160px", I18N.message("information"),
							MessageBox.Icon.ERROR, I18N.message("please.check.only.one.reason"), Alignment.MIDDLE_RIGHT,
							new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
					mb.show();
				} else if (nbMaxSelectReason > quotationSupportDecisions.size() && quotationStatus == QuotationWkfStatus.RFC) {
					MessageBox mb = new MessageBox(UI.getCurrent(), "320px", "160px", I18N.message("information"),
							MessageBox.Icon.ERROR, I18N.message("please.check.at.least.one.reason"), Alignment.MIDDLE_RIGHT,
							new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
					mb.show();
				}else if (nbMaxSelectReason > quotationSupportDecisions.size() && quotationStatus == QuotationWkfStatus.AWU) {
					if(StringUtils.isEmpty(txtComment.getValue())){
						MessageBox mb = new MessageBox(UI.getCurrent(), "320px", "160px", I18N.message("information"),
								MessageBox.Icon.ERROR, I18N.message("please.select.a.condition.or.write.down.condition.in.free.field "), Alignment.MIDDLE_RIGHT,
								new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
						mb.show();
					}else{
						MessageBox mb = new MessageBox(UI.getCurrent(), "320px", "160px", I18N.message("information"),
								MessageBox.Icon.ERROR, I18N.message("please.check.at.least.one.reason"), Alignment.MIDDLE_RIGHT,
								new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
						mb.show();
					}	
			    }else if (nbMaxSelectReason > quotationSupportDecisions.size() && quotationStatus == QuotationWkfStatus.AWS) {
			    	if(StringUtils.isEmpty(txtComment.getValue())){
						MessageBox mb = new MessageBox(UI.getCurrent(), "320px", "160px", I18N.message("information"),
								MessageBox.Icon.ERROR, I18N.message("please.select.a.condition.or.write.down.condition.in.free.field"), Alignment.MIDDLE_RIGHT,
								new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
						mb.show();
					}else{
						MessageBox mb = new MessageBox(UI.getCurrent(), "320px", "160px", I18N.message("information"),
								MessageBox.Icon.ERROR, I18N.message("please.check.at.least.one.reason"), Alignment.MIDDLE_RIGHT,
								new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
						mb.show();
					}	
		       }else if (commentMandatory && StringUtils.isEmpty(txtComment.getValue())) {
					MessageBox mb;
					if (describeRequirementCannotMeet) {
						mb = new MessageBox(UI.getCurrent(), "320px", "160px", I18N.message("information"),
								MessageBox.Icon.ERROR, I18N.message("please.describe.requirement.cannot.meet"), Alignment.MIDDLE_RIGHT,
								new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
					} else {
							mb = new MessageBox(UI.getCurrent(), "320px", "160px", I18N.message("information"),
									MessageBox.Icon.ERROR, I18N.message("please.enter.comment"), Alignment.MIDDLE_RIGHT,
									new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
							mb.show();
						}
				} else {
						
					if (!quotationSupportDecisions.isEmpty()) {
						quotation.setQuotationSupportDecisions(quotationSupportDecisions);
						quotationService.saveOrUpdateQuotationSupportDecisions(quotation);
					}
				    	
					if (StringUtils.isNotEmpty(txtComment.getValue())) {
						SecUser secUser = (SecUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
						Comment comment = new Comment();
						comment.setDesc(txtComment.getValue());
						comment.setOnlyForUW(cbOnlyForUw.getValue());
						comment.setForManager(forManager);
						comment.setQuotation(quotation);
						comment.setUser(secUser);
						quotationService.saveOrUpdate(comment);
					}
					if (onSaveListener != null) {
						onSaveListener.buttonClick(event);
					}
					close();
				}
            }
        });
		btnSave.setIcon(new ThemeResource("../nkr-default/icons/16/save.png"));
		
		Button btnCancel = new NativeButton(I18N.message("close"), new Button.ClickListener() {
			private static final long serialVersionUID = 3975121141565713259L;
			public void buttonClick(ClickEvent event) {
            	close();
            }
        });
		btnCancel.setIcon(new ThemeResource("../nkr-default/icons/16/delete.png"));
		
		navigationPanel = new NavigationPanel();
		navigationPanel.addButton(btnSave);
		navigationPanel.addButton(btnCancel);		
		
		txtComment = new TextArea(I18N.message("desc.en"));
		txtComment.setSizeFull();
		txtComment.setWidth("100%");
		txtComment.setRows(8);
		txtComment.setMaxLength(1000);
		cbOnlyForUw = new CheckBox(I18N.message("only.for.underwriter"));
		lblDescreption = new Label();
		
		commentLayout = new VerticalLayout();
		commentLayout.setMargin(true);
		commentLayout.setSpacing(true);
		commentLayout.setWidth("100%");
		
		commentLayoutCommentDetail = new VerticalLayout();
		commentLayoutCommentDetail.setMargin(true);
		commentLayoutCommentDetail.setSpacing(true);
		commentLayoutCommentDetail.setWidth("100%");
		
		EWkfStatus quoStatus = quotationStatus;
		if (quotationStatus.equals(QuotationWkfStatus.AWS) || 
		   (quotationStatus.equals(QuotationWkfStatus.AWT) && quotation.getWkfStatus().equals(QuotationWkfStatus.AWS))) {
			quoStatus = QuotationWkfStatus.AWU;
		}
		
		List<SupportDecision> supportDecisions = DataReference.getInstance().getSupportDecisions(quoStatus);
		if (supportDecisions != null && !supportDecisions.isEmpty()) {

			final GridLayout gridLayout = new GridLayout(2, supportDecisions.size() / 2 + 1);	
			gridLayout.setSpacing(true);
			int col = 0;
			int row = 0;
			for (SupportDecision supportDecision : supportDecisions) {
				if (supportDecision.isMandatory()) {
					isMandatory = true;
				}
				CheckBox cbSupportDecision = new CheckBox(supportDecision.getDescEn());
				
				quotationSupportDecisionCheckeds = getListQuotationSupportDecisions(quotation);
				if (quoStatus == QuotationWkfStatus.RFC && quotationSupportDecisionCheckeds != null && 
					!quotationSupportDecisionCheckeds.isEmpty()) {
						assignValueToCheckboxs(supportDecision, cbSupportDecision);
				} else if ((quoStatus == QuotationWkfStatus.AWU && ProfileUtil.isManager())) {
					if (quotationSupportDecisionCheckeds != null) {
						assignValueToCheckboxs(supportDecision, cbSupportDecision);
						cbSupportDecision.setEnabled(false);
					}	
				}
				
				cbSupportDecision.setData(supportDecision);
				cbSupportDecisions.add(cbSupportDecision);
				
				gridLayout.addComponent(cbSupportDecision, col, row);
				col++;
				if (col != 0 && col % 2 == 0) {
					col = 0;
					row++;
				}
			}
			commentLayout.addComponent(new Panel(gridLayout));
		}
		
		commentLayout.addComponent(txtComment);
		commentLayout.addComponent(cbOnlyForUw);
		
		commentLayoutCommentDetail.addComponent(lblDescreption);
		
		contentLayout.addComponent(navigationPanel);
		contentLayout.addComponent(commentLayout);
		contentLayout.addComponent(commentLayoutCommentDetail);
        setContent(contentLayout);
        contentLayout.removeComponent(commentLayoutCommentDetail);
	}
	
	/**
	 * 
	 * @param comment
	 */
	public void assignValueToDescription( Comment comment){
		contentLayout.removeComponent(navigationPanel);
		contentLayout.removeComponent(commentLayout);
		contentLayout.addComponent(commentLayoutCommentDetail);
		lblDescreption.setValue(comment.getDesc());
	}
	
	/**
	 * 
	 * @param supportDecision
	 * @param cbSupportDecision
	 */
	private void assignValueToCheckboxs(final SupportDecision supportDecision, final CheckBox cbSupportDecision) {
		for (QuotationSupportDecision quotationSupportDecision : quotationSupportDecisionCheckeds) {
			if (supportDecision.getId() == quotationSupportDecision.getSupportDecision().getId()) {
				cbSupportDecision.setValue(true);
				break;
			}
		}
	}
	
	/**
	 * 
	 * @param quotation
	 * @return
	 */
	private List<QuotationSupportDecision> getListQuotationSupportDecisions(Quotation quotation) {
		BaseRestrictions<QuotationSupportDecision> restrictions = new BaseRestrictions<QuotationSupportDecision>(
				QuotationSupportDecision.class);		
		restrictions.addCriterion(Restrictions.eq("quotation.id", quotation.getId()));
		return entityService.list(restrictions);
	}
}

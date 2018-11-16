package com.nokor.efinance.core.quotation.panel.popup;

import java.util.ArrayList;
import java.util.List;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.service.EntityService;

import com.nokor.common.app.tools.UserSessionManager;
import com.nokor.efinance.core.applicant.model.ECommentType;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.quotation.model.Comment;
import com.nokor.frmk.security.context.SecApplicationContextHolder;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;

/**
 * Contract Comment Pop up Panel
 * @author bunlong.taing
 *
 */
public class ContractCommentPopupPanel extends Window implements ClickListener {
	
	private static final long serialVersionUID = -903665717452652791L;
	
	private static int INCREASE_HEIGHT = 190;
	private NativeButton btnSubmit;
	private NativeButton btnCancel;
	private Button btnAdd;
	private Button btnClose;
	private Contract contract;
	private VerticalLayout commentLayout;
	private VerticalLayout newCommentLayout;
	private List<CommentFormLayout> commentFormLayouts;
	
	protected List<String> errors;
	private VerticalLayout messagePanel;
	
	private final EntityService entityService = (EntityService) SecApplicationContextHolder.getContext().getBean("entityService");
	
	/**
	 * Contract Comment Pop up Panel
	 * @param caption
	 */
	public ContractCommentPopupPanel(String caption, Contract contract) {
		super(I18N.message(caption));
		this.contract = contract;
		setModal(true);
		setResizable(false);
		setWidth(480, Unit.PIXELS);
		setHeight(280, Unit.PIXELS);
		init();
	}
	
	/**
	 * Init component in window
	 */
	private void init() {
		commentFormLayouts = new ArrayList<>();
		btnSubmit = new NativeButton(I18N.message("submit"));
		btnSubmit.addClickListener(this);
		btnCancel = new NativeButton(I18N.message("cancel"));
		btnCancel.addClickListener(this);
		btnAdd = ComponentFactory.getButton();
		btnAdd.setIcon(FontAwesome.PLUS);
		btnAdd.setWidth("420px");
		btnAdd.addClickListener(this);
		
		errors = new ArrayList<String>();
		messagePanel = new VerticalLayout();
		messagePanel.setMargin(true);
		messagePanel.setVisible(false);
		messagePanel.addStyleName("message");
		
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addButton(btnSubmit);
		navigationPanel.addButton(btnCancel);
		
		CommentFormLayout commentFormLayout = new CommentFormLayout();
		commentLayout = new VerticalLayout();
		commentLayout.setSpacing(true);
		commentLayout.addComponent(commentFormLayout);
		commentFormLayouts.add(commentFormLayout);
		
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.addComponent(navigationPanel);
		verticalLayout.addComponent(messagePanel);
		verticalLayout.addComponent(commentLayout);
		verticalLayout.addComponent(btnAdd);
		verticalLayout.setComponentAlignment(btnAdd, Alignment.BOTTOM_CENTER);
		
		setContent(verticalLayout);
	}
	
	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnAdd) {
			addCommentFormLayout();
		} else if (event.getButton() == btnCancel) {
			close();
		} else if (event.getButton() == btnSubmit) {
			saveQuotationComment();
		}
	}
	
	/**
	 * Add Comment FormLayout
	 */
	private void addCommentFormLayout() {
		CommentFormLayout commentFormLayout = new CommentFormLayout();
		newCommentLayout = new VerticalLayout();
		
		btnClose = ComponentFactory.getButton("X");
		btnClose.addStyleName(Reindeer.BUTTON_LINK);
		btnClose.addClickListener(new CloseComment(newCommentLayout, commentFormLayout));
		
		newCommentLayout.addComponent(btnClose);
		newCommentLayout.setComponentAlignment(btnClose, Alignment.MIDDLE_RIGHT);
		newCommentLayout.addComponent(commentFormLayout);
		commentLayout.addComponent(newCommentLayout);
		commentFormLayouts.add(commentFormLayout);
		setHeight(getHeight() + INCREASE_HEIGHT, Unit.PIXELS);
	}
	/**
	 * save contract comment when click button submit
	 */
	private void saveQuotationComment() {
		if (validate()) {
			SecUser secUser = UserSessionManager.getCurrentUser();
			for (CommentFormLayout commentFormLayout : commentFormLayouts) {
				Comment comment = new Comment();
				comment.setContract(contract);
				comment.setUser(secUser);
				comment.setCommentType(commentFormLayout.cbxType.getSelectedEntity());
				comment.setDesc(commentFormLayout.txtRemark.getValue());
				entityService.saveOrUpdate(comment);
			}
			close();
			Notification.show("", I18N.message("save.successfully"), Notification.Type.HUMANIZED_MESSAGE);
		} else {
			displayErrors();
		}
	}
	
	/**
	 * Validate required field
	 * @return
	 */
	private boolean validate() {
		messagePanel.setVisible(false);
		messagePanel.removeAllComponents();
		errors.clear();
		for (CommentFormLayout commentFormLayout : commentFormLayouts) {
			if (commentFormLayout.cbxType.getSelectedEntity() == null) {
				errors.add(I18N.message("field.required.1", new String[] { I18N.message("type") }));
			}
		}
		return errors.isEmpty();
	}
	/**
	 * Display errors
	 */
	public void displayErrors() {
		this.messagePanel.removeAllComponents();
		if (!(this.errors.isEmpty())) {
			for (String error : this.errors) {
				Label messageLabel = new Label(error);
				messageLabel.addStyleName("error");
				this.messagePanel.addComponent(messageLabel);
			}
			this.messagePanel.setVisible(true);
		}
	}
	
	/**
	 * Comment Form Layout
	 * @author bunlong.taing
	 */
	private class CommentFormLayout extends VerticalLayout {

		/** */
		private static final long serialVersionUID = 7441876718611527502L;
		public ERefDataComboBox<ECommentType> cbxType;
		public TextArea txtRemark;
		
		public CommentFormLayout() {
			setMargin(true);
			cbxType = new ERefDataComboBox<>(I18N.message("type"), ECommentType.class);
			cbxType.setRequired(true);
			txtRemark = ComponentFactory.getTextArea("remark", false, 300, 100);
			
			FormLayout formLayout = new FormLayout();
			formLayout.addComponent(cbxType);
			formLayout.addComponent(txtRemark);
			
			addComponent(formLayout);
		}
	}
	/**
	 * Lisenter to Button Close CommentFormLayout 
	 * @author buntha.chea
	 *
	 */
	private class CloseComment implements ClickListener {

		private static final long serialVersionUID = 4152994615445066995L;
		private VerticalLayout commentPanel;
		private CommentFormLayout commentFormLayout;
		
		public CloseComment(VerticalLayout commentLayout, CommentFormLayout commentFormLayout) {
			this.commentPanel = commentLayout;
			this.commentFormLayout = commentFormLayout;
		}

		/**
		 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
		 */
		@Override
		public void buttonClick(ClickEvent event) {
			float height = ContractCommentPopupPanel.this.getHeight() - 180;
			ContractCommentPopupPanel.this.setHeight(height, Unit.PIXELS);
			commentLayout.removeComponent(this.commentPanel);
			commentFormLayouts.remove(this.commentFormLayout);
		}
	}
}

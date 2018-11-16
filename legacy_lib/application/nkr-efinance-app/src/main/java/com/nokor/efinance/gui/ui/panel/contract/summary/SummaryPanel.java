package com.nokor.efinance.gui.ui.panel.contract.summary;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.MyNumberUtils;

import com.nokor.common.app.tools.UserSessionManager;
import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.applicant.model.EApplicantCategory;
import com.nokor.efinance.core.applicant.model.Individual;
import com.nokor.efinance.core.asset.model.Asset;
import com.nokor.efinance.core.collection.model.Collection;
import com.nokor.efinance.core.common.IProfileCode;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.ContractSimulation;
import com.nokor.efinance.core.contract.model.ContractUserInbox;
import com.nokor.efinance.core.contract.service.ContractUtils;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.core.shared.util.ProfileUtil;
import com.nokor.efinance.gui.ui.panel.contract.ContractFormPanel;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;


/**
 * Summary panel in CM latest version
 * @author uhout.cheng
 */
public class SummaryPanel extends AbstractTabPanel implements ClickListener, FinServicesHelper {
	
	/** */
	private static final long serialVersionUID = -4143892689892192282L;

	private Label lblContractIDValue;
	private Label lblDateValue;
	private Label lblContractStatusValue;
	private Label lblPhoneNOValue;
	private Label lblPaymentPatternValue;
	private Label lblCustomerIDValue;
	private Label lblApplicantStatusValue;
	private Label lblODMValue;
	private Label lblDOBValue;
	private Label lblAppCategoryValue;
	private Label lblAssetValue;
	private Label lblCustomerValue;
	
	private Button btnMoveNext;
	private Button btnMovePrevious;
	private Label lblCurrentIndexed;
	
	private ContractFormPanel deleget;
	
	private List<Long> contractIds;
	private Long previousId;
	private Long nextId;
	
	/**
	 * 
	 * @param deleget
	 */
	public SummaryPanel(ContractFormPanel deleget) {
		this.deleget = deleget;
		super.setMargin(new MarginInfo(false, true, false, true));
	}
	
	/**
	 * 
	 */
	public SummaryPanel() {
		super.setMargin(new MarginInfo(false, true, false, true));
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		HorizontalLayout topRightLayout = getTopRightLayout();
		
		CustomLayout customLayout = getSummaryDetailPanel();
		
		HorizontalLayout mainLayout = ComponentLayoutFactory.getHorizontalLayout(false, true);
		mainLayout.setSizeFull();
		mainLayout.addComponent(customLayout);
		mainLayout.addComponent(ComponentFactory.getSpaceLayout(20, Unit.PIXELS));
		mainLayout.addComponent(topRightLayout);
		mainLayout.setExpandRatio(customLayout, 1);
		
		mainLayout.setComponentAlignment(topRightLayout, Alignment.TOP_RIGHT);
		
		return mainLayout;
	}
	
	/**
	 * 
	 * @param value
	 * @return
	 */
	private String getDescription(String value) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("<b>");
		stringBuffer.append(getDefaultString(value));
		stringBuffer.append("</b>");
		return stringBuffer.toString();
	}
	
	/**
	 * 
	 * @return
	 */
	private Label getLabelValue() {
		Label label = new Label("", ContentMode.HTML);
		return label;
	}
	
	/**
	 * 
	 * @return
	 */
	private Label getLabel(String caption) {
		return ComponentFactory.getLabel(caption);
	}
	
	/**
	 * 
	 * @param date
	 * @return
	 */
	private String getDateFormat(Date date) {
		return DateUtils.getDateLabel(date, DateUtils.FORMAT_DDMMYYYY_SLASH);
	}
	
	/**
	 * 
	 * @return
	 */
	private CustomLayout getSummaryDetailPanel() {
		lblContractIDValue = getLabelValue();
		lblDateValue = getLabelValue();
		lblContractStatusValue = getLabelValue();
		lblPhoneNOValue = getLabelValue();
		lblPaymentPatternValue = getLabelValue();
		lblCustomerIDValue = getLabelValue();
		lblApplicantStatusValue = getLabelValue();
		lblODMValue = getLabelValue();
		lblDOBValue = getLabelValue();
		lblAppCategoryValue = getLabelValue();
		lblAssetValue = getLabelValue();
		lblCustomerValue = getLabelValue();
		
		String template = "summaryHeaderPageLayout";
		InputStream layoutFile = getClass().getResourceAsStream("/VAADIN/themes/efinance/layouts/summary/" + template + ".html");
		CustomLayout customLayout = null;
		try {
			customLayout = new CustomLayout(layoutFile);
		} catch (IOException e) {
			Notification.show("Could not locate template " + template, e.getMessage(), Type.ERROR_MESSAGE);
		}
		customLayout.addComponent(getLabel("contract.id"), "lblContractID");
		customLayout.addComponent(lblContractIDValue, "lblContractIDValue");
		customLayout.addComponent(getLabel("date"), "lblDate");
		customLayout.addComponent(lblDateValue, "lblDateValue");
		customLayout.addComponent(getLabel("contract.status"), "lblContractStatus");
		customLayout.addComponent(lblContractStatusValue, "lblContractStatusValue");
		customLayout.addComponent(getLabel("phone.no"), "lblPhoneNO");
		customLayout.addComponent(lblPhoneNOValue, "lblPhoneNOValue");
		customLayout.addComponent(getLabel("payment.pattern"), "lblPaymentPattern");
		customLayout.addComponent(lblPaymentPatternValue, "lblPaymentPatternValue");
		customLayout.addComponent(getLabel("customer.id"), "lblCustomerID");
		customLayout.addComponent(lblCustomerIDValue, "lblCustomerIDValue");
		customLayout.addComponent(getLabel("customer.status"), "lblApplicantStatus");
		customLayout.addComponent(lblApplicantStatusValue, "lblStatusValue");
		customLayout.addComponent(getLabel("odm"), "lblODM");
		customLayout.addComponent(lblODMValue, "lblODMValue");
		customLayout.addComponent(getLabel("dob"), "lblDOB");
		customLayout.addComponent(lblDOBValue, "lblDOBValue");
		customLayout.addComponent(getLabel("type"), "lblAppCategory");
		customLayout.addComponent(lblAppCategoryValue, "lblAppCategoryValue");
		customLayout.addComponent(getLabel("asset"), "lblAsset");
		customLayout.addComponent(lblAssetValue, "lblAssetValue");
		customLayout.addComponent(getLabel("customer"), "lblCustomer");
		customLayout.addComponent(lblCustomerValue, "lblCustomerValue");
		
		return customLayout;
	}
	
		
	/**
	 * 
	 * @param contract
	 */
	public void assignValues(Contract contract) {
		if (contract != null) {
			Asset asset = contract.getAsset();
			Applicant applicant = null;
			Individual individual = null;
			setUpNavigation(contract.getId());
			
			if (ContractUtils.isPendingTransfer(contract)) {
				ContractSimulation contractSimulation = ContractUtils.getLastContractSimulation(contract.getId());
				if (contractSimulation != null) {
					applicant = contractSimulation.getApplicant();
					if (applicant != null) {
						if (EApplicantCategory.INDIVIDUAL.equals(applicant.getApplicantCategory())
								|| EApplicantCategory.GLSTAFF.equals(applicant.getApplicantCategory())) {
							individual = applicant.getIndividual();
						}
					}
				}
			} else {
				applicant = contract.getApplicant();
				if (applicant != null) {
					if (EApplicantCategory.INDIVIDUAL.equals(applicant.getApplicantCategory())
							|| EApplicantCategory.GLSTAFF.equals(applicant.getApplicantCategory())) {
						individual = applicant.getIndividual();
					}
				}
			} 
			
			String appCategory = StringUtils.EMPTY;
			if (applicant != null && applicant.getApplicantCategory() != null) {
				appCategory = applicant.getApplicantCategory().getDescLocale();
			}
			lblAppCategoryValue.setValue(getDescription(appCategory));
			assignGeneralInfo(individual);
			refreshContractData(contract);
			
			String assetDetail = StringUtils.EMPTY;
			if (asset != null) {
				assetDetail = asset.getBrandDescLocale() + "-" + asset.getModelDescLocale();
			}
			lblAssetValue.setValue(getDescription(assetDetail));
			
			Collection collection = contract.getCollection();
			String transferDesc = StringUtils.EMPTY;
			if (ContractUtils.isTranfered(contract)) {
				transferDesc = contract.getReference() + StringUtils.SPACE + "(T)";
			} else {
				transferDesc = contract.getReference();
			}
			lblContractIDValue.setValue(getDescription(transferDesc));
			lblPaymentPatternValue.setValue("");
			if (collection != null) {
				lblODMValue.setValue(getDescription(getDefaultString(MyNumberUtils.getInteger(collection.getDebtLevel()))));
			}
		} else {
			btnMovePrevious.setEnabled(false);
			btnMoveNext.setEnabled(false);
			lblCurrentIndexed.setCaption("<b style=\"font-size:20px\">" + 0 + "/" + 0 + "</b>");
		}
	}
	
	/**
	 * 
	 * @param contract
	 */
	public void refreshContractData(Contract contract) {
		lblDateValue.setValue(getDescription(getDateFormat(contract.getStartDate())));
		lblContractStatusValue.setValue(getDescription(contract.getWkfStatus().getDesc()));
	}
	
	/**
	 * 
	 * @param individual
	 */
	public void assignGeneralInfo(Individual individual) {
		String title = StringUtils.EMPTY;
		String prefix = StringUtils.EMPTY;
		String firstName = StringUtils.EMPTY;
		String lastName = StringUtils.EMPTY;
		if (individual != null) {
			title = individual.getTitle() != null ? individual.getTitle().getDescLocale() : StringUtils.EMPTY;
			prefix = individual.getCivility() != null ? individual.getCivility().getDescLocale() : StringUtils.EMPTY;
			firstName = individual.getFirstNameLocale();
			lastName = individual.getLastNameLocale();
			List<String> generalInfos = Arrays.asList(new String[] { title, prefix, firstName, lastName });
			String generalInfo = StringUtils.EMPTY;
			if (generalInfos != null && !generalInfos.isEmpty()) {
				for (int i = 0; i < generalInfos.size(); i++) {
					if (StringUtils.isNotEmpty(generalInfos.get(i))) {
						generalInfo += generalInfos.get(i);
						if (generalInfos.size() - i > 1) {
							generalInfo += StringUtils.SPACE;
						}
					}
				}
			}
			lblCustomerValue.setValue(getDescription(generalInfo));
			lblPhoneNOValue.setValue(getDescription(individual.getIndividualPrimaryContactInfo()));
			lblCustomerIDValue.setValue(getDescription(individual.getReference()));
			lblApplicantStatusValue.setValue(getDescription(individual.getWkfStatus().getDescLocale()));
			lblDOBValue.setValue(getDescription(getDateFormat(individual.getBirthDate())));
		}
	}
	
	/**
	 * 
	 */
	public void reset() {
		lblContractIDValue.setValue(StringUtils.EMPTY);
		lblDateValue.setValue(StringUtils.EMPTY);
		lblContractStatusValue.setValue(StringUtils.EMPTY);
		lblPhoneNOValue.setValue(StringUtils.EMPTY);
		lblPaymentPatternValue.setValue(StringUtils.EMPTY);
		lblCustomerIDValue.setValue(StringUtils.EMPTY);
		lblApplicantStatusValue.setValue(StringUtils.EMPTY);
		lblDOBValue.setValue(StringUtils.EMPTY);
		lblAppCategoryValue.setValue(StringUtils.EMPTY);
		lblAssetValue.setValue(StringUtils.EMPTY);
		lblCustomerValue.setValue(StringUtils.EMPTY);
	}
	
	/**
	 * 
	 * @return
	 */
	private HorizontalLayout getTopRightLayout() {
		lblCurrentIndexed = ComponentFactory.getLabel();
		lblCurrentIndexed.setCaptionAsHtml(true);
	     
	    btnMoveNext = getButton(FontAwesome.FORWARD);
	    btnMoveNext.addStyleName("btn-icon-padding");
		btnMovePrevious = getButton(FontAwesome.BACKWARD);
	        
		HorizontalLayout topRightLayout = new HorizontalLayout();
		topRightLayout.setSpacing(true);
		topRightLayout.addComponent(btnMovePrevious);
		topRightLayout.addComponent(lblCurrentIndexed);
		topRightLayout.addComponent(btnMoveNext);
		topRightLayout.setComponentAlignment(btnMovePrevious, Alignment.MIDDLE_RIGHT);
		topRightLayout.setComponentAlignment(lblCurrentIndexed, Alignment.MIDDLE_RIGHT);
		topRightLayout.setComponentAlignment(btnMoveNext, Alignment.MIDDLE_RIGHT);
		return topRightLayout;
	}
	
	/**
	 * 
	 * @param icon
	 * @return
	 */
	private Button getButton(Resource icon) {
		Button btn = ComponentLayoutFactory.getDefaultButton(null, icon, 40);
		btn.addClickListener(this);
		return btn;
	}
	
	/**
	 * Setting up navigation
	 * @param contractId
	 */
	private void setUpNavigation(Long contractId) {
		previousId = null;
		nextId = null;
		if (contractIds == null) {
			contractIds = getContractIds();
		}
		if (isExistContractId(contractId, contractIds)) {
			int size = contractIds.size();
			lblCurrentIndexed.setCaption("<b style=\"font-size:20px\">" + (size == 0 ? 0 : getCurrentIndexed(contractId)) + "/" + size + "</b>");
			for (Long id : contractIds) {
				if (id < contractId) {
					previousId = (previousId != null && previousId > id) ? previousId : id;
				} else if (id > contractId) {
					nextId = (nextId != null && nextId < id) ? nextId : id;
				}
			}
			btnMovePrevious.setEnabled(previousId != null);
			btnMoveNext.setEnabled(nextId != null);
		} else {
			lblCurrentIndexed.setCaption("<b style=\"font-size:20px\">" + 1 + "/" + 1 + "</b>");
			btnMoveNext.setEnabled(false);
			btnMovePrevious.setEnabled(false);
		}
	}
	
	/**
	 * 
	 * @param currentContractId
	 * @param contractIds
	 * @return
	 */
	private boolean isExistContractId(Long currentContractId, List<Long> contractIds) {
		if (contractIds != null && !contractIds.isEmpty()) {
			for (Long contraId : contractIds) {
				if (contraId == currentContractId) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	private long getCurrentIndexed(Long id) {
		List<Long> conIds = getContractIds();
		if (conIds != null && !conIds.isEmpty()) {
			long currentIndexed = 1;
			for (Long conId : conIds) {
				if (id == conId) {
					return currentIndexed;
				}
				currentIndexed++;
			}
		}
		return 0l;
	}
	
	/**
	 * 
	 * @return
	 */
	private List<Long> getContractIds() {
		SecUser userLogin = UserSessionManager.getCurrentUser();
		List<ContractUserInbox> contractUsers = INBOX_SRV.getContractUserInboxByUser(userLogin.getId());
		if (ProfileUtil.isColPhoneLeader(userLogin) || ProfileUtil.isColPhoneSupervisor(userLogin)) {
			contractUsers = INBOX_SRV.getContractUserInboxByProCode(IProfileCode.COL_PHO_STAFF);
		} else if (ProfileUtil.isColFieldLeader(userLogin) || ProfileUtil.isColFieldSupervisor(userLogin)) {
			contractUsers = INBOX_SRV.getContractUserInboxByProCode(IProfileCode.COL_FIE_STAFF);
		} else if (ProfileUtil.isColInsideRepoLeader()) {
			contractUsers = INBOX_SRV.getContractUserInboxByProCode(IProfileCode.COL_INS_STAFF);
		} else if (ProfileUtil.isCallCenterLeader()) {
			contractUsers = INBOX_SRV.getContractUserInboxByProCode(IProfileCode.CAL_CEN_STAFF);
		}
		List<Long> ids = new ArrayList<Long>();
		if (contractUsers != null && !contractUsers.isEmpty()) {
			for (ContractUserInbox contractUserInbox : contractUsers) {
				Contract con = contractUserInbox.getContract();
				if (con != null) {
					ids.add(con.getId());
				}
			}
		}
		return ids;
	}
	
	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnMoveNext) {
			deleget.assignValues(nextId, true);
		} else if (event.getButton() == btnMovePrevious) {
			deleget.assignValues(previousId, true);
		}
		
	}
}

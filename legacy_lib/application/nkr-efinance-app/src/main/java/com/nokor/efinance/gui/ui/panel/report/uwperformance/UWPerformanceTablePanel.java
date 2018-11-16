package com.nokor.efinance.gui.ui.panel.report.uwperformance;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.quotation.model.Quotation;
import com.nokor.efinance.core.quotation.model.QuotationStatusHistory;
import com.nokor.efinance.core.shared.quotation.QuotationEntityField;
import com.nokor.efinance.core.shared.system.FMProfile;
import com.nokor.efinance.core.shared.util.DateFilterUtil;
import com.nokor.efinance.core.shared.util.ProfileUtil;
import com.nokor.efinance.core.workflow.QuotationWkfStatus;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel;
import com.nokor.frmk.vaadin.ui.widget.table.EntityColumnRenderer;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.EntityPagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.table.impl.EntityPagedTable;
import com.vaadin.ui.Table.Align;

/**
 * 
 * @author sotheara.leang
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class UWPerformanceTablePanel extends AbstractTablePanel<Quotation> implements QuotationEntityField {

	private static final long serialVersionUID = -5108003734598681093L;
	
	private Quotation quotation;
	//private QuotationProcessTime quotationProcessTime;
	private QuotationReturn quotationReturn;
	
	private QuotationProcessingTimeByProfile qProcessingTimeByProfile;

	@PostConstruct
	public void PostConstruct() {
		setCaption(I18N.message("uw.performance.report"));
		setSizeFull();
		setMargin(true);
		setSpacing(true);

		super.init(null);
		
	}

	@Override
	protected PagedDataProvider<Quotation> createPagedDataProvider() {
		PagedDefinition<Quotation> pagedDefinition = new PagedDefinition<Quotation>(searchPanel.getRestrictions());
		pagedDefinition.addColumnDefinition("firstSubmissionDate",I18N.message("apply.date"), Date.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition("endDate", I18N.message("enddate"),Date.class, Align.LEFT, 100, new EndDateRenderer());
		pagedDefinition.addColumnDefinition("applicant", I18N.message("application"), String.class, Align.LEFT, 125, new ApplicantRenderer());
		pagedDefinition.addColumnDefinition("dealer.nameEn", I18N.message("dealer"), String.class, Align.LEFT, 150);
		pagedDefinition.addColumnDefinition("underwriter.desc", I18N.message("uw"), String.class, Align.LEFT, 125);
		pagedDefinition.addColumnDefinition("underwriterSupervisor.desc", I18N.message("us"), String.class, Align.LEFT, 125);
		pagedDefinition.addColumnDefinition("manager.desc", I18N.message("mgt"), String.class, Align.LEFT, 125);

		pagedDefinition.addColumnDefinition("POSProcessTime", I18N.message("pos.process.time"), Float.class, Align.LEFT, 100, new POSProcessTimeRenderer());
		pagedDefinition.addColumnDefinition("UWProcessTime", I18N.message("uw.process.time"), Float.class, Align.LEFT, 100, new UWProcessTimeRenderer());
		pagedDefinition.addColumnDefinition("USProcessTime", I18N.message("us.process.time"), Float.class, Align.LEFT, 100, new USProcessTimeRenderer());
		pagedDefinition.addColumnDefinition("MAProcessTime", I18N.message("ma.process.tme"), Float.class, Align.LEFT, 100, new MAProcessTimeRenderer());
		pagedDefinition.addColumnDefinition("RFCProcessTime", I18N.message("rfc.process.tme"), Float.class, Align.LEFT, 100, new RFCProcessTimeRenderer());
		
		pagedDefinition.addColumnDefinition("RFC", I18N.message("field.check"), String.class, Align.LEFT, 100, new RFCRenderer());
		pagedDefinition.addColumnDefinition("AWC", I18N.message("approved.with.condition"), String.class, Align.LEFT, 160, new ApprovedWithConditionRenderer());
		pagedDefinition.addColumnDefinition("totalProcessTime", I18N.message("total.process.time"), Float.class, Align.LEFT, 130, new TotalProcessTimeRenderer());
		pagedDefinition.addColumnDefinition("UWProfilesProcessTime", I18N.message("uw.profiles.process.time"), Float.class, Align.LEFT, 120, new UWProfilesProcessTimeRenderer());
		pagedDefinition.addColumnDefinition("result", I18N.message("result"), String.class, Align.LEFT, 100, new ResultRenderer());
		pagedDefinition.addColumnDefinition("duration", I18N.message("duration"), Float.class, Align.LEFT, 100, new DurationRenderer());
		pagedDefinition.addColumnDefinition("PostApprovalProcessTime", I18N.message("post.approval.process.time"), Float.class, Align.LEFT, 130, new PostApprovalProcessTimeRenderer());
		
		pagedDefinition.addColumnDefinition("POSReturn", I18N.message("pos.return"), Integer.class, Align.LEFT, 100, new POSReturnRenderer());
		pagedDefinition.addColumnDefinition("UWReturn", I18N.message("uw.return"), Integer.class, Align.LEFT, 100, new UWReturnRenderer());
		pagedDefinition.addColumnDefinition("SUWReturn", I18N.message("suw.return"), Integer.class, Align.LEFT, 100, new SUWReturnRenderer());
		
		pagedDefinition.addColumnDefinition("advancePayment", I18N.message("advance.payment"), String.class, Align.LEFT, 100, new AdvancePaymentRenderer());
		pagedDefinition.addColumnDefinition("employmentStatus", I18N.message("employment.status"), String.class, Align.LEFT, 130, new EmploymentStatusRenderer());
		pagedDefinition.addColumnDefinition("housing", I18N.message("housing"), String.class, Align.LEFT, 100, new HousingRenderer());

		EntityPagedDataProvider<Quotation> pagedDataProvider = new EntityPagedDataProvider<Quotation>();
		pagedDataProvider.setPagedDefinition(pagedDefinition);
		return pagedDataProvider;
	}
	
	private class ApplyDateRenderer extends EntityColumnRenderer {

		@Override
		public Object getValue() {
			quotation = ((Quotation) getEntity());
			return quotation.getFirstSubmissionDate();
		}
	}

	/* Column Renderer */

	/**
	 * Renderer the End Date field
	 */
	private class EndDateRenderer extends EntityColumnRenderer {

		@Override
		public Object getValue() {
			quotation = ((Quotation) getEntity());
			EWkfStatus quotationStatus = quotation.getWkfStatus();
			
			if (quotationStatus.equals(QuotationWkfStatus.ACT)) {
				return quotation.getActivationDate();
			} else if (quotationStatus.equals(QuotationWkfStatus.REJ)) {
				return quotation.getRejectDate();
			} else if (quotationStatus.equals(QuotationWkfStatus.DEC)) {
				return quotation.getDeclineDate();
			}
			return null;
		}
	}
	
	/**
	 * Renderer the Application field
	 */
	private class ApplicantRenderer extends EntityColumnRenderer {

		@Override
		public Object getValue() {
			qProcessingTimeByProfile = new QuotationProcessingTimeByProfile(quotation);
			Applicant applicant = quotation.getApplicant();
			return applicant.getIndividual().getLastNameEn() + " " + applicant.getIndividual().getFirstNameEn();
		}
	}
	
	/**
	 * Renderer the POS Proc. Time field 
	 */
	private class POSProcessTimeRenderer extends EntityColumnRenderer {

		@Override
		public Object getValue() {
						
			if(qProcessingTimeByProfile != null){
				/*
				BigDecimal value = new BigDecimal(qProcessingTimeByProfile.getPOSTime()/(1000*3600f),new MathContext(4, RoundingMode.DOWN));
			    return value.floatValue();
			    */
				return getTimeInHour(qProcessingTimeByProfile.getPOSTime());
			}

			return null;
		}
	}
	
	/**
	 * Renderer the UW Proc. Time Field
	 */
	private class UWProcessTimeRenderer extends EntityColumnRenderer {

		@Override
		public Object getValue() {
						
			if(qProcessingTimeByProfile != null){
				/*
				BigDecimal value = new BigDecimal(qProcessingTimeByProfile.getUWTime()/(1000*3600f),new MathContext(4, RoundingMode.DOWN));
			    return value.floatValue();
			    */
				return getTimeInHour(qProcessingTimeByProfile.getUWTime());
			}
			
			return null;
		}
	}
	
	/**
	 * Renderer the US Proc. Time field
	 */
	private class USProcessTimeRenderer extends EntityColumnRenderer {

		@Override
		public Object getValue() {
						
			if(qProcessingTimeByProfile != null){
				/*
				BigDecimal value = new BigDecimal(qProcessingTimeByProfile.getUSTime()/(1000*3600f),new MathContext(4, RoundingMode.DOWN));
			    return value.floatValue();
			    */
				return getTimeInHour(qProcessingTimeByProfile.getUSTime());
			}
			return null;
		}
	}
	
	/**
	 * Renderer the MGT Proc. Time field
	 */
	private class MAProcessTimeRenderer extends EntityColumnRenderer {

		@Override
		public Object getValue() {
			/*
			if (quotationProcessTime != null && quotation.getManager() != null) {
				return getProcessTime(quotationProcessTime.getMATime());
			}
			*/
			
			if(qProcessingTimeByProfile != null){
				/*
				BigDecimal value = new BigDecimal(qProcessingTimeByProfile.getMATime()/(1000*3600f),new MathContext(4, RoundingMode.DOWN));
			    return value.floatValue();
			    */
				return getTimeInHour(qProcessingTimeByProfile.getMATime());
			}
			
			return null;
		}
	}
	
	/**
	 * Renderer the Request Field Check Proc. Time field
	 */
	private class RFCProcessTimeRenderer extends EntityColumnRenderer {

		@Override
		public Object getValue() {
			
			if(qProcessingTimeByProfile != null){
				/*
				BigDecimal value = new BigDecimal(qProcessingTimeByProfile.getRFCTime()/(1000*3600f),new MathContext(4, RoundingMode.DOWN));
			    return value.floatValue();
			    */
				return getTimeInHour(qProcessingTimeByProfile.getRFCTime());
			}
			
			return null;
		}
	}
	
	/**
	 * Renderer the Request Field Check field
	 */
	private class RFCRenderer extends EntityColumnRenderer {

		@Override
		public Object getValue() {
			return isRFC() ? I18N.message("yes") : I18N.message("no");
		}
	}
	
	/**
	 * Renderer the Approved With Condition field
	 */
	private class ApprovedWithConditionRenderer extends EntityColumnRenderer {

		@Override
		public Object getValue() {
			return isApprovedWithCondition() ? I18N.message("yes") : I18N.message("no");
		}
	}
	
	/**
	 * Renderer the Total Process Time field
	 */
	private class TotalProcessTimeRenderer extends EntityColumnRenderer {

		@Override
		public Object getValue() {		
						
			if(qProcessingTimeByProfile != null){
				/*
				BigDecimal posTime = new BigDecimal(qProcessingTimeByProfile.getPOSTime()/(1000*3600f),new MathContext(4, RoundingMode.DOWN));
				BigDecimal uwTime = new BigDecimal(qProcessingTimeByProfile.getUWTime()/(1000*3600f),new MathContext(4, RoundingMode.DOWN));
				BigDecimal usTime = new BigDecimal(qProcessingTimeByProfile.getUSTime()/(1000*3600f),new MathContext(4, RoundingMode.DOWN));
				BigDecimal maTime = new BigDecimal(qProcessingTimeByProfile.getMATime()/(1000*3600f),new MathContext(4, RoundingMode.DOWN));
				BigDecimal rfcTime = new BigDecimal(qProcessingTimeByProfile.getRFCTime()/(1000*3600f),new MathContext(4, RoundingMode.DOWN));
				
				float value = posTime.floatValue() + uwTime.floatValue() + usTime.floatValue() + maTime.floatValue() + rfcTime.floatValue();
			    return new Float(value);
			    */
				float posTime = getTimeInHour(qProcessingTimeByProfile.getPOSTime());
				float uwTime = getTimeInHour(qProcessingTimeByProfile.getUWTime());
				float usTime = getTimeInHour(qProcessingTimeByProfile.getUSTime());
				float maTime = getTimeInHour(qProcessingTimeByProfile.getMATime());
				//float rfcTime= getTimeInHour(qProcessingTimeByProfile.getRFCTime());
				return posTime + uwTime + usTime + maTime;
			}
			
			return null;
		}
	}
	
	/**
	 * Renderer the Post Approval Process Time field = Quotation.activated time - Quotation.approved time
	 */
	private class PostApprovalProcessTimeRenderer extends EntityColumnRenderer {

		@Override
		public Object getValue() {
			EWkfStatus quotationStatus = quotation.getWkfStatus();
			if (quotationStatus.equals(QuotationWkfStatus.APV)
					|| quotationStatus.equals(QuotationWkfStatus.PPO)
					|| quotationStatus.equals(QuotationWkfStatus.ACT)) {
				return getTimeInHour(qProcessingTimeByProfile.getPostApprovalProcessingTime());
			}
			return null;
		}
	}
	
	/**
	 * Renderer process time for all UW profiles
	 */
	private class UWProfilesProcessTimeRenderer extends EntityColumnRenderer {

		@Override
		public Object getValue() {
						
			if(qProcessingTimeByProfile != null){				
				/*
				BigDecimal uwTime = new BigDecimal(qProcessingTimeByProfile.getUWTime()/(1000*3600f),new MathContext(4, RoundingMode.DOWN));
				BigDecimal usTime = new BigDecimal(qProcessingTimeByProfile.getUSTime()/(1000*3600f),new MathContext(4, RoundingMode.DOWN));
			    return uwTime.floatValue() + usTime.floatValue();
			    */
				return getTimeInHour(qProcessingTimeByProfile.getUWTime()) + getTimeInHour(qProcessingTimeByProfile.getUSTime()) + getTimeInHour(qProcessingTimeByProfile.getMATime());
			}
			
			return null;			
		}
	}
	
	/**
	 * Renderer the Result field
	 */
	private class ResultRenderer extends EntityColumnRenderer {

		@Override
		public Object getValue() {
			EWkfStatus quotationStatus = quotation.getWkfStatus();
			
			if (quotationStatus.equals(QuotationWkfStatus.ACT)) {
				return I18N.message("activated");
			} else if (quotationStatus.equals(QuotationWkfStatus.REJ)) {
				return I18N.message("rejected");
			} else if (quotationStatus.equals(QuotationWkfStatus.DEC)) {
				return I18N.message("declined");
			}
			return null;
		}
	}
	
	/**
	 * Renderer the Duration field
	 */
	private class DurationRenderer extends EntityColumnRenderer {

		@Override
		public Object getValue() {
			EWkfStatus quotationStatus = quotation.getWkfStatus();
			
			if (quotationStatus.equals(QuotationWkfStatus.ACT)) {
				return getFormatTimeInHour("###.########", quotation.getActivationDate().getTime() - quotation.getFirstSubmissionDate().getTime());
			} else if (quotationStatus.equals(QuotationWkfStatus.REJ)) {
				return getFormatTimeInHour("###.########", quotation.getRejectDate().getTime() - quotation.getFirstSubmissionDate().getTime());
			} else if (quotationStatus.equals(QuotationWkfStatus.DEC)) {
				return getFormatTimeInHour("###.########", quotation.getDeclineDate().getTime() - quotation.getFirstSubmissionDate().getTime());
			}
			return null;
		}
	}
	
	/**
	 * Renderer the Advance Payment field 
	 */
	private class AdvancePaymentRenderer extends EntityColumnRenderer {

		@Override
		public Object getValue() {
			double advancePaymentPercentage = quotation.getAdvancePaymentPercentage();
			return advancePaymentPercentage + "%";
		}
	}
	
	/**
	 * Renderer the POS Return field
	 */
	private class POSReturnRenderer extends EntityColumnRenderer {

		@Override
		public Object getValue() {
			if (quotationReturn != null) {
				return quotationReturn.getPOSReturn();
			}
			return null;
		}
	}
	
	/**
	 * Renderer the UW Return field
	 */
	private class UWReturnRenderer extends EntityColumnRenderer {

		@Override
		public Object getValue() {
			if (quotationReturn != null) {
				return quotationReturn.getUWReturn();
			}
			return null;
		}
	}
	
	/**
	 * Renderer the SUW Return field
	 */
	private class SUWReturnRenderer extends EntityColumnRenderer {

		@Override
		public Object getValue() {
			if (quotationReturn != null) {
				return quotationReturn.getSUWReturn();
			}
			return null;
		}
	}
	
	/**
	 * Renderer the Employment Status
	 */
	private class EmploymentStatusRenderer extends EntityColumnRenderer {

		@Override
		public Object getValue() {
			Applicant application = quotation.getApplicant();
			return application.getIndividual().getCurrentEmployment().getEmploymentStatus().getDescEn();
		}
	}
	
	/**
	 * Renderer the Hosing field
	 */
	private class HousingRenderer extends EntityColumnRenderer {

		@Override
		public Object getValue() {
			Applicant application = quotation.getApplicant();
			return application.getIndividual().getMainAddress().getProperty().getDesc();
		}
	}
	
	/* End Column Renderer */
	
	/**
	 * Add the time to quotation process time variable associated with the user
	 * @param processTime
	 * @param user
	 * @param value
	 */
	private void setQuotationProcessTimeFor(QuotationProcessTime processTime, SecUser user, long value) {
		if (ProfileUtil.isPOS(user)) {
			processTime.incrementPOSTime(value);
		} else if (ProfileUtil.isUnderwriter(user)) {
			processTime.incrementUWTime(value);
		} else if (ProfileUtil.isUnderwriterSupervisor(user)) {
			processTime.incrementUSTime(value);
		} else if (ProfileUtil.isManager(user)) {
			processTime.incrementMATime(value);
		}
	}
	
	/**
	 * Get the status histories of the quotation
	 * @return
	 */
	private List<QuotationStatusHistory> getQuotationStatusHisotires() {
		// TODO PYI
//		BaseRestrictions<QuotationStatusHistory> restrictions = new BaseRestrictions<>(QuotationStatusHistory.class);
//		restrictions.addAssociation("user", "SEC_USER", JoinType.INNER_JOIN);
//		restrictions.addAssociation("SEC_USER.defaultProfile", "DEFAULT_PROFILE", JoinType.INNER_JOIN);
//		restrictions.addCriterion(Restrictions.eq("quotation.id", quotation.getId()));
//		restrictions.addOrder(Order.desc("createDate"));
//		return entityService.list(restrictions);
		return null;
	}
	
	private List<QuotationStatusHistory> getQuotationStatusHisotires2() {
		// TODO PYI
//		BaseRestrictions<QuotationStatusHistory> restrictions = new BaseRestrictions<>(QuotationStatusHistory.class);
//		restrictions.addAssociation("user", "SEC_USER", JoinType.INNER_JOIN);
//		restrictions.addAssociation("SEC_USER.defaultProfile", "DEFAULT_PROFILE", JoinType.INNER_JOIN);
//		restrictions.addCriterion(Restrictions.eq("quotation.id", quotation.getId()));
//		restrictions.addOrder(Order.asc("updateDate"));
//		return entityService.list(restrictions);
		return null;
	}
	
	
	
	/**
	 * Calculate the process time for a quotation
	 * @param list
	 * @return
	 */
	private QuotationProcessTime getQuotationProcessTime(List<QuotationStatusHistory> list) {
		// TODO PYI
//		if (list != null && !list.isEmpty()) {
//			QuotationProcessTime processTime = new QuotationProcessTime();
//			for (int i=0; i<list.size(); i++) {
//				Long time;
//				QuotationStatusHistory status = list.get(i);
//				if (i < list.size() - 1) {
//					QuotationStatusHistory nextStatus = list.get(i + 1);
//					Boolean requestFieldCheck = ProfileUtil.isPOS(status.getUser()) 
//							&& status.getPreviousWkfStatus().equals(WkfQuotationStatus.RFC) 
//							&& status.getWkfStatus().equals(WkfQuotationStatus.PRO);
//					time = calculateTime(nextStatus.getCreateDate(), status.getCreateDate());
//					setQuotationProcessTimeFor(processTime, status.getUser(), time);
//					if (requestFieldCheck) {
//						processTime.incrementRFCime(time);
//					}
//				} else {
//					time = calculateTime(quotation.getStartCreationDate(), status.getCreateDate());
//					setQuotationProcessTimeFor(processTime, status.getUser(), time);
//				}
//			}
//			return processTime;
//		}
		return null;
	}
	
	/**
	 * Calculate the time between the two dates in accordance to the GLF working day time
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	private Long calculateTime(Date startDate, Date endDate) {
		if (startDate == null || endDate == null) {
			return null;
		}
		startDate = validateWorkingDate(startDate);
		endDate = validateWorkingDate(endDate);
		long diffDays = DateUtils.getDiffInDays(DateUtils.getDateAtBeginningOfDay(endDate), DateUtils.getDateAtBeginningOfDay(startDate));
		if (diffDays == 0) {
			return endDate.getTime() - startDate.getTime();
		} else {
			long time = 0;
			for (int i=0; i<=diffDays; i++) {
				Date nextDay = DateUtils.addDaysDate(startDate, i);
				if (i == 0) {
					time += DateFilterUtil.getWorkingEndDate(nextDay).getTime() - startDate.getTime();
				} else if (i == diffDays) {
					time += endDate.getTime() - DateFilterUtil.getWorkingStartDate(nextDay).getTime();
				} else {
					time += DateFilterUtil.getWorkingEndDate(nextDay).getTime() - DateFilterUtil.getWorkingStartDate(nextDay).getTime(); 
				}
			}
			return time;
		}
	}
	
	private Date validateWorkingDate(Date date) {
		Date startWorkingDate = DateFilterUtil.getWorkingStartDate(date);
		Date endWorkingDate = DateFilterUtil.getWorkingEndDate(date);
		if (date.before(startWorkingDate)) {
			return startWorkingDate;
		} else if (date.after(endWorkingDate)) {
			return endWorkingDate;
		}
		return date;
	}
	
	/**
	 * Calculate the POS Return, UW Return and SUW Return for a quotation
	 * @param list
	 * @return
	 */
	private QuotationReturn getQuotationReturn(List<QuotationStatusHistory> list) {
		// TODO PYI
//		if (list != null && !list.isEmpty()) {
//			QuotationReturn quotationReturn = new QuotationReturn();
//			for (QuotationStatusHistory status : list) {
//				if (ProfileUtil.isUnderwriter(status.getUser())) {
//					if (status.getWkfStatus().equals(WkfQuotationStatus.QUO) || status.getWkfStatus().equals(WkfQuotationStatus.RFC)) {
//						quotationReturn.incrementPOSReturn(1);
//					}
//				} else if (ProfileUtil.isUnderwriterSupervisor(status.getUser())) {
//					if (status.getWkfStatus().equals(WkfQuotationStatus.QUO) || status.getWkfStatus().equals(WkfQuotationStatus.RFC)) {
//						quotationReturn.incrementPOSReturn(1);
//					} else if (status.getWkfStatus().equals(WkfQuotationStatus.PRO)) {
//						quotationReturn.incrementUWReturn(1);
//					}
//				} else if (ProfileUtil.isManager(status.getUser())) {
//					if (status.getWkfStatus().equals(WkfQuotationStatus.PRO)) {
//						quotationReturn.incrementSUWReturn(1);
//						quotationReturn.incrementUWReturn(1);
//					}
//				}
//			}
//			return quotationReturn;
//		}
		return null;
	}
	
	/**
	 * Check if a quotation have RFC
	 * @return
	 */
	private boolean isRFC() {
		// TODO PYI
//		BaseRestrictions<QuotationStatusHistory> restrictions = new BaseRestrictions<>(QuotationStatusHistory.class);
//		restrictions.addCriterion(Restrictions.eq("quotation.id", quotation.getId()));
//		List<QuotationStatusHistory> list = entityService.list(restrictions);
//		for (QuotationStatusHistory status : list) {
//			if (status.getWkfStatus().equals(WkfQuotationStatus.RFC)) {
//				return true;
//			}
//		}
		return false;
	}
	
	/**
	 * Check if a quotation is approved with condition
	 * @return
	 */
	private boolean isApprovedWithCondition() {
		// TODO PYI
//		BaseRestrictions<QuotationStatusHistory> restrictions = new BaseRestrictions<>(QuotationStatusHistory.class);
//		restrictions.addCriterion(Restrictions.eq("quotation.id", quotation.getId()));
//		List<QuotationStatusHistory> list = entityService.list(restrictions);
//		for (QuotationStatusHistory status : list) {
//			EWkfStatus quotationStatus = status.getWkfStatus();
//			if (quotationStatus.equals(WkfQuotationStatus.AWU) || quotationStatus.equals(WkfQuotationStatus.AWS)) {
//				return true;
//			}
//		}
		return false;
	}
	
	/**
	 * Convert time in milliseconds to the string ([d:][h:]m)
	 * @param millis
	 * @return
	 */
	private String getProcessTime(Long millis) {
		if (millis != null) {
			StringBuilder time = new StringBuilder();
			long m = (millis / (1000 * 60)) % 60;
			long h = (millis / (1000 * 60 * 60)) % 24;
			long d = (millis / (1000 * 60 * 60 * 24));
			if (d != 0) {
				time.append(d + "d:");
			}
			if (h != 0) {
				time.append(h + "h:");
			}
			time.append(m + "m");
			return time.toString();
		}
		return null;
	}
	
	/**
	 * Convert time in milliseconds to string ([d:][h:]m:s)
	 * @param millis
	 * @return
	 */
	private String getDurationTime(Long millis) {
		if (millis != null) {
			StringBuilder time = new StringBuilder();
			long s = (millis / 1000) % 60;
			long m = (millis / (1000 * 60)) % 60;
			long h = (millis / (1000 * 60 * 60)) % 24;
			long d = (millis / (1000 * 60 * 60 * 24));
			if (d != 0) {
				time.append(d + "d:");
			}
			if (h != 0) {
				time.append(h + "h:");
			}
			time.append(m + "m:");
			time.append(s + "s");
			return time.toString();
		}
		return null;
	}
	
	@Override
	protected Quotation getEntity() {
		return null;
	}

	@Override
	protected UWPerformanceSearchPanel createSearchPanel() {
		
		return  new UWPerformanceSearchPanel(this);
	}
	
	/**
	 * Represent the Quotation Process Time 
	 */
	protected class QuotationProcessTime {
		
		private long POSTime;
		private long UWTime;
		private long USTime;
		private long MATime;
		private long RFCTime;
		
		public long getPOSTime() {
			return POSTime;
		}
		
		public void setPOSTime(long pOSTime) {
			POSTime = pOSTime;
		}
		
		public long getUWTime() {
			return UWTime;
		}
		
		public void setUWTime(long uWTime) {
			UWTime = uWTime;
		}
		
		public long getUSTime() {
			return USTime;
		}
		
		public void setUSTime(long uSTime) {
			USTime = uSTime;
		}
		
		public long getMATime() {
			return MATime;
		}
		
		public void setMATime(long mATime) {
			MATime = mATime;
		}

		public long getRFCTime() {
			return RFCTime;
		}

		public void setRFCTime(long requestFieldCheckTime) {
			this.RFCTime = requestFieldCheckTime;
		}
		
		public void incrementPOSTime(long value) {
			POSTime += value;
		}
		
		public void incrementUWTime(long value) {
			UWTime += value;
		}
		
		public void incrementUSTime(long value) {
			USTime += value;
		}
		
		public void incrementMATime(long value) {
			MATime += value;
		}
		
		public void incrementRFCime(long value) {
			RFCTime += value;
		}
	}
	
	/**
	 * Represent the Quotation return 
	 */
	protected class QuotationReturn {
		
		int POSReturn;
		int UWReturn;
		int SUWReturn;
		
		public int getPOSReturn() {
			return POSReturn;
		}
		
		public void setPOSReturn(int pOSReturn) {
			POSReturn = pOSReturn;
		}
		
		public int getUWReturn() {
			return UWReturn;
		}
		
		public void setUWReturn(int uWReturn) {
			UWReturn = uWReturn;
		}
		
		public int getSUWReturn() {
			return SUWReturn;
		}
		
		public void setSUWReturn(int sUWReturn) {
			SUWReturn = sUWReturn;
		}
		
		public void incrementPOSReturn(int value) {
			POSReturn += value;
		}
		
		public void incrementUWReturn(int value) {
			UWReturn += value;
		}
		
		public void incrementSUWReturn(int value) {
			SUWReturn += value;
		}
	} 
	
	class QuotationProcessingTimeByProfile{
		private Quotation quotation;
		private List<QuotationStatusHistory> quotationStatusHistories;
		private Map<Long, Long> timing;
						
		public QuotationProcessingTimeByProfile(Quotation quotation){
			this.quotation = quotation;
			this.quotationStatusHistories = getQuotationStatusHisotires2();
			timing = getTiming(this.quotation, this.quotationStatusHistories);
			quotationReturn = getQuotationReturn(quotationStatusHistories);
			//System.out.println("*************** UW time: "+timeToString(getUWTime()));
		}
		
		/**
		 * 
		 * @return processing time in milliseconds after management approval
		 */
		public long getPostApprovalProcessingTime(){			
			Date approvedDate = null;
			Date activatedDate = null;
			
			// TODO PYI
//			for(QuotationStatusHistory quotationStatusHistory: quotationStatusHistories){
//				if(quotationStatusHistory.getPreviousWkfStatus().equals(WkfQuotationStatus.APS) 
//						&& quotationStatusHistory.getWkfStatus().equals(WkfQuotationStatus.APV)){
//					approvedDate = quotationStatusHistory.getCreateDate();
//				}else if(quotationStatusHistory.getPreviousWkfStatus().equals(WkfQuotationStatus.PPO)
//						&& quotationStatusHistory.getWkfStatus().equals(WkfQuotationStatus.ACT)){
//					activatedDate = quotationStatusHistory.getCreateDate();
//				}
//			}
//			
			if(approvedDate == null && activatedDate == null)
				return 0;
			else if(approvedDate == null & activatedDate != null)
				return 0;
			else if(approvedDate != null && activatedDate == null)
				return DateUtils.today().getTime() - approvedDate.getTime();
			else			
				return activatedDate.getTime() - approvedDate.getTime();
			
		}
		
		private Map<Long, Long> getTiming(Quotation quotation, List<QuotationStatusHistory> quotationStatusHistories) {		
			Date start = quotation.getStartCreationDate() != null ? quotation
					.getStartCreationDate() : DateUtils.today();
			Map<Long, Long> timing = new HashMap<Long, Long>();
									
			for (QuotationStatusHistory quotationStatusHistory : quotationStatusHistories) {
				// TODO PYI
//				SecUser secUser = quotationStatusHistory.getUser();
//				Date end = quotationStatusHistory.getCreateDate();
//				
//				if (ProfileUtil.isPOS(secUser)) {
//					long time = MyNumberUtils.getLong(timing.get(FMProfile.CO));
//					long diff = end.getTime() - start.getTime();
//					
//					if (diff > 0) {
//						time += diff;
//					}
//					timing.put(FMProfile.CO, time);
//				} else {
//					long time = MyNumberUtils.getLong(timing.get(secUser.getDefaultProfile().getId()));
//					long diff = end.getTime() - start.getTime();
//					
//					if(diff > 0)
//						time += diff;
//					timing.put(secUser.getDefaultProfile().getId(), time);
//				}
//				
//				start = end;
			}
			return timing;
		}
		
		public long getPOSTime() {			
			return MyNumberUtils.getLong(timing.get(FMProfile.CO));
		}
		
		public long getUWTime() {
			return MyNumberUtils.getLong(timing.get(FMProfile.UW));
		}
				
		public long getUSTime() {
			return MyNumberUtils.getLong(timing.get(FMProfile.US));
		}
				
		public long getMATime() {
			return MyNumberUtils.getLong(timing.get(FMProfile.MA));
		}
		
		public long getRFCTime() {
			
			return getRFCProcessingTime(quotationStatusHistories);
		}
		
		private long getRFCProcessingTime(List<QuotationStatusHistory> list){
			long time = 0l;
			int size = list.size();
			Date start = null;
			Date end = null;
			
			// TODO PYI
//			for(int i=0; i<size; i++){
//				QuotationStatusHistory status = (QuotationStatusHistory)list.get(i);
//				EWkfStatus previousQuotationStatus = status.getPreviousWkfStatus();
//				
//				if ((previousQuotationStatus.equals(WkfQuotationStatus.PRO) 
//						|| previousQuotationStatus.equals(WkfQuotationStatus.PRA) 
//						|| previousQuotationStatus.equals(WkfQuotationStatus.APU) 
//						|| previousQuotationStatus.equals(WkfQuotationStatus.AWU) 
//						|| previousQuotationStatus.equals(WkfQuotationStatus.RFC)
//					) && previousQuotationStatus.equals(WkfQuotationStatus.RFC)) {
//					start = status.getCreateDate();
//				}
//				
//				if (previousQuotationStatus.equals(WkfQuotationStatus.RFC)
//						&& (status.getWkfStatus().equals(WkfQuotationStatus.PRO) || status.getWkfStatus().equals(WkfQuotationStatus.DEC))){
//					end = status.getCreateDate();
//					
//					if(start != null){
//						time += (end.getTime() - start.getTime());
//						start = null;
//						end = null;
//					}
//				}
//			}
			
			if(start != null && end == null){
				time += DateUtils.today().getTime() - start.getTime();
			}
			
			return time;
		}
		
		private QuotationReturn getQuotationReturn(List<QuotationStatusHistory> list) {
			if (list != null && !list.isEmpty()) {
				QuotationReturn quotationReturn = new QuotationReturn();
				for (QuotationStatusHistory status : list) {
					// TODO PYI
//					EWkfStatus quotationStatus = status.getWkfStatus();
//					
//					if (ProfileUtil.isUnderwriter(status.getUser())) {
//						if (quotationStatus.equals(WkfQuotationStatus.QUO) || quotationStatus.equals(WkfQuotationStatus.RFC)) {
//							quotationReturn.incrementPOSReturn(1);
//						}
//					} else if (ProfileUtil.isUnderwriterSupervisor(status.getUser())) {
//						if (quotationStatus.equals(WkfQuotationStatus.QUO) || quotationStatus.equals(WkfQuotationStatus.RFC)) {
//							quotationReturn.incrementPOSReturn(1);
//						} else if (quotationStatus.equals(WkfQuotationStatus.PRO)) {
//							quotationReturn.incrementUWReturn(1);
//						}
//					} else if (ProfileUtil.isManager(status.getUser())) {
//						if (quotationStatus.equals(WkfQuotationStatus.PRO)) {
//							quotationReturn.incrementSUWReturn(1);
//							quotationReturn.incrementUWReturn(1);
//						}
//					}
				}
				return quotationReturn;
			}
			return null;
		}
	}
			
	/**
	 * 
	 * @param caption
	 * @return
	 */
	protected EntityPagedTable<Quotation> createPagedTable(String caption) {
		UWEntityPagedTable<Quotation> pagedTable = new UWEntityPagedTable<Quotation>(caption, createPagedDataProvider());
		pagedTable.buildGrid();
				
		pagedTable.addGeneratedColumn("POSProcessTime", new SimpleColumnGenerator());		
		pagedTable.addGeneratedColumn("UWProcessTime", new SimpleColumnGenerator());		
		pagedTable.addGeneratedColumn("USProcessTime", new SimpleColumnGenerator());		
		pagedTable.addGeneratedColumn("MAProcessTime", new SimpleColumnGenerator());		
		pagedTable.addGeneratedColumn("RFCProcessTime", new SimpleColumnGenerator());
		pagedTable.addGeneratedColumn("totalProcessTime", new SimpleColumnGenerator());
		pagedTable.addGeneratedColumn("UWProfilesProcessTime", new SimpleColumnGenerator());
		pagedTable.addGeneratedColumn("duration", new SimpleColumnGenerator());
		pagedTable.addGeneratedColumn("PostApprovalProcessTime", new SimpleColumnGenerator());
		
		pagedTable.setVisibleColumns(new Object[] {"firstSubmissionDate", "endDate", "applicant", "dealer.nameEn",
				"underwriter.desc", "underwriterSupervisor.desc", "manager.desc", "POSProcessTime",
				"UWProcessTime", "USProcessTime", "MAProcessTime", "RFCProcessTime",
				"RFC", "AWC", "totalProcessTime", "UWProfilesProcessTime", "result", "duration","PostApprovalProcessTime",
				"POSReturn", "UWReturn", "SUWReturn", "advancePayment", "employmentStatus", "housing" });
		
		
		return pagedTable;
	}
	
	public float getTimeInHour(long val){
		DecimalFormat fmt = new DecimalFormat("###.##");
	    float time = val/(1000*3600f);
	    try{
	       time = Float.valueOf(fmt.format(time)).floatValue();
	    }catch(Exception e){
	    	
	    }
		return time;
	}
	
	public float getFormatTimeInHour(String pattern, long val){
		if(pattern == null)
			pattern = "###.##";
		DecimalFormat fmt = new DecimalFormat(pattern);
	    float time = val/(1000*3600f);
	    try{
	       time = Float.valueOf(fmt.format(time)).floatValue();
	    }catch(Exception e){
	    	
	    }
		return time;
	}
}

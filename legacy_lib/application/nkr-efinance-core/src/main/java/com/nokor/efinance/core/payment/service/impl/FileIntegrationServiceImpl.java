package com.nokor.efinance.core.payment.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.seuksa.frmk.model.entity.MainEntity;
import org.seuksa.frmk.service.impl.MainEntityServiceImpl;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.seuksa.frmk.tools.exception.DaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nokor.efinance.core.collection.model.EPaymentChannel;
import com.nokor.efinance.core.payment.dao.PaymentFileDao;
import com.nokor.efinance.core.payment.model.EPaymentFileFormat;
import com.nokor.efinance.core.payment.model.EPaymentMethod;
import com.nokor.efinance.core.payment.model.PaymentFile;
import com.nokor.efinance.core.payment.model.PaymentFileItem;
import com.nokor.efinance.core.payment.service.FileIntegrationService;
import com.nokor.efinance.core.payment.service.PaymentFileItemRestriction;
import com.nokor.frmk.vaadin.util.i18n.I18N;

/**
 * @author bunlong.taing
 */
@Service("fileIntegrationService")
public class FileIntegrationServiceImpl extends MainEntityServiceImpl implements FileIntegrationService {
	/** */
	private static final long serialVersionUID = 2424767742940704070L;
	
	private static final String FORMATE_TIME = "HHmmss";
	
	@Autowired
	private PaymentFileDao dao;
	
	/**
	 */
	public FileIntegrationServiceImpl() {
	}
	
	/**
	 * @see org.seuksa.frmk.service.impl.MainEntityServiceImpl#getDao()
	 */
	@Override
	public PaymentFileDao getDao() {
		return dao;
	}
	
	/**
	 * @see org.seuksa.frmk.service.impl.MainEntityServiceImpl#createProcess(org.seuksa.frmk.model.entity.MainEntity)
	 */
	@Override
	public void createProcess(MainEntity mainEntity) throws DaoException {
		super.createProcess(mainEntity);
	}
	
	/**
	 * @see org.seuksa.frmk.service.impl.MainEntityServiceImpl#updateProcess(org.seuksa.frmk.model.entity.MainEntity)
	 */
	@Override
	public void updateProcess(MainEntity mainEntity) throws DaoException {
		super.updateProcess(mainEntity);
	}
	
	/**
	 * @see org.seuksa.frmk.service.impl.MainEntityServiceImpl#deleteProcess(org.seuksa.frmk.model.entity.MainEntity)
	 */
	@Override
	public void deleteProcess(MainEntity mainEntity) throws DaoException {
		throwIntoRecycledBin(mainEntity);
	}

	/**
	 * @see com.nokor.efinance.core.payment.service.FileIntegrationService#integrateFilePayment(java.lang.String, com.nokor.efinance.core.payment.model.EPaymentFileFormat)
	 */
	@Override
	public void integrateFilePayment(String fileName, EPaymentFileFormat fileFormat) {
		if (StringUtils.isEmpty(fileName) || fileFormat == null) {
			return;
		}
		
		BufferedReader br = null;
		try {
			PaymentFile paymentFile = PaymentFile.createInstance();
			paymentFile.setFormat(fileFormat);
			List<PaymentFileItem> paymentFileItems = new ArrayList<>();

			br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(fileName)), "UTF8"));
			String curLine;
			while ((curLine = br.readLine()) != null) {
				if (isHeader(curLine)) {
					fillPaymentFileHeader(paymentFile, curLine, fileFormat);
				} else if (isDetail(curLine)) {
					PaymentFileItem item = fillPaymentFileItem(paymentFile, curLine);
					paymentFileItems.add(item);
				} else if (isFooter(curLine)) {
					fillPaymentFileFooter(paymentFile, curLine, fileFormat);
				}
			}
			saveOrUpdate(paymentFile);
			if (!paymentFileItems.isEmpty()) {
				for (PaymentFileItem paymentFileItem : paymentFileItems) {
					saveOrUpdate(paymentFileItem);
				}
			}
		} catch (IOException e) {
			String msg = "Error in Integration file name: " + fileName;
			logger.debug(msg, e);
			throw new IllegalStateException(msg, e);
		} finally {
			try {
				if (br != null) {
					br.close();
				}
			} catch (IOException ex) {
				String msg = "Error in Integration file name: " + fileName;
				logger.debug(msg, ex);
			}
		}
	}
	
	/**
	 * @see com.nokor.efinance.core.payment.service.FileIntegrationService#validateFileFormat(java.lang.String, com.nokor.efinance.core.payment.model.EPaymentFileFormat)
	 */
	@Override
	public String validateFileFormat(String fileName, EPaymentFileFormat fileFormat) {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(fileName)), "UTF8"));
			String curLine = br.readLine();
			String detailCode = StringUtils.EMPTY;
			
			String formatHDT = StringUtils.EMPTY;
			do { 
		    	if (StringUtils.isNotEmpty(curLine)) {
		    		if (formatHDT.length() == 0) {
						if ("H".equals(curLine.substring(0, 1))) {
							formatHDT += curLine.substring(0, 1);
						}
					} else if (formatHDT.length() == 1) {
						if ("D".equals(curLine.substring(0, 1))) {
							formatHDT += curLine.substring(0, 1);
						}
					} else if (formatHDT.length() == 2) {
						if ("T".equals(curLine.substring(0, 1))) {
							formatHDT += curLine.substring(0, 1);
						}
					}
		    		detailCode = curLine.substring(0, 1);
		    	}
		    	if (!isCorrectedFormat(curLine, fileFormat, detailCode)) {
					return I18N.message("msg.wrong.batch.file.format", new String[] { fileFormat.getCode() });
				} 
		    } while((curLine = br.readLine()) != null);
			if (!"HDT".equals(formatHDT)) {
				return I18N.message("msg.wrong.batch.file.format", new String[] { fileFormat.getCode() });
	    	}
		} catch (IOException e) {
			String msg = "Error in Integration file name: " + fileName;
			logger.debug(msg, e);
			throw new IllegalStateException(msg, e);
		} finally {
			try {
				if (br != null) {
					br.close();
				}
			} catch (IOException ex) {
				String msg = "Error in Integration file name: " + fileName;
				logger.debug(msg, ex);
			}
		}
		return StringUtils.EMPTY;
	}
	
	/**
	 * 
	 * @param line
	 * @param fileFormat
	 * @param detailCode
	 * @return
	 */
	private boolean isCorrectedFormat(String line, EPaymentFileFormat fileFormat, String detailCode) {
		String bankCode = StringUtils.EMPTY;
		if (EPaymentFileFormat.BAY.equals(fileFormat)) {
			bankCode = "025";
		} else if (EPaymentFileFormat.SCB.equals(fileFormat)) {
			bankCode = "014";
		} else if (EPaymentFileFormat.SCIB.equals(fileFormat)) {
			bankCode = "065";
		} else if (EPaymentFileFormat.PAP.equals(fileFormat)) {
			bankCode = "TP";
		} else if (EPaymentFileFormat.CSV.equals(fileFormat)) {
			bankCode = "024";
		} else if (EPaymentFileFormat.GSB.equals(fileFormat)) {
			bankCode = "030";
		} else if (EPaymentFileFormat.LOT.equals(fileFormat)) {
			bankCode = "";
		} else if (EPaymentFileFormat.TMN.equals(fileFormat)) {
			bankCode = "TMN";
		} else if (EPaymentFileFormat.KBK.equals(fileFormat)) {
			bankCode = "004";
		} else if (EPaymentFileFormat.BAA.equals(fileFormat)) {
			bankCode = "034";
		}
		if (isHeader(line)) {
			if (isCorrectHeaderLength(line, fileFormat) && StringUtils.trim(bankCode).equals(StringUtils.trim(line.substring(7, 10)))) {
				return true;
			}
		} else if (isDetail(line)) {
			if (isCorrectDetailLength(line, fileFormat)) {
				return true;
			}
		} else if (isFooter(line)) {
			if (isCorrectFooterLength(line, fileFormat)) {
				return true;
			}
		} else {
			if (StringUtils.isEmpty(detailCode) || "D".equals(detailCode) || "H".equals(detailCode)) {
				return false;
			}
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @param line
	 * @param fileFormat
	 * @return
	 */
	private boolean isCorrectHeaderLength(String line, EPaymentFileFormat fileFormat) {
		if (EPaymentFileFormat.BAY.equals(fileFormat)
				|| EPaymentFileFormat.SCB.equals(fileFormat)
				|| EPaymentFileFormat.CSV.equals(fileFormat)
				|| EPaymentFileFormat.GSB.equals(fileFormat)
				|| EPaymentFileFormat.LOT.equals(fileFormat)
				|| EPaymentFileFormat.TMN.equals(fileFormat)
				|| EPaymentFileFormat.KBK.equals(fileFormat)
				|| EPaymentFileFormat.SCIB.equals(fileFormat)
				|| EPaymentFileFormat.BAA.equals(fileFormat)) {
			if (256 == line.length()) {
				return true;
			}
		} else if (EPaymentFileFormat.PAP.equals(fileFormat)) {
			if (158 == line.length()) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param line
	 * @param fileFormat
	 * @return
	 */
	private boolean isCorrectDetailLength(String line, EPaymentFileFormat fileFormat) {
		if (EPaymentFileFormat.BAY.equals(fileFormat)
				|| EPaymentFileFormat.SCB.equals(fileFormat)
				|| EPaymentFileFormat.CSV.equals(fileFormat)
				|| EPaymentFileFormat.GSB.equals(fileFormat)
				|| EPaymentFileFormat.LOT.equals(fileFormat)
				|| EPaymentFileFormat.TMN.equals(fileFormat)
				|| EPaymentFileFormat.KBK.equals(fileFormat)
				|| EPaymentFileFormat.BAA.equals(fileFormat)) {
			if (256 == line.length()) {
				return true;
			}
		} else if (EPaymentFileFormat.SCIB.equals(fileFormat)) {
			if (127 == line.length()) {
				return true;
			}
		} else if (EPaymentFileFormat.PAP.equals(fileFormat)) {
			if (158 == line.length()) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param line
	 * @param fileFormat
	 * @return
	 */
	private boolean isCorrectFooterLength(String line, EPaymentFileFormat fileFormat) {
		if (EPaymentFileFormat.BAY.equals(fileFormat)
				|| EPaymentFileFormat.SCB.equals(fileFormat)
				|| EPaymentFileFormat.CSV.equals(fileFormat)
				|| EPaymentFileFormat.GSB.equals(fileFormat)
				|| EPaymentFileFormat.LOT.equals(fileFormat)
				|| EPaymentFileFormat.TMN.equals(fileFormat)
				|| EPaymentFileFormat.KBK.equals(fileFormat)
				|| EPaymentFileFormat.BAA.equals(fileFormat)) {
			if (256 == line.length()) {
				return true;
			}
		} else if (EPaymentFileFormat.SCIB.equals(fileFormat)) {
			if (33 == line.length()) {
				return true;
			}
		} else if (EPaymentFileFormat.PAP.equals(fileFormat)) {
			if (158 == line.length()) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param file
	 * @param line
	 * @param fileFormat
	 */
	private void fillPaymentFileHeader(PaymentFile file, String line, EPaymentFileFormat fileFormat) {
		if (StringUtils.isNotEmpty(line)) {
			if (EPaymentFileFormat.BAY.equals(fileFormat)
					|| EPaymentFileFormat.SCB.equals(fileFormat)
					|| EPaymentFileFormat.CSV.equals(fileFormat)
					|| EPaymentFileFormat.GSB.equals(fileFormat)
					|| EPaymentFileFormat.LOT.equals(fileFormat)
					|| EPaymentFileFormat.TMN.equals(fileFormat)
					|| EPaymentFileFormat.KBK.equals(fileFormat)) {
				
				fillCommonPaymentFileHeader(file, line);
			} else if (EPaymentFileFormat.SCIB.equals(fileFormat)) {
				fillScibPaymentFileHeader(file, line);
			} else if (EPaymentFileFormat.PAP.equals(fileFormat)) {
				fillPapPaymentFileHeader(file, line);
			} else if (EPaymentFileFormat.BAA.equals(fileFormat)) {
				fillBaaPaymentFileHeader(file, line);
			}
		}
	}
	
	/**
	 * @param file
	 * @param line
	 * @return
	 */
	private PaymentFileItem fillPaymentFileItem(PaymentFile file, String line) {
		if (StringUtils.isEmpty(line)) {
			return null;
		}
		PaymentFileItem item = PaymentFileItem.createInstance();
		item.setPaymentFile(file);
		
		if (EPaymentFileFormat.BAY.equals(file.getFormat())) {
			fillBayPaymentFileItem(item, line);
		} else if (EPaymentFileFormat.SCB.equals(file.getFormat())) {
			fillScbPaymentFileItem(item, line);
		} else if (EPaymentFileFormat.SCIB.equals(file.getFormat())) {
			fillScibPaymentFileItem(item, line);
		} else if (EPaymentFileFormat.PAP.equals(file.getFormat())) {
			fillPapPaymentFileItem(item, line);
		} else if (EPaymentFileFormat.CSV.equals(file.getFormat())
				|| EPaymentFileFormat.LOT.equals(file.getFormat())) {
			
			fillCommonPaymentFileItem(item, line, true);
		} else if (EPaymentFileFormat.GSB.equals(file.getFormat())
				|| EPaymentFileFormat.BAA.equals(file.getFormat())) {
			
			fillCommonPaymentFileItem(item, line, false);
		} else if (EPaymentFileFormat.TMN.equals(file.getFormat())) {
			fillTmnPaymentFileItem(item, line);
		} else if (EPaymentFileFormat.KBK.equals(file.getFormat())) {
			fillKbkPaymentFileItem(item, line);
		}
		return item;
	}
	
	/**
	 * @param file
	 * @param line
	 * @param fileFormat
	 */
	private void fillPaymentFileFooter(PaymentFile file, String line, EPaymentFileFormat fileFormat) {
		if (StringUtils.isNotEmpty(line)) {
			if (EPaymentFileFormat.BAY.equals(fileFormat)
					|| EPaymentFileFormat.GSB.equals(fileFormat)
					|| EPaymentFileFormat.KBK.equals(fileFormat)
					|| EPaymentFileFormat.BAA.equals(fileFormat)) {
				
				fileCommonPaymentFileFooter(file, line, false);
			} else if (EPaymentFileFormat.SCB.equals(fileFormat)
					|| EPaymentFileFormat.CSV.equals(fileFormat)
					|| EPaymentFileFormat.LOT.equals(fileFormat)
					|| EPaymentFileFormat.TMN.equals(fileFormat)) {
				
				fileCommonPaymentFileFooter(file, line, true);
			} else if (EPaymentFileFormat.SCIB.equals(fileFormat)) {
				fileScibPaymentFileFooter(file, line);
			} else if (EPaymentFileFormat.PAP.equals(fileFormat)) {
				filePapPaymentFileFooter(file, line);
			}
		}
	}
	
	/**
	 * File Common Payment File Footer
	 * For BAY, SCB
	 * @param file
	 * @param line
	 */
	private void fileCommonPaymentFileFooter(PaymentFile file, String line, boolean isSpare) {
		file.setLastSequence(StringUtils.trim(getSubstring(line, 1, 7)));
		file.setFooterBankCode(StringUtils.trim(getSubstring(line, 7, 10)));
		file.setFooterCompanyAccount(StringUtils.trim(getSubstring(line, 10, 20)));
		file.setTotalDebitAmount(MyNumberUtils.getDouble(getSubstringDecimal(line, 20, 33, 2), 0));
		file.setTotalDebitTransaction(MyNumberUtils.getInteger(getSubstring(line, 33, 39), 0));
		file.setTotalCreditAmount(MyNumberUtils.getDouble(getSubstringDecimal(line, 39, 52, 2), 0));
		file.setTotalCreditTransaction(MyNumberUtils.getInteger(getSubstring(line, 52, 58), 0));
		
		if (!isSpare) {
			file.setFilter(StringUtils.trim(getSubstring(line, 58, 256)));
		}
	}
	
	/**
	 * File SCIB Payment File Footer
	 * @param file
	 * @param line
	 */
	private void fileScibPaymentFileFooter(PaymentFile file, String line) {
		file.setLastSequence(StringUtils.trim(getSubstring(line, 1, 7)));
		file.setFooterBankCode(StringUtils.trim(getSubstring(line, 7, 10)));
		file.setFixCode(StringUtils.trim(getSubstring(line, 10, 20)));
		file.setTotalAmount(MyNumberUtils.getDouble(getSubstringDecimal(line, 20, 33, 2), 0));
	}
	
	/**
	 * File PAP Payment File Footer
	 * @param file
	 * @param line
	 */
	private void filePapPaymentFileFooter(PaymentFile file, String line) {
		file.setLastSequence(StringUtils.trim(getSubstring(line, 1, 7)));
		file.setFooterBankCode(StringUtils.trim(getSubstring(line, 7, 10)));
		file.setFooterCompanyAccount(StringUtils.trim(getSubstring(line, 10, 20)));
		file.setTotalAmount(MyNumberUtils.getDouble(getSubstringDecimal(line, 20, 33, 2), 0));
		file.setTotalTransaction(MyNumberUtils.getInteger(getSubstring(line, 33, 39), 0));
		file.setFilter(StringUtils.trim(getSubstring(line, 39, 158)));
	}
	
	/**
	 * Fill Common Payment File Header
	 * For BAY, SCB, CSV, GSB, LOT, TMN, KBK
	 * @param file
	 * @param line
	 */
	private void fillCommonPaymentFileHeader(PaymentFile file, String line) {
		file.setSequence(StringUtils.trim(getSubstring(line, 1, 7)));
		file.setBankCode(StringUtils.trim(getSubstring(line, 7, 10)));
		file.setCompanyAccount(StringUtils.trim(getSubstring(line, 10, 20)));
		file.setCompanyName(StringUtils.trim(getSubstring(line, 20, 60)));
		
		Date effectiveDate = DateUtils.string2DateDDMMYYYYNoSeparator(StringUtils.trim(getSubstring(line, 60, 68)));
		file.setEffectiveDate(effectiveDate);
		file.setServiceCode(StringUtils.trim(getSubstring(line, 68, 76)));
		file.setFilter(StringUtils.trim(getSubstring(line, 76, 256)));
	}
	
	/**
	 * Fill SCIB Payment File Header
	 * @param file
	 * @param line
	 */
	private void fillScibPaymentFileHeader(PaymentFile file, String line) {
		file.setSequence(StringUtils.trim(getSubstring(line, 1, 7)));
		file.setBankCode(StringUtils.trim(getSubstring(line, 7, 10)));
		file.setCompanyAccount(StringUtils.trim(getSubstring(line, 10, 20)));
		file.setCompanyName(StringUtils.trim(getSubstring(line, 20, 45)));
		
		Date effectiveDate = DateUtils.string2DateDDMMYYYYNoSeparator(StringUtils.trim(getSubstring(line, 45, 51)));
		file.setEffectiveDate(effectiveDate);
		file.setServiceCode(StringUtils.trim(getSubstring(line, 51, 54)));
	}
	
	/**
	 * Fill PAP Payment File Header
	 * @param file
	 * @param line
	 */
	private void fillPapPaymentFileHeader(PaymentFile file, String line) {
		file.setSequence(StringUtils.trim(getSubstring(line, 1, 7)));
		file.setBankCode(StringUtils.trim(getSubstring(line, 7, 10)));
		file.setCompanyAccount(StringUtils.trim(getSubstring(line, 10, 20)));
		file.setCompanyName(StringUtils.trim(getSubstring(line, 20, 60)));
		
		Date effectiveDate = DateUtils.string2DateDDMMYYYYNoSeparator(StringUtils.trim(getSubstring(line, 60, 68)));
		file.setEffectiveDate(effectiveDate);
		file.setServiceCode(StringUtils.trim(getSubstring(line, 68, 76)));
		file.setFilter(StringUtils.trim(getSubstring(line, 76, 158)));
	}
	
	/**
	 * Fill BAA Payment File Header
	 * @param file
	 * @param line
	 */
	private void fillBaaPaymentFileHeader(PaymentFile file, String line) {
		file.setSequence(StringUtils.trim(getSubstring(line, 1, 7)));
		file.setBankCode(StringUtils.trim(getSubstring(line, 7, 10)));
		file.setCompanyAccount(StringUtils.trim(getSubstring(line, 10, 20)));
		file.setCompanyName(StringUtils.trim(getSubstring(line, 20, 60)));
		
		Date effectiveDate = DateUtils.string2DateDDMMYYYYNoSeparator(StringUtils.trim(getSubstring(line, 60, 68)));
		file.setEffectiveDate(effectiveDate);
		file.setServiceCode(StringUtils.trim(getSubstring(line, 68, 76)));
		file.setCompanyAccountOptional(StringUtils.trim(getSubstring(line, 76, 88)));
		file.setFilter(StringUtils.trim(getSubstring(line, 88, 256)));
	}
	
	/**
	 * Fill Common Payment File Item
	 * For CSV, GSB, LOT, BAA
	 * @param item
	 * @param line
	 */
	private void fillCommonPaymentFileItem(PaymentFileItem item, String line, boolean isSpare) {
		item.setSequence(StringUtils.trim(getSubstring(line, 1, 7)));
		item.setBankCode(StringUtils.trim(getSubstring(line, 7, 10)));
		item.setCompanyAccount(StringUtils.trim(getSubstring(line, 10, 20)));
		
		String dateTime = StringUtils.trim(getSubstring(line, 20, 28) + getSubstring(line, 28, 34)); // Date + Time
		Date paymentDate = DateUtils.getDate(dateTime, DateUtils.FORMAT_STR_DDMMYYYY_NOSEP + FORMATE_TIME);
		item.setPaymentDate(paymentDate);
		
		item.setCustomerName(StringUtils.trim(getSubstring(line, 34, 84)));
		item.setCustomerRef1(StringUtils.trim(getSubstring(line, 84, 104)));
		item.setCustomerRef2(StringUtils.trim(getSubstring(line, 104, 124)));
		item.setCustomerRef3(StringUtils.trim(getSubstring(line, 124, 144)));
		item.setBranchNo(StringUtils.trim(getSubstring(line, 144, 148)));
		item.setTellerNo(StringUtils.trim(getSubstring(line, 148, 152)));
		item.setTransactionKind(StringUtils.trim(getSubstring(line, 152, 153)));
		item.setTransactionCode(StringUtils.trim(getSubstring(line, 153, 156)));	// BAA is Payment By
		item.setChequeNo(StringUtils.trim(getSubstring(line, 156, 163)));
		item.setAmount(MyNumberUtils.getDouble(getSubstringDecimal(line, 163, 176, 2), 0));
		item.setChequeBankCode(StringUtils.trim(getSubstring(line, 176, 179)));
		// Spare 179-256
		if (!isSpare) {
			item.setFilter1(StringUtils.trim(getSubstring(line, 179, 256)));
		}
	}
	
	/**
	 * Fill BAY Payment File Item
	 * @param item
	 * @param line
	 */
	private void fillBayPaymentFileItem(PaymentFileItem item, String line) {
		item.setSequence(StringUtils.trim(getSubstring(line, 1, 7)));
		item.setBankCode(StringUtils.trim(getSubstring(line, 7, 10)));
		item.setCompanyAccount(StringUtils.trim(getSubstring(line, 10, 20)));
		
		String dateTime = StringUtils.trim(getSubstring(line, 20, 28)) + StringUtils.trim(getSubstring(line, 28, 34)); // Date + Time
		Date paymentDate = DateUtils.getDate(dateTime, DateUtils.FORMAT_STR_DDMMYYYY_NOSEP + FORMATE_TIME);
		item.setPaymentDate(paymentDate);
		
		item.setCustomerName(StringUtils.trim(getSubstring(line, 34, 84)));
		item.setCustomerRef1(StringUtils.trim(getSubstring(line, 84, 104)));
		item.setCustomerRef2(StringUtils.trim(getSubstring(line, 104, 124)));
		item.setCustomerRef3(StringUtils.trim(getSubstring(line, 124, 144)));
		item.setBranchNo(StringUtils.trim(getSubstring(line, 144, 148)));
		item.setTellerNo(StringUtils.trim(getSubstring(line, 148, 152)));
		item.setTransactionKind(StringUtils.trim(getSubstring(line, 152, 153)));
		item.setTransactionCode(StringUtils.trim(getSubstring(line, 153, 156)));
		item.setChequeNo(StringUtils.trim(getSubstring(line, 156, 163)));
		item.setAmount(MyNumberUtils.getDouble(getSubstringDecimal(line, 163, 176, 2), 0));
		item.setChequeBankCode(StringUtils.trim(getSubstring(line, 176, 179)));
		item.setChequeBranchCode(StringUtils.trim(getSubstring(line, 179, 183)));
		item.setFilter1(StringUtils.trim(getSubstring(line, 183, 195)));
		item.setChequeNoNew(StringUtils.trim(getSubstring(line, 195, 203)));
		item.setFilter2(StringUtils.trim(getSubstring(line, 203, 246)));
		item.setChequeNoNew1(StringUtils.trim(getSubstring(line, 246, 256)));
		item.setPaymentChannel(EPaymentChannel.BAY);
		item.setPaymentMethod(EPaymentMethod.TRANSFER);
	}
	
	/**
	 * Fill SCB Payment File Item
	 * @param item
	 * @param line
	 */
	private void fillScbPaymentFileItem(PaymentFileItem item, String line) {
		item.setSequence(StringUtils.trim(getSubstring(line, 1, 7)));
		item.setBankCode(StringUtils.trim(getSubstring(line, 7, 10)));
		item.setCompanyAccount(StringUtils.trim(getSubstring(line, 10, 20)));
		
		String dateTime = StringUtils.trim(getSubstring(line, 20, 28)) + StringUtils.trim(getSubstring(line, 28, 34)); // Date + Time
		Date paymentDate = DateUtils.getDate(dateTime, DateUtils.FORMAT_STR_DDMMYYYY_NOSEP + FORMATE_TIME);
		item.setPaymentDate(paymentDate);

		item.setCustomerName(StringUtils.trim(getSubstring(line, 34, 84)));
		item.setCustomerRef1(StringUtils.trim(getSubstring(line, 84, 104)));
		item.setCustomerRef2(StringUtils.trim(getSubstring(line, 104, 124)));
		item.setCustomerRef3(StringUtils.trim(getSubstring(line, 124, 144)));
		item.setBranchNo(StringUtils.trim(getSubstring(line, 144, 148)));
		item.setTellerNo(StringUtils.trim(getSubstring(line, 148, 152)));
		item.setTransactionKind(StringUtils.trim(getSubstring(line, 152, 153)));
		item.setTransactionCode(StringUtils.trim(getSubstring(line, 153, 156)));
		item.setChequeNo(StringUtils.trim(getSubstring(line, 156, 163)));
		item.setAmount(MyNumberUtils.getDouble(getSubstringDecimal(line, 163, 176, 2), 0));
		// Spare 177-246
		item.setChequeNoNew1(StringUtils.trim(getSubstring(line, 246, 256)));
	}
	
	/**
	 * Fill SCIB Payment File Item
	 * @param item
	 * @param line
	 */
	private void fillScibPaymentFileItem(PaymentFileItem item, String line) {
		item.setSequence(StringUtils.trim(getSubstring(line, 1, 7)));
		item.setBankCode(StringUtils.trim(getSubstring(line, 7, 10)));
		item.setAmount(MyNumberUtils.getDouble(getSubstringDecimal(line, 10, 21, 2), 0));
		item.setBranchNo(StringUtils.trim(getSubstring(line, 21, 25)));
		item.setTransactionCode(StringUtils.trim(getSubstring(line, 25, 28)));
		item.setFixCode(StringUtils.trim(getSubstring(line, 28, 47)));
		item.setCustomerName(StringUtils.trim(getSubstring(line, 47, 87)));
		item.setCustomerRef1(StringUtils.trim(getSubstring(line, 87, 107)));
		item.setCustomerRef2(StringUtils.trim(getSubstring(line, 107, 127)));
	}
	
	/**
	 * Fill PAP Payment File Item
	 * @param item
	 * @param line
	 */
	private void fillPapPaymentFileItem(PaymentFileItem item, String line) {
		item.setSequence(StringUtils.trim(getSubstring(line, 1, 7)));
		item.setBankCode(StringUtils.trim(getSubstring(line, 7, 10)));
		
		Date paymentDate = DateUtils.string2DateDDMMYYYYNoSeparator(StringUtils.trim(getSubstring(line, 10, 18)));
		item.setPaymentDate(paymentDate);
		item.setCustomerId(StringUtils.trim(getSubstring(line, 18, 38)));
		item.setCustomerRef1(StringUtils.trim(getSubstring(line, 38, 58)));
		item.setCustomerName(StringUtils.trim(getSubstring(line, 58, 108)));
		item.setBranchNo(StringUtils.trim(getSubstring(line, 108, 113)));	// Post Office Id
		item.setPostCode(StringUtils.trim(getSubstring(line, 113, 126)));
		item.setTransactionCode(StringUtils.trim(getSubstring(line, 126, 135)));
		item.setReceiveNo(StringUtils.trim(getSubstring(line, 135, 144)));
		item.setAmount(MyNumberUtils.getDouble(getSubstringDecimal(line, 144, 157, 2), 0));
		item.setFilter1(StringUtils.trim(getSubstring(line, 157, 158)));
	}
	
	/**
	 * Fill TMN Payment File Item
	 * @param item
	 * @param line
	 */
	private void fillTmnPaymentFileItem(PaymentFileItem item, String line) {
		item.setSequence(StringUtils.trim(getSubstring(line, 1, 7)));
		item.setBankCode(StringUtils.trim(getSubstring(line, 7, 10)));
		item.setCompanyAccount(StringUtils.trim(getSubstring(line, 10, 20)));
		
		String dateTime = StringUtils.trim(getSubstring(line, 20, 28)) + StringUtils.trim(getSubstring(line, 28, 34)); // Date + Time
		Date paymentDate = DateUtils.getDate(dateTime, DateUtils.FORMAT_STR_DDMMYYYY_NOSEP + FORMATE_TIME);
		item.setPaymentDate(paymentDate);
		
		item.setCustomerName(StringUtils.trim(getSubstring(line, 34, 84)));
		item.setCustomerRef1(StringUtils.trim(getSubstring(line, 84, 104)));
		item.setCustomerRef2(StringUtils.trim(getSubstring(line, 104, 124)));
		item.setCustomerRef3(StringUtils.trim(getSubstring(line, 124, 144)));
		item.setBranchNo(StringUtils.trim(getSubstring(line, 144, 148)));
		item.setTellerNo(StringUtils.trim(getSubstring(line, 148, 152)));
		item.setTransactionKind(StringUtils.trim(getSubstring(line, 152, 153)));
		item.setTransactionCode(StringUtils.trim(getSubstring(line, 153, 156)));
		item.setChequeNo(StringUtils.trim(getSubstring(line, 156, 163)));
		item.setAmount(MyNumberUtils.getDouble(getSubstringDecimal(line, 163, 176, 2), 0));
		// Spare 176-256
	}
	
	/**
	 * Fill KBK Payment File Item
	 * @param item
	 * @param line
	 */
	private void fillKbkPaymentFileItem(PaymentFileItem item, String line) {
		item.setSequence(StringUtils.trim(getSubstring(line, 1, 7)));
		item.setBankCode(StringUtils.trim(getSubstring(line, 7, 10)));
		item.setCompanyAccount(StringUtils.trim(getSubstring(line, 10, 20)));
		
		String dateTime = StringUtils.trim(getSubstring(line, 20, 28)) + StringUtils.trim(getSubstring(line, 28, 34)); // Date + Time
		Date paymentDate = DateUtils.getDate(dateTime, DateUtils.FORMAT_STR_DDMMYYYY_NOSEP + FORMATE_TIME);
		item.setPaymentDate(paymentDate);
		
		item.setCustomerName(StringUtils.trim(getSubstring(line, 34, 84)));
		item.setCustomerRef1(StringUtils.trim(getSubstring(line, 84, 104)));
		item.setCustomerRef2(StringUtils.trim(getSubstring(line, 104, 124)));
		item.setCustomerRef3(StringUtils.trim(getSubstring(line, 124, 144)));
		item.setBranchNo(StringUtils.trim(getSubstring(line, 144, 148)));
		item.setTellerNo(StringUtils.trim(getSubstring(line, 148, 152)));
		item.setTransactionKind(StringUtils.trim(getSubstring(line, 152, 153)));
		item.setTransactionCode(StringUtils.trim(getSubstring(line, 153, 156)));
		item.setChequeNo(StringUtils.trim(getSubstring(line, 156, 163)));
		item.setAmount(MyNumberUtils.getDouble(getSubstringDecimal(line, 163, 176, 2), 0));
		item.setChequeBankCode(StringUtils.trim(getSubstring(line, 176, 179)));
		item.setPayeeFeeSameZone(StringUtils.trim(getSubstring(line, 179, 187)));
		item.setPayeeFeeDiffZone(StringUtils.trim(getSubstring(line, 187, 195)));
		item.setFilter1(StringUtils.trim(getSubstring(line, 195, 246)));
		item.setChequeNoNew(StringUtils.trim(getSubstring(line, 246, 256)));
	}
	
	/**
	 * @param value
	 * @param start
	 * @param end
	 * @return
	 */
	private String getSubstring(String value, int start, int end) {
		if (start > value.length() -1 || end > value.length()) {
			return null;
		}
		return value.substring(start, end);
	}
	
	/**
	 * 
	 * @param value
	 * @param start
	 * @param end
	 * @param decimal
	 * @return
	 */
	private String getSubstringDecimal(String value, int start, int end, int decimal) {
		String result = getSubstring(value, start, end);
		if (result == null) {
			return null;
		}
		int length = result.length();
		return result.substring(0, length - decimal) + "." + result.substring(length - decimal);
	}
	
	/**
	 * Is header
	 * @return
	 */
	private boolean isHeader(String line) {
		if (StringUtils.isNotEmpty(line)) {
			String headerCode = "H";
			if (headerCode.equals(line.substring(0, 1))) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Is detail
	 * @param line
	 * @return
	 */
	private boolean isDetail(String line) {
		if (StringUtils.isNotEmpty(line)) {
			String detailCode = "D";
			if (detailCode.equals(line.substring(0, 1))) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Is footer
	 * @param line
	 * @return
	 */
	private boolean isFooter(String line) {
		if (StringUtils.isNotEmpty(line)) {
			String detailCode = "T";
			if (detailCode.equals(line.substring(0, 1))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @see com.nokor.efinance.core.payment.service.FileIntegrationService#listPaymentFileItemByPaymentFile(java.lang.Long)
	 */
	@Override
	public List<PaymentFileItem> listPaymentFileItemByPaymentFile(Long id) {
		PaymentFileItemRestriction restrictions = new PaymentFileItemRestriction();
		restrictions.setPaymentFileId(id);
		return list(restrictions);
	}

}

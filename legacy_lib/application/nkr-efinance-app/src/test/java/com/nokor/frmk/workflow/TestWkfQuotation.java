package com.nokor.frmk.workflow;

import com.nokor.frmk.testing.BaseTestCase;


/**
 * @author prasnar
 * @version $Revision$
 */
public class TestWkfQuotation extends BaseTestCase {



    /**
     * 
     */
    public TestWkfQuotation() {
    }
    
    public void testCreateWkfFlowStatus() {
    	try {
    		
//    		List<EWkfStatus> statuses = QuotationWkfStatus.values();
//    		for (EWkfStatus sta : statuses) {
//				logger.info("Status [" + sta.getCode() + "] : " + sta.getDesc());
//			}
//	    	WkfFlowStatus floSta = EntityFactory.createInstance(WkfFlowStatus.class);
//	    	floSta.setStatus(WkfQuotationStatus.QUO);
//	    	floSta.setFlow(FinWkfFlow.QUOTATION);
//	    	ENTITY_SRV.create(floSta);
        	
    		logger.info("************SUCCESS**********");
        	
    	} catch (Exception e) {
    		logger.error(e.getMessage(), e);
    	}
    }
   
   

}

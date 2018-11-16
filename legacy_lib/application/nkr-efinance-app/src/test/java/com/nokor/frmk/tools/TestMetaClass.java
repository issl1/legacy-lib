package com.nokor.frmk.tools;

import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.asset.model.Asset;
import com.nokor.efinance.core.auction.model.Auction;
import com.nokor.efinance.core.collection.model.Collection;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.payment.model.Payment;
import com.nokor.efinance.core.quotation.model.Quotation;
import com.nokor.efinance.core.quotation.model.QuotationApplicant;
import com.nokor.frmk.testing.BaseTestCase;


/**
 * @author prasnar
 * @version $Revision$
 */
public class TestMetaClass extends BaseTestCase {


    /**
     * 
     */
    public TestMetaClass() {
        
    }
    
    /**
     * 
     * @return
     */
    protected boolean initSpring() {
    	return false;
    }
   
    /**
     *  
     */
    public void testClass() {
    	generateMetaClass(Contract.class, "D:/");
    	generateMetaClass(Payment.class, "D:/");
    	generateMetaClass(Quotation.class, "D:/");
    	generateMetaClass(Collection.class, "D:/");
    	generateMetaClass(Auction.class, "D:/");
    	generateMetaClass(Dealer.class, "D:/");
    	generateMetaClass(Asset.class, "D:/");
    	generateMetaClass(Applicant.class, "D:/");
    	generateMetaClass(QuotationApplicant.class, "D:/");
    }
    

    
   
}

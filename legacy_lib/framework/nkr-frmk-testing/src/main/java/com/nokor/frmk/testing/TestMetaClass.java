package com.nokor.frmk.testing;

import com.nokor.ersys.core.hr.model.organization.Employee;



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
    	generateMetaClass(Employee.class, "D:/");
//    	generateMetaClass(ESecPage.class, "D:/");
//    	generateMetaClass(ESecProfile.class, "D:/");
//    	generateMetaClass(SecUser.class, "D:/");
//    	
//    	generateMetaClass(ESecForm.class, "D:/");
//    	generateMetaClass(ESecAction.class, "D:/");
//    	generateMetaClass(ESecOther.class, "D:/");
    	
    }
    

    
   
}

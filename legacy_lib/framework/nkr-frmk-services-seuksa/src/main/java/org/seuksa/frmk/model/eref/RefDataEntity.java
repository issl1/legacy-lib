package org.seuksa.frmk.model.eref;

import org.seuksa.frmk.model.entity.EntityA;
import org.seuksa.frmk.model.entity.MyDataId;


/**
 * 
 * @author prasnar
 *
 */
public interface RefDataEntity <T extends EntityA> extends MyDataId {

    T getEntity();

}

package org.seuksa.frmk.model.entity;

import java.util.List;

/**
 * Is a secured Entity
 * 
 * @author prasnar
 * @version $Revision$
 */
public interface ISecuredEntity {
    List<String> getEncryptedProperties();
}

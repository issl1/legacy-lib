package org.seuksa.frmk.model.entity;


/**
 * This class is table reference which used a string Code as Id (primary key)
 * 
 * @author prasnar
 * @version $Revision$
 */
public abstract class EntityRefS extends EntityS {
    /** */
	private static final long serialVersionUID = 6293225586824576485L;
	protected String desc;

    /**
     * 
     * @param clazz
     * @param code
     * @return
     * @throws Exception
     */
    public static EntityRefS NewInstance(Class<? extends EntityRefS> clazz, String code) throws Exception {
        Class<EntityRefS> entity = (Class<EntityRefS>) Class.forName(clazz.getName());
        EntityRefS entityRef = entity.newInstance();
        entityRef.setCode(code);
        return entityRef;
    }

    /**
     * 
     * @return
     */
    public abstract String getDesc();

    /**
     * @param desc the desc to set
     */
    public void setDesc(String desc) {
        this.desc = desc;
    }

  
}

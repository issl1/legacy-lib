package com.nokor.common.app.eref;

import org.seuksa.frmk.model.eref.BaseERefData;

import javax.persistence.AttributeConverter;
import java.util.List;

/**
 * Created by ki.kao on 6/21/2017.
 */
public class EProductLineCode extends BaseERefData implements AttributeConverter<EProductLineCode, Long> {

    private static final long serialVersionUID = -6543711325864922643L;

    public final static EProductLineCode MFP = new EProductLineCode("MFP", 1); // Moto for plus
    public final static EProductLineCode KFP = new EProductLineCode("KFP", 2); // Kubuta for plus
    public final static EProductLineCode EFINANCE = new EProductLineCode("EFINANCE", 3); // Efinance
    public final static EProductLineCode SOLAR = new EProductLineCode("SOLAR", 4); // Efinance

    /**
     *
     */
    public EProductLineCode() {
    }

    /**
     * @param code
     * @param id
     */
    public EProductLineCode(String code, long id) {
        super(code, id);
    }

    @SuppressWarnings("unchecked")
    @Override
    public EProductLineCode convertToEntityAttribute(Long id) {
        return super.convertToEntityAttribute(id);
    }

    @Override
    public Long convertToDatabaseColumn(EProductLineCode arg0) {
        return super.convertToDatabaseColumn(arg0);
    }

    /**
     * @return
     */
    public static List<EProductLineCode> values() {
        return getValues(EProductLineCode.class);
    }

    /**
     * @param code
     * @return
     */
    public static EProductLineCode getByCode(String code) {
        return getByCode(EProductLineCode.class, code);
    }

    /**
     * @param id
     * @return
     */
    public static EProductLineCode getById(long id) {
        return getById(EProductLineCode.class, id);
    }
}
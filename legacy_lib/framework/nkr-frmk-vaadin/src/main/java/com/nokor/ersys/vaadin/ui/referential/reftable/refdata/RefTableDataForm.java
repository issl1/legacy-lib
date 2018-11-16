/*
 * Created on 21/05/2015.
 */
package com.nokor.ersys.vaadin.ui.referential.reftable.refdata;

import com.nokor.common.app.cfield.model.ECusType;
import com.nokor.common.app.tools.helper.AppServicesHelper;
import com.nokor.ersys.core.finance.model.eref.ECurrency;
import com.nokor.frmk.config.model.ERefType;
import com.nokor.frmk.config.model.RefData;
import com.nokor.frmk.config.model.RefTable;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.component.NumberField;
import com.vaadin.ui.*;
import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.seuksa.frmk.model.entity.EntityRefA;
import org.seuksa.frmk.model.entity.RefDataId;
import org.seuksa.frmk.model.eref.BaseERefEntity;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * RefTable Data Form.
 *
 * @author pengleng.huot
 */
public class RefTableDataForm extends AbstractControlPanel implements IRefTableDataForm, AppServicesHelper {

    /** */
    private static final long serialVersionUID = 4255441718211900843L;

    private RefTable refTable;
    private RefData refData;
    private BaseERefEntity refEntity;
    private VerticalLayout messagePanel;

    private TextField txtCode;
    private TextField txtDesc;
    private TextField txtDescEn;
    private TextField txtSymbol;
    private NumberField txtSortIndex;
    private CheckBox cbActive;

    private Map<String, Component> fieldValues;
    private FormLayout fieldValueFormLayout;

    /**
     *
     */
    public RefTableDataForm(RefTable refTable) {
        super();

        this.refTable = refTable;
        initForm();
        addControlsToForm();
    }

    @Override
    public Component getForm() {
        return this;
    }

    /**
     * init form controls.
     */
    protected void initForm() {
        fieldValues = new HashMap<String, Component>();

        messagePanel = new VerticalLayout();
        messagePanel.setMargin(true);
        messagePanel.setVisible(false);
        messagePanel.addStyleName("message");

        txtCode = ComponentFactory.getTextField("code", true, 60, 200);
        txtDescEn = ComponentFactory.getTextField("desc.en", true, 60, 200);
        txtDesc = ComponentFactory.getTextField35("desc", false, 60, 200);
        txtSymbol = ComponentFactory.getNumberField("symbol", false, 10, 200);
        txtSortIndex = ComponentFactory.getNumberField("sort.index", false, 10, 200);
        cbActive = new CheckBox(I18N.message("active"));
        cbActive.setValue(true);
    }

    /**
     * Add controls into form.
     */
    protected void addControlsToForm() {
        VerticalLayout verticalLayout = new VerticalLayout();

        FormLayout formLayout = new FormLayout();
        formLayout.addComponent(txtCode);
        formLayout.addComponent(txtDesc);
        formLayout.addComponent(txtDescEn);
        if (refTable.getName().equals(ECurrency.class.getCanonicalName()))
            formLayout.addComponent(txtSymbol);
        formLayout.addComponent(txtSortIndex);
        fieldValueFormLayout = new FormLayout();

        verticalLayout.addComponent(formLayout);
        verticalLayout.addComponent(fieldValueFormLayout);

        formLayout = new FormLayout();
        formLayout.addComponent(cbActive);
        verticalLayout.addComponent(formLayout);

        addComponent(messagePanel);
        addComponent(verticalLayout);
    }

    /**
     * Assign values.
     *
     * @param refData
     */
    @Override
    public void assignValues(RefData refData) {
        reset();
        this.refData = refData;

        txtCode.setValue(refData.getCode());
        txtDescEn.setValue(refData.getDescEn());
        txtDesc.setValue(refData.getDesc());
        txtSortIndex.setValue(refData.getSortIndex() != null ? refData.getSortIndex().toString() : "");
        cbActive.setValue(refData.getStatusRecord().equals(EStatusRecord.ACTIV));

        RefTable refTable = refData.getTable();
        assignFieldValueControl(refTable.getFieldName1(), refTable.getCusType1(), refData.getFieldValue1());
        assignFieldValueControl(refTable.getFieldName2(), refTable.getCusType2(), refData.getFieldValue2());
        assignFieldValueControl(refTable.getFieldName3(), refTable.getCusType3(), refData.getFieldValue3());
        assignFieldValueControl(refTable.getFieldName4(), refTable.getCusType4(), refData.getFieldValue4());
        assignFieldValueControl(refTable.getFieldName5(), refTable.getCusType5(), refData.getFieldValue5());
    }

    public void assignValues(BaseERefEntity baseERefData, RefTable refTable) {
        reset();
        this.refEntity = baseERefData;

        txtCode.setValue(baseERefData.getCode());
        txtDescEn.setValue(baseERefData.getDescEn());
        txtDesc.setValue(baseERefData.getDesc());
        if (baseERefData instanceof ECurrency)
            txtSymbol.setValue(((ECurrency) baseERefData).getSymbol());
        txtSortIndex.setValue(baseERefData.getSortIndex() != null ? baseERefData.getSortIndex().toString() : "");
        cbActive.setValue(baseERefData.getStatusRecord() == null ? false : baseERefData.getStatusRecord().equals(EStatusRecord.ACTIV));

        assignFieldValueControl(refTable.getFieldName1(), refTable.getCusType1(), "");
        assignFieldValueControl(refTable.getFieldName2(), refTable.getCusType2(), "");
        assignFieldValueControl(refTable.getFieldName3(), refTable.getCusType3(), "");
        assignFieldValueControl(refTable.getFieldName4(), refTable.getCusType4(), "");
        assignFieldValueControl(refTable.getFieldName5(), refTable.getCusType5(), "");
    }

    /**
     * Assign value to Field Value Control by field name
     *
     * @param fieldName
     * @param cusType
     * @param value
     */
    private void assignFieldValueControl(String fieldName, ECusType cusType, String value) {
        if (StringUtils.isEmpty(fieldName) || cusType == null) {
            return;
        }
        Component fieldValue = fieldValues.get(fieldName);

        if (cusType.isString()) {
            ((TextField) fieldValue).setValue((String) cusType.getCusValue(value, null));
        } else if (cusType.isNumber()) {
            ((NumberField) fieldValue).setValue((String) cusType.getCusValue(value, null));
        } else if (cusType.isDate()) {
            ((AutoDateField) fieldValue).setValue((Date) cusType.getCusValue(value, null));
        } else if (cusType.isBoolean()) {
            ((CheckBox) fieldValue).setValue((Boolean) cusType.getCusValue(value, null));
        } else if (cusType.isEnumRefData()) {
            ((TextField) fieldValue).setValue(refTable.getFieldName1());
        }
    }

    /**
     * getFieldValueControl
     *
     * @param fieldName
     * @param cusType
     * @return
     */
    private void addFieldValueControl(String fieldName, ECusType cusType) {
        if (StringUtils.isEmpty(fieldName) || cusType == null) {
            return;
        }
        Component fieldValueControl = null;
        if (cusType.isString()) {
            fieldValueControl = ComponentFactory.getTextField(fieldName, false, 100, 200);
        } else if (cusType.isNumber()) {
            fieldValueControl = ComponentFactory.getNumberField(fieldName, false, 100, 200);
        } else if (cusType.isDate()) {
            fieldValueControl = ComponentFactory.getAutoDateField(fieldName, false);
        } else if (cusType.isBoolean()) {
            fieldValueControl = new CheckBox(I18N.message(fieldName));
        } else if (cusType.isEnumRefData()) {
            fieldValueControl = ComponentFactory.getTextField("ECusType", false, 100, 200);
            fieldValueControl.setEnabled(false);
            fieldValueFormLayout.removeAllComponents();
            // throw new IllegalStateException(I18N.message("error.invalid.field.type", new String[]{cusType.getCode()}));
        }
        fieldValueFormLayout.addComponent(fieldValueControl);
        fieldValues.put(fieldName, fieldValueControl);
    }

    /**
     * @see com.nokor.ersys.vaadin.ui.referential.reftable.refdata.IRefTableDataForm#addFieldValueControls()
     */
    @Override
    public void addFieldValueControls() {
        //fieldValueFormLayout.removeAllComponents();
        fieldValues.clear();

        addFieldValueControl(refTable.getFieldName1(), refTable.getCusType1());
        addFieldValueControl(refTable.getFieldName2(), refTable.getCusType2());
        addFieldValueControl(refTable.getFieldName3(), refTable.getCusType3());
        addFieldValueControl(refTable.getFieldName4(), refTable.getCusType4());
        addFieldValueControl(refTable.getFieldName5(), refTable.getCusType5());
    }

    @Override
    public RefTable getTable() {
        return refTable;
    }

    /**
     * Get values.
     *
     * @return
     */
    @Override
    public RefData getValue() {
        refData.setCode(txtCode.getValue());
        refData.setDesc(txtDesc.getValue());
        refData.setDescEn(txtDescEn.getValue());
        refData.setSortIndex(getInteger(txtSortIndex));
        refData.setStatusRecord(cbActive.getValue() ? EStatusRecord.ACTIV : EStatusRecord.INACT);

        refData.setFieldValue1(getFieldValueControlValue(refTable.getFieldName1(), refTable.getCusType1()));
        refData.setFieldValue2(getFieldValueControlValue(refTable.getFieldName2(), refTable.getCusType2()));
        refData.setFieldValue3(getFieldValueControlValue(refTable.getFieldName3(), refTable.getCusType3()));
        refData.setFieldValue4(getFieldValueControlValue(refTable.getFieldName4(), refTable.getCusType4()));
        refData.setFieldValue5(getFieldValueControlValue(refTable.getFieldName5(), refTable.getCusType5()));

        return refData;
    }

    public BaseERefEntity getRefEntity() {
        refEntity.setCode(txtCode.getValue());
        refEntity.setDesc(txtDesc.getValue());
        refEntity.setDescEn(txtDescEn.getValue());
        if (refEntity instanceof ECurrency)
            ((ECurrency) refEntity).setSymbol(txtSymbol.getValue());
        refEntity.setSortIndex(getInteger(txtSortIndex));
        refEntity.setStatusRecord(cbActive.getValue() ? EStatusRecord.ACTIV : EStatusRecord.INACT);
        return refEntity;
    }

    /**
     * Get FieldValue Control Value by RefTable field name and field type
     *
     * @param fieldName
     * @param cusType
     * @return
     */
    private String getFieldValueControlValue(String fieldName, ECusType cusType) {
        if (StringUtils.isEmpty(fieldName) || cusType == null) {
            return null;
        }

        Component fieldValue = fieldValues.get(fieldName);
        String result = "";
        if (cusType.isString()) {
            result = ((TextField) fieldValue).getValue();
        } else if (cusType.isInteger()) {
            Integer value = getInteger((NumberField) fieldValue);
            if (value != null) {
                result = value.toString();
            }
        } else if (cusType.isDecimal()) {
            Float value = getFloat((NumberField) fieldValue);
            if (value != null) {
                result = value.toString();
            }
        } else if (cusType.isDate()) {
            Date value = ((AutoDateField) fieldValue).getValue();
            if (value != null) {
                result = String.valueOf(value.getTime());
            }
        } else if (cusType.isBoolean()) {
            Boolean value = ((CheckBox) fieldValue).getValue();
            if (value != null) {
                result = value.toString();
            }
        }
        return result;
    }

    /**
     * Reset control value.
     */
    @Override
    public void reset() {
        refData = (RefData) EntityRefA.newInstance(RefData.class);
        if (refTable != null && refTable.getType().getId().equals(ERefType.ENTITY_REF.getId())) {
            try {
                Class clazz = Class.forName(refTable.getName());
                refEntity = BaseERefEntity.newInstance(clazz);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        txtCode.setValue("");
        txtDescEn.setValue("");
        txtDesc.setValue("");
        txtSymbol.setValue("");
        txtSortIndex.setValue("");
        cbActive.setValue(true);
        errors.clear();
        messagePanel.setVisible(false);
        for (Component fieldValue : fieldValues.values()) {
            if (fieldValue instanceof TextField || fieldValue instanceof NumberField) {
                ((TextField) fieldValue).setValue("");
            } else if (fieldValue instanceof AutoDateField) {
                ((AutoDateField) fieldValue).setValue(null);
            } else if (fieldValue instanceof CheckBox) {
                ((CheckBox) fieldValue).setValue(false);
            }
        }
    }

    /**
     * validate Controls.
     *
     * @return
     */
    @Override
    public boolean validate() {
        errors.clear();
        checkMandatoryField(txtDesc, "desc");
        checkMandatoryField(txtDescEn, "desc.en");
        checkIntegerField(txtSortIndex, "sort.index");

        validateFieldValueControl(refTable.getFieldName1(), refTable.getCusType1());
        validateFieldValueControl(refTable.getFieldName2(), refTable.getCusType2());
        validateFieldValueControl(refTable.getFieldName3(), refTable.getCusType3());
        validateFieldValueControl(refTable.getFieldName4(), refTable.getCusType4());
        validateFieldValueControl(refTable.getFieldName5(), refTable.getCusType5());

        return errors.isEmpty();
    }

    /**
     * Validate Field Value Control
     *
     * @param fieldName
     * @param cusType
     */
    private void validateFieldValueControl(String fieldName, ECusType cusType) {
        if (StringUtils.isEmpty(fieldName) || cusType == null) {
            return;
        }

        Component fieldValue = fieldValues.get(fieldName);
        if (cusType.isInteger()) {
            checkIntegerField((NumberField) fieldValue, fieldName);
        } else if (cusType.isDecimal()) {
            checkFloatField((NumberField) fieldValue, fieldName);
        } else if (cusType.isBoolean()) {
            checkBooleanField((NumberField) fieldValue, fieldName);
        }
    }

    /**
     * Display errors messages
     */
    @Override
    public void displayErrors() {
        messagePanel.removeAllComponents();
        if (!errors.isEmpty()) {
            for (String error : errors) {
                Label messageLabel = new Label(error);
                messageLabel.addStyleName("error");
                messagePanel.addComponent(messageLabel);
            }
            messagePanel.setVisible(true);
        }
    }

}

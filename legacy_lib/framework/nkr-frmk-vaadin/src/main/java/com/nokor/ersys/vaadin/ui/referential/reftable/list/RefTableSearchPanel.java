/*
 * Created on 21/05/2015.
 */
package com.nokor.ersys.vaadin.ui.referential.reftable.list;

import com.nokor.frmk.config.model.ERefType;
import com.nokor.frmk.config.model.RefTable;
import com.nokor.frmk.config.service.RefTableRestriction;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.BooleanOptionGroup;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * RefTable Search Panel.
 *
 * @author pengleng.huot
 */
public class RefTableSearchPanel extends AbstractSearchPanel<RefTable> {

    /**     */
    private static final long serialVersionUID = -7475468649215844920L;
    protected TextField txtSearchCode;
    protected TextField txtSearchShortName;
    protected TextField txtSearchDes;
    protected BooleanOptionGroup cbReadOnly;
    protected BooleanOptionGroup cbVisible;

    /**
     * @param districtTablePanel
     */
    public RefTableSearchPanel(RefTableTablePanel districtTablePanel) {
        super(null, districtTablePanel);
    }

    /**
     * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#createForm()
     */
    @Override
    protected Component createForm() {
        txtSearchCode = ComponentFactory.getTextField(null, false, 100, 200);
        txtSearchDes = ComponentFactory.getTextField(null, false, 100, 200);
        txtSearchShortName = ComponentFactory.getTextField(null, false, 100, 200);

        HorizontalLayout optionsLayout = new HorizontalLayout();
        Label lblIsReadonly = ComponentFactory.getLabel("read.only");
        Label lblIsVisible = ComponentFactory.getLabel("visible");
        cbReadOnly = new BooleanOptionGroup();
        cbVisible = new BooleanOptionGroup(Boolean.TRUE);
        optionsLayout.addComponent(lblIsReadonly);
        optionsLayout.addComponent(new Label("  ", ContentMode.PREFORMATTED));
        optionsLayout.addComponent(cbReadOnly);
        optionsLayout.addComponent(new Label("      ", ContentMode.PREFORMATTED));
        optionsLayout.addComponent(lblIsVisible);
        optionsLayout.addComponent(new Label("  ", ContentMode.PREFORMATTED));
        optionsLayout.addComponent(cbVisible);
        optionsLayout.setComponentAlignment(lblIsReadonly, Alignment.MIDDLE_CENTER);
        optionsLayout.setComponentAlignment(lblIsVisible, Alignment.MIDDLE_CENTER);
        cbVisible.setVisible(false);
        lblIsVisible.setVisible(false);

        final GridLayout criteriaLayout = new GridLayout(4, 2);
        criteriaLayout.setSpacing(true);
        Label lblSearchCode = ComponentFactory.getLabel("code");
        Label lblSearchDesc = ComponentFactory.getLabel("desc.en");
        Label lblSearchShortName = ComponentFactory.getLabel("short.name");

        criteriaLayout.addComponent(lblSearchCode);
        criteriaLayout.addComponent(txtSearchCode);
        criteriaLayout.addComponent(lblSearchDesc);
        criteriaLayout.addComponent(txtSearchDes);
        criteriaLayout.addComponent(lblSearchShortName);
        criteriaLayout.addComponent(txtSearchShortName);
        criteriaLayout.addComponent(new Label("      ", ContentMode.PREFORMATTED));
        criteriaLayout.addComponent(optionsLayout);
        criteriaLayout.setComponentAlignment(txtSearchCode, Alignment.MIDDLE_CENTER);
        criteriaLayout.setComponentAlignment(txtSearchDes, Alignment.MIDDLE_CENTER);
        criteriaLayout.setComponentAlignment(txtSearchShortName, Alignment.MIDDLE_CENTER);

        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.addComponent(criteriaLayout);
        mainLayout.addComponent(new Label("  ", ContentMode.PREFORMATTED));

        return mainLayout;
    }


    /**
     * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#getRestrictions()
     */
    @Override
    public BaseRestrictions<RefTable> getRestrictions() {
        RefTableRestriction restrictions = new RefTableRestriction(RefTable.class);
        List<Criterion> criterions = new ArrayList<Criterion>();

        if (StringUtils.isNotEmpty(txtSearchCode.getValue())) {
            String packageName = RefTableTablePanel.packageName.get(txtSearchCode.getValue().toString());
            criterions.add(Restrictions.eq("code", packageName));
        }
        if (StringUtils.isNotEmpty(txtSearchDes.getValue())) {
            criterions.add(Restrictions.like("descEn", this.txtSearchDes.getValue(), MatchMode.ANYWHERE));
        }
        if (StringUtils.isNotEmpty(txtSearchShortName.getValue())) {
            criterions.add(Restrictions.eq("shortName", txtSearchShortName.getValue().toString()));
        }
        Collection<Boolean> selectedReadonlyItems = (Collection<Boolean>) cbReadOnly.getValue();
        if (selectedReadonlyItems.size() != 2) {
            restrictions.setIsReadonly(selectedReadonlyItems.contains(Boolean.TRUE));
        }
        Collection<Boolean> selectedVisibleItems = (Collection<Boolean>) cbVisible.getValue();
        if (selectedVisibleItems.size() != 2) {
            restrictions.setIsVisible(selectedVisibleItems.contains(Boolean.TRUE));
        }

        criterions.add(Restrictions.eq(RefTable.TYPE, ERefType.REFDATA));

        restrictions.setCriterions(criterions);
        restrictions.addOrder(Order.asc(RefTable.ID));
        return restrictions;
    }

    /**
     * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#reset()
     */
    @Override
    protected void reset() {
        txtSearchCode.setValue("");
        cbReadOnly.setValue(false);
    }
}

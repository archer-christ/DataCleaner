/**
 * DataCleaner (community edition)
 * Copyright (C) 2014 Neopost - Customer Information Management
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU
 * Lesser General Public License, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution; if not, write to:
 * Free Software Foundation, Inc.
 * 51 Franklin Street, Fifth Floor
 * Boston, MA  02110-1301  USA
 */
package org.datacleaner.widgets.properties;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.swing.JButton;

import org.datacleaner.descriptors.ConfiguredPropertyDescriptor;
import org.datacleaner.job.builder.ComponentBuilder;
import org.datacleaner.panels.DCPanel;
import org.datacleaner.reference.SynonymCatalog;
import org.datacleaner.user.MutableReferenceDataCatalog;
import org.datacleaner.user.ReferenceDataChangeListener;
import org.datacleaner.util.IconUtils;
import org.datacleaner.util.WidgetFactory;
import org.datacleaner.windows.ReferenceDataDialog;

public class MultipleSynonymCatalogsPropertyWidget extends AbstractMultipleCheckboxesPropertyWidget<SynonymCatalog>
        implements ReferenceDataChangeListener<SynonymCatalog> {

    private final MutableReferenceDataCatalog _referenceDataCatalog;
    private final Provider<ReferenceDataDialog> _referenceDataDialogProvider;

    @Inject
    public MultipleSynonymCatalogsPropertyWidget(final ComponentBuilder componentBuilder,
            final ConfiguredPropertyDescriptor propertyDescriptor,
            final MutableReferenceDataCatalog referenceDataCatalog,
            final Provider<ReferenceDataDialog> referenceDataDialogProvider) {
        super(componentBuilder, propertyDescriptor, SynonymCatalog.class);
        _referenceDataCatalog = referenceDataCatalog;
        _referenceDataDialogProvider = referenceDataDialogProvider;
    }

    @Override
    public void onPanelAdd() {
        super.onPanelAdd();
        _referenceDataCatalog.addSynonymCatalogListener(this);
    }

    @Override
    public void onPanelRemove() {
        super.onPanelRemove();
        _referenceDataCatalog.removeSynonymCatalogListener(this);
    }

    @Override
    protected DCPanel createButtonPanel() {
        final DCPanel buttonPanel = super.createButtonPanel();

        final JButton dialogButton = WidgetFactory.createSmallButton(IconUtils.MENU_OPTIONS);
        dialogButton.setToolTipText("Configure synonym catalogs");
        dialogButton.addActionListener(e -> {
            final ReferenceDataDialog dialog = _referenceDataDialogProvider.get();
            dialog.selectSynonymsTab();
            dialog.setVisible(true);
        });

        buttonPanel.add(dialogButton);
        return buttonPanel;
    }

    @Override
    protected SynonymCatalog[] getAvailableValues() {
        final String[] names = _referenceDataCatalog.getSynonymCatalogNames();
        final SynonymCatalog[] result = new SynonymCatalog[names.length];
        for (int i = 0; i < names.length; i++) {
            result[i] = _referenceDataCatalog.getSynonymCatalog(names[i]);
        }
        return result;
    }

    @Override
    protected String getName(final SynonymCatalog item) {
        return item.getName();
    }

    @Override
    public void onAdd(final SynonymCatalog synonymCatalog) {
        addCheckBox(synonymCatalog, false);
    }

    @Override
    public void onRemove(final SynonymCatalog synonymCatalog) {
        removeCheckBox(synonymCatalog);
    }

    @Override
    protected String getNotAvailableText() {
        return "- no synonym catalogs available - ";
    }

    @Override
    public void onChange(final SynonymCatalog oldSynonymCatalog, final SynonymCatalog newSynonymCatalog) {
        editCheckBox(oldSynonymCatalog, newSynonymCatalog);
    }
}

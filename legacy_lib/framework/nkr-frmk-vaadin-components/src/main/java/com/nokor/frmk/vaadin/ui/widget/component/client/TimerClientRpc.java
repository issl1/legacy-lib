package com.nokor.frmk.vaadin.ui.widget.component.client;

import com.vaadin.shared.communication.ClientRpc;

/**
 * @author nora.ky
 *
 */
public interface TimerClientRpc extends ClientRpc {

    void start(boolean repeat, int interval);
    void stop();
}
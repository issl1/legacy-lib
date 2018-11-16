package com.nokor.efinance.tools.sync;

import java.net.URI;
import java.util.regex.Pattern;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;

/**
 * @author nora.ky
 *
 */
public class ClientSocket extends WebSocketClient {
	private String appCode;

	public ClientSocket(URI serverUri, Draft draft) {
		super(serverUri, draft);
	}

	@Override
    public void onOpen( ServerHandshake arg0 ) {
		appCode = ConnectionManager.getConnectionSetting().getAppCode();
		send(Command.APPCODE + Command.SEPERATOR + appCode);
    }
 
    @Override
    public void onMessage( String arg0 ) {
    	String[] parts = Pattern.compile(Command.SEPERATOR).split(arg0); 
    	//String[] parts = arg0.split(Command.SEPERATOR);
    	if (parts.length == 0) {
    		return;
    	}
    	// command at parts[0]
    	switch (parts[0]) {
    	case Command.PUSHNOTIFY:
    		NotifySender.pushNotify(Integer.parseInt(parts[1]), parts[2]);
    		break;
    	}
    }
 
    @Override
    public void onError( Exception arg0 )
    {
    	
    }
 
    @Override
    public void onClose( int arg0, String arg1, boolean arg2 )
    {
    	
    }
    
}
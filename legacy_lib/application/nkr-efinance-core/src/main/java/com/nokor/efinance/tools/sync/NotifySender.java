package com.nokor.efinance.tools.sync;

import java.util.LinkedList;

/**
 * @author nora.ky
 *
 */
public class NotifySender {
	public static final String NOTIFY_ALL = "ALL";
	//public static ExecutorService executorService = Executors.newSingleThreadExecutor();
	public interface PushReceiver {
        public void receiveNotify(String message);
    }
    
    private static final LinkedList<PushReceiver> pushReceivers = new LinkedList<PushReceiver>();

    /**
     * @param receiver
     * @param userId
     * @param profile
     */
    public static synchronized void registerPushReceiver(PushReceiver receiver, Long userId, String profile) {
    	pushReceivers.add(receiver);
    	int index = pushReceivers.indexOf(receiver);
    	ClientSocket socket = ConnectionManager.getInstance().getClientSocket(); 
    	if (socket != null) {
    		socket.send(Command.REGISTER + Command.SEPERATOR +
    				index + Command.SEPERATOR +
    				userId + Command.SEPERATOR +
    				profile + Command.SEPERATOR +
    				ConnectionManager.getConnectionSetting().getAppCode());
    	}
    }

    /**
     * @param receiver
     */
    public static synchronized void unregisterPushReceiver(PushReceiver receiver) {
    	int index = pushReceivers.indexOf(receiver);
    	pushReceivers.remove(index);
    	ClientSocket socket = ConnectionManager.getInstance().getClientSocket(); 
    	if (socket != null) {
    		socket.send(Command.UNREGISTER + Command.SEPERATOR +
    				index + Command.SEPERATOR +
    				ConnectionManager.getConnectionSetting().getAppCode());
    	}
    }
    
    /**
     * @param receiverId
     * @param message
     */
    public static synchronized void pushNotify(int receiverId, String message) {
    	//executorService.execute(new Runnable() {
        //  @Override
          //public void run() 
          	//receiver.receiveNotify(message);
          //}
      //});
    	PushReceiver receiver = pushReceivers.get(receiverId);
    	if (receiver != null) {
    		receiver.receiveNotify(message);
    	}
    }
    
    /**
     * @param message message for notification
     * @param usrProfile notify for user profile, use NOTIFY_ALL for all user profile
     * @param lifetime in milliseconds, 0 will stay forever until queue is full
     * @param immerdiate send without delay
     */
	 public static void addNotify(String message, String usrProfile, long lifetime, boolean immerdiate) {
		ClientSocket socket = ConnectionManager.getInstance().getClientSocket();
		if (socket != null) {
			socket.send(Command.ADDNOTIFY + Command.SEPERATOR +
					message + Command.SEPERATOR +
					usrProfile + Command.SEPERATOR +
					lifetime + Command.SEPERATOR +
					immerdiate);
		}
    }
    
    /**
     * @param message
     */
	public static void addNotify(String message) {
    	addNotify(message, NOTIFY_ALL, 0, false);
    }
}

package com.nokor.efinance.tools.sync;

import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.java_websocket.WebSocket.READYSTATE;
import org.java_websocket.drafts.Draft_17;
import org.smslib.AGateway;
import org.smslib.GatewayException;
import org.smslib.IOutboundMessageNotification;
import org.smslib.OutboundMessage;
import org.smslib.SMSLibException;
import org.smslib.Service;
import org.smslib.TimeoutException;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;



/**
 * @author nora.ky
 *
 */
public class ConnectionManager implements Serializable {
	private static final long serialVersionUID = -4249329311254742664L;
	
	public interface ConnectionFeedback {
        public void onConnectionClose();
        public void onConnectionError();
    }
	
	private static ConnectionSetting connectionSetting;
	
	/**
	 * @return
	 */
	public static ConnectionSetting getConnectionSetting() {
		return connectionSetting;
	}

	/**
	 * @param connectionSetting
	 */
	public static void setConnectionSetting(ConnectionSetting connectionSetting) {
		ConnectionManager.connectionSetting = connectionSetting;
	}

	private static ConnectionManager instance = null;
	private static ClientSocket clientSocket = null;
	
	private static ConnectionFeedback connectionFeedback = null;
	public static void setConnectionFeedback(ConnectionFeedback connectionFeedback) {
		ConnectionManager.connectionFeedback = connectionFeedback;
	}

	private static int tryConnect = 0;

	private static JavaMailSenderImpl mailSender;
	
	/**
     * @return
     */
    public static ConnectionManager getInstance() {
        if (instance == null) {
            instance = new ConnectionManager();
        }
        return instance;
    }
    
    /**
     * 
     */
    private ConnectionManager() {
    	mailSender = new JavaMailSenderImpl();
    }
	
	/**
	 * 
	 */
	public void connectSyncServer() {
		try {
			if (clientSocket == null) {
				clientSocket = new ClientSocket(new URI(connectionSetting.getSyncServer()), new Draft_17());
			}
			
			final READYSTATE state = clientSocket.getReadyState(); 
			if (state == READYSTATE.NOT_YET_CONNECTED) {
				clientSocket.connect();
			} else if (state == READYSTATE.CLOSED) {
				// never connected, problem of configuration or server not run.
				if (tryConnect == 0) {
					clientSocket = new ClientSocket(new URI(connectionSetting.getSyncServer()), new Draft_17());
					clientSocket.connect();
					if (connectionFeedback != null) {
						connectionFeedback.onConnectionError();
					}
					
				} else {
					tryConnect++;
					clientSocket.connect();
					if (connectionFeedback != null) {
						connectionFeedback.onConnectionClose();
					}
				}
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}	

    /**
     * @param gateway
     */
    public void addGateway(AGateway gateway) {
    	try {
    		org.smslib.Service.getInstance().addGateway(gateway);
		} catch (GatewayException e) {
			e.printStackTrace();
		}
    	gateway.setOutbound(true);
		// Do we need secure (https) communication?
		// True uses "https", false uses "http" - default is false.
    	//gateway.setSecure(false);
    }
    
    /**
     * 
     */
    public void startSMSService() {
    	try {
    		org.smslib.Service.getInstance().startService();
		} catch (TimeoutException e) {
			e.printStackTrace();
		} catch (GatewayException e) {
			e.printStackTrace();
		} catch (SMSLibException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * 
     */
    public void stopSMSService() {
    	try {
    		org.smslib.Service.getInstance().stopService();
		} catch (TimeoutException e) {
			e.printStackTrace();
		} catch (GatewayException e) {
			e.printStackTrace();
		} catch (SMSLibException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }

	/**
     * @param message
     * @param toAddress
     */
	public void sendMail(final String subject, final String message, final String toAddress) {
        MimeMessagePreparator preparator = new MimeMessagePreparator() {
            public void prepare(MimeMessage mimeMessage) throws MessagingException {
                mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(toAddress));
                mimeMessage.setFrom(new InternetAddress(connectionSetting.getMailFrom()));
                mimeMessage.setSubject(subject);
                mimeMessage.setText(message);
            }
        };
        
        try {
        	mailSender.setHost(connectionSetting.getMailHost());
        	mailSender.send(preparator);
        }
        catch (MailException e) {
        	e.printStackTrace();
        }
    }
    
    /**
     * @param message
     * @param toAddress
     * @param attachment
     */
	public void sendMail(final String subject, final String message, final String toAddress, FileSystemResource attachment) {
    	MimeMessage mMessage = mailSender.createMimeMessage();
    	try {
    		// use the true flag to indicate you need a multipart message
        	MimeMessageHelper helper = new MimeMessageHelper(mMessage, true);
        	helper.setFrom(connectionSetting.getMailFrom());
        	helper.setTo(toAddress);
        	helper.setSubject(subject);
        	helper.setText(message, true);
        	helper.addAttachment(attachment.getFilename(), attachment);
        	
        	mailSender.setHost(connectionSetting.getMailHost());
        	mailSender.send(mMessage);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }

    /**
     * @param message
     * @param toNumber
     */
    public void sendSMS(String message, String toNumber) {
    	sendSMS(message, toNumber, null);
    }
    
    /**
     * @param message
     * @param toNumber
     * @param outboundNotification
     */
    public void sendSMS(String message, String toNumber, IOutboundMessageNotification outboundNotification) {
    	OutboundMessage msg;

		msg = new OutboundMessage(toNumber, message);
		if (connectionSetting.getSmsFrom() != null) {
			msg.setFrom(connectionSetting.getSmsFrom());
		}
		if (outboundNotification != null) {
			Service.getInstance().setOutboundMessageNotification(outboundNotification);
		}
		try {
			Service.getInstance().sendMessage(msg);
		} catch (TimeoutException e) {
			e.printStackTrace();
		} catch (GatewayException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }
    
    /**
	 * @return
	 */
	public ClientSocket getClientSocket() {
		if (clientSocket == null || clientSocket.getReadyState() != READYSTATE.OPEN) {
			return null;
		}
		return clientSocket;
	}
}
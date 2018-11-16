package com.nokor.efinance.third.wing.server.payment.vo;

import com.nokor.efinance.core.shared.system.MessageType;

/**
 * This interface is used to manage error message
 */
public enum Message {

   REQUEST_MESSAGE_MANDATORY("RequestMessage is mandatory"),
   SERVICE_HEADER_MANDATORY("ServiceHeader is mandatory"),
   THIRDPARTY_MANDATORY("Third Party is mandatory"),
   THIRDPARTY_NOT_FOUND("Unknown Third Party"),
   USERNAME_MANDATORY("Username is mandatory"),
   PASSWORD_MANDATORY("Password is mandatory"),
   USERNAME_NOT_FOUND("Unknown username"),
   TOKEN_ID_NOT_FOUND("Token id is mandatory"),
   TOKEN_ID_INVALID("Token id invalid"),
   OTHER_ERROR("Other error"),
   CONTRACT_NOT_FOUND("Reference number is mandatory"),
   CONTRACT_LENGTH_NOT_CORRECT("Reference number should be 8 digits"),
   UNKNOWN_CONTRACT("Unknown reference number"),
   PAID_AMOUNT_NOT_MATCH("Paid");

   /** Message type */
   private MessageType type;
   
   /** Message text */
   private String text;

   /**
    * @param text
    */
   private Message(String text) {
      this.text = text;
      this.type = MessageType.ERROR;
   }

   /**
    * @return MessageType
    */
   public MessageType getType() {
      return type;
   }

   /**
    * @return text as string
    */
   public String getText() {
      return text;
   }

   /**
    * @param text
    * @return Message
    */
   public static Message fromText(String text) {
      if (text != null) {
         for (Message message : values()) {
            if (message.getText().equals(text)) {
               return message;
            }
         }
      }
      return null;
   }
}

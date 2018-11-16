package com.nokor.efinance.core.shared.system;

/**
 * This interface is used to manage error message
 * @author ly.youhort
 */
public enum MessageType {
   ERROR("Error"), 
   WARN("Warning"), 
   FATAL("Fatal"), 
   INFO("Info");

   private String value;

   /**
    * @param value
    */
   private MessageType(String value) {
      this.value = value;
   }

   /**
    * @return
    */
   public String getValue() {
      return value;
   }
}

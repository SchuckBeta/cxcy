package com.oseasy.dr.modules.dr.manager;

public class CardConstants {

    public enum commandType {

        DEVICE_COMMAND("DEVICE", "设备命令"),
        DOOR_COMMAND("DOOR", "门命令");

        private String type;
        private String description;;

        commandType(String type, String description) {
            this.type = type;
            this.description = description;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
}

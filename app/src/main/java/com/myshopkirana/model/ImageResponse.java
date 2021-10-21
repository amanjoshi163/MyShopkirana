package com.myshopkirana.model;

import com.google.gson.annotations.SerializedName;

public class ImageResponse {

        @SerializedName("status")
        private boolean status;
        @SerializedName("Name")
        private String Name;

        public boolean getStatus() {
            return status;
        }

        public void setStatus(boolean status) {
            this.status = status;
        }

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
        }
}

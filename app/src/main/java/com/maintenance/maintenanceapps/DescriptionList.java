package com.maintenance.maintenanceapps;

import android.os.Parcel;
import android.os.Parcelable;

public class DescriptionList implements Parcelable {

        private String tanggall;
        private String descc;
        private String userr;

    public DescriptionList(String tanggall, String descc, String userr) {
        this.tanggall = tanggall;
        this.descc = descc;
        this.userr = userr;
    }

        public String getTanggall() {
            return tanggall;
        }

        public void setTanggall(String tanggall) {
            this.tanggall = tanggall;
        }

        public String getDescc() {
            return descc;
        }

        public void setDescc(String descc) {
            this.descc = descc;
        }

        public String getUserr() {
            return userr;
        }

        public void setUserr(String userr) {
            this.userr = userr;
        }



    protected DescriptionList(Parcel in) {
    }

    public static final Creator<DescriptionList> CREATOR = new Creator<DescriptionList>() {
        @Override
        public DescriptionList createFromParcel(Parcel in) {
            return new DescriptionList(in);
        }

        @Override
        public DescriptionList[] newArray(int size) {
            return new DescriptionList[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}

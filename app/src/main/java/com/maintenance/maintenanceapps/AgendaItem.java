package com.maintenance.maintenanceapps;

import android.os.Parcel;
import android.os.Parcelable;

public class AgendaItem implements Parcelable {
    private int imageResource;
    private String agendaText;
    private String agendaText2;
    private String tglText;
    private String OutCode;
    private String TglPerbaiki;
    private String Transaksi;
    private String Occuredate;
    private String Duedate;
    private String problem;
    private String status;
    private int rak;
    private String motor;
    private int problemId;
    private String transaksiId;


//    public  AgendaItem(int imageresource1, String agendatext1, String tgltext1 ){
//        imageResource = imageresource1;
//        agendaText = agendatext1;
//        tglText = tgltext1;
//
//    }

    public AgendaItem(int imageResource, String agendaText, String agendaText2, String tglText, String outCode, String tglPerbaiki, String transaksi, String occuredate, String duedate, String problem, String status, int rak, String motor, int problemId, String transaksiId) {
        this.imageResource = imageResource;
        this.agendaText = agendaText;
        this.agendaText2 = agendaText2;
        this.tglText = tglText;
        OutCode = outCode;
        TglPerbaiki = tglPerbaiki;
        Transaksi = transaksi;
        Occuredate = occuredate;
        Duedate = duedate;
        this.problem = problem;
        this.status = status;
        this.rak = rak;
        this.motor = motor;
        this.problemId = problemId;
        this.transaksiId = transaksiId;
    }

    public AgendaItem() {
    }

    protected AgendaItem(Parcel in) {
        imageResource = in.readInt();
        agendaText = in.readString();
        agendaText2 = in.readString();
        tglText = in.readString();
        OutCode = in.readString();
        TglPerbaiki = in.readString();
        Transaksi = in.readString();
        Occuredate = in.readString();
        Duedate = in.readString();
        problem = in.readString();
        status = in.readString();
        rak = in.readInt();
        motor = in.readString();
        problemId = in.readInt();
        transaksiId = in.readString();
    }

    public static final Creator<AgendaItem> CREATOR = new Creator<AgendaItem>() {
        @Override
        public AgendaItem createFromParcel(Parcel in) {
            return new AgendaItem(in);
        }

        @Override
        public AgendaItem[] newArray(int size) {
            return new AgendaItem[size];
        }
    };

    public int getImageResource() {
        return imageResource;
    }

    public String getAgendaText() {
        return agendaText;
    }

    public String getTglText() {
        return tglText;
    }

    public String getOutCode() {
        return OutCode;
    }

    public String getTransaksi() {
        return Transaksi;
    }

    public void setTransaksi(String transaksi) {
        this.Transaksi = transaksi;
    }
    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public void setAgendaText(String agendaText) {
        this.agendaText = agendaText;
    }

    public void setTglText(String tglText) {
        this.tglText = tglText;
    }

    public String getTglPerbaiki() {
        return TglPerbaiki;
    }

    public void setTglPerbaiki(String tglPerbaiki) {
        TglPerbaiki = tglPerbaiki;
    }

    public String getAgendaText2() {
        return agendaText2;
    }

    public void setAgendaText2(String agendaText2) {
        this.agendaText2 = agendaText2;
    }

    public String getOccuredate() {
        return Occuredate;
    }

    public void setOccuredate(String occuredate) {
        Occuredate = occuredate;
    }

    public String getDuedate() {
        return Duedate;
    }

    public void setDuedate(String duedate) {
        Duedate = duedate;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getRak() {
        return rak;
    }

    public void setRak(int rak) {
        this.rak = rak;
    }

    public String getMotor() {
        return motor;
    }

    public void setMotor(String motor) {
        this.motor = motor;
    }

    public int getProblemId() {
        return problemId;
    }
    public String getTransaksiId() {
        return transaksiId;
    }

    public void setTransaksiId(String transaksiId) {
        this.transaksiId = transaksiId;
    }

    public void setProblemId(int problemId) {
        this.problemId = problemId;
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(imageResource);
        dest.writeString(agendaText);
        dest.writeString(agendaText2);
        dest.writeString(tglText);
        dest.writeString(OutCode);
        dest.writeString(TglPerbaiki);
        dest.writeString(Transaksi);
        dest.writeString(Occuredate);
        dest.writeString(Duedate);
        dest.writeString(problem);
        dest.writeString(status);
        dest.writeInt(rak);
        dest.writeString(motor);
        dest.writeInt(problemId);
        dest.writeString(transaksiId);
    }



}


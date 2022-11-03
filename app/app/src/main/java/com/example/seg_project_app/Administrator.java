package com.example.seg_project_app;

import java.util.ArrayList;
import java.util.List;

public class Administrator extends User{

    public List<Complaint> complaintList;
    public Administrator(String email, String password, String userID){
        super(email,password,"administrator", userID);

        complaintList = new ArrayList<Complaint>();
    }

    public void addComplaint(Complaint complaint){
        complaintList.add(complaint);
    }

    public void removeComplaint(Complaint complaint){
        complaintList.remove(complaint);
    }

    public void dismissComplaint(Complaint complaint){
        complaint.makeDecision("dismiss");
    }

    public void permanentlyBan(Complaint complaint){
        complaint.makeDecision("permanently");
    }

    public void temporaryBan(Complaint complaint,int banDays){
        complaint.makeDecision("temporary", banDays);
    }
}

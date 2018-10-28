package com.lumiata.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import java.util.Date;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Event implements Comparable<Event>{
    Date date;
    String system;
    String icdCode;

    public Event(Date date, String icdVersion, String icdCode) {
        this.date = date;
        this.icdCode = icdCode;
        if(icdVersion.equals("9"))
        {
            this.system = "http://hl7.org/fhir/sid/icd-9-cm";
        }
        else if(icdVersion.equals("10"))
        {
            this.system = "http://hl7.org/fhir/sid/icd-10";
        }
        else
        {
            this.system = "";
        }
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public String getIcdCode() {
        return icdCode;
    }

    public void setIcdCode(String icdCode) {
        this.icdCode = icdCode;
    }

    @Override
    public int compareTo(Event o) {
        return o.getDate().compareTo(getDate());
    }
}

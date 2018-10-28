package com.lumiata.Service;

import com.lumiata.model.Patient;
import java.util.Comparator;

public class PatientLengthComparator implements Comparator<Patient> {

    @Override
    public int compare(Patient p1, Patient p2) {
        return p1.getLength() - p2.getLength();
    }
}

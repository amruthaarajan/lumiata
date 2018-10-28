package com.lumiata.Service;

import com.lumiata.model.Patient;
import java.util.Comparator;

public class PatientAgeComparator implements Comparator<Patient> {

    @Override
    public int compare(Patient p1, Patient p2) {
        return p1.getAge() - p2.getAge();
    }
}

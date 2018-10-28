package com.lumiata.Service;

import com.lumiata.model.Patient;
import java.util.List;

public interface IPatientService {
    public Patient findOne(String id);
    public List<Patient> findAll();
}

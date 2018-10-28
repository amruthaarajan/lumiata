package com.lumiata.controller;

import com.lumiata.Service.PatientAgeComparator;
import com.lumiata.Service.PatientLengthComparator;
import com.lumiata.Service.PatientService;
import com.lumiata.model.Patient;
import com.lumiata.utilities.JsonDownloader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class PatientController {

    @Autowired
    PatientService patientService;

    @RequestMapping("/")
    public String index() {
        return "Welcome to patient system!";
    }

    @RequestMapping(method=RequestMethod.GET, value="/patients")
    public Iterable<Patient> patient() {
        return patientService.getValidPatients();
    }

    @RequestMapping(method=RequestMethod.GET, value="/patients/download")
    public void downloadPatient()
    {
        // In future we can tweak this to download all json in future as zip
        JsonDownloader jd=new JsonDownloader(patientService.getValidPatients());
    }

    @RequestMapping(method=RequestMethod.GET, value="/patients/{id}")
    public Patient get(@PathVariable String id) {
        return patientService.findOne(id);
    }

    @RequestMapping(method=RequestMethod.GET, value="/patients/metrics")
    public String validPatientMetrics() {
        List<Patient> list= patientService.getValidPatients();
        int maleCount=0;
        int femaleCount=0;
        int maxLength;
        int minLength;
        double medianLength;
        int maxAge;
        int minAge;
        double medianAge;

        list.sort(new PatientAgeComparator());
        minAge=list.get(0).getAge();
        maxAge=list.get(list.size()-1).getAge();
        if (list.size() % 2 == 0)
        {
            medianAge = ((double) list.get(list.size() / 2 - 1).getAge() + (double) list.get(list.size() / 2 - 1).getAge()) / 2;
        }
        else
        {
            medianAge = (double) list.get(list.size() / 2).getAge();
        }
        list.sort(new PatientLengthComparator());
        minLength=list.get(0).getLength();
        maxLength=list.get(list.size()-1).getLength();
        if (list.size() % 2 == 0)
        {
            medianLength = ((double) list.get(list.size() / 2 - 1).getLength() + (double) list.get(list.size() / 2 - 1).getLength()) / 2;
        }
        else
        {
            medianLength = (double) list.get(list.size() / 2).getLength();
        }
        for(Patient p : list)
        {
            if(p.getGender().equals("M"))
            {
                maleCount++;
            }
            else
            {
                femaleCount++;
            }
        }
        return "{'totalCount': '" + list.size() + "'," +
                "'maleCount': '" + maleCount + "'," +
                "'minAge': '" + minAge + "'," +
                "'maxAge': '" + maxAge + "'," +
                "'medianAge': '" + medianAge + "'," +
                "'minLength': '" + minLength + "'," +
                "'maxLength': '" + maxLength + "'," +
                "'medianLength': '" + medianLength + "'," +
                "'femaleCount': '" + femaleCount +  "'}";
    }
}
package com.lumiata.Service;

import com.lumiata.model.Event;
import com.lumiata.model.Patient;
import org.springframework.stereotype.Service;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;


@Service
public class PatientService implements IPatientService {

    private final Map<String,Patient> patients;

    public PatientService() {
        patients = new HashMap<>();
        readPatients();
        readPatientEvents();
    }

    private void readPatients()
    {
        String line ="";
        String fileName = "src/main/resources/demo.psv";
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            br.readLine();
            while ((line = br.readLine()) != null){
                String[] nextLine = line.split("[|]");
                // skip if demographic information is not present
                if(nextLine.length<3)
                {
                    continue;
                }
                if(nextLine[1]== null || nextLine[1].length()<1 || nextLine[2]== null || nextLine[2].length()<1)
                {
                    continue;
                }
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                Patient p = new Patient(nextLine[0], formatter.parse(nextLine[1]), nextLine[2],new ArrayList<>());
                patients.put(nextLine[0],p);
            }

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    private void readPatientEvents()
    {
        String line ="";
        String fileName = "src/main/resources/events.psv";
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            br.readLine();
            while ((line = br.readLine()) != null){
                String[] nextLine = line.split("[|]");
                if(nextLine.length<4)
                {
                    continue;
                }
                if(patients.containsKey(nextLine[0]))
                {
                    // skip if the event date, version code is empty. Dont add to patient
                    if(nextLine[1]== null || nextLine[1].length()<1 || nextLine[2]== null || nextLine[2].length()<1 || nextLine[3]== null || nextLine[3].length()<1)
                    {
                        continue;
                    }
                    Patient temp=patients.get(nextLine[0]);
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                    Event e=new Event(formatter.parse(nextLine[1]), nextLine[2],nextLine[3]);
                    if(e.getSystem().length()<1)
                    {
                        continue;
                    }
                    temp.getEvents().add(e);
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Patient findOne(String id)
    {
        Patient temp=patients.get(id);
        if(temp!=null) {
            Collections.sort(temp.getEvents());
            List<Event> events=temp.getEvents();
            if(events.size()<1)
            {
                return null;
            }
            else
            {
                temp.setLength(getLengthInDays(events.get(0).getDate(),events.get(events.size()-1).getDate()));
                temp.setAge(getAgeInYears(events.get(0).getDate(),temp.getBirthDate()));
            }
        }
        return temp;
    }

    @Override
    public List<Patient> findAll() {
        Collection<Patient> p=patients.values();
        List<Patient> result=new ArrayList<>();
        for(Patient item : p)
        {
            Collections.sort(item.getEvents());
            List<Event> events=item.getEvents();
            if(events.size()<1)
            {
                continue;
            }
            else
            {
                item.setLength(getLengthInDays(events.get(0).getDate(),events.get(events.size()-1).getDate()));
                item.setAge(getAgeInYears(events.get(0).getDate(),item.getBirthDate()));
            }
            result.add(item);
        }
        return result;
    }

    public List<Patient> getValidPatients()
    {
        return findAll();
    }

    private int getLengthInDays(Date d1,Date d2)
    {
        long diff=d1.getTime() - d2.getTime();
        return (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }

    private int getAgeInYears(Date d1,Date d2)
    {
        long diff= d1.getTime() - d2.getTime();
        return (int) (TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)/365.25);
    }
}

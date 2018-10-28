package com.lumiata.utilities;

import com.google.gson.Gson;
import com.lumiata.model.Patient;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class JsonDownloader {
    public JsonDownloader(List<Patient> patients)
    {
        Gson gson = new Gson();

        for(Patient i: patients)
        {
            String json = gson.toJson(i);
            try {
                FileWriter writer = new FileWriter("data/" + i.getId() + ".json");
                writer.write(json);
                writer.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

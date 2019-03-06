package ann.annotators;

import ann.filereaders.*;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.Gson;
import com.google.gson.*;

import java.util.List;
import java.util.Set;
import java.util.HashSet;

import java.io.*;
import java.util.ArrayList;

public class ltfAnnotator {

    public static void main(String[] args) {
        List<Sample> listOfsents = LTFFileReader.reader("/home/prajnya/Desktop/WSD/data/ltf/ltf/HC00000DW.ltf.xml");
        for (Sample sampletext : listOfsents) {
            System.out.println(sampletext.getId() + " -- " + sampletext.getOrigSent());
            System.out.println();

        }
    }
}

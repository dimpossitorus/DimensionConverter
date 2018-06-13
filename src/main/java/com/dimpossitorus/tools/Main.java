package com.dimpossitorus.tools;

import com.dimpossitorus.tools.converter.DimensionConverter;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) {
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.print("File Url (dimens.xml) : ");
            String url = reader.readLine();

            System.out.print("Multiplier (float) : ");
            String multiplierInput = reader.readLine();
            while (!NumberUtils.isCreatable(multiplierInput)){
                System.out.println("Please input number");
                System.out.print("Multiplier (float) : ");
                multiplierInput = reader.readLine();
            }
            double multiplier = Double.valueOf(multiplierInput);
            DimensionConverter converter = new DimensionConverter(url);
            System.out.println("New dimens.xml File : "+converter.getUpdatedFile(multiplier));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

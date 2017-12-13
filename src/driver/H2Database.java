package driver;

import appliance.AirConditioner;
import appliance.Appliance;
import appliance.Refrigerator;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by Tim on 12/7/2017.
 */
public class H2Database {

    private Connection conn;
    private ResultSet result;
    //Holds all read inputs as the appropriate Appliance sub-type
    private ArrayList<Appliance> appliances;
    private String currentAppliance;

    public H2Database() {
        try {
            Class.forName("org.h2.Driver");
            conn = DriverManager.getConnection("jdbc:h2:~/NotAppDB");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // database commands
        clear();
        parseCsvFiles();

        appliances = new ArrayList<Appliance>();
    }

    public void selectTable(String str) {
        currentAppliance = str;

        String query = generateSelectQuery(str);
        sql(query);
    }

    public ArrayList<Appliance> getAppliances() {
        try {
            while(result.next()) {
                String m = result.getString("model"),
                        b = result.getString("brand");
                double e = result.getDouble("energy");

                if (currentAppliance.equals("airconditioner")) {
                    appliances.add(new AirConditioner(m, b, e));
                } else if (currentAppliance.equals("refrigerator")) {
                    appliances.add(new Refrigerator(m, b, e));
                } else if (currentAppliance.equals("washingmachine")) {
                    appliances.add(new Refrigerator(m, b, e));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return appliances;
    }

    /**
     * For SQL statements
     * @param str
     */
    public void sql(String str) {
        try {
            Statement stmt = conn.createStatement();
            result = stmt.executeQuery(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * For DBMS commands
     * @param r
     */
    public void command(String str) {
        try {
            Statement stmt = conn.createStatement();
            stmt.execute(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() throws Exception {
        conn.close();
    }

    private void clear() {
        command("DROP ALL OBJECTS");
    }

    /*Edited By Devon, 12/12/2017
      updated file paths to match new CSV files
     */
    private void parseCsvFiles() {
        command("CREATE TABLE AIR_CONDITIONER AS SELECT * FROM CSVREAD('./database/ESC_Room_Air_Conditioners.csv');");
        command("CREATE TABLE AIR_CLEANER AS SELECT * FROM CSVREAD('./database/ESC_Room_Air_Cleaners.csv');");
        command("CREATE TABLE WASHING_MACHINE AS SELECT * FROM CSVREAD('./database/ESC_Residential_Clothes_Washers.csv');");
        command("CREATE TABLE DRYING_MACHINE AS SELECT * FROM CSVREAD('./database/ESC_Residential_Clothes_Dryers.csv');");
        command("CREATE TABLE REFRIGERATOR AS SELECT * FROM CSVREAD('./database/ESC_Residential_Refrigerators.csv');");
        command("CREATE TABLE FREEZER AS SELECT * FROM CSVREAD('./database/ESC_Residential_Freezers.csv');");
        command("CREATE TABLE DISHWASHER AS SELECT * FROM CSVREAD('./database/ESC_Residential_Dishwashers.csv');");
    }

    /*Edited By Devon, 12/12/2017
      updated query attribute names to match new CSV files
     */
    /**
     *  AIRCONDITIONER(model, brand, energy)    energy = watts
     *  WASHINGMACHINE(model, brand, energy)    energy = cubic_feet_per_kilowatt_hour_per_cycle
     *  REFRIGERATOR(mode, brand, energy)       energy = Annual_Energy_Use__Kilowatt_Hours_Year__d
     *
     * @param s - type of appliance
     * @return query string for sql
     */
    private String generateSelectQuery(String s) {
        String query = "";

        switch (s) {
            case "airconditioner":
                query = "SELECT Model_Number AS model," +
                                "Brand_Name AS brand," +
                                "Estimated_Annual_Energy_Use AS energy" +
                        " FROM AIR_CONDITIONER";
                break;
            case "washingmachine":
                query = "SELECT Model_Number AS model," +
                        "Brand_Name AS brand," +
                        "Estimated_Annual_Energy_Use AS energy" +
                        " FROM WASHING_MACHINE";
                break;
            case "refrigerator":
                query = "SELECT  Individual_Model_Number_Covered_by_Basic_Model_m AS model," +
                                "Brand_Name_s__s AS brand," +
                                "Estimated_Annual_Energy_Use AS energy" +
                        " FROM REFRIGERATOR";
                break;
        }

        return query;
    }

}

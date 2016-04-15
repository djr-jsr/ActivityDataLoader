/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  android.os.Environment
 *  java.io.File
 *  java.io.FileOutputStream
 *  java.io.OutputStream
 *  java.io.PrintStream
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.util.Date
 */
package com.example.snigdhanup.activitydataloader;

import android.os.Environment;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Date;

public class DataWriter {
    private static File archiveDir;
    private static File newFolder;
    private static File toBeUploadedDir;
    private File activityfile;
    public String device;
    private File eaccfile;
    private File ecomfile;
    private File elaccfile;
    private File emagfile;
    private File gpsfile;
    private File taccfile;
    private File tcomfile;
    private File tlaccfile;
    private File tmagfile;

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public DataWriter(String string2) {
        try {
            newFolder = new File(Environment.getExternalStorageDirectory(), "ActivityDataLogger1");
            if (!newFolder.exists()) {
                newFolder.mkdir();
            }
            if (!(DataWriter.toBeUploadedDir = new File(newFolder, "ToBeUploaded")).exists()) {
                toBeUploadedDir.mkdir();
            }
            if (!(DataWriter.archiveDir = new File(newFolder, "Archive")).exists()) {
                archiveDir.mkdir();
            }
        }
        catch (Exception var2_3) {
            System.out.println("creating file error" + var2_3.toString());
        }
        this.device = string2;
        long l = new Date().getTime() / 1000;
        this.gpsfile = new File(toBeUploadedDir, this.device + "_" + l + "_GPS.txt");
        this.activityfile = new File(toBeUploadedDir, this.device + "_training_" + l + "_Activity.txt");
        this.taccfile = new File(toBeUploadedDir, this.device + "_training_" + l + "_ACC.txt");
        this.tlaccfile = new File(toBeUploadedDir, this.device + "_training_" + l + "_LACC.txt");
        this.tmagfile = new File(toBeUploadedDir, this.device + "_training_" + l + "_MAG.txt");
        this.tcomfile = new File(toBeUploadedDir, this.device + "_training_" + l + "_COM.txt");
        try {
            if (!this.gpsfile.exists()) {
                this.gpsfile.createNewFile();
            }
            if (!this.activityfile.exists()) {
                this.activityfile.createNewFile();
            }
            if (!this.taccfile.exists()) {
                this.taccfile.createNewFile();
            }
            if (!this.tlaccfile.exists()) {
                this.tlaccfile.createNewFile();
            }
            if (!this.tmagfile.exists()) {
                this.tmagfile.createNewFile();
            }
            if (!this.tcomfile.exists()) {
                this.tcomfile.createNewFile();
            }
            return;
        }
        catch (Exception var5_4) {
            var5_4.printStackTrace();
            System.out.println("Fail file");
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void writeAccData(float f, float f2, float f3, boolean bl) {
        File file = bl ? this.taccfile : this.eaccfile;
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file, true);
            PrintStream printStream = new PrintStream((OutputStream)fileOutputStream);
            printStream.println("" + f + " " + f2 + " " + f3 + " " + new Date().toString());
            fileOutputStream.close();
            printStream.close();
            return;
        }
        catch (Exception var8_8) {
            var8_8.printStackTrace();
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void writeComData(float f, boolean bl) {
        File file = bl ? this.tcomfile : this.ecomfile;
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file, true);
            PrintStream printStream = new PrintStream((OutputStream)fileOutputStream);
            printStream.println("" + f + " " + new Date().toString());
            fileOutputStream.close();
            printStream.close();
            return;
        }
        catch (Exception var6_6) {
            var6_6.printStackTrace();
            return;
        }
    }

    public void writeGpsData(double d, double d2) {
        File file = this.gpsfile;
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file, true);
            PrintStream printStream = new PrintStream((OutputStream)fileOutputStream);
            printStream.println("" + d + " " + d2 + " " + new Date().toString());
            fileOutputStream.close();
            printStream.close();
            return;
        }
        catch (Exception var8_6) {
            var8_6.printStackTrace();
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void writeLaccData(float f, float f2, float f3, boolean bl) {
        File file = bl ? this.tlaccfile : this.elaccfile;
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file, true);
            PrintStream printStream = new PrintStream((OutputStream)fileOutputStream);
            printStream.println("" + f + " " + f2 + " " + f3 + " " + new Date().toString());
            fileOutputStream.close();
            printStream.close();
            return;
        }
        catch (Exception var8_8) {
            var8_8.printStackTrace();
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void writeMagData(float f, float f2, float f3, boolean bl) {
        File file = bl ? this.tmagfile : this.emagfile;
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file, true);
            PrintStream printStream = new PrintStream((OutputStream)fileOutputStream);
            printStream.println("" + f + " " + f2 + " " + f3 + " " + new Date().toString());
            fileOutputStream.close();
            printStream.close();
            return;
        }
        catch (Exception var8_8) {
            var8_8.printStackTrace();
            return;
        }
    }

    public void writeVehicleData(String string2) {
        File file = this.activityfile;
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file, true);
            PrintStream printStream = new PrintStream((OutputStream)fileOutputStream);
            printStream.println("Activity: " + string2 + " Time: " + new Date().toString());
            fileOutputStream.close();
            printStream.close();
            return;
        }
        catch (Exception var5_5) {
            var5_5.printStackTrace();
            return;
        }
    }
}


package com.sunqiang.bluetooth.database;

import java.io.File;
import java.security.Timestamp;

/**
 * Created by sunqiang on 2017/2/17.
 */

public class Record {

    private String id;// varchar(20),
    private String name;// varchar(20),
    private int pose;// int,
    private int position;// int,
    private Timestamp recordTime;// record_time timestamp,
    private String result;// varchar(20),
    private String mark;// varchar(100),
    private String advise;// varchar(50),
    private int score;// int,
    private File file;// blob
    private Callback callback;
    public enum Pose{
        STANDING(""),SITTING(""),SIEDLIE("");

        private Pose(String hah){

        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPose() {
        return pose;
    }

    public void setPose(int pose) {
        this.pose = pose;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Timestamp getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(Timestamp recordTime) {
        this.recordTime = recordTime;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getAdvise() {
        return advise;
    }

    public void setAdvise(String advise) {
        this.advise = advise;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public interface Callback{
        void gotoview();
    }

    @Override
    public String toString() {
        return "Record{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", pose=" + pose +
                ", position=" + position +
                ", recordTime=" + recordTime +
                ", result='" + result + '\'' +
                ", mark='" + mark + '\'' +
                ", advise='" + advise + '\'' +
                ", score=" + score +
                ", file=" + file +
                '}';
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}

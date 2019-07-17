package com.nenu.domain;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

public class GoldWeight implements Serializable {
	

	@Id
	@GeneratedValue
	private int goldWeight_ID;//id
	private double goldWeight_start;//金重区间上限
	private double goldWeight_end;//金重区间下限

    public int getGoldWeight_ID() {
        return goldWeight_ID;
    }

    public void setGoldWeight_ID(int goldWeight_ID) {
        this.goldWeight_ID = goldWeight_ID;
    }

    public double getGoldWeight_start() {
        return goldWeight_start;
    }

    public void setGoldWeight_start(double goldWeight_start) {
        this.goldWeight_start = goldWeight_start;
    }

    public double getGoldWeight_end() {
        return goldWeight_end;
    }

    public void setGoldWeight_end(double goldWeight_end) {
        this.goldWeight_end = goldWeight_end;
    }
}

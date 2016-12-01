package com.sprocomm.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import android.util.Log;

public class CpuInfo {
	
	private static String TAG = "CpuInfo";
	/** get CPU rate
     * @return
     */
    public int getProcessCpuRate() {

        StringBuilder tv = new StringBuilder();
        int rate = 0;

        try {
            String Result;
            Process p;
            p = Runtime.getRuntime().exec("top -n 1");

            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((Result = br.readLine()) != null) {
                if (Result.trim().length() < 1) {
                    continue;
                } else {
                    String[] CPUusr = Result.split("%");
                    tv.append("USER:" + CPUusr[0] + "\n");
                    String[] CPUusage = CPUusr[0].split("User");
                    String[] SYSusage = CPUusr[1].split("System");
                    tv.append("CPU:" + CPUusage[1].trim() + " length:" + CPUusage[1].trim().length() + "\n");
                    tv.append("SYS:" + SYSusage[1].trim() + " length:" + SYSusage[1].trim().length() + "\n");

                    rate = Integer.parseInt(CPUusage[1].trim()) + Integer.parseInt(SYSusage[1].trim());
                    break;
                }
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Log.d("AgingTest", TAG+"--cpu--rate--->"+rate);
        return rate;
    }

}

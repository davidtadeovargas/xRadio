package com.ypyglobal.xradio.firebasemsg;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;


public class XRadioJobService extends JobService {
    @Override
    public boolean onStartJob(JobParameters job) {
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return false;
    }
}

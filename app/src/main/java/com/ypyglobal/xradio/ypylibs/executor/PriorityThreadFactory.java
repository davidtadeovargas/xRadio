/*
 * Copyright (c) 2017. YPY Global - All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at.
 *
 *         http://ypyglobal.com/sourcecode/policy
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.ypyglobal.xradio.ypylibs.executor;

import android.os.Process;

import java.util.concurrent.ThreadFactory;


public class PriorityThreadFactory implements ThreadFactory {

    private final int mThreadPriority;
    private Thread mThread;

    public PriorityThreadFactory(int threadPriority) {
        mThreadPriority = threadPriority;
    }

    @Override
    public Thread newThread(final Runnable runnable) {
        Runnable wrapperRunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Process.setThreadPriority(mThreadPriority);
                }
                catch (Throwable t) {
                    t.printStackTrace();

                }
                runnable.run();
            }
        };
        mThread=new Thread(wrapperRunnable);
        return mThread;

    }
    public void onDestroy(){
        try{
            if(mThread!=null){
                mThread.interrupt();
                mThread=null;
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
}

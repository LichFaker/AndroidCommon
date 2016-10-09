package com.lichfaker.common.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.lichfaker.common.utils.LogUtils;
import com.lichfaker.common.utils.NetUtils;

import java.util.ArrayList;

/**
 * @author LichFaker on 16/3/16.
 * @Email lichfaker@gmail.com
 */
public class NetStateReceiver extends BroadcastReceiver {

    public interface NetChangeObserver {
        void onNetConnected();
        void onNetDisConnect();
    }

    public final static String CUSTOM_ANDROID_NET_CHANGE_ACTION = "com.lichfaker.library.net.conn.CONNECTIVITY_CHANGE";
    private final static String ANDROID_NET_CHANGE_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";

    private static boolean isNetAvailable = false;
    private static ArrayList<NetChangeObserver> mNetChangeObservers;
    private static BroadcastReceiver mBroadcastReceiver;

    private static BroadcastReceiver getReceiver() {
        if (null == mBroadcastReceiver) {
            synchronized (NetStateReceiver.class) {
                if (null == mBroadcastReceiver) {
                    mBroadcastReceiver = new NetStateReceiver();
                }
            }
        }
        return mBroadcastReceiver;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            if (intent.getAction().equalsIgnoreCase(ANDROID_NET_CHANGE_ACTION)
                    || intent.getAction().equalsIgnoreCase(CUSTOM_ANDROID_NET_CHANGE_ACTION)) {
                if (!NetUtils.isAvailable(context)) {
                    LogUtils.i("<---- Network disconnected --->");
                    isNetAvailable = false;
                } else {
                    LogUtils.i("<---- Network connected --->");
                    isNetAvailable = true;
                }
                notifyObserver();
            }
        }
    }

    public static void registerNetworkStateReceiver(Context context) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(CUSTOM_ANDROID_NET_CHANGE_ACTION);
        filter.addAction(ANDROID_NET_CHANGE_ACTION);
        context.getApplicationContext().registerReceiver(getReceiver(), filter);
    }

    public static void unRegisterNetworkStateReceiver(Context context) {
        if (mBroadcastReceiver != null) {
            try {
                context.getApplicationContext().unregisterReceiver(mBroadcastReceiver);
            } catch (Exception e) {
                LogUtils.e(e.getMessage());
            }
        }
    }

    public static void checkNetworkState(Context context) {
        Intent intent = new Intent();
        intent.setAction(CUSTOM_ANDROID_NET_CHANGE_ACTION);
        context.sendBroadcast(intent);
    }

    public static boolean isNetworkAvailable() {
        return isNetAvailable;
    }

    private void notifyObserver() {
        if (!mNetChangeObservers.isEmpty()) {
            for (NetChangeObserver observer : mNetChangeObservers) {
                try {
                    if (observer != null) {
                        if (isNetAvailable) {
                            observer.onNetConnected();
                        } else {
                            observer.onNetDisConnect();
                        }
                    }
                } catch (Exception e) {

                }
            }
        }
    }

    public static void registerObserver(NetChangeObserver observer) {
        if (mNetChangeObservers == null) {
            mNetChangeObservers = new ArrayList<>();
        }
        mNetChangeObservers.add(observer);
    }

    public static void removeRegisterObserver(NetChangeObserver observer) {
        if (mNetChangeObservers != null) {
            if (mNetChangeObservers.contains(observer)) {
                mNetChangeObservers.remove(observer);
            }
        }
    }
}

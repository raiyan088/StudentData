package com.rr.student.data;

import android.app.Application;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static boolean checkInternet() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface interf = interfaces.nextElement();
                if (interf.isUp() && !interf.isLoopback()) {
                    List<InterfaceAddress> adrs = interf.getInterfaceAddresses();
                    for (Iterator<InterfaceAddress> iter = adrs.iterator(); iter.hasNext();) {
                        InterfaceAddress adr = iter.next();
                        InetAddress inadr = adr.getAddress();
                        if (inadr instanceof Inet4Address) return true;
                    }
                }
            }
        } catch (Exception e) {}
        return false;
    }
}

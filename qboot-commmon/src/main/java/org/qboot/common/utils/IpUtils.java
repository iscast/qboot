package org.qboot.common.utils;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

/**
 * @Description:
 * @Author: iscast
 * @Date: 2020/8/22 19:22
 */
public class IpUtils {
    public IpUtils() {
    }

    public static String getIpAddr(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }

        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }

        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if (ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1")) {
                InetAddress inet = null;

                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException var4) {
                    var4.printStackTrace();
                }

                ipAddress = inet.getHostAddress();
            }
        }

        String[] ips = StringUtils.split(ipAddress, ",");
        return ips[0];
    }

    public static String getLocalIp() {
        String localip = null;
        String netip = null;

        try {
            Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip = null;
            boolean finded = false;

            while(true) {
                while(netInterfaces.hasMoreElements() && !finded) {
                    NetworkInterface ni = (NetworkInterface)netInterfaces.nextElement();
                    Enumeration address = ni.getInetAddresses();

                    while(address.hasMoreElements()) {
                        ip = (InetAddress)address.nextElement();
                        if (!ip.isSiteLocalAddress() && !ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":") == -1) {
                            netip = ip.getHostAddress();
                            finded = true;
                            break;
                        }

                        if (ip.isSiteLocalAddress() && !ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":") == -1) {
                            localip = ip.getHostAddress();
                        }
                    }
                }

                return netip != null && !"".equals(netip) ? netip : localip;
            }
        } catch (SocketException var7) {
            var7.printStackTrace();
            return netip != null && !"".equals(netip) ? netip : localip;
        }
    }
}

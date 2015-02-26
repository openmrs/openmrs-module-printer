package org.openmrs.module.printer.handler;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.module.printer.Printer;
import org.openmrs.module.printer.PrinterConstants;
import org.openmrs.module.printer.UnableToPrintViaSocketException;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Map;

public class SocketPrintHandler implements PrintHandler {

    private final Log log = LogFactory.getLog(getClass());

    @Override
    public String getDisplayName() {
        return "Socket Print Handler";
    }

    @Override
    public String getBeanName() {
        return PrinterConstants.SOCKET_PRINT_HANDLER_BEAN_NAME;
    }

    @Override
    public void print(Printer printer, Map<String, Object> paramMap) throws UnableToPrintViaSocketException
    {

        Socket socket = new Socket();
        String encoding = paramMap.containsKey("encoding") ? (String) paramMap.get("encoding") : "Windows-1252";
        Integer wait = paramMap.containsKey("wait") ? (Integer) paramMap.get("wait") : null;
        String data = (String) paramMap.get("data");

        // Create a socket with a timeout
        try {

            InetAddress addr = InetAddress.getByName(printer.getIpAddress());
            SocketAddress sockaddr = new InetSocketAddress(addr, Integer.valueOf(printer.getPort()));

            // This method will block no more than timeoutMs
            // If the timeout occurs, SocketTimeoutException is thrown.
            int timeoutMs = 1000;   // 1s
            socket.connect(sockaddr, timeoutMs);

            if (encoding.equals("Windows-1252")) {
                IOUtils.write(data.toString().getBytes("Windows-1252"), socket.getOutputStream());
            } else {
                IOUtils.write(data.toString(), socket.getOutputStream(), encoding);
            }

            // wait before returning if a wait to specified (we hold lock on printer during that time)
            if (wait != null) {
                Thread.sleep(wait);
            }

        } catch (Exception e) {
            throw new UnableToPrintViaSocketException("Unable to print to printer " + printer.getName(), e);
        } finally {
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                log.error("failed to close the socket to printer " + printer.getName(), e);
            }
        }

    }

}

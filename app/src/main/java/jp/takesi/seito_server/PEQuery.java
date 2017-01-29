package jp.takesi.seito_server;

import android.app.DownloadManager;

import java.net.BindException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import jp.takesi.seito_server.Utils;

public class PEQuery {
    final static byte HANDSHAKE = 9;
    final static byte STAT = 0;
    private MainActivity MainActivity;

    String serverAddress = "101.143.25.2";// localhost 101.143.28.97
    int queryPort = 19132; // the default minecraft query port 25565

    int localPort = 19132; // the local port we're connected to the server on 25566

    private DatagramSocket socket = null; // prevent socket already bound
    // exception
    private int token;

    public PEQuery(String address, int port) {
        serverAddress = address;
        queryPort = port;
    }

    public boolean hand(String result) {
        Request req = new Request();
        req.type = HANDSHAKE;
        req.sessionID = generateSessionID();
        int val = 11 - req.toBytes().length; // should be 11 bytes total
        byte[] input = Utils.padArrayEnd(req.toBytes(), val);

        if (sendUDP(input)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean sendUDP(byte[] input) {
        try {
            while (socket == null) {
                try {
                    socket = new DatagramSocket(localPort); // create the socket
                } catch (BindException e) {
                    ++localPort; // increment if port is already in use
                }
            }

            // create a packet from the input data and send it on the socket
            InetAddress address = InetAddress.getByName(serverAddress);
            DatagramPacket packet1 = new DatagramPacket(input, input.length,
                    address, queryPort);
            socket.send(packet1);

            // receive a response in a new packet
            byte[] out = new byte[1024 * 100]; // TODO guess at max size
            DatagramPacket packet = new DatagramPacket(out, out.length);
            socket.setSoTimeout(5000); // one half second timeout
            socket.receive(packet);

            return true;
        } catch (SocketException e) {
            return false;
        } catch (SocketTimeoutException e) {
            return false;
        } catch (UnknownHostException e) {
            return false;
        } catch (Exception e) // any other exceptions that may occur
        {
            return false;
        }
    }

    private int generateSessionID() {
		/*
		 * Can be anything, so we'll just use 1 for now. Apparently it can be
		 * omitted altogether. TODO: increment each time, or use a random int
		 */
        return 1;
    }

    @Override
    public void finalize() {
        socket.close();
    }
}

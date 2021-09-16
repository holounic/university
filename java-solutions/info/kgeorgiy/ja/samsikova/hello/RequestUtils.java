package info.kgeorgiy.ja.samsikova.hello;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class RequestUtils {

    private static final Charset CHARSET = StandardCharsets.UTF_8;
    public static final int TIMEOUT = 100;

    private RequestUtils() {}

    public static String formRequest(String prefix, int thread, int id) {
        return String.format("%s%d_%d", prefix, thread, id);
    }

    public static String formResponse(String request) {
        return String.format("Hello, %s", request);
    }

    public static String extractData(DatagramPacket packet) {
        return new String(packet.getData(), packet.getOffset(), packet.getLength(), CHARSET);
    }

    public static void setData(DatagramPacket packet, String data) {
        packet.setData(data.getBytes(CHARSET));
    }

    public static void send(DatagramPacket packet, DatagramSocket socket, String data) throws IOException {
        setData(packet, data);
        socket.send(packet);
    }

    public static String receive(DatagramPacket packet, DatagramSocket socket, byte[] data) throws IOException {
        packet.setData(data);
        socket.receive(packet);
        return extractData(packet);
    }

    public static void shutdown(ExecutorService service) {
        service.shutdown();
        while (true) {
            try {
                if (service.awaitTermination(TIMEOUT, TimeUnit.SECONDS)) {
                    break;
                }
                System.out.println("Service is not responding");
            } catch (InterruptedException e) {
                System.out.println("Service was silent for too long");
            }
        }
    }

}

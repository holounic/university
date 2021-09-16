package info.kgeorgiy.ja.samsikova.hello;

import info.kgeorgiy.java.advanced.hello.HelloServer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public class HelloUDPServer implements HelloServer {

    private DatagramSocket socket;
    private ExecutorService executorService;

    public static void main(String[] args) {
        new HelloUDPServer().start(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
    }

    @Override
    public void start(int port, int threads) {
        executorService = Executors.newFixedThreadPool(threads);
        try {
            socket = new DatagramSocket(port);
            IntStream.range(0, threads)
                    .forEach(x -> executorService.submit(this::process));
        } catch (SocketException e) {
            System.out.println("Failed to create socket");
        }
    }

    @Override
    public void close() {
        socket.close();
        RequestUtils.shutdown(executorService);
    }

    private void process() {
        int bufferSize = 0;
        try {
            bufferSize = socket.getReceiveBufferSize();
        } catch (SocketException e) {
            System.out.println("Failed to crate UDP");
            return;
        }
        byte[] receiveBuffer = new byte[bufferSize];
        DatagramPacket packet = new DatagramPacket(receiveBuffer, receiveBuffer.length);
        while (!Thread.interrupted() && !socket.isClosed()) {
            try {
                String requestBody = RequestUtils.receive(packet, socket, receiveBuffer);
                String responseBody = RequestUtils.formResponse(requestBody);
                RequestUtils.send(packet, socket, responseBody);
            } catch (IOException e) {
                System.out.println("Failed while receiving request");
            }
        }
    }
}

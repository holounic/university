package info.kgeorgiy.ja.samsikova.hello;

import info.kgeorgiy.java.advanced.hello.HelloClient;

import java.io.IOException;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public class HelloUDPClient implements HelloClient {
    public static void main(String[] args) {
        new HelloUDPClient().run(args[0], Integer.parseInt(args[1]), args[2], Integer.parseInt(args[3]),
                Integer.parseInt(args[4]));
    }

    @Override
    public void run(String host, int port, String prefix, int threads, int requests) {
        SocketAddress address = new InetSocketAddress(host, port);
        ExecutorService executorService = Executors.newFixedThreadPool(threads);
        IntStream.range(0, threads).forEach(x -> executorService.submit(
                () -> processThread(x, prefix, requests, address)));
        RequestUtils.shutdown(executorService);
    }

    private void processThread(int threadId, String prefix, int requests, SocketAddress address) {
        try (DatagramSocket socket = new DatagramSocket()) {
            socket.setSoTimeout(RequestUtils.TIMEOUT);
            DatagramPacket packet = new DatagramPacket(new byte[0], 0, address);
            byte[] receiveBuffer = new byte[socket.getReceiveBufferSize()];
            IntStream.range(0, requests).forEach(x -> {
                String requestBody = RequestUtils.formRequest(prefix, threadId, x);
                while (!Thread.interrupted() && !socket.isClosed()) {
                    try {
                        RequestUtils.send(packet, socket, requestBody);
                        String responseBody = RequestUtils.receive(packet, socket, receiveBuffer);
                        if (isResponseValid(requestBody, responseBody)) {
                            break;
                        }
                    } catch (IOException e) {
                         System.out.println("Failed while waiting for response");
                    }
                }
            });
        } catch (SocketException e) {
            System.out.println("Error creating socket");
        }

    }

    private boolean isResponseValid(String requestBody, String responseBody) {
        return responseBody.contains(requestBody);
    }
}

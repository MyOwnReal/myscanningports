import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Scanning { public boolean portIsOpen(String ip, int port, int timeout) {

try {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(ip, port), timeout);
            socket.close();
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
    public static Future<Boolean> portIsOpen(final ExecutorService es, final String ip, final int port, final int timeout) {
        return es.submit(() -> {
            try {
                Socket socket = new Socket();
                socket.connect(new InetSocketAddress(ip, port), timeout);
                socket.close();
                return true;
            } catch (Exception ex) {
                return false;

            }
        });
    }
    public static void main(final String... args) throws ExecutionException, InterruptedException {
        final ExecutorService es = Executors.newFixedThreadPool(20);
        final String ip = "127.0.0.1";
        final int timeout = 200;
        final List<Future<Boolean>> futures = new ArrayList<>();
        for (int port = 1; port <= 65535; port++) {
            futures.add(portIsOpen(es, ip, port, timeout));
        }
        es.shutdown();
        int openPorts = 0;
        for (final Future<Boolean> f : futures) {
            if (f.get()) {
                openPorts++;
            }
        }
        System.out.println("There are " + openPorts + " open ports on host " + ip + " (probed with a timeout of " + timeout + "ms)");
    }
    }
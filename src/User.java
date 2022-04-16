import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class User {
    private static final int PORT = 23456;
    private static final String HOST = "127.0.0.1";

    public static void main(String[] args) {
        InetSocketAddress socketAddress = new InetSocketAddress(HOST, PORT);
        try (SocketChannel socketChannel = SocketChannel.open(); Scanner scanner = new Scanner(System.in)) {
            socketChannel.connect(socketAddress);
            final ByteBuffer inputBuffer = ByteBuffer.allocate(2 << 10);

            String msg;
            while (true) {
                System.out.println("Ведите текст, в котором необходимо удалить пробелы или введите \"end\" для " +
                        "завершения работы с сервером. ");
                msg = scanner.nextLine();
                if ("end".equals(msg)) {
                    System.out.println("Завершение работы клиента.");
                    break;
                }
                socketChannel.write(
                        ByteBuffer.wrap(msg.getBytes(StandardCharsets.UTF_8)));
                Thread.sleep(3000);
                int bytesCount = socketChannel.read(inputBuffer);
                System.out.println(new String(inputBuffer.array(), 0, bytesCount, StandardCharsets.UTF_8).trim());
                inputBuffer.clear();
            }
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }
}

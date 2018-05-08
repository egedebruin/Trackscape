import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class Main {

    public static void main(String[] args) throws IOException {
        String string = "https://www.howtogeek.com/howto/28609/how-can-i-tell-what-is-listening-on-a-tcpip-port-in-windows/";
        String file = "C:\\Users\\Egel\\Documents\\Repositories\\Trackscape\\webcast.mov";
        URL address = new URL("file:///" + file);
        InputStream in = address.openStream();
    }
}

package fitnesse.responders.run;

import java.net.Socket;

public interface SocketDoner {
   Socket donateSocket();

   void finishedWithSocket() throws Exception;
}
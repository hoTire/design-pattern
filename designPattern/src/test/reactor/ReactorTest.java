package reactor;

import java.io.IOException;

public class ReactorTest {

  public static void main(String args[]) throws IOException {
    Reactor reactor = new Reactor(8080);
    reactor.run();
  }
}
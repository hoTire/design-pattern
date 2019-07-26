package reactor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * http://i5on9i.blogspot.com/2013/11/reactor-pattern.html
 */

public class Reactor implements Runnable {
  final Selector selector;
  final ServerSocketChannel serverSocket;

  public Reactor(int port) throws IOException {
    selector = Selector.open();
    serverSocket = ServerSocketChannel.open();
    serverSocket.socket().bind(
      new InetSocketAddress(port));
    serverSocket.configureBlocking(false);
    SelectionKey sk =
      serverSocket.register(selector,SelectionKey.OP_ACCEPT);
    sk.attach(new Acceptor());
  }

  /*
  Alternatively, use explicit SPI provider:
  SelectorProvider p = SelectorProvider.provider();
  selector = p.openSelector();
  serverSocket = p.openServerSocketChannel();
  */
  public void run() { // normally in a new Thread
    try {
      while (!Thread.interrupted()) {
        selector.select();
        Set selected = selector.selectedKeys();
        Iterator it = selected.iterator();
        while (it.hasNext())
          dispatch((SelectionKey) it.next());
        selected.clear();
      }
    } catch (IOException ex) { /* ... */ }
  }

  void dispatch(SelectionKey k) {
    Runnable r = (Runnable) (k.attachment());
    if (r != null)
      r.run();
  }

  class Acceptor implements Runnable { // inner

    public void run() {
      try {
        SocketChannel c = serverSocket.accept();
        if (c != null)
          new Handler(selector, c);
      } catch (IOException ex) { /* ... */ }
    }
  }
}

package memento;


import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.List;
import java.util.Stack;
import org.assertj.core.util.Lists;
import org.junit.Test;

public class OriginatorTest {

  @Test
  public void test() {
    // Given
    final Stack<Memento<String>> mementos = new Stack<>();
    Originator<String> originator = new Originator<>();
    List<String> states = Lists.newArrayList("state 1", "state 2", "state 3");

    // When
    states.forEach(state -> mementos.push(originator.setState(state).createMemento()));

    // Then
    Collections.reverse(states);
    states.forEach(state -> {
      originator.resotreMemento(mementos.pop());
      assertThat(originator.getState()).isEqualTo(state);
    });
  }
}
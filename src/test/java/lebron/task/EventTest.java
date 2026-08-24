package lebron.task;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import lebron.exception.LeBronException;

public class EventTest {

    @Test
    public void getTypeIcon_returnsE() throws LeBronException {
        Event event = new Event("project meeting",
                TaskDateTime.parseInput("6/8/2019 1400"), TaskDateTime.parseInput("6/8/2019 1600"));

        assertEquals("E", event.getTypeIcon());
    }

    @Test
    public void getFromAndGetTo_returnConstructorValues() throws LeBronException {
        TaskDateTime from = TaskDateTime.parseInput("6/8/2019 1400");
        TaskDateTime to = TaskDateTime.parseInput("6/8/2019 1600");
        Event event = new Event("project meeting", from, to);

        assertEquals(from, event.getFrom());
        assertEquals(to, event.getTo());
    }

    @Test
    public void toString_includesFromAndToSuffix() throws LeBronException {
        Event event = new Event("project meeting",
                TaskDateTime.parseInput("6/8/2019 1400"), TaskDateTime.parseInput("6/8/2019 1600"));

        assertEquals("[E][ ] project meeting (from: Aug 06 2019, 2:00pm to: Aug 06 2019, 4:00pm)",
                event.toString());
    }

    @Test
    public void toString_done_includesXStatusAndFromToSuffix() throws LeBronException {
        Event event = new Event("project meeting",
                TaskDateTime.parseInput("6/8/2019"), TaskDateTime.parseInput("7/8/2019"));
        event.mark();

        assertEquals("[E][X] project meeting (from: Aug 06 2019 to: Aug 07 2019)", event.toString());
    }
}

package lebron.task;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import lebron.exception.LeBronException;

public class DeadlineTest {

    @Test
    public void getTypeIcon_returnsD() throws LeBronException {
        Deadline deadline = new Deadline("return book", TaskDateTime.parseInput("2/12/2019"));

        assertEquals("D", deadline.getTypeIcon());
    }

    @Test
    public void getBy_returnsConstructorValue() throws LeBronException {
        TaskDateTime by = TaskDateTime.parseInput("2/12/2019");
        Deadline deadline = new Deadline("return book", by);

        assertEquals(by, deadline.getBy());
    }

    @Test
    public void toString_includesByDateSuffix() throws LeBronException {
        Deadline deadline = new Deadline("return book", TaskDateTime.parseInput("2/12/2019"));

        assertEquals("[D][ ] return book (by: Dec 02 2019)", deadline.toString());
    }

    @Test
    public void toString_done_includesXStatusAndByDateSuffix() throws LeBronException {
        Deadline deadline = new Deadline("return book", TaskDateTime.parseInput("2/12/2019 1800"));
        deadline.mark();

        assertEquals("[D][X] return book (by: Dec 02 2019, 6:00pm)", deadline.toString());
    }
}

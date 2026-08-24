package lebron.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import lebron.exception.LeBronException;

public class TaskDateTimeTest {

    @Test
    public void parseInput_dateOnly_returnsDateWithoutTime() throws LeBronException {
        TaskDateTime dateTime = TaskDateTime.parseInput("2/12/2019");

        assertEquals("Dec 02 2019", dateTime.toString());
    }

    @Test
    public void parseInput_dateAndTime_returnsDateWithTime() throws LeBronException {
        TaskDateTime dateTime = TaskDateTime.parseInput("2/12/2019 1800");

        assertEquals("Dec 02 2019, 6:00pm", dateTime.toString());
    }

    @Test
    public void parseInput_invalidText_throwsLeBronException() {
        assertThrows(LeBronException.class, () -> TaskDateTime.parseInput("not a date"));
    }

    @Test
    public void parseInput_wrongSeparator_throwsLeBronException() {
        assertThrows(LeBronException.class, () -> TaskDateTime.parseInput("2-12-2019"));
    }

    @Test
    public void parseInput_incompleteTime_throwsLeBronException() {
        assertThrows(LeBronException.class, () -> TaskDateTime.parseInput("2/12/2019 18"));
    }

    @Test
    public void parseStorage_isoDateOnly_returnsDateWithoutTime() throws LeBronException {
        TaskDateTime dateTime = TaskDateTime.parseStorage("2019-12-02");

        assertEquals("Dec 02 2019", dateTime.toString());
    }

    @Test
    public void parseStorage_isoDateTime_returnsDateWithTime() throws LeBronException {
        TaskDateTime dateTime = TaskDateTime.parseStorage("2019-12-02T18:00");

        assertEquals("Dec 02 2019, 6:00pm", dateTime.toString());
    }

    @Test
    public void parseStorage_invalidText_throwsLeBronException() {
        assertThrows(LeBronException.class, () -> TaskDateTime.parseStorage("garbage"));
    }

    @Test
    public void toStorageString_dateOnly_roundTripsThroughParseStorage() throws LeBronException {
        TaskDateTime original = TaskDateTime.parseInput("2/12/2019");

        TaskDateTime roundTripped = TaskDateTime.parseStorage(original.toStorageString());

        assertEquals(original.toString(), roundTripped.toString());
    }

    @Test
    public void toStorageString_dateAndTime_roundTripsThroughParseStorage() throws LeBronException {
        TaskDateTime original = TaskDateTime.parseInput("2/12/2019 1800");

        TaskDateTime roundTripped = TaskDateTime.parseStorage(original.toStorageString());

        assertEquals(original.toString(), roundTripped.toString());
    }

    @Test
    public void toString_dateOnly_omitsTime() throws LeBronException {
        TaskDateTime dateTime = TaskDateTime.parseInput("6/6/2019");

        assertEquals("Jun 06 2019", dateTime.toString());
    }

    @Test
    public void toString_dateAndTime_lowercasesAmPm() throws LeBronException {
        TaskDateTime dateTime = TaskDateTime.parseInput("6/6/2019 0900");

        assertEquals("Jun 06 2019, 9:00am", dateTime.toString());
    }
}

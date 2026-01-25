package peggy;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

public class ParserTest {

    @Test
    void parseIndex_validIndex_returnsZeroBasedIndex() {
        int idx = Parser.parseIndex("mark 2", 5, "mark");
        assertEquals(1, idx);
    }

    @Test
    void parseIndex_outOfRange_throws() {
        assertThrows(IllegalArgumentException.class,
                () -> Parser.parseIndex("mark 10", 3, "mark"));
    }

    @Test
    void parseDeadline_validInput_splitsCorrectly() {
        String[] out = Parser.parseDeadline("deadline return book /by 2/12/2019 1800");
        assertEquals("return book", out[0]);
        assertEquals("2/12/2019 1800", out[1]);
    }

    @Test
    void parseDateTime_dayMonthYearWithTime_parsesCorrectly() {
        // expects 2 Dec 2019, 18:00
        LocalDateTime dt = Parser.parseDateTime("2/12/2019 1800");
        assertEquals(LocalDateTime.of(2019, 12, 2, 18, 0), dt);
    }
}

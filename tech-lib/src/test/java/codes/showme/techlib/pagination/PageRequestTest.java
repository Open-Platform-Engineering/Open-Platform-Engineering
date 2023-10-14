package codes.showme.techlib.pagination;

import org.junit.Assert;
import org.junit.Test;

public class PageRequestTest {

    @Test
    public void getFirstRow() {
        PageRequest pageRequest = new PageRequest(1, 20);

        Assert.assertEquals(0, pageRequest.getFirstRow());

        PageRequest pageRequest1 = new PageRequest(-11, 20);

        Assert.assertEquals(0, pageRequest.getFirstRow());
    }
}
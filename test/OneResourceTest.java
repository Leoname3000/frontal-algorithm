import org.junit.Test;
import org.junit.Assert;

import java.util.HashMap;
import java.util.HashSet;

public class OneResourceTest {
    @Test
    public void oneResourceTest() {

        HashSet<Resource> allResources = new HashSet<>();

        Resource res = new Resource("1", allResources, 10, 18);

        ResourceGroup group = new ResourceGroup();
        group.add(res);


        HashSet<OperationLot> allLots = new HashSet<>();

        OperationLot lot1 = new OperationLot(allLots, 10, 0);

        Operation op11 = new Operation("11", lot1, group,
                new HashMap<>() {{ put(res, 3); }},
                new HashMap<>() {{ put(res, false); }});
        Operation op12 = new Operation("12", lot1, group,
                new HashMap<>() {{ put(res, 6); }},
                new HashMap<>() {{ put(res, true); }});
        Operation op13 = new Operation("13", lot1, group,
                new HashMap<>() {{ put(res, 6); }},
                new HashMap<>() {{ put(res, false); }});

        OperationLot lot2 = new OperationLot(allLots, 12, 0);

        Operation op21 = new Operation("21", lot2, group,
                new HashMap<>() {{ put(res, 2); }},
                new HashMap<>() {{ put(res, false); }});
        Operation op22 = new Operation("22", lot2, group,
                new HashMap<>() {{ put(res, 7); }},
                new HashMap<>() {{ put(res, true); }});

        op11.addFollowingOperation(op12);
        op12.addPrecedentOperation(op11);
        op12.addFollowingOperation(op13);
        op13.addPrecedentOperation(op12);
        op21.addFollowingOperation(op22);
        op22.addPrecedentOperation(op21);


        Time time = new Time(allResources, 24);


        Solution calculatedSolution = FrontalAlgorithm.run(allLots, allResources, time);

        Solution expectedSolution = new Solution(new HashMap<>() {{ put(op11, new HashMap<>() {{ put(res, 10); }});
                                                                    put(op12, new HashMap<>() {{ put(res, 15); }});
                                                                    put(op13, new HashMap<>() {{ put(res, 60); }});
                                                                    put(op21, new HashMap<>() {{ put(res, 13); }});
                                                                    put(op22, new HashMap<>() {{ put(res, 37); }});
                                                                  }});

        expectedSolution.print();
        calculatedSolution.print();

        Assert.assertEquals(expectedSolution, calculatedSolution);
    }
}

import org.junit.Test;
import org.junit.Assert;

import java.util.HashMap;

public class OneResourceTest {
    @Test
    public void oneResourceTest() {

        Resource res = ResourceManager.createResource("1", 10, 18);

        ResourceGroup group = new ResourceGroup();
        group.add(res);


        OperationLot lot1 = LotManager.createLot(10, 0);

        Operation op11 = OperationManager.createOperation("11", lot1, group,
                new HashMap<>() {{ put(res, 3); }},
                new HashMap<>() {{ put(res, false); }});
        Operation op12 = OperationManager.createOperation("12", lot1, group,
                new HashMap<>() {{ put(res, 6); }},
                new HashMap<>() {{ put(res, true); }});
        Operation op13 = OperationManager.createOperation("13", lot1, group,
                new HashMap<>() {{ put(res, 6); }},
                new HashMap<>() {{ put(res, false); }});

        OperationLot lot2 = LotManager.createLot(12, 0);

        Operation op21 = OperationManager.createOperation("21", lot2, group,
                new HashMap<>() {{ put(res, 2); }},
                new HashMap<>() {{ put(res, false); }});
        Operation op22 = OperationManager.createOperation("22", lot2, group,
                new HashMap<>() {{ put(res, 7); }},
                new HashMap<>() {{ put(res, true); }});

        OperationManager.addRelation(op11, op12);
        OperationManager.addRelation(op12, op13);
        OperationManager.addRelation(op21, op22);


        Time time = new Time(ResourceManager.allResources, 24);


        Solution calculatedSolution = FrontalAlgorithm.run(LotManager.allLots, ResourceManager.allResources, time);

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

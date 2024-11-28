package core.basesyntax.service.action;

import static core.basesyntax.storage.Storage.storageOfFruits;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import core.basesyntax.model.FruitTransaction;
import core.basesyntax.model.Operation;
import core.basesyntax.service.ActionStrategy;
import core.basesyntax.service.ActionStrategyImpl;
import java.util.Map;
import org.junit.jupiter.api.Test;

class PurchaseActionTest {
    private static final Map<Operation, ActionHandler> actionHandlerMap = Map.of(
            Operation.BALANCE, new BalanceAction(),
            Operation.PURCHASE, new PurchaseAction(),
            Operation.RETURN, new ReturnAction(),
            Operation.SUPPLY, new SupplyAction()
    );
    private final ActionStrategy strategy = new ActionStrategyImpl(actionHandlerMap);

    @Test
    void checkPurchaseActionCount_Ok() {
        FruitTransaction fruitTransaction1 =
                new FruitTransaction(Operation.BALANCE, "banana", 20);
        FruitTransaction fruitTransaction2 =
                new FruitTransaction(Operation.PURCHASE, "banana", 10);
        strategy.get(fruitTransaction1.getOperation())
                .count(fruitTransaction1.getFruit(), fruitTransaction1.getQuantity());
        strategy.get(fruitTransaction2.getOperation())
                .count(fruitTransaction2.getFruit(), fruitTransaction2.getQuantity());
        int expected = 10;
        assertEquals(expected, storageOfFruits.get("banana"));
    }

    @Test
    void checkPurchaseActionCountWithSmallerAmountThatShopHas_NotOk() {
        FruitTransaction fruitTransaction1 =
                new FruitTransaction(Operation.BALANCE, "banana", 20);
        FruitTransaction fruitTransaction2 =
                new FruitTransaction(Operation.PURCHASE, "banana", 30);
        strategy.get(fruitTransaction1.getOperation())
                .count(fruitTransaction1.getFruit(), fruitTransaction1.getQuantity());
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> strategy.get(fruitTransaction2.getOperation())
                        .count(fruitTransaction2.getFruit(), fruitTransaction2.getQuantity()));
        assertEquals("Shop does not have a such amount of fruits", exception.getMessage());
    }

    @Test
    void checkBPurchaseActionGetWithWrongAmount_NotOk() {
        FruitTransaction fruitTransaction =
                new FruitTransaction(Operation.PURCHASE, "banana", -20);
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                strategy.get(fruitTransaction.getOperation())
                        .count(fruitTransaction.getFruit(), fruitTransaction.getQuantity()));

        assertEquals("You can not add negative amount of fruit,"
                + " please change your report", exception.getMessage());
    }
}

package handson.notUsed;

import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.models.state.State;
import com.commercetools.api.models.state.StateResourceIdentifierBuilder;
import com.commercetools.api.models.state.StateTypeEnum;
import handson.solutions.impl.ApiPrefixHelper;
import handson.solutions.impl.StateMachineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static handson.solutions.impl.ClientService.createApiClient;


public class Task04a_STATEMACHINE {

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {

        final String apiClientPrefix = ApiPrefixHelper.API_DEV_CLIENT_PREFIX.getPrefix();

        Logger logger = LoggerFactory.getLogger(Task04a_STATEMACHINE.class.getName());
        final ProjectApiRoot client = createApiClient(apiClientPrefix);
        final StateMachineService stateMachineService = new StateMachineService(client);

        // TODO
        // Use StateMachineService.java to create your designed order state machine
        //
        State orderPackedState =
                stateMachineService.createState("mhOrderPacked", StateTypeEnum.ORDER_STATE, true, "MH Order Packed")
                        .toCompletableFuture().get()
                        .getBody();
        State orderShippedState =
                stateMachineService.createState("mhOrderShipped", StateTypeEnum.ORDER_STATE, false, "MH Order Shipped")
                        .toCompletableFuture().get()
                        .getBody();

        logger.info("State info {}",
                stateMachineService.setStateTransitions(
                        orderPackedState,
                        Stream.of(
                                StateResourceIdentifierBuilder.of().
                                        id(orderShippedState.getId())
                                        .build()
                        )
                                .collect(Collectors.toList())
                )
                        .toCompletableFuture().get()
        );

        logger.info("State info {}",
                stateMachineService.setStateTransitions(
                                orderShippedState,
                                new ArrayList<>()
                        )
                        .toCompletableFuture().get()
        );

        client.close();
    }
}

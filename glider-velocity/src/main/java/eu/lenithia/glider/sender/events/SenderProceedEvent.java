package eu.lenithia.glider.sender.events;

import com.velocitypowered.api.event.ResultedEvent;
import eu.lenithia.glider.sender.Sender;
import lombok.Getter;
import lombok.Setter;

public class SenderProceedEvent implements ResultedEvent<ResultedEvent.GenericResult> {
    @Getter
    private final Sender sender;
    private GenericResult result = GenericResult.allowed();
    @Getter
    @Setter
    private boolean handled = false;

    public SenderProceedEvent(Sender sender) {
        this.sender = sender;
    }

    @Override
    public GenericResult getResult() {
        return result;
    }

    @Override
    public void setResult(GenericResult result) {
        this.result = result;
    }
}
`` java

import io.mailtrap.client.MailtrapClient;
import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.factory.MailtrapClientFactory;
import io.mailtrap.model.request.emails.Address;
import io.mailtrap.model.request.emails.MailtrapMail;

import java.util.List;

public class MailtrapJavaSDKTest {

    @Value("${mailtrap.token}")
    private String token;

    public static void main(String[] args) {
        final MailtrapConfig config = new MailtrapConfig.Builder()
            .token(TOKEN)
            .build();

        final MailtrapClient client = MailtrapClientFactory.createMailtrapClient(config);

        final MailtrapMail mail = MailtrapMail.builder()
            .from(new Address("hello@presencelink.app", "Mailtrap Test"))
            .to(List.of(new Address("juniornoumedem02@gmail.com")))
            .subject("You are awesome!")
            .text("Congrats for sending test email with Mailtrap!")
            .category("Integration Test")
            .build();

        try {
            System.out.println(client.send(mail));
        } catch (Exception e) {
            System.out.println("Caught exception : " + e);
        }
    }
}
``
## f6da54d4e4f0540749acb1e0b6bca5f7

## send.api.mailtrap.io

----------zhln xizi flgk jgfy
K8xT!H55rMm@p6e
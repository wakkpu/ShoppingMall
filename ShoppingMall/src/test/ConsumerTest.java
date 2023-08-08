package test;


import Entity.Consumer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import repo.ConsumerRepository;

public class ConsumerTest extends RootTest {

    ConsumerRepository consumerRepository = new ConsumerRepository();

    @Test
    public void insertTest() {

        Consumer con = Consumer.builder()
                .userEmail("test3@test.com")
                .password("1234")
                .isAdmin(false)
                .phoneNumber("1234")
                .address("1234")
                .userName("userTest")
                .membershipId(1L)
                .build();

        consumerRepository.insert(connection, con);

        Consumer select = consumerRepository.selectByEmail("test3@test.com");
        System.out.println(select);
        Assertions.assertEquals(con.getAddress(), select.getAddress());
    }
}

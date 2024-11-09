package guru.qa.niffler.test.api;

import guru.qa.niffler.api.UsersApiClient;

public class ApiTest {

    private static final UsersApiClient usersApiClient = new UsersApiClient();

//    @Order(MIN_VALUE)
//    @Test
//    void testForHw8_2() {
//        List<UserJson> userJsonList = usersApiClient.getAllUsersByUsernameAndSearchQuery("test", null);
//
//        assertThat(userJsonList)
//                .as("Список пользователей пустой")
//                .isNotEmpty();
//    }
//
//    @Order(MAX_VALUE)
//    @Test
//    void test2ForHw8_2() {
//        List<UserJson> userJsonList = usersApiClient.getAllUsersByUsernameAndSearchQuery("☺", "☺");
//
//        assertThat(userJsonList)
//                .as("Список пользователей не пустой")
//                .isEmpty();
//    }

//    @Test
//    void testSpen() {
//        SpendRepositoryHibernate spendRepositoryHibernate = new SpendRepositoryHibernate();
////        Optional<SpendEntity> spendEntity = spendRepositoryHibernate.findByUsernameAndSpendDescription("duck", "обучение");
//
//        System.out.println("123");
//    }
}

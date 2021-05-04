package shoplistgener.domain;

import shoplistgener.dao.ShoplistgenerDAO;

public class CreateTestData {
    public static void createRandomTestData(ShoplistgenerDAO sqliteHander) throws Exception {
        sqliteHander.insertTestData();
    }    
}

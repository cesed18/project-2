package ly.generalassemb.project2;

import android.app.Application;
import android.database.Cursor;
import android.test.ApplicationTestCase;

import ly.generalassemb.project2.product.database.AppDatabase;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    public void testDatabase(){
        AppDatabase database = AppDatabase.getInstance(getContext());
        Cursor cursor = database.getAllProducts();

        assertTrue(cursor.moveToFirst());

    }
}
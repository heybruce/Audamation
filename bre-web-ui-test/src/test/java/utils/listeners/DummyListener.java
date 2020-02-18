package utils.listeners;

import org.testng.ITestContext;
import org.testng.TestListenerAdapter;

public class DummyListener extends TestListenerAdapter {

    @Override
    public synchronized void onStart(ITestContext context) {
        System.out.println("hello world!!");
    }

    @Override
    public synchronized void onFinish(ITestContext context) {
        System.out.println("goodbye world!!");
    }
}


package apps;

import static apps.Application.applications;

/*
 */
public class SmartPen {
    public static void main(String[] args) {
        // each app has to do so (except the OS) to allow the OS to display all the available apps
        applications.add(new TestApp());
        applications.add(new Login());
        new OS().run();
    }
}

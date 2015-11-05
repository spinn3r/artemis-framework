package com.spinn3r.artemis.init;

/**
 *
 */
public class LauncherShutdownHook extends Thread {

    private final Launcher launcher;

    public LauncherShutdownHook( Launcher launcher) {
        super( LauncherShutdownHook.class.getName() );
        this.launcher = launcher;
    }

    @Override
    public void run() {

        try {
            launcher.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}

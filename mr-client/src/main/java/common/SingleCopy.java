package common;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

/**
 *
 * @author vova
 */
/*public class SingleCopy {

    private static File f;
    private static FileChannel channel;
    private static FileLock lock;

    public SingleCopy() {

        try {

            f = new File("RingOnRequest.lock");
            // Check if the lock exist  
            if (f.exists()) {
                // if exist try to delete it  
                f.delete();
            }
            // Try to get the lock  
            channel = new RandomAccessFile(f, "rw").getChannel();
            lock = channel.tryLock();
            if (lock == null) {
                // File is lock by other application  
                channel.close();
                JOptionPane.showMessageDialog(null,
                   "already_running" ,
                   "error_message",
                    JOptionPane.ERROR_MESSAGE);
                throw new RuntimeException("Only 1 instance of MyApp can run.");
            }
            // Add shutdown hook to release lock when application shutdown  
            ShutdownHook shutdownHook = new ShutdownHook();
            Runtime.getRuntime().addShutdownHook(shutdownHook);
            try {

                //Your application tasks here..
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(SingleCopy.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (IOException e) {
            throw new RuntimeException("Could not start process.", e);
        }
    }

    public static void unlockFile() {
        // release and delete file lock  
        try {
            if (lock != null) {
                lock.release();
                channel.close();
                f.delete();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class ShutdownHook extends Thread {
        @Override
        public void run() {
            unlockFile();
        }
    }
}*/




public class SingleCopy extends Thread {
    public static boolean f = false;
    private static FileChannel channel;
    private static FileLock lock;

    private static String path;

    public SingleCopy(String p) {
        super();
        path = p;
    }

    public static void unlockFile() {
        // release and delete file lock
        try {
            if (lock != null) {
                lock.release();
                channel.close();
                //file.delete();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            ShutdownHook shutdownHook = new ShutdownHook();
            Runtime.getRuntime().addShutdownHook(shutdownHook);
            channel = new FileOutputStream(path + "/check.lock").getChannel();
            lock = channel.tryLock();
            if (lock != null) {
                synchronized (SingleCopy.class) {
                    SingleCopy.class.wait();
                }
            } else
                channel = new FileOutputStream(path + "/check2.lock").getChannel();
            lock = channel.tryLock();
            if (lock != null) {
                synchronized (SingleCopy.class) {
                    SingleCopy.class.wait();
                }
            } else
                channel = new FileOutputStream(path + "/check3.lock").getChannel();
            lock = channel.tryLock();
            if (lock != null) {
                synchronized (SingleCopy.class) {
                    SingleCopy.class.wait();
                }
            } else
                f = true;
        } catch (Exception e) {
            System.out.println("332 " + e);
        }
    }

    static class ShutdownHook extends Thread {
        @Override
        public void run() {
            unlockFile();
        }
    }
}

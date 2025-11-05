package cipher;


import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Andy 06.06.2018 - 12:09.
 */
public class CipherReader {

    private SerialPort port;
    private String _portNumber;
    private int step = 0;
    private List<String> list;
    private int RecordCount = 0;

    public CipherReader(final String _portNumber) {
        this._portNumber = _portNumber;
    }

    private void initializePort() {
        try {
            port = new SerialPort("COM4");
            port.openPort();
            port.addEventListener(new EventListener(port));
            port.setParams(SerialPort.BAUDRATE_38400, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE, false, false);
            port.writeString(String.valueOf((char) 54));
            port.setParams(SerialPort.BAUDRATE_115200, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE, true, false);

            sleep(100);
        } catch (Exception e) {
            e.printStackTrace();
        }

        list = new ArrayList<>();
        RecordCount = 0;
        step = 0;
    }

    public List<String> ReadScanData() {
        initializePort();
        sleep(100);
        SetStep(1);

        while (step == 1) {
            SetStep(1);
        }

        while (step == 2) {
            SetStep(2);
        }

        while (step == 3) {
            while (list.size() < RecordCount) {
                SetStep(3);
            }
            SetStep(4);
        }
        try {
            port.closePort();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private void SetStep(int _step) {
        try {
            step = _step;
            //Console.WriteLine("Установка режима : "+step);
            //System.out.println("Установка режима : "+step);
            switch (_step) {
                case 0:
                    break;
                case 1:
                    port.writeString("ACK1" + (char) 13);
                    //port.writeBytes(("ACK1" + (char) 13).getBytes());
                    // System.Threading.Thread.Sleep(50);
                    sleep(50);
                    break;
                case 2:
                    //System.Threading.Thread.Sleep(50);
                    sleep(50);
                    port.writeString("ACK2" + (char) 13);
                    break;
                case 3:
                    //System.Threading.Thread.Sleep(50);
                    sleep(50);
                    port.writeString("ACK3" + (char) 13);
                    break;
                case 4:
                    port.writeString("ACK" + (char) 13);
                    //System.Threading.Thread.Sleep(50);
                    sleep(50);
                    port.writeString("OK" + (char) 13);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void Reader(String input) {
        if (input.trim().equals("CIPHER")) {
            SetStep(2);
            return;
        }

        if (step == 2) {
            RecordCount = Integer.valueOf(input.trim());
            SetStep(3);
            return;
        }

        if (step == 3) {
            list.add(input);
        }

    }

    private void sleep(int delay_) {
        try {
            Thread.sleep(delay_);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    private class EventListener implements SerialPortEventListener {
        SerialPort serialPort;

        public EventListener(final SerialPort serialPort) {
            this.serialPort = serialPort;
        }

        public void serialEvent(SerialPortEvent event) {

            try {
                System.out.println();
                String s = serialPort.readString();
                Reader(s);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            /*

            try {
                System.out.println(serialPort.readString());
            }catch (Exception ex){
                ex.printStackTrace();
            }

            if (event.isRXCHAR () && event.getEventValue () > 0){
                try {
                    String data = serialPort.readString (event.getEventValue ());
                    System.out.print (data);
                }
                catch (SerialPortException ex) {
                    System.out.println (ex);
                }
            }
            */
        }
    }
}

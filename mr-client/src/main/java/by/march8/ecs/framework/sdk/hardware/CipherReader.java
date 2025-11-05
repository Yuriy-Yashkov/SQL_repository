package by.march8.ecs.framework.sdk.hardware;


import by.march8.ecs.MainController;
import by.march8.ecs.framework.common.BackgroundTask;
import by.march8.ecs.framework.common.model.Breaker;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;

import javax.swing.*;

/**
 * @author Andy 06.06.2018 - 12:09.
 */
public class CipherReader {
    private SerialPort port;
    private String _portNumber;
    private int step = 0;
    private int recordCount = 0;
    private int currRecordCount = 0;

    private String buffer;
    private JLabel taskProgressLabel;

    public CipherReader(final String _portNumber) {
        this._portNumber = _portNumber;
    }

    /**
     * Инициализация порта, и настройка терминала, по образу и подобию
     * реализации в march8
     * !!!!!Не жалейте тайминга одидания
     */
    private void initializePort() {
        try {
            port = new SerialPort(_portNumber);
            port.openPort();

            port.addEventListener(new EventListener(port));

            port.setParams(SerialPort.BAUDRATE_38400, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE, false, true);
            sleep(500);
            port.writeString(String.valueOf((char) 7));
            sleep(50);
            port.writeString(String.valueOf((char) 54));
            sleep(50);
            port.writeString(String.valueOf((char) 81));

            sleep(50);
            port.setParams(SerialPort.BAUDRATE_115200, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE, true, true);
            sleep(50);
        } catch (Exception e) {
            e.printStackTrace();
        }
        recordCount = 0;
        step = 0;
    }

    private String[] ReadScanData(Breaker breaker) {
        buffer = "";
        currRecordCount = 0;
        String[] textList = null;
        initializePort();
        sleep(100);
        setStep(1);

        while (step == 1 && (!breaker.isBreak())) {
            setStep(1);
        }

        while (step == 2 && (!breaker.isBreak())) {
            setStep(2);
        }

        while (step == 3 && (!breaker.isBreak())) {
            while (step == 3 && (currRecordCount) < recordCount) {
                if (breaker.isBreak()) {
                    break;
                }
                if (step > 3) {
                    break;
                }
                sleep(50);
            }
            setStep(4);
        }

        try {
            port.closePort();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!breaker.isBreak()) {
            System.out.println("*****************************************");
            System.out.println(buffer);
            textList = buffer.split("-");
            System.out.println("Amount " + textList.length);
            for (String s : textList) {
                System.out.println(s);
            }
            System.out.println("*****************************************");
        }
        return textList;
    }

    /**
     * Установка режимов для терминала в процессе чтения данных.
     * Реализация восстановлена по исходникам march8 и задаче терминала
     * из cipher.bas
     * @param _step шаг в работе с терминалом
     */
    private void setStep(int _step) {
        try {
            step = _step;
            switch (_step) {
                case 0:
                    break;
                case 1:
                    port.writeString("ACK1" + (char) 13);
                    taskProgressLabel.setText("Инициализация терминала...");
                    sleep(50);
                    break;
                case 2:
                    sleep(50);
                    taskProgressLabel.setText("Запрос количества записей...");
                    port.writeString("ACK2" + (char) 13);
                    break;
                case 3:
                    sleep(50);
                    taskProgressLabel.setText("Получение записи " + currRecordCount + " из " + recordCount);
                    port.writeString("ACK3" + (char) 13);
                    break;
                case 4:
                    sleep(50);
                    port.writeString("ACK" + (char) 13);
                    taskProgressLabel.setText("Отключение терминала...");
                    sleep(50);
                    port.writeString("OK" + (char) 13);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // Пополняем буфферную строку данными из терминала
    private void addString(String s) {
        // Если встретили новую строку - реплейсин на дефис, для удобной
        // работы с результирующим массивом кодов
        if (s.contains("\r")) {
            currRecordCount++;
        }
        buffer += s.replace("\r", "-");

//        if(currRecordCount==recordCount){
//            setStep(4);
//        }
    }

    /**
     * Чтение данных com-порта, и выставление режимов в засвисимости от
     * текущего этапа/шага выполнения
     * @param input строка из порта
     * @param rx состояние флага RX
     */
    private void portReader(String input, boolean rx) {
        if (input == null) {
            return;
        }

        if (input.trim().equals("CIPHER")) {
            setStep(2);
            return;
        }

        if (step == 2) {
            recordCount = Integer.valueOf(input.trim());
            setStep(3);
            return;
        }

        if (step == 3) {
            addString(input);
            if (rx) {
                setStep(3);
            }
        }
    }

    private void sleep(int delay_) {
        try {
            Thread.sleep(delay_);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    public String[] getCodeListExternal(final Breaker breaker) {

        class Task extends BackgroundTask {
            String[] codeArray;

            public Task(final String messageText) {
                super(messageText, breaker);
            }

            @Override
            protected String[] doInBackground() throws Exception {
                try {
                    codeArray = ReadScanData(breaker);
                    return codeArray;
                } catch (Exception e) {
                    MainController.exception(e, "Ошибка получения данных от терминала");
                    return null;
                }
            }

            public String[] getCodeArray() {
                return codeArray;
            }
        }

        Task task = new Task("Инициализация порта [" + _portNumber + "]...");
        try {
            taskProgressLabel = task.getProgressLabel();
            task.executeTask();
            return task.getCodeArray();
        } catch (Exception e) {
            MainController.exception(e, "Ошибка получения данных от терминала");
            return null;
        }
    }

    public int getRecordCount() {
        return recordCount;
    }

    private class EventListener implements SerialPortEventListener {
        SerialPort serialPort;

        public EventListener(final SerialPort serialPort) {
            this.serialPort = serialPort;
        }

        public void serialEvent(SerialPortEvent event) {
            boolean rx = false;
            try {
                //System.out.println();
                String s = serialPort.readString();
                System.out.println(s);

                if (event.isRXCHAR() && event.getEventValue() > 0) {
                    try {
                        rx = true;
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                }

                portReader(s, rx);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
    }
}

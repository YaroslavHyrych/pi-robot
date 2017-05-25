package com.hyrych.pirobot;

import com.pi4j.io.gpio.*;

import java.util.Scanner;

/**
 * Created by yagy0913 on 5/25/2017.
 */
public class Port {
    private GpioController gpio = GpioFactory.getInstance();
    GpioPinDigitalOutput m_1_A = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_16, "MyLED", PinState.HIGH);
    GpioPinDigitalOutput m_1_B = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_18, "MyLED", PinState.HIGH);
    GpioPinDigitalOutput m_2_A = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_13, "MyLED", PinState.HIGH);
    GpioPinDigitalOutput m_2_B = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_15, "MyLED", PinState.HIGH);

    public Port() throws InterruptedException {
        m_1_A.setShutdownOptions(true, PinState.LOW);
        m_1_B.setShutdownOptions(true, PinState.LOW);
        m_2_A.setShutdownOptions(true, PinState.LOW);
        m_2_B.setShutdownOptions(true, PinState.LOW);
        Thread.sleep(2000);
    }

    public static void main(String[] args) throws InterruptedException {
        final Port motor = new Port();

        System.out.println("--> Init CLI");

        Scanner sc = new Scanner(System.in);
        boolean isReverse = false;
        int index = 0;

        while (true) {
            System.out.println("Enter direction:");
            index = sc.nextInt();
            GpioPinDigitalOutput port = motor.getPort(index);

            if (index == 0) {
                break;
            }

            if (index == 9) {
                isReverse = true;
                continue;
            }

            if (port == null) {
                continue;
            }

            if (isReverse) {
                motor.setHight(port);
            } else {
                motor.setLow(port);
            }
            Thread.sleep(5000);
        }
    }

    private GpioPinDigitalOutput getPort(int index) {
        switch (index) {
            case 1:
                return this.m_1_A;
            case 2:
                return this.m_1_B;
            case 3:
                return this.m_2_A;
            case 4:
                return this.m_2_B;
        }
        return null;
    }

    private void setLow(GpioPinDigitalOutput port) {
        port.low();
    }

    private void setHight(GpioPinDigitalOutput port) {
        port.high();
    }

}

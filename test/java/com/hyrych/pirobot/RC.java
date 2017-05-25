package com.hyrych.pirobot;

import com.pi4j.io.gpio.*;

import java.util.Scanner;

/**
 * Created by yagy0913 on 5/25/2017.
 */
public class RC
{
    private  GpioController gpio = GpioFactory.getInstance();
    private GpioPinDigitalOutput m_1_A = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_16, "MyLED", PinState.HIGH);
    private GpioPinDigitalOutput m_1_B = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_18, "MyLED", PinState.HIGH);
    private GpioPinDigitalOutput m_2_A = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_13, "MyLED", PinState.HIGH);
    private GpioPinDigitalOutput m_2_B = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_15, "MyLED", PinState.HIGH);

    public RC() {
        m_1_A.setShutdownOptions(true, PinState.LOW);
        m_1_B.setShutdownOptions(true, PinState.LOW);
        m_2_A.setShutdownOptions(true, PinState.LOW);
        m_2_B.setShutdownOptions(true, PinState.LOW);
    }

    public static void main(String[] args) {
        final RC motor = new RC();

        System.out.println("--> Init CLI");

        Scanner sc = new Scanner(System.in);
        boolean isWorking = true;
        char command = 's';

        while(isWorking) {
            System.out.println("Enter direction:");
            command = sc.next().charAt(0);
            switch(command) {
                case 's':
                    motor.stop();
                    break;
                case 'w':
                    motor.forward();
                    break;
                case 'a':
                    motor.left();
                    break;
                case 'd':
                    motor.right();
                    break;
                case 'x':
                    motor.back();
                    break;
                case 'r':
                    isWorking = false;
                    break;
            }
        }
    }

    void stop() {
        m_1_A.low();
        m_1_B.low();
        m_2_A.low();
        m_2_B.low();
    }

    void forward() {
        m_1_A.high();
        m_1_B.low();
        m_2_A.low();
        m_2_B.high();
    }

    void back() {
        m_1_A.low();
        m_1_B.high();
        m_2_A.high();
        m_2_B.low();
    }

    void left() {
        m_1_A.high();
        m_1_B.low();
        m_2_A.high();
        m_2_B.low();
    }

    void right() {
        m_1_A.low();
        m_1_B.high();
        m_2_A.low();
        m_2_B.high();
    }

    void off() {
        gpio.shutdown();
    }

}

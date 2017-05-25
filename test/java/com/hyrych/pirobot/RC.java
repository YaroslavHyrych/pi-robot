package com.hyrych.pirobot;

import com.pi4j.io.gpio.*;

import java.util.Scanner;

/**
 * Created by yagy0913 on 5/25/2017.
 */
public class RC
{
    private  GpioController gpio = GpioFactory.getInstance();
    private GpioPinDigitalOutput m_1_A = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_16, "MyLED_1", PinState.LOW);
    private GpioPinDigitalOutput m_1_B = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_18, "MyLED_2", PinState.LOW);
    private GpioPinDigitalOutput m_2_A = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_13, "MyLED_3", PinState.LOW);
    private GpioPinDigitalOutput m_2_B = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_15, "MyLED_4", PinState.LOW);

    public RC() throws InterruptedException {
        m_1_A.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
        m_1_B.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
        m_2_A.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
        m_2_B.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
        Thread.sleep(2000);
        this.stop();
        Thread.sleep(2000);
    }

    public static void main(String[] args) throws InterruptedException {
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
                    motor.off();
                    break;
            }
        }
    }

    private void stop() throws InterruptedException {
        System.out.println("Motor: stop");
        m_1_A.low();
        m_1_B.low();
        m_2_A.low();
        m_2_B.low();
        Thread.sleep(2000);
    }

    private void forward() throws InterruptedException {
        System.out.println("Motor: forward");
        m_1_A.high();
        m_1_B.low();
        m_2_A.low();
        m_2_B.high();
        Thread.sleep(2000);
    }

    private void back() throws InterruptedException {
        System.out.println("Motor: back");
        m_1_A.low();
        m_1_B.high();
        m_2_A.high();
        m_2_B.low();
        Thread.sleep(2000);
    }

    private void left() throws InterruptedException {
        System.out.println("Motor: left");
        m_1_A.high();
        m_1_B.low();
        m_2_A.high();
        m_2_B.low();
        Thread.sleep(2000);
    }

    private void right() throws InterruptedException {
        System.out.println("Motor: right");
        m_1_A.low();
        m_1_B.high();
        m_2_A.low();
        m_2_B.high();
        Thread.sleep(2000);
    }

    private void off() {
        System.out.println("Motor: GPIO off");
        gpio.shutdown();
    }

}

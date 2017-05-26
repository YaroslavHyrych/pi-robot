package com.hyrych.pirobot;

import com.googlecode.lanterna.TerminalFacade;
import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.screen.Screen;
import com.pi4j.io.gpio.*;

/**
 * Created by yagy0913 on 5/25/2017.
 */
public class RC
{
    private  GpioController gpio = GpioFactory.getInstance();
    private GpioPinDigitalOutput m_1_A = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04, "MyLED_1", PinState.LOW);
    private GpioPinDigitalOutput m_1_B = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_05, "MyLED_2", PinState.LOW);
    private GpioPinDigitalOutput m_2_A = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02, "MyLED_3", PinState.LOW);
    private GpioPinDigitalOutput m_2_B = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03, "MyLED_4", PinState.LOW);

    public RC() throws InterruptedException {
        m_1_A.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
        m_1_B.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
        m_2_A.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
        m_2_B.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
    }

    public static void main(String[] args) throws InterruptedException {
        final RC motor = new RC();

        System.out.println("--> Init window");
        Screen screen = TerminalFacade.createScreen();
        screen.startScreen();
        screen.clear();

        boolean keepRunning = true;

        while(keepRunning) {
            Key key = screen.readInput();

            while(key == null) {
                key = screen.readInput();
            }

            System.out.println("Key pressed:" + key.getKind().toString());

            switch (key.getKind()) {
                case ArrowLeft:
                    motor.left();
                    break;
                case ArrowRight:
                    motor.right();
                    break;
                case ArrowDown:
                    motor.back();
                    break;
                case ArrowUp:
                    motor.forward();
                    break;
                case Escape:
                    keepRunning = false;
                    break;
                case Enter:
                    motor.stop();
                    break;
                default:
                    System.out.println("Other key:" + key.getKind().toString());
            }
        }

        motor.off();
        screen.stopScreen();
    }

    private void stop() throws InterruptedException {
        m_1_A.low();
        m_1_B.low();
        m_2_A.low();
        m_2_B.low();
    }

    private void forward() throws InterruptedException {
        m_1_A.high();
        m_1_B.low();
        m_2_A.low();
        m_2_B.high();
    }

    private void back() throws InterruptedException {
        m_1_A.low();
        m_1_B.high();
        m_2_A.high();
        m_2_B.low();
    }

    private void right() throws InterruptedException {
        m_1_A.high();
        m_1_B.low();
        m_2_A.low();
        m_2_B.low();
    }

    private void left() throws InterruptedException {
        m_1_A.low();
        m_1_B.low();
        m_2_A.low();
        m_2_B.high();
    }

    private void off() throws InterruptedException {
        stop();
        gpio.shutdown();
    }

}

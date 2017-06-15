package com.hyrych.pirobot;

import com.googlecode.lanterna.TerminalFacade;
import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.screen.Screen;
import com.pi4j.io.gpio.*;
import com.pi4j.wiringpi.Gpio;
import com.pi4j.wiringpi.SoftPwm;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yagy0913 on 5/25/2017.
 */
public class RC
{
    private GpioController gpio = GpioFactory.getInstance();
    private int speed = 0;
//    private GpioPinDigitalOutput m_1_A = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04, "MyLED_1", PinState.LOW);
//    private GpioPinDigitalOutput m_1_B = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_05, "MyLED_2", PinState.LOW);
//    private GpioPinDigitalOutput m_2_A = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02, "MyLED_3", PinState.LOW);
//    private GpioPinDigitalOutput m_2_B = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03, "MyLED_4", PinState.LOW);

    public RC() throws InterruptedException {
        SoftPwm.softPwmCreate(RaspiPin.GPIO_04.getAddress(), 0, 100);
        SoftPwm.softPwmCreate(RaspiPin.GPIO_05.getAddress(), 0, 100);
        SoftPwm.softPwmCreate(RaspiPin.GPIO_02.getAddress(), 0, 100);
        SoftPwm.softPwmCreate(RaspiPin.GPIO_03.getAddress(), 0, 100);

//        m_1_A.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
//        m_1_B.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
//        m_2_A.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
//        m_2_B.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
    }

    public static void main(String[] args) throws InterruptedException {
        final RC motor = new RC();

        Map<String, Command> commands = new HashMap<>() ;
        commands.put("forward", motor.new ForwardCommand());
        commands.put("stop", motor.new StopCommand());
        commands.put("left", motor.new LeftCommand());
        commands.put("right", motor.new RightCommand());
        commands.put("back", motor.new BackCommand());

        System.out.println("--> Init window");
        Screen screen = TerminalFacade.createScreen();
        screen.startScreen();
        screen.clear();

        boolean keepRunning = true;
        Command prevCommand = null;
        Command command = null;

        while(keepRunning) {
            Key key = screen.readInput();

            while(key == null) {
                key = screen.readInput();
            }

            switch (key.getKind()) {
                case ArrowLeft:
                    prevCommand = command;
                    command = commands.get("left");
                    break;
                case ArrowRight:
                    prevCommand = command;
                    command = commands.get("right");
                    break;
                case ArrowDown:
                    prevCommand = command;
                    command = commands.get("back");
                    break;
                case ArrowUp:
                    prevCommand = command;
                    command = commands.get("forward");
                    break;
                case Escape:
                    keepRunning = false;
                    break;
                case NormalKey:
                    int newSpeed = Character.getNumericValue(key.getCharacter());
                    if (newSpeed > 0 && newSpeed <= 9) {
                        motor.setSpeed(newSpeed + 1);
                    }
                    break;
                case Enter:
                    prevCommand = command;
                    command = commands.get("stop");
                    break;
                default:
                    System.out.println("Other key:" + key.getKind().toString());
            }
            if (command != null) {
                command.go(prevCommand);
            }
        }

        motor.off();
        screen.stopScreen();
    }

    private void setSpeed(int newSpeed) {
        this.speed = newSpeed * 10;
    }

    private void off() throws InterruptedException {
        gpio.shutdown();
    }

    interface Command {
        void go(Command prevCommand);
    }

    class ForwardCommand implements Command {
        @Override
        public void go(Command prevCommand) {
            SoftPwm.softPwmWrite(RaspiPin.GPIO_04.getAddress(), speed);
            SoftPwm.softPwmWrite(RaspiPin.GPIO_05.getAddress(), 0);
            SoftPwm.softPwmWrite(RaspiPin.GPIO_02.getAddress(), 0);
            SoftPwm.softPwmWrite(RaspiPin.GPIO_03.getAddress(), speed);
        }
    }

    class StopCommand implements Command {
        @Override
        public void go(Command prevCommand) {
            SoftPwm.softPwmWrite(RaspiPin.GPIO_04.getAddress(), 0);
            SoftPwm.softPwmWrite(RaspiPin.GPIO_05.getAddress(), 0);
            SoftPwm.softPwmWrite(RaspiPin.GPIO_02.getAddress(), 0);
            SoftPwm.softPwmWrite(RaspiPin.GPIO_03.getAddress(), 0);
        }
    }

    class LeftCommand implements Command {
        @Override
        public void go(Command prevCommand) {
            if (prevCommand == null || prevCommand instanceof StopCommand) {
                SoftPwm.softPwmWrite(RaspiPin.GPIO_04.getAddress(), 0);
                SoftPwm.softPwmWrite(RaspiPin.GPIO_05.getAddress(), speed);
                SoftPwm.softPwmWrite(RaspiPin.GPIO_02.getAddress(), 0);
                SoftPwm.softPwmWrite(RaspiPin.GPIO_03.getAddress(), speed);
            }
            if (prevCommand instanceof ForwardCommand) {
                SoftPwm.softPwmWrite(RaspiPin.GPIO_04.getAddress(), speed/2);
                SoftPwm.softPwmWrite(RaspiPin.GPIO_05.getAddress(), 0);
                SoftPwm.softPwmWrite(RaspiPin.GPIO_02.getAddress(), 0);
                SoftPwm.softPwmWrite(RaspiPin.GPIO_03.getAddress(), speed);
            } else if (prevCommand instanceof BackCommand) {
                SoftPwm.softPwmWrite(RaspiPin.GPIO_04.getAddress(), 0);
                SoftPwm.softPwmWrite(RaspiPin.GPIO_05.getAddress(), speed/2);
                SoftPwm.softPwmWrite(RaspiPin.GPIO_02.getAddress(), speed);
                SoftPwm.softPwmWrite(RaspiPin.GPIO_03.getAddress(), 0);
            }
        }
    }

    class RightCommand implements Command {
        @Override
        public void go(Command prevCommand) {
            if (prevCommand == null || prevCommand instanceof StopCommand) {
                SoftPwm.softPwmWrite(RaspiPin.GPIO_04.getAddress(), speed);
                SoftPwm.softPwmWrite(RaspiPin.GPIO_05.getAddress(), 0);
                SoftPwm.softPwmWrite(RaspiPin.GPIO_02.getAddress(), speed);
                SoftPwm.softPwmWrite(RaspiPin.GPIO_03.getAddress(), 0);
            }
            if (prevCommand instanceof ForwardCommand) {
                SoftPwm.softPwmWrite(RaspiPin.GPIO_04.getAddress(), speed);
                SoftPwm.softPwmWrite(RaspiPin.GPIO_05.getAddress(), 0);
                SoftPwm.softPwmWrite(RaspiPin.GPIO_02.getAddress(), 0);
                SoftPwm.softPwmWrite(RaspiPin.GPIO_03.getAddress(), speed/2);
            } else if (prevCommand instanceof BackCommand) {
                SoftPwm.softPwmWrite(RaspiPin.GPIO_04.getAddress(), speed/2);
                SoftPwm.softPwmWrite(RaspiPin.GPIO_05.getAddress(), 0);
                SoftPwm.softPwmWrite(RaspiPin.GPIO_02.getAddress(), 0);
                SoftPwm.softPwmWrite(RaspiPin.GPIO_03.getAddress(), speed);
            }

        }
    }

    class BackCommand implements Command {
        @Override
        public void go(Command prevCommand) {
            SoftPwm.softPwmWrite(RaspiPin.GPIO_04.getAddress(), 0);
            SoftPwm.softPwmWrite(RaspiPin.GPIO_05.getAddress(), speed);
            SoftPwm.softPwmWrite(RaspiPin.GPIO_02.getAddress(), speed);
            SoftPwm.softPwmWrite(RaspiPin.GPIO_03.getAddress(), 0);
        }
    }

}
